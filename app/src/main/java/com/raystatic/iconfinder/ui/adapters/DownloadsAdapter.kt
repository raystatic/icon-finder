package com.raystatic.iconfinder.ui.adapters

import android.graphics.BitmapFactory
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.raystatic.iconfinder.data.models.DownloadedIcon
import com.raystatic.iconfinder.databinding.ItemIconBinding
import com.raystatic.iconfinder.utils.Utility
import java.io.File

class DownloadsAdapter(
    val onShare:(file:File)->Unit
): RecyclerView.Adapter<DownloadsAdapter.DownloadsViewHolder>() {

    inner class DownloadsViewHolder(private val binding:ItemIconBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(currentItem: DownloadedIcon?) {
            currentItem?.let {
                val file = File(it.path)
                if (file.exists()){

                    binding.apply {
                        Glide.with(itemView)
                            .load(Uri.fromFile(file))
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .override(256,256)
                            .into(imgIcon)

                        tvFreemium.isVisible = false
                        tvShare.isVisible = true
                        tvSize.isVisible = true

                        val fileSize = Utility.fileSizeInMb(file)

                        tvSize.text = fileSize

                        tvShare.setOnClickListener {
                            onShare(file)
                        }
                    }
                }
            }
        }

    }

    private val diffCallback = object : DiffUtil.ItemCallback<DownloadedIcon>(){
        override fun areItemsTheSame(oldItem: DownloadedIcon, newItem: DownloadedIcon): Boolean =
            oldItem.path == newItem.path

        override fun areContentsTheSame(
            oldItem: DownloadedIcon,
            newItem: DownloadedIcon
        ) = oldItem == newItem
    }

    private val differ = AsyncListDiffer(this,diffCallback)

    fun submitData(list:List<DownloadedIcon>) = differ.submitList(list)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DownloadsViewHolder {
        val binding = ItemIconBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return DownloadsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DownloadsViewHolder, position: Int) {
        val currentItem = differ.currentList[position]

        holder.bind(currentItem)

    }

    override fun getItemCount(): Int =  differ.currentList.size
}