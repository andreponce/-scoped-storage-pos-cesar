package com.cesarschool.gallery.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cesarschool.gallery.DetailActivity
import com.cesarschool.gallery.MainActivity
import com.cesarschool.gallery.R
import com.cesarschool.gallery.databinding.ItemGalleryBinding
import com.cesarschool.gallery.model.Media

class MediaStoreAdapter(private val context: Activity, var originalDataSet: List<Media>) : RecyclerView.Adapter<MediaStoreAdapter.MediaStoreHolder>() {

    public var dataSet: ArrayList<Media> = ArrayList<Media>()

    init {
        dataSet.addAll(originalDataSet)
    }

    class MediaStoreHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemGalleryBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaStoreHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_gallery, parent, false);
        return MediaStoreHolder(view)
    }

    override fun getItemCount(): Int {
        return this.dataSet.size
    }

    override fun onBindViewHolder(holder: MediaStoreHolder, position: Int) {
        val media = dataSet.get(position)
        with(holder) {
            binding.image.setImageURI(media.uri)
            binding.nameTxt.text = media.name

            binding.root.setOnClickListener {
                val detailActivity = Intent(context, DetailActivity::class.java)
                detailActivity.putExtra(DetailActivity.EXTRA,media)
                context.startActivityForResult(detailActivity, MainActivity.REMOVE_MEDIA_REQUEST_CODE);
            }
        }
    }
}