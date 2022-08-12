package com.practice.hanbitlunch.calendar

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.domain.date.DayType
import com.example.domain.date.calculateDayType
import com.example.domain.date.toKor
import com.practice.hanbitlunch.theme.HanbitCalendarTheme
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.*

@Composable
fun Calendar(
    modifier: Modifier = Modifier,
    calendarState: CalendarState = rememberCalendarState()
) {
    // TODO: movable content 적용하기
    val calendarDays = calendarDays()
    val calendarDates = CalendarRow.getInstance(calendarState.year, calendarState.month).dates
    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = modifier
            .fillMaxWidth()
    ) {
        items(items = calendarDays, key = { it.ordinal }) {
            CalendarDay(day = it)
        }
        items(items = calendarDates, key = { it.hashCode() }) {
            CalendarDate(
                date = it,
                onClick = { /* TODO: 구현 */ },
                currentMonth = calendarState.month,
                isSelected = (it == calendarState.selectedDate),
            )
        }
    }
}

private fun calendarDays(): List<DayOfWeek> {
    val values = DayOfWeek.values().toList()
    Collections.rotate(values, 1)
    return values
}

/**
 * 달력 맨 윗 줄의 요일
 */
@Composable
private fun CalendarDay(
    day: DayOfWeek,
    modifier: Modifier = Modifier,
) {
    CalendarElement(
        text = day.toKor(),
        modifier = modifier,
        textColor = day.color(),
    )
}

private val WeekdayColor = Color(0xFF000000)
private val SaturdayColor = Color(0xFF5151FF)
private val HolidayColor = Color(0xFFFF5F5F)

private fun DayOfWeek.color() = when (this) {
    DayOfWeek.SUNDAY -> HolidayColor
    DayOfWeek.SATURDAY -> SaturdayColor
    else -> WeekdayColor
}

/**
 * 실제 날짜
 */
@Composable
private fun CalendarDate(
    date: LocalDate,
    onClick: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
    currentMonth: Int = date.monthValue,
    isSelected: Boolean = false,
) {
    val background by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colors.secondary else Color.Transparent
    )
    CalendarElement(
        text = date.dayOfMonth.toString(),
        modifier = modifier
            .clip(shape = CircleShape)
            .background(color = background)
            .clickable { onClick(date) },
        textColor = date.color(currentMonth),
        textStyle = MaterialTheme.typography.body2,
    )
}

@Composable
private fun CalendarElement(
    text: String,
    modifier: Modifier = Modifier,
    textColor: Color = Color.Unspecified,
    textStyle: TextStyle = MaterialTheme.typography.body1,
) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .then(modifier)
            .aspectRatio(1f)
    ) {
        Text(
            text = text,
            modifier = Modifier.align(Alignment.Center),
            color = textColor,
            style = textStyle,
        )
    }
}

@Composable
private fun LocalDate.color(currentMonth: Int = this.monthValue) =
    when (calculateDayType(currentMonth)) {
        DayType.Weekday -> WeekdayColor
        DayType.WeekdayOverMonth -> Color(0xFF999999)
        DayType.Saturday -> SaturdayColor
        DayType.SaturdayOverMonth -> Color(0xFF9999FF)
        DayType.Holiday -> HolidayColor
        DayType.HolidayOverMonth -> Color(0xFFFF9999)
    }

@Preview(showBackground = true)
@Composable
private fun CalendarDayPreview() {
    HanbitCalendarTheme {
        CalendarDay(
            day = DayOfWeek.MONDAY,
            modifier = Modifier.size(50.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CalendarDatePreview() {
    HanbitCalendarTheme {
        CalendarDate(
            date = LocalDate.of(2022, 8, 12),
            onClick = {},
            modifier = Modifier.size(50.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CalendarDatePreview_Selected() {
    HanbitCalendarTheme {
        CalendarDate(
            date = LocalDate.of(2022, 8, 17),
            onClick = {},
            isSelected = true,
            modifier = Modifier.size(50.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CalendarPreview() {
    val calendarState = rememberCalendarState(
        year = 2022,
        month = 8,
        selectedDate = LocalDate.of(2022, 8, 12)
    )
    HanbitCalendarTheme {
        Calendar(calendarState = calendarState)
    }
}