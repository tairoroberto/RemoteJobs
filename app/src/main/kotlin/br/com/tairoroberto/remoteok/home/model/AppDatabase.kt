package com.tairoroberto.nextel.home.model

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import br.com.tairoroberto.remoteok.home.model.domain.Job
import com.tairoroberto.nextel.base.converter.DateConverter
import com.tairoroberto.nextel.home.model.dao.jobsDAO

@Database(entities = arrayOf(Job::class), version = 1)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun jobsDAO(): jobsDAO

    companion object {

        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context?): AppDatabase =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: buildDatabase(context as Context).also { INSTANCE = it }
                }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext,
                        AppDatabase::class.java, "remoteok.db")
                        .build()
    }
}
