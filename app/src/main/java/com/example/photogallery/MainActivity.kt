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

class MainActivity : AppCompatActivity() {

    private lateinit var image_list: ArrayList<String>
    private lateinit var binding: ActivityMainBinding
    private lateinit var galleryAdapter: GalleryAdapter

    private var readPermissionGranted = false
    private var writePermissionGranted = false
    private var readImagesPermissionGranted = false
    private var cameraPermissionGranted = false

    private val PERMISSIONS = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE,
        android.Manifest.permission.CAMERA)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        image_list = ArrayList()

        checkPermission()
    }

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()){permissions->
        readPermissionGranted = permissions[android.Manifest.permission.READ_EXTERNAL_STORAGE] ?: readPermissionGranted
        writePermissionGranted = permissions[android.Manifest.permission.WRITE_EXTERNAL_STORAGE] ?: writePermissionGranted
        readImagesPermissionGranted = permissions[android.Manifest.permission.READ_MEDIA_IMAGES] ?: readImagesPermissionGranted
        cameraPermissionGranted = permissions[android.Manifest.permission.CAMERA] ?: cameraPermissionGranted

        if (readPermissionGranted && writePermissionGranted && cameraPermissionGranted){
            loadImages()
            Toast.makeText(this,"Read & Write Permission Granted",Toast.LENGTH_SHORT).show()
        }else if (readPermissionGranted && readImagesPermissionGranted && cameraPermissionGranted){
            loadImages()
            Toast.makeText(this,"Read Images Permission Granted",Toast.LENGTH_SHORT).show()
        }else {
            showPermissionDialog()
        }
    }

    private val activityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        checkPermission()
    }

    private fun checkPermission(){

        val hasReadPermission = ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        val hasWritePermission = ContextCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        val hasReadImagesPermission = ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED
        val hasCameraPermission = ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

        val minSdk29 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

        readPermissionGranted = hasReadPermission || Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
        writePermissionGranted = hasWritePermission || minSdk29
        readImagesPermissionGranted = hasReadImagesPermission || Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU
        cameraPermissionGranted = hasCameraPermission

        if (readPermissionGranted && writePermissionGranted && cameraPermissionGranted) {
            loadImages()
            Toast.makeText(this,"Read & Write Permission Granted",Toast.LENGTH_SHORT).show()
        } else if (readImagesPermissionGranted && cameraPermissionGranted) {
            loadImages()
            Toast.makeText(this,"Read Images Permission Granted",Toast.LENGTH_SHORT).show()
        } else {
            permissionLauncher.launch(PERMISSIONS)
        }
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
    private fun showPermissionDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Permission Required")
        builder.setMessage("Some permissions are needed to be allowed to use this features")
        builder.setPositiveButton("Grant") { d, _ ->
            d.cancel()
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:$packageName"))
            activityLauncher.launch(intent)
        }
        builder.setNegativeButton("Cancel") { d, _ ->
            d.dismiss()
        }
        builder.show()
    }
}