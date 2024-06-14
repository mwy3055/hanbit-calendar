package com.practice.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.practice.designsystem.a11y.isLargeFont
import com.practice.designsystem.components.BlindarDialog
import com.practice.designsystem.components.dialogContentPadding
import com.practice.main.calendar.CalendarMainScreen
import com.practice.main.daily.DailyMainScreen
import com.practice.main.dialog.MealDialogContents
import com.practice.main.dialog.MemoDialogContents
import com.practice.main.dialog.NutrientDialogContents
import com.practice.main.dialog.ScheduleDialogContents
import com.practice.main.loading.LoadingMainScreen
import com.practice.main.state.MainUiMode
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
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

    val mealPagerState = rememberPagerState { uiState.selectedDateDataState.uiMeals.mealTimes.size }

    Scaffold {
        val paddingModifier = backgroundModifier.padding(it)
        when (uiState.mainUiMode) {
            MainUiMode.LOADING -> {
                LoadingMainScreen(
                    modifier = paddingModifier,
                )
            }

            MainUiMode.CALENDAR -> {
                CalendarMainScreen(
                    windowSize = windowSize,
                    viewModel = viewModel,
                    mealPagerState = mealPagerState,
                    onNavigateToSettingsScreen = onNavigateToSettingsScreen,
                    onNavigateToSelectSchoolScreen = onNavigateToSelectSchoolScreen,
                    modifier = paddingModifier,
                )
            }

            MainUiMode.DAILY -> {
                DailyMainScreen(
                    windowSize = windowSize,
                    viewModel = viewModel,
                    mealPagerState = mealPagerState,
                    onNavigateToSettingsScreen = onNavigateToSettingsScreen,
                    onNavigateToSelectSchoolScreen = onNavigateToSelectSchoolScreen,
                    modifier = paddingModifier,
                )
            }
        }
    }
    MainScreenDialogs(
        viewModel = viewModel,
        mealPagerState = mealPagerState,
    )
}

val WindowSizeClass.mealColumns: Int
    @Composable get() = when {
        LocalDensity.current.isLargeFont -> 1
        this.widthSizeClass == WindowWidthSizeClass.Compact -> 2
        else -> 3
    }

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MainScreenDialogs(
    viewModel: MainScreenViewModel,
    mealPagerState: PagerState,
) {
    val uiState by viewModel.uiState

    if (uiState.isNutrientDialogVisible) {
        val selectedMealIndex = uiState.selectedMealIndex
        val selectedMeal = uiState.selectedDateDataState.uiMeals[selectedMealIndex]
        BlindarDialog(
            onDismissRequest = viewModel::closeNutrientDialog,
            properties = DialogProperties(usePlatformDefaultWidth = false),
        ) {
            NutrientDialogContents(
                uiMeal = selectedMeal,
                onClose = viewModel::closeNutrientDialog,
                modifier = Modifier.padding(dialogContentPadding),
            )
        }
    }
    if (uiState.isMemoDialogVisible) {
        BlindarDialog(
            onDismissRequest = viewModel::closeMemoDialog,
            properties = DialogProperties(usePlatformDefaultWidth = false),
        ) {
            MemoDialogContents(
                date = uiState.selectedDateDataState.uiMemos.date,
                memoDialogElements = uiState.selectedDateDataState.memoDialogElements,
                onAddMemo = viewModel::addMemo,
                onContentsChange = viewModel::updateMemoOnLocal,
                onMemoDelete = viewModel::deleteMemo,
                onDialogClose = viewModel::closeMemoDialog,
                modifier = Modifier.padding(dialogContentPadding),
            )
        }
    }

    if (uiState.isMealDialogVisible) {
        val scope = rememberCoroutineScope()
        BlindarDialog(
            onDismissRequest = viewModel::onMealDialogClose,
            properties = DialogProperties(usePlatformDefaultWidth = false),
        ) {
            MealDialogContents(
                uiMeals = uiState.selectedDateDataState.uiMeals,
                mealPagerState = mealPagerState,
                onMealTimeClick = {
                    viewModel.onMealTimeClick(it)
                    scope.launch { mealPagerState.animateScrollToPage(it) }
                },
                onNutrientDialogOpen = viewModel::openNutrientDialog,
                onMealDialogClose = viewModel::onMealDialogClose,
                modifier = Modifier
                    .padding(dialogContentPadding)
                    .widthIn(max = 600.dp),
            )
        }
    }

    if (uiState.isScheduleDialogVisible) {
        BlindarDialog(
            onDismissRequest = viewModel::onScheduleDialogClose,
            properties = DialogProperties(usePlatformDefaultWidth = false),
        ) {
            ScheduleDialogContents(
                scheduleElements = uiState.selectedDateDataState.memoDialogElements,
                onMemoDialogOpen = viewModel::openMemoDialog,
                onScheduleDialogClose = viewModel::onScheduleDialogClose,
                modifier = Modifier
                    .padding(dialogContentPadding)
                    .widthIn(max = 600.dp),
            )
        }
    }
}