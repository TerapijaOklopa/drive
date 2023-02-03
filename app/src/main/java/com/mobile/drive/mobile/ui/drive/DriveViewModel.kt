package com.mobile.drive.mobile.ui.drive

import androidx.lifecycle.ViewModel
import com.google.api.services.drive.model.File
import com.mobile.drive.R
import com.mobile.drive.data.drive.DriveRepository
import com.mobile.drive.mobile.ui.model.FileUiModel
import com.mobile.drive.mobile.ui.model.FileUiModel.Companion.toFileUIModelList
import com.mobile.drive.mobile.utils.DelayedTextWatcher
import com.mobile.drive.mobile.utils.Strings
import com.mobile.drive.mobile.utils.coroutines.CoroutineDispatcherProvider
import com.mobile.drive.mobile.vo.ErrorData
import com.mobile.drive.mobile.vo.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

const val DEFAULT_FOLDER_ID = "root"

class DriveViewModel(
    private val driveRepository: DriveRepository,
    private val dispatcherProvider: CoroutineDispatcherProvider
) : ViewModel() {
    private var _uiContent = MutableStateFlow(DriveContent())
    val uiContent = _uiContent.asStateFlow()

    init {
        getDriveFiles()
    }

    fun getDriveFiles() {
        CoroutineScope(dispatcherProvider.io).launch {
            val response = driveRepository.getFolderFiles(DEFAULT_FOLDER_ID)
            val result = response.getOrNull()
            if (response.isSuccess && result != null) {
                _uiContent.update {
                    it.copy(state = Resource.success(result.toFileUIModelList()))
                }
            } else {
                response.exceptionOrNull()?.let { t ->
                    _uiContent.update {
                        it.copy(
                            state = Resource.error(
                                ErrorData(
                                    message = t.message
                                        ?: Strings.get(R.string.error_unexpected_message)
                                )
                            )
                        )
                    }
                }
            }
        }
    }

    var searchWatcher = DelayedTextWatcher(
        callback = object : DelayedTextWatcher.Callback {
            override fun onTextChange(text: String) {
                if (_uiContent.value.query == text) {
                    return
                }
                if (text.isEmpty()) {
                    _uiContent.update {
                        it.copy(query = "", state = Resource.loading())
                    }
                    getDriveFiles()
                } else {
                    searchFiles(text)
                }
            }
        }
    )

    fun searchFiles(query: String) {
        _uiContent.update {
            it.copy(state = Resource.loading())
        }
        CoroutineScope(dispatcherProvider.io).launch {
            val response = driveRepository.searchFiles(query)
            val result = response.getOrNull()
            if (response.isSuccess && result != null) {
                _uiContent.update {
                    it.copy(
                        query = query,
                        state = Resource.success(result.toFileUIModelList())
                    )
                }
            } else {
                response.exceptionOrNull()?.let { t ->
                    _uiContent.update {
                        it.copy(
                            query = query,
                            state = Resource.error(
                                ErrorData(
                                    message = t.message
                                        ?: Strings.get(R.string.error_unexpected_message)
                                )
                            )
                        )
                    }
                }
            }
        }
    }
}

data class DriveContentLeka(
    val state: Resource<List<File>> = Resource.loading()
)

data class DriveContent(
    val query: String = "",
    val state: Resource<List<FileUiModel>> = Resource.loading()
)
