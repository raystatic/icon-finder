package com.raystatic.iconfinder.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.raystatic.iconfinder.R
import com.raystatic.iconfinder.data.models.Icon
import com.raystatic.iconfinder.databinding.ItemIconBinding

class IconAdapter(val context:Context): PagingDataAdapter<Icon, IconAdapter.IconViewHolder>(ICON_COMPARATOR) {

    inner class IconViewHolder(private val binding:ItemIconBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(icon: Icon) {
            binding.apply {
                val rasterSize = icon.raster_sizes.size
                val rasterIcons = icon.raster_sizes[rasterSize - 2]
                Glide.with(itemView)
                    .load(rasterIcons.formats[0].preview_url)
                    .centerCrop()
                    .apply(RequestOptions().override(rasterIcons.size_width, rasterIcons.size_height))
                    .into(imgIcon)

                if (icon.is_premium){
                    tvFreemium.text = context.getString(R.string.premium)
                }else{
                    tvFreemium.text = context.getString(R.string.free)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconViewHolder {
        val binding = ItemIconBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return IconViewHolder(binding)
    }


    override fun onBindViewHolder(holder: IconViewHolder, position: Int) {
        val currentItem = getItem(position)
        currentItem?.let {
            holder.bind(currentItem)
        }
    }


    companion object{
        private val ICON_COMPARATOR = object : DiffUtil.ItemCallback<Icon>(){
            override fun areItemsTheSame(oldItem: Icon, newItem: Icon): Boolean =
                oldItem.icon_id == newItem.icon_id

            override fun areContentsTheSame(
                oldItem: Icon,
                newItem: Icon
            ) = oldItem == newItem
        }
    }

}