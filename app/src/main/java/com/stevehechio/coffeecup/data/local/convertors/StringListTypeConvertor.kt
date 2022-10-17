package com.stevehechio.coffeecup.data.local.convertors

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
// Created by Steve Hechio on 17/10/2022.
 */
object StringListTypeConvertor {
    @TypeConverter
    fun fromStringToStringList(value: String): List<String>{
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromStringListToString(list: List<String>): String = Gson().toJson(list)
}