package com.remotejobs.io.app.model

import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Entity(tableName = "companies")
@Parcelize
data class Company(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        var id: Long = 0,

        @ColumnInfo(name = "logo")
        @SerializedName("logo")
        var logo: String = "",

        @ColumnInfo(name = "name")
        @SerializedName("name")
        var name: String = "") : Parcelable