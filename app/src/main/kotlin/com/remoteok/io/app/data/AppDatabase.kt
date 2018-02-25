package com.remoteok.io.app.data

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.arch.persistence.room.migration.Migration
import android.content.Context
import com.remoteok.io.app.data.dao.CompaniesDao
import com.remoteok.io.app.data.dao.JobsDao
import com.remoteok.io.app.model.Company
import com.remoteok.io.app.model.Job


@Database(entities = [(Job::class), (Company::class)], version = 4)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun jobsDAO(): JobsDao
    abstract fun companiesDAO(): CompaniesDao

    companion object {

        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context?): AppDatabase =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: buildDatabase(context as Context).also { INSTANCE = it }
                }


        private val MIGRATION_1_4: Migration = object : Migration(1, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Since we didn't alter the table, there's nothing else to do here.
            }
        }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext,
                        AppDatabase::class.java, "remoteok.db")
                        .addMigrations(MIGRATION_1_4)
                        .fallbackToDestructiveMigration()
                        .build()
    }
}
