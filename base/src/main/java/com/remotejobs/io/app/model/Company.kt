package com.remotejobs.io.app.model

import android.annotation.SuppressLint
import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Entity(tableName = "companies")
@Parcelize
data class Company(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        var id: Long = 0,

        @ColumnInfo(name = "image")
        @SerializedName("image")
        var image: String = "",

        @ColumnInfo(name = "rank")
        @SerializedName("rank")
        var rank: String = "",

        @ColumnInfo(name = "company")
        @SerializedName("company")
        var company: String = "",

        @ColumnInfo(name = "aggregateRating")
        @SerializedName("aggregateRating")
        var aggregateRating: String = "",

        @ColumnInfo(name = "tags")
        @SerializedName("tags")
        var tags: List<String>? = ArrayList()) : Parcelable