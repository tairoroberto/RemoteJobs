package com.remotejobs.io.app.model

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class CompaniesResponse(@SerializedName("date")
                             var date: String = "",
                             @SerializedName("items")
                             var items: List<Company>? = ArrayList()) : Parcelable