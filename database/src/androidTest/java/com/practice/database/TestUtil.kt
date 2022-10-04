package com.practice.database

import com.practice.database.meal.entity.MealEntity
import com.practice.database.meal.entity.MenuEntity
import com.practice.database.meal.entity.NutrientEntity
import com.practice.database.meal.entity.OriginEntity
import com.practice.database.meal.room.MealEntityRoom
import com.practice.database.schedule.entity.ScheduleEntity
import com.practice.database.schedule.room.ScheduleEntityRoom

object TestUtil {
    fun createMealEntityRoom(count: Int): List<MealEntityRoom> {
        return (0 until count).map {
            MealEntityRoom(
                date = (20220601 + it).toString(),
                menu = "menu $it",
                origin = "origin $it",
                calorie = 0.0,
                nutrient = "nutrient $it",
                schoolCode = 0
            )
        }
    }

    fun createMealEntity(count: Int): List<MealEntity> = (0 until count).map {
        MealEntity(
            schoolCode = 10,
            year = 2022,
            month = 5,
            day = 1 + it,
            menus = listOf(MenuEntity(name = "menu", allergies = emptyList())),
            origins = listOf(OriginEntity(ingredient = "rice", origin = "korea")),
            calorie = 10.0,
            nutrients = listOf(NutrientEntity("protein", "g", 10.0))
        )
    }

    fun createScheduleEntityRoom(count: Int): List<ScheduleEntityRoom> = (0 until count).map {
        ScheduleEntityRoom(
            id = it,
            date = (20220601 + it).toString(),
            eventName = "event $it",
            eventContent = "content $it",
        )
    }

    fun createScheduleEntity(count: Int): List<ScheduleEntity> = (0 until count).map {
        ScheduleEntity(
            id = it,
            year = 2022,
            month = 6,
            day = 1 + it,
            eventName = "event $it",
            eventContent = "content $it"
        )
    }
}