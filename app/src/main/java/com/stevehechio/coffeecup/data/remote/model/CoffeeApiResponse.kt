package com.stevehechio.coffeecup.data.remote.model

import com.stevehechio.coffeecup.data.local.entities.CoffeeEntity

/**
// Created by Steve Hechio on 14/10/2022.
 */
data class CoffeeApiResponse(
    val coffeeList: List<CoffeeEntity>
)

data class CoffeeRandomResponse(
    val file: String
)