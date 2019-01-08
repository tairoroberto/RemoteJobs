package com.remotejobs.io.app.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "favorites")
data class Favorite(
        @PrimaryKey
        @ColumnInfo(name = "name")
        var name: String = ""
) : Parcelable