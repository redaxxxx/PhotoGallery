package com.example.photogallery

import android.content.pm.PackageManager
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.example.photogallery.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var image_list: ArrayList<String>
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        image_list = ArrayList()

        checkPermission()
    }

    private fun checkPermission(){
        val result = ContextCompat.checkSelfPermission(applicationContext, android.Manifest.permission.READ_EXTERNAL_STORAGE)
        if (result == PackageManager.PERMISSION_GRANTED){
            loadImages()
        }else {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                100)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults.isNotEmpty()){
            val accepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
            if (accepted){
                loadImages()
            }else {
                Toast.makeText(this, "You have denied the permissions...", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadImages() {
        val sdCard = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)
        if (sdCard){
            val projection = arrayOf(
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media._ID
            )

            val order = MediaStore.Images.Media.DATE_TAKEN + " DESC"
            val cursor: Cursor? = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
                null, null, order)

            val count = cursor!!.count
            binding.totalItemsTv.setText("Total items: " + count)
            val columnIndex = cursor!!.getColumnIndex(MediaStore.Images.Media.DATA)
            while (cursor.moveToFirst()){
                image_list.add(cursor.getString(columnIndex))
                prepareRecyclerView(image_list)
            }
            cursor.close()
        }

    }

    private fun prepareRecyclerView(images: ArrayList<String>){
        val adapter = GalleryAdapter(images, this)
        val managerLayout = GridLayoutManager(this, 3)
        binding.imagesRv.layoutManager = managerLayout
        binding.imagesRv.adapter = adapter

        adapter.notifyDataSetChanged()
    }
}