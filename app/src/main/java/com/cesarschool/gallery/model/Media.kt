package com.cesarschool.gallery.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Media(
    val id :Long,
    val name :String,
    val uri : Uri
) : Parcelable