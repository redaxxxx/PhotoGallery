package com.example.photogallery

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.photogallery.databinding.ItemLayoutBinding
import java.io.File

class GalleryAdapter(var imageList: List<String>, val context:Context, val onclickListener: OnClickListener):
    RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {

    class ViewHolder( val binding: ItemLayoutBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val image = imageList[position]
//        val image_file = File(imageList[position])
        Glide.with(context).load(image).into(holder.binding.galleryImage)

        holder.itemView.setOnClickListener {
            onclickListener.onClick(image)
        }
    }

    class OnClickListener(val clickListener: (image: String) -> Unit){
        fun onClick(image: String) = clickListener(image)
    }

}