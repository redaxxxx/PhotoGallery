package com.example.photogallery

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.photogallery.databinding.ActivityViewImageBinding

class ViewImageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityViewImageBinding>(this, R.layout.activity_view_image)

        val intent = intent
        val imagePath = intent.getStringExtra("IMAGE_PATH")
        Glide.with(this).load(imagePath).into(binding.image)

        binding.shareIcon.setOnClickListener{
            if (imagePath != null) {
                shareImage(imagePath)
            }
        }

        binding.arrowBack.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun shareImage(imagePath: String){
        var shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.setType("image/*")
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(imagePath))
        startActivity(Intent.createChooser(shareIntent, "Share Image"))

    }
}