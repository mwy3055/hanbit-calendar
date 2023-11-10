package com.practice.main.state

import com.hsk.ktx.date.Date
import com.practice.designsystem.calendar.core.YearMonth
import com.practice.domain.School
import com.practice.domain.meal.Meal
import com.practice.domain.schedule.Schedule
import com.practice.preferences.ScreenMode
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

data class MainUiState(
    val year: Int,
    val month: Int,
    val selectedDate: Date,
    val monthlyDataState: List<DailyDataState>,
    val isLoading: Boolean,
    val screenMode: ScreenMode,
    val selectedSchool: School,
    val isNutrientPopupVisible: Boolean,
) {
    val yearMonth: YearMonth
        get() = YearMonth(year, month)

    val selectedSchoolCode: Int
        get() = selectedSchool.schoolCode

    val selectedDateDataState: DailyDataState
        get() = monthlyDataState.firstOrNull { it.date == selectedDate }
            ?: DailyDataState.Empty
}

/**
 * Ui state of meal
 */

data class MealUiState(
    val year: Int,
    val month: Int,
    val day: Int,
    val menus: ImmutableList<Menu>,
    val nutrients: ImmutableList<Nutrient>,
) {
    val isEmpty: Boolean
        get() = menus.isEmpty()

    val description: String
        get() = if (menus.isEmpty()) "식단이 없습니다." else menus.joinToString(", ") { it.name }

    companion object {
        val EmptyMealState = MealUiState(
            year = Date.now().year,
            month = Date.now().month,
            day = Date.now().dayOfMonth,
            menus = persistentListOf(),
            nutrients = persistentListOf(),
        )
    }
}

fun Meal.toMealUiState() = if (this.menus.isEmpty()) {
    MealUiState.EmptyMealState
} else {
    MealUiState(
        year = year,
        month = month,
        day = day,
        menus = menus.filter { it.name != "급식우유" }.map { Menu(it.name) }.toPersistentList(),
        nutrients = nutrients.map { Nutrient(it.name, it.amount, it.unit) }.toPersistentList()
            .add(0, Nutrient("열량", calorie, "kcal")),
    )
}

data class Menu(
    val name: String,
)

data class Nutrient(
    val name: String,
    val amount: Double,
    val unit: String,
) {
    val description: String
        get() = "$name $amount $unit"
}

/**
 * Ui state of schedule
 */

data class ScheduleUiState(
    val schedules: ImmutableList<Schedule>,
) {
    val isEmpty: Boolean
        get() = schedules.isEmpty()

    val description: String
        get() = if (schedules.isEmpty()) "학사일정이 없습니다." else schedules.joinToString(", ") { it.displayText }

    companion object {
        val EmptyScheduleState = ScheduleUiState(persistentListOf())
    }
}

val Schedule.displayText: String
    get() = if (eventName == eventContent) eventName else "$eventName - $eventContent"