package com.remotejobs.io.app.model

import android.annotation.SuppressLint
import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Entity(tableName = "jobs")
@Parcelize
data class Job(
        @ColumnInfo(name = "date")
        @SerializedName("date")
        var date: String = "",

        @ColumnInfo(name = "logo")
        @SerializedName("logo")
        var logo: String = "",

        @ColumnInfo(name = "description")
        @SerializedName("description")
        var description: String = "",

        @ColumnInfo(name = "epoch")
        @SerializedName("epoch")
        var epoch: String = "",

        @ColumnInfo(name = "company")
        @SerializedName("company")
        var company: String = "",

        @PrimaryKey
        @ColumnInfo(name = "id")
        @SerializedName("id")
        var id: String = "",

        @ColumnInfo(name = "position")
        @SerializedName("position")
        var position: String = "",

        @ColumnInfo(name = "slug")
        @SerializedName("slug")
        var slug: String = "",

        @ColumnInfo(name = "url")
        @SerializedName("url")
        var url: String = "",

        @ColumnInfo(name = "tags")
        @SerializedName("tags")
        var tags: List<String>? = ArrayList(),

        @ColumnInfo(name = "url_apply")
        @SerializedName("url_apply")
        var urlApply: String? = "") : Parcelable