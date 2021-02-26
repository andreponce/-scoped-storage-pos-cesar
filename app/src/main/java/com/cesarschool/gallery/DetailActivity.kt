package com.cesarschool.gallery

import android.app.Activity
import android.app.RecoverableSecurityException
import android.content.ContentUris
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.cesarschool.gallery.databinding.ActivityMainBinding
import com.cesarschool.gallery.databinding.DetailActivityBinding
import com.cesarschool.gallery.model.Media

class DetailActivity : AppCompatActivity() {

    companion object{
        const val EXTRA = "midia"
        const val REQUEST_DELETE = 1
    }

    private lateinit var binding: DetailActivityBinding
    private lateinit var media: Media

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DetailActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        media = intent.getParcelableExtra<Media>(EXTRA)!!;
        binding.image.setImageURI(media.uri)

        binding.deleteBt.setOnClickListener {
            deleteMedia(media.uri);
        }
    }

    private fun deleteMedia(uri: Uri) {
        try {
            contentResolver.delete(uri, "${MediaStore.Images.Media._ID} = ?", arrayOf(ContentUris.parseId(uri).toString()))
            setResult(Activity.RESULT_OK, intent)
            finish()
        } catch (securityException: SecurityException) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val recoverSecException = securityException as? RecoverableSecurityException ?: throw securityException
                val intentSender = recoverSecException.userAction.actionIntent.intentSender
                startIntentSenderForResult(intentSender, REQUEST_DELETE, null, 0, 0, 0, null)
            }else throw securityException
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == REQUEST_DELETE) deleteMedia(media.uri)
    }
}