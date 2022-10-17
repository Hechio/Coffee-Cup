package com.stevehechio.coffeecup.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.stevehechio.coffeecup.data.local.convertors.StringListTypeConvertor
import com.stevehechio.coffeecup.data.local.dao.CoffeeDao
import com.stevehechio.coffeecup.data.local.entities.CoffeeEntity
import com.stevehechio.coffeecup.utils.AppConstants

/**
// Created by Steve Hechio on 14/10/2022.
 */

@Database(
    entities = [CoffeeEntity::class],
    version = AppConstants.DB_VERSION, exportSchema = false
)
@TypeConverters(StringListTypeConvertor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun coffeeDao(): CoffeeDao

    //Room should only be initiated once, marked volatile to be thread safe.
    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance
            ?: synchronized(LOCK){
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                AppConstants.DB_NAME
            ).fallbackToDestructiveMigration()
                .build()


    }

}