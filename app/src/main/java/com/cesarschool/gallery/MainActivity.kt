package com.cesarschool.gallery

import android.Manifest
import android.app.Activity
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.cesarschool.gallery.adapter.MediaStoreAdapter
import com.cesarschool.gallery.databinding.ActivityMainBinding
import com.cesarschool.gallery.model.Media

class MainActivity : AppCompatActivity() {
    companion object{
        const val READ_EXTERNAL_REQUEST_CODE = 1
        const val REMOVE_MEDIA_REQUEST_CODE = 2
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter :MediaStoreAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setup();
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == READ_EXTERNAL_REQUEST_CODE){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) loadMedias();
            else Toast.makeText(this, "Permissão negada.", Toast.LENGTH_LONG).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK){
            if(requestCode==REMOVE_MEDIA_REQUEST_CODE){
                loadMedias();
                Toast.makeText(this, "IMAGEM DELETADA!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private fun setup(){
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), READ_EXTERNAL_REQUEST_CODE)
    }

    private fun loadMedias() {
        val list = mutableListOf<Media>()
        val projection = arrayOf(MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.DISPLAY_NAME)
        contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, null ).use {
            it?.let { cursor ->
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns._ID)
                val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DISPLAY_NAME)
                while(cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val name = cursor.getString(nameColumn)
                    val uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                    list.add(Media(id, name, uri))
                }
            }
        }
        adapter = MediaStoreAdapter(this, list);
        binding.recycle.adapter = adapter;
    }
}