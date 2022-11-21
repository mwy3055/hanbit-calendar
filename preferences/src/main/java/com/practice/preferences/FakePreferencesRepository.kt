package com.practice.preferences

import kotlinx.coroutines.flow.MutableStateFlow

class FakePreferencesRepository : PreferencesRepository {

    private val initialPreferences = UserPreferences(
        uiMode = UiMode.Graphic,
        themeMode = ThemeMode.SystemDefault,
        isFirstExecution = true,
        runningWorksCount = 0,
    )
    private var preferences: UserPreferences = initialPreferences

    override val userPreferencesFlow = MutableStateFlow(preferences)

    override suspend fun updateUiMode(uiMode: UiMode) {
        preferences = preferences.copy(uiMode = uiMode)
        emitNewValue()
    }

    override suspend fun updateThemeMode(themeMode: ThemeMode) {
        preferences = preferences.copy(themeMode = themeMode)
        emitNewValue()
    }

    override suspend fun updateIsFirstExecution(isFirstExecution: Boolean) {
        preferences = preferences.copy(isFirstExecution = isFirstExecution)
        emitNewValue()
    }

    override suspend fun increaseRunningWorkCount() {
        val count = preferences.runningWorksCount
        preferences = preferences.copy(runningWorksCount = count + 1)
        emitNewValue()
    }

    override suspend fun decreaseRunningWorkCount() {
        val count = preferences.runningWorksCount
        preferences = preferences.copy(runningWorksCount = count - 1)
        emitNewValue()
    }

    override suspend fun clear() {
        preferences = initialPreferences
        emitNewValue()
    }

    override suspend fun fetchInitialPreferences(): UserPreferences {
        return preferences
    }

    private suspend fun emitNewValue() {
        userPreferencesFlow.emit(preferences)
    }
}