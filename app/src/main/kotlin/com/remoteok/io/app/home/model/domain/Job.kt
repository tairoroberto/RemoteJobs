package com.remoteok.io.app.home.model.domain

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Entity(tableName = "jobs")
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
        var tags: List<String>? = null) : Parcelable {

        constructor(parcel: Parcel) : this(
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.createStringArrayList())

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeString(date)
                parcel.writeString(logo)
                parcel.writeString(description)
                parcel.writeString(epoch)
                parcel.writeString(company)
                parcel.writeString(id)
                parcel.writeString(position)
                parcel.writeString(slug)
                parcel.writeString(url)
                parcel.writeStringList(tags)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<Job> {
                override fun createFromParcel(parcel: Parcel): Job {
                        return Job(parcel)
                }

                override fun newArray(size: Int): Array<Job?> {
                        return arrayOfNulls(size)
                }
        }
}