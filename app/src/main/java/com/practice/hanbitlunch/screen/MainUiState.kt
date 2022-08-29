package com.practice.hanbitlunch.screen

import com.practice.database.meal.entity.MealEntity
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import java.time.LocalDate

data class MainUiState(
    val year: Int,
    val month: Int,
    val selectedDate: LocalDate,
    val mealUiState: MealUiState,
    val scheduleUiState: ScheduleUiState,
)

/**
 * Ui state of meal
 */

data class MealUiState(
    val menus: ImmutableList<Menu>
) {
    val isEmpty: Boolean
        get() = menus.isEmpty()

    companion object {
        val EmptyMealState = MealUiState(persistentListOf())
    }
}

fun MealEntity.toMealUiState() = if (this.menus.isEmpty()) {
    MealUiState.EmptyMealState
} else {
    MealUiState(menus.map { Menu(it.name) }.toPersistentList())
}

data class Menu(
    val name: String,
)

/**
 * Ui state of schedule
 */

data class ScheduleUiState(
    val schedules: ImmutableList<Schedule>,
) {
    val isEmpty: Boolean
        get() = schedules.isEmpty()

    companion object {
        val EmptyScheduleState = ScheduleUiState(persistentListOf())
    }
}

data class Schedule(
    val scheduleName: String,
)