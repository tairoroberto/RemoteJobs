package com.tairoroberto.nextel.base.converter

import android.arch.persistence.room.TypeConverter

import java.util.Date

object DateConverter {
    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        return if (timestamp == null) null else Date(timestamp)
    }

    @TypeConverter
    fun toTimestamp(date: Date?): Long? {
        return date?.time
    }
}
