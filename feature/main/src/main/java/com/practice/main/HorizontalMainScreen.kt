package com.practice.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Preview
import com.hsk.ktx.date.Date
import com.practice.designsystem.calendar.core.CalendarState
import com.practice.designsystem.calendar.core.YearMonth
import com.practice.designsystem.calendar.core.rememberCalendarState
import com.practice.designsystem.calendar.largeCalendarDateShape
import com.practice.designsystem.theme.BlindarTheme
import com.practice.domain.School
import com.practice.main.state.DailyMealScheduleState
import com.practice.main.state.MainUiState
import com.practice.main.state.MealUiState
import com.practice.main.state.ScheduleUiState
import com.practice.preferences.ScreenMode

@Composable
fun HorizontalMainScreen(
    calendarPageCount: Int,
    modifier: Modifier = Modifier,
    uiState: MainUiState,
    onScreenModeChange: (ScreenMode) -> Unit,
    calendarState: CalendarState,
    mealColumns: Int,
    onDateClick: (Date) -> Unit,
    onSwiped: (YearMonth) -> Unit,
    getContentDescription: (Date) -> String,
    getClickLabel: (Date) -> String,
    drawUnderlineToScheduleDate: DrawScope.(Date) -> Unit,
    onNavigateToSelectSchoolScreen: () -> Unit,
) {
    Column(modifier = modifier) {
        MainScreenHeader(
            year = uiState.year,
            month = uiState.month,
            screenModeIconsEnabled = false,
            selectedScreenMode = uiState.screenMode,
            onScreenModeIconClick = onScreenModeChange,
        )
        Row {
            CalendarCard(
                calendarPageCount = calendarPageCount,
                modifier = Modifier.weight(1f),
                calendarState = calendarState,
                onDateClick = onDateClick,
                onSwiped = onSwiped,
                getContentDescription = getContentDescription,
                getClickLabel = getClickLabel,
                drawBehindElement = drawUnderlineToScheduleDate,
                dateShape = largeCalendarDateShape,
                dateArrangement = Arrangement.Top,
            )
            MainScreenContents(
                mealUiState = uiState.selectedDateMealScheduleState.mealUiState,
                scheduleUiState = uiState.selectedDateMealScheduleState.scheduleUiState,
                mealColumns = mealColumns,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            )
        }
    }
}

@Preview(showBackground = true, device = "spec:width=1280dp,height=800dp,dpi=480")
@Composable
private fun HorizontalMainScreenPreview() {
    val year = 2022
    val month = 10
    val selectedDate = Date(2022, 10, 11)

    val uiState = MainUiState(
        year = year,
        month = month,
        selectedDate = selectedDate,
        monthlyMealScheduleState = (1..3).map {
            DailyMealScheduleState(
                schoolCode = 1,
                date = Date(2022, 10, 11).plusDays(it),
                mealUiState = MealUiState(previewMenus),
                scheduleUiState = ScheduleUiState(previewSchedules),
            )
        },
        isLoading = false,
        screenMode = ScreenMode.Default,
        selectedSchool = School(
            name = "어떤 학교",
            schoolCode = -1,
        ),
    )
    val calendarState = rememberCalendarState(
        year = year,
        month = month,
        selectedDate = selectedDate,
    )
    BlindarTheme {
        HorizontalMainScreen(
            calendarPageCount = 13,
            modifier = Modifier.background(MaterialTheme.colorScheme.surface),
            uiState = uiState,
            onScreenModeChange = {},
            calendarState = calendarState,
            mealColumns = 3,
            onDateClick = {},
            onSwiped = { },
            getContentDescription = { "" },
            getClickLabel = { "" },
            drawUnderlineToScheduleDate = {},
            onNavigateToSelectSchoolScreen = {},
        )
    }
}