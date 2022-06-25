package com.practice.database.meal

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.practice.database.TestUtil
import com.practice.database.meal.room.MealDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class MealLocalDataSourceTest {
    private lateinit var database: MealDatabase
    private lateinit var source: MealLocalDataSource

    @Before
    fun init() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            MealDatabase::class.java
        ).build()
        source = MealLocalDataSource(database.mealDao())
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun source_insertMeals() = runTest {
        val meals = TestUtil.createMealEntity(10)
        source.insertMeals(meals)

        val mealsInDatabase = source.getMeals(meals[0].year, meals[0].month)
        assertEquals(meals, mealsInDatabase)
    }

    @Test
    fun source_deleteMeals() = runTest {
        val meals = TestUtil.createMealEntity(10)
        source.insertMeals(meals)
        source.deleteMeals(meals)

        val mealsInDatabase = source.getMeals(meals[0].year, meals[0].month)
        assert(mealsInDatabase.isEmpty())
    }

    @Test
    fun source_deleteMeals_ByDate() = runTest {
        val meals = TestUtil.createMealEntity(10)
        source.insertMeals(meals)
        source.deleteMeals(meals[0].year, meals[0].month)

        val mealsInDatabase = source.getMeals(meals[0].year, meals[0].month)
        assert(mealsInDatabase.isEmpty())
    }

    @Test
    fun source_clear() = runTest {
        val meals = TestUtil.createMealEntity(10)
        source.insertMeals(meals)
        source.clear()

        val mealsInDatabase = source.getMeals(meals[0].year, meals[0].month)
        assert(mealsInDatabase.isEmpty())
    }

}