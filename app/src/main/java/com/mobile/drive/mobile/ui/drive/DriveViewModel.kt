package com.mobile.drive.mobile.ui.drive

import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.mobile.drive.data.drive.DriveRepository
import com.mobile.drive.mobile.ui.model.FileUiModel
import com.mobile.drive.mobile.ui.model.FileUiModel.Companion.toFileUIModelList
import com.mobile.drive.mobile.vo.ErrorData
import com.mobile.drive.mobile.vo.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

const val DEFAULT_FOLDER_ID = "root"

class DriveViewModel(
    val googleSignInOptions: GoogleSignInOptions,
    private val driveRepository: DriveRepository
) : ViewModel() {
    private var _uiContent = MutableStateFlow(DriveContent())
    val uiContent = _uiContent.asStateFlow()

    init {
        getDriveFiles()
    }

    private fun getDriveFiles() {
        CoroutineScope(Dispatchers.IO).launch {
            val response = driveRepository.getFolderFiles(DEFAULT_FOLDER_ID)
            if (response.isSuccess) {
                _uiContent.update {
                    it.copy(state = Resource.success(response.getOrNull()?.toFileUIModelList()))
                }
            } else {
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
}

data class DriveContent(
    val state: Resource<List<FileUiModel>> = Resource.loading()
)
