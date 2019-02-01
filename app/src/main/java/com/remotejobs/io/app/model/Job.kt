package com.remotejobs.io.app.model

import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Entity(tableName = "jobs")
@Parcelize
data class Job(

        @PrimaryKey
        @ColumnInfo(name = "id")
        @SerializedName("id")
        var id: String = "",

        @ColumnInfo(name = "categoryKey")
        @SerializedName("categoryKey")
        var categoryKey: String = "",

        @ColumnInfo(name = "companyKey")
        @SerializedName("companyKey")
        var companyKey: String = "",

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