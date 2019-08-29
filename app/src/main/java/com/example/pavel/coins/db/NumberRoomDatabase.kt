package com.example.pavel.coins.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.example.pavel.coins.Model.Number


@Database(entities = [Number::class], version = 3)
abstract class NumberRoomDatabase: RoomDatabase() {
    abstract fun numberDao(): NumberDAO


    companion object {
        @Volatile
        private var INSTANCE: NumberRoomDatabase? = null

        fun getInstance(context: Context): NumberRoomDatabase?{
            if (INSTANCE == null){
                synchronized(NumberRoomDatabase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        NumberRoomDatabase::class.java,
                        "number.db").fallbackToDestructiveMigration().build()
                }
            }
            return INSTANCE
        }
    }
}