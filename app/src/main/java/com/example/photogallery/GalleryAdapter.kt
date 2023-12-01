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

class GalleryAdapter(var imageList: List<String>, val context:Context): RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {

    class ViewHolder( val binding: ItemLayoutBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val image_file = File(imageList[position])
        if (image_file.exists()){
            Glide.with(context).load(image_file).into(holder.binding.galleryImage)
        }
    }

}