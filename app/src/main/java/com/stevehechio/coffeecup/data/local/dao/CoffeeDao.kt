package com.stevehechio.coffeecup.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.stevehechio.coffeecup.data.local.entities.CoffeeEntity
import io.reactivex.rxjava3.core.Observable

/**
// Created by Steve Hechio on 14/10/2022.
 */

@Dao
interface CoffeeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(coffeeEntity: List<CoffeeEntity>)

    @Query("DELETE FROM COFFEE_CUP_TABLE")
    fun deleteAll()

    @Query("SELECT * FROM coffee_cup_table WHERE type = :type")
    fun getCoffeeDetails(type: String): Observable<List<CoffeeEntity>>
}