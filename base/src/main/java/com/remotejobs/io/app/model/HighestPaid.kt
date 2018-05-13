package com.remotejobs.io.app.model

import android.annotation.SuppressLint
import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Entity(tableName = "highestpaid")
@Parcelize
data class HighestPaid(

        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        var id: Long = 0,

        @ColumnInfo(name = "amount")
        @SerializedName("amount")
        var amount: String = "",

        @ColumnInfo(name = "deviation")
        @SerializedName("deviation")
        var deviation: String = "",

        @ColumnInfo(name = "salary")
        @SerializedName("salary")
        var salary: String = "",

        @ColumnInfo(name = "order")
        @SerializedName("order")
        var order: String = "",

        @ColumnInfo(name = "tags")
        @SerializedName("tags")
        var tags: String = "") : Parcelable