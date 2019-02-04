package com.remotejobs.io.app.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.remotejobs.io.app.data.database.dao.CompaniesDao
import com.remotejobs.io.app.data.database.dao.FavoritesDao
import com.remotejobs.io.app.data.database.dao.JobsDao
import com.remotejobs.io.app.model.Company
import com.remotejobs.io.app.model.Favorite
import com.remotejobs.io.app.model.Job


@Database(entities = [(Job::class), (Company::class), (Favorite::class)], version = 9, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun jobsDAO(): JobsDao
    abstract fun companiesDAO(): CompaniesDao
    abstract fun favoritesDaoDao(): FavoritesDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context?): AppDatabase =
                INSTANCE ?: synchronized(this) {
                    INSTANCE
                            ?: buildDatabase(context as Context).also { INSTANCE = it }
                }


        /*private val MIGRATION_1_5: Migration = object : Migration(1, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Since we didn't alter the table, there's nothing else to do here.
            }
        }*/

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext,
                        AppDatabase::class.java, "remotejobs.db")
                        //.addMigrations(MIGRATION_1_5)
                        .fallbackToDestructiveMigration()
                        .build()
    }
}
