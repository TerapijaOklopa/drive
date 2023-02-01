package com.mobile.drive.mobile.ui.drive

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mobile.drive.databinding.ListItemFileViewBinding
import com.mobile.drive.mobile.ui.model.FileUiModel
import com.mobile.drive.mobile.utils.Drawables

internal class FileAdapter(private val onItemClicked: (file: FileUiModel) -> Unit) :
    ListAdapter<FileUiModel, FileAdapter.FileViewHolder>(FileDiffCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FileViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return FileViewHolder(
            ListItemFileViewBinding.inflate(
                inflater,
                parent,
                false
            )
        ).apply {
            binding.root.setOnClickListener {
                if (adapterPosition != -1) {
                    onItemClicked(getItem(adapterPosition))
                }
            }
        }
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        bindFileItem(holder, getItem(position) as FileUiModel)
    }

    private fun bindFileItem(
        holder: FileViewHolder,
        file: FileUiModel
    ) {
        holder.binding.apply {
            label.text = file.name
            icon.setImageDrawable(Drawables.get(file.icon))
        }
    }

    internal class FileViewHolder(val binding: ListItemFileViewBinding) :
        RecyclerView.ViewHolder(binding.root)

    internal object FileDiffCallback : DiffUtil.ItemCallback<FileUiModel>() {

        override fun areItemsTheSame(oldItem: FileUiModel, newItem: FileUiModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FileUiModel, newItem: FileUiModel): Boolean {
            return oldItem == newItem
        }
    }
}
