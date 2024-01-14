package com.practice.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.practice.main.calendar.CalendarMainScreen
import com.practice.main.daily.DailyMainScreen
import com.practice.main.loading.LoadingMainScreen
import com.practice.main.popup.MainScreenModePopup
import com.practice.main.popup.MemoPopup
import com.practice.main.popup.NutrientPopup
import com.practice.main.popup.popupPadding
import com.practice.main.state.MainUiMode

@Composable
fun MainScreen(
    windowSize: WindowSizeClass,
    viewModel: MainScreenViewModel,
    onNavigateToSelectSchoolScreen: () -> Unit,
    onNavigateToSettingsScreen: () -> Unit,
    modifier: Modifier = Modifier,
    onLaunch: suspend () -> Unit = {},
) {
    val systemUiController = rememberSystemUiController()
    val systemBarColor = MaterialTheme.colorScheme.surface
    LaunchedEffect(true) {
        systemUiController.setStatusBarColor(systemBarColor)
        systemUiController.setNavigationBarColor(systemBarColor)
        onLaunch()
        viewModel.onLaunch()
    }

    val uiState by viewModel.uiState

    val backgroundModifier = modifier.background(MaterialTheme.colorScheme.surface)

    Scaffold {
        val paddingModifier = backgroundModifier.padding(it)
        when (uiState.mainUiMode) {
            MainUiMode.LOADING -> {
                LoadingMainScreen(
                    modifier = paddingModifier,
                )
            }

            MainUiMode.NOT_SET, MainUiMode.CALENDAR -> {
                CalendarMainScreen(
                    windowSize = windowSize,
                    viewModel = viewModel,
                    onNavigateToSettingsScreen = onNavigateToSettingsScreen,
                    onNavigateToSelectSchoolScreen = onNavigateToSelectSchoolScreen,
                    modifier = paddingModifier,
                )
            }

            MainUiMode.DAILY -> {
                DailyMainScreen(
                    windowSize = windowSize,
                    viewModel = viewModel,
                    onNavigateToSettingsScreen = onNavigateToSettingsScreen,
                    onNavigateToSelectSchoolScreen = onNavigateToSelectSchoolScreen,
                    modifier = paddingModifier,
                )
            }
        }
    }

    if (uiState.isNutrientPopupVisible) {
        val uiMeal = uiState.selectedDateDataState.uiMeals
        MainScreenPopup(
            onClose = viewModel::closeNutrientPopup,
        ) {
            NutrientPopup(
                uiMeal = uiMeal,
                nutrients = uiMeal.nutrients,
                onClose = viewModel::closeNutrientPopup,
                modifier = Modifier.padding(popupPadding),
            )
        }
    }
    if (uiState.isMemoPopupVisible) {
        MainScreenPopup(onClose = viewModel::closeMemoPopup) {
            MemoPopup(
                date = uiState.selectedDateDataState.uiMemos.date,
                memoPopupElements = uiState.selectedDateDataState.memoPopupElements,
                onAddMemo = viewModel::addMemo,
                onContentsChange = viewModel::updateMemoOnLocal,
                onMemoDelete = viewModel::deleteMemo,
                onPopupClose = viewModel::closeMemoPopup,
                modifier = Modifier.padding(popupPadding),
            )
        }
    }

    if (uiState.mainUiMode == MainUiMode.NOT_SET) {
        MainScreenPopup(onClose = { viewModel.onMainScreenModeSet(MainUiMode.CALENDAR) }) {
            MainScreenModePopup(
                onScreenModeSet = viewModel::onMainScreenModeSet,
                modifier = Modifier
                    .padding(popupPadding)
                    .widthIn(max = 600.dp),
            )
        }
    }
}

val WindowSizeClass.mealColumns: Int
    get() = if (this.widthSizeClass == WindowWidthSizeClass.Compact) 2 else 3