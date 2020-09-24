package com.example.kovid.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.kovid.data.entities.StateValue
import com.example.kovid.data.entities.USValues

@Database(entities = arrayOf(StateValue::class, USValues::class), version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun covidDao(): CovidDao

    companion object {
        @Volatile private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            instance ?: synchronized(this) { instance ?: buildDatabase(context).also { instance = it } }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, AppDatabase::class.java, "coviddb")
                .fallbackToDestructiveMigration()
                .build()
    }

}