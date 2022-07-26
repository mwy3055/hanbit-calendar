package com.practice.neis.meal

import com.practice.neis.common.NeisApi
import com.practice.neis.meal.retrofit.mealApi
import com.practice.neis.meal.util.MealDeserializerException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

@OptIn(ExperimentalCoroutinesApi::class)
class MealRemoteDataSourceTest {

    private val mealRemoteDataSource = MealRemoteDataSource(NeisApi.mealApi)

    @Test
    fun getMeal() = runTest {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH) + 1

        val result = mealRemoteDataSource.getMeal(currentYear, currentMonth)
        assert(result.isNotEmpty())
    }

    @Test
    fun getMeal_NoData(): Unit = runTest {
        val calendar = Calendar.getInstance().apply {
            add(Calendar.MONTH, 1)
        }
        val year = calendar.get(Calendar.YEAR) + 1
        val month = calendar.get(Calendar.MONTH)

        assertThrows<MealDeserializerException>("해당하는 데이터가 없습니다.") {
            println(mealRemoteDataSource.getMeal(year, month))
        }
    }

}