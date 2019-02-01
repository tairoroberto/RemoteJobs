package com.remotejobs.io.app.model

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class CompaniesResponse(
        @SerializedName("success")
        var success: Boolean = false,
        @SerializedName("response")
        var items: List<Company>? = ArrayList(),
        @SerializedName("message")
        var message: String = "") : Parcelable