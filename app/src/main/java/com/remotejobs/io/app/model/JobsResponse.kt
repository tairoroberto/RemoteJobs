package com.remotejobs.io.app.model

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
        @SerializedName("success")
        var success: Boolean = false,

        @SerializedName("response")
        var response: List<Job>? = ArrayList(),

        @SerializedName("lastItem")
        var lastItem: String? = ""
) : Parcelable