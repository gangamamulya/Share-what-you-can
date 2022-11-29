package com.example.sharewhatyoucanproject.donor

import org.junit.Before
import org.junit.Test;

import org.junit.Assert.*;

internal class AddDonorViewModelTest {

    lateinit var donorViewModel: AddDonorViewModel

    @Before
    fun setup() {
        donorViewModel = AddDonorViewModel(null, null, null)
    }

    @Test
    fun isFoodNotValid_CookedFood_12hours() {
        donorViewModel.setFoodType("Cooked Food")
        donorViewModel.setFoodData(title = "", description = "", hourSet = "12")

        val isValid = donorViewModel.isFoodNotValid()

        assertEquals(false, isValid)
    }

    @Test
    fun isFoodNotValid_CookedFood_50hours() {
        donorViewModel.setFoodType("Cooked Food")
        donorViewModel.setFoodData(title = "", description = "", hourSet = "50")

        val isValid = donorViewModel.isFoodNotValid()

        assertEquals(true, isValid)
    }

    @Test
    fun isFoodNotValid_Groceries_12hours() {
        donorViewModel.setFoodType("Groceries")
        donorViewModel.setFoodData(title = "", description = "", hourSet = "150")

        val isValid = donorViewModel.isFoodNotValid()

        assertEquals(false, isValid)
    }

    @Test
    fun isFoodNotValid_Groceries_1550hours() {
        donorViewModel.setFoodType("Groceries")
        donorViewModel.setFoodData(title = "", description = "", hourSet = "1550")
        val isValid = donorViewModel.isFoodNotValid()

        assertEquals(true, isValid)
    }

    @Test
    fun testIsDataCompleted_FullData() {
        donorViewModel.setFoodData(
            title = "my title",
            description = "my description",
            hourSet = "1",
        )

        val isValid = donorViewModel.isDataCompleted()

        assertEquals(true, isValid)
    }

    @Test
    fun testIsDataCompleted_WithoutTitle() {
        donorViewModel.setFoodData(title = "", description = "my description", hourSet = "1")

        val isValid = donorViewModel.isDataCompleted()

        assertEquals(false, isValid)
    }

    @Test
    fun testIsDataCompleted_WithoutDescription() {
        donorViewModel.setFoodData(title = "my title", description = "", hourSet = "1")

        val isValid = donorViewModel.isDataCompleted()

        assertEquals(false, isValid)
    }

    @Test
    fun testIsDataCompleted_withoutHourSet() {
        donorViewModel.setFoodData(title = "my title", description = "my description", hourSet = "")

        val isValid = donorViewModel.isDataCompleted()

        assertEquals(false, isValid)
    }

}
