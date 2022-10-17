package com.stevehechio.coffeecup.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.stevehechio.coffeecup.data.local.convertors.StringListTypeConvertor
import com.stevehechio.coffeecup.utils.AppConstants
import java.io.Serializable

/**
// Created by Steve Hechio on 14/10/2022.
 */
@Entity(tableName = AppConstants.TABLE_NAME)
data class CoffeeEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val description: String,
    @TypeConverters(StringListTypeConvertor::class)
    val ingredients: List<String>,
    val image: String,
    var type: String
): Serializable
