package com.remotejobs.io.app.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "favorites")
data class Favorite(
        @PrimaryKey
        @ColumnInfo(name = "name")
        var name: String = ""
) : Parcelable