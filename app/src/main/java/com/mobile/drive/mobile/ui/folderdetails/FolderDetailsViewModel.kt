package com.mobile.drive.mobile.ui.folderdetails

import androidx.lifecycle.ViewModel
import com.mobile.drive.data.drive.DriveRepository
import com.mobile.drive.mobile.ui.model.FileUiModel
import com.mobile.drive.mobile.ui.model.FileUiModel.Companion.toFileUIModelList
import com.mobile.drive.mobile.utils.ErrorData
import com.mobile.drive.mobile.utils.Resource
import com.mobile.drive.mobile.utils.coroutines.CoroutineDispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FolderDetailsViewModel(
    private val driveRepository: DriveRepository,
    private val dispatcherProvider: CoroutineDispatcherProvider,
    folder: FileUiModel
) : ViewModel() {

    private val folderIdList = mutableListOf(folder)

    private var _uiContent = MutableStateFlow(FolderDetailsContent())
    val uiContent = _uiContent.asStateFlow()

    init {
        getDriveFiles(folder.id ?: "")
    }

    private fun getDriveFiles(id: String) {
        _uiContent.update {
            it.copy(state = Resource.loading())
        }
        CoroutineScope(dispatcherProvider.io).launch {
            val response = driveRepository.getFolderFiles(id)
            val result = response.getOrNull()
            if (response.isSuccess && result != null) {
                _uiContent.update {
                    it.copy(state = Resource.success(result.toFileUIModelList()))
                }
            } else {
                folderIdList.removeLast()
                response.exceptionOrNull()?.let { t ->
                    _uiContent.update {
                        it.copy(
                            state = Resource.error(
                                ErrorData(message = t.message)
                            )
                        )
                    }
                }
            }
        }
    }

    fun onItemClick(file: FileUiModel) {
        if (file.id == null || folderIdList.contains(file)) {
            return
        }
        folderIdList.add(file)
        getDriveFiles(file.id)
    }

    fun handleBack(): FileUiModel? {
        folderIdList.removeLast()
        if (folderIdList.isNotEmpty()) {
            getDriveFiles(folderIdList.last().id ?: "")
            return folderIdList.last()
        }
        return null
    }
}

data class FolderDetailsContent(
    val state: Resource<List<FileUiModel>> = Resource.loading()
)
