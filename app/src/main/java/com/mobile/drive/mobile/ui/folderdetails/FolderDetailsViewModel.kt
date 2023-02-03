package com.mobile.drive.mobile.ui.folderdetails

import androidx.lifecycle.ViewModel
import com.mobile.drive.data.drive.DriveRepository
import com.mobile.drive.mobile.ui.model.FileUiModel
import com.mobile.drive.mobile.ui.model.FileUiModel.Companion.toFileUIModelList
import com.mobile.drive.mobile.utils.coroutines.CoroutineDispatcherProvider
import com.mobile.drive.mobile.vo.ErrorData
import com.mobile.drive.mobile.vo.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FolderDetailsViewModel(
    private val driveRepository: DriveRepository,
    private val dispatcherProvider: CoroutineDispatcherProvider,
    folderId: String
) : ViewModel() {

    private val folderIdList = mutableListOf(folderId)

    private var _uiContent = MutableStateFlow(FolderDetailsContent())
    val uiContent = _uiContent.asStateFlow()

    init {
        getDriveFiles(folderId)
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
                                ErrorData(
                                    message = t.message ?: ""
                                )
                            )
                        )
                    }
                }
            }
        }
    }

    fun onItemClick(file: FileUiModel) {
        if (file.id == null || folderIdList[folderIdList.size - 1] == file.id) {
            return
        }
        folderIdList.add(file.id)
        getDriveFiles(file.id)
    }

    fun handleBack(): Boolean {
        folderIdList.removeLast()
        if (folderIdList.isNotEmpty()) {
            getDriveFiles(folderIdList[folderIdList.size - 1])
            return false
        }
        return true
    }
}

data class FolderDetailsContent(
    val state: Resource<List<FileUiModel>> = Resource.loading()
)
