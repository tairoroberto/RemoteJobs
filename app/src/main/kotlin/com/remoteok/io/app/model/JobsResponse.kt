package com.remoteok.io.app.model

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by tairo on 14/11/17.
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class JobsResponse(
        @SerializedName("date")
        var date: String,

        @SerializedName("items")
        var list: List<Job>? = ArrayList()) : Parcelable