package com.example.photogallery

import android.app.Instrumentation.ActivityResult
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.example.photogallery.databinding.ActivityMainBinding
import android.Manifest

class MainActivity : AppCompatActivity() {

    private lateinit var image_list: ArrayList<String>
    private lateinit var binding: ActivityMainBinding
    private lateinit var galleryAdapter: GalleryAdapter

    private var readPermissionGranted = false
    private var writePermissionGranted = false
    private var readImagesPermissionGranted = false
    private var cameraPermissionGranted = false

    private val PERMISSIONS = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        image_list = ArrayList()

        if (allPermissionGranted()){
            loadImages()
        }else {
            requestPermission()
        }

        binding.cameraBtn.setOnClickListener {
            startActivity(Intent(this, CameraActivity::class.java))
        }
    }

    private fun allPermissionGranted() = Constants.REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext,it) == PackageManager.PERMISSION_GRANTED
    }
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions())
    {permissions->
        var permissionGranted = true
        permissions.entries.forEach{
            if (it.key in Constants.REQUIRED_PERMISSIONS && !it.value){
                permissionGranted = false
            }
        }
        if (!permissionGranted){
            Toast.makeText(baseContext, "Permission request denied", Toast.LENGTH_SHORT).show()
        }else {
            loadImages()
        }

    }

    private fun requestPermission(){
        permissionLauncher.launch(Constants.REQUIRED_PERMISSIONS)
    }

    private fun loadImages() {
        binding.imagesRv.setHasFixedSize(true)
        binding.imagesRv.layoutManager = GridLayoutManager(this, 3)
        image_list = GalleryImages().listOfImages(this)
        galleryAdapter = GalleryAdapter(image_list, this, GalleryAdapter.OnClickListener{
            val intent = Intent(this, ViewImageActivity::class.java)
            intent.putExtra("IMAGE_PATH", it)
            startActivity(intent)
        })
        binding.imagesRv.adapter = galleryAdapter
        binding.totalItemsTv.text = "Total items (${image_list.size})"


    }
}