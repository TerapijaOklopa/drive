package com.mobile.drive.mobile.ui.model

import android.os.Parcelable
import com.google.api.services.drive.model.File
import com.mobile.drive.R
import kotlinx.parcelize.Parcelize

@Parcelize
data class FileUiModel(
    val id: String?,
    val parentId: List<String>?,
    val name: String?,
    val mimeType: String?,
    val icon: Int,
    val isFolder: Boolean
) : Parcelable {
    companion object {
        fun List<File>.toFileUIModelList() = map { it.toFileUIModel() }

        private fun File.toFileUIModel(): FileUiModel {
            return FileUiModel(
                id = id,
                parentId = parents,
                name = name,
                mimeType = mimeType,
                icon = getFileTypeIcon(mimeType),
                isFolder = mimeType != null && mimeType.contains("folder")
            )
        }

        private fun getFileTypeIcon(mimeType: String?): Int {
            if (mimeType == null) return R.drawable.ic_unknown_file
            return if (mimeType.contains("folder")) {
                R.drawable.ic_folder
            } else if (mimeType.contains(Regex("png|jpg|jpeg|gif|bmp"))) {
                R.drawable.ic_image
            } else if (mimeType.contains(Regex("mp3|wav|ogg|midi"))) {
                R.drawable.ic_audio
            } else if (mimeType.contains(Regex("mp4|rmvb|avi|flv|3gp"))) {
                R.drawable.ic_video
            } else if (mimeType.contains(Regex("jsp|html|htm|js|php"))) {
                R.drawable.ic_web
            } else if (mimeType.contains(Regex("txt|c|cpp|xml|py|json|log|xls|xlsx|doc|docx|ppt|pptx|pdf"))) {
                R.drawable.ic_file
            } else if (mimeType.contains(Regex("jar|zip|rar|gz"))) {
                R.drawable.ic_zip
            } else {
                R.drawable.ic_unknown_file
            }
        }
    }
}
