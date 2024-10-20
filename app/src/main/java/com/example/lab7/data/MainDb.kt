package com.example.lab7.data
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Group::class, Student::class], version = 1)
abstract class MainDb : RoomDatabase() {
    abstract fun getDao() : MainDbDao

    companion object {
        fun getDb(context: Context) : MainDb
        {
            return Room.databaseBuilder(
                context.applicationContext,
                MainDb::class.java,
                "main.db"
            ).build()
        }
    }
}