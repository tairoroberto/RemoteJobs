package com.remotejobs.io.app.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CategoriesResponse(@SerializedName("success")
                              val success: Boolean? = false,
                              @SerializedName("response")
                              val response: List<String>? = ArrayList()) : Parcelable