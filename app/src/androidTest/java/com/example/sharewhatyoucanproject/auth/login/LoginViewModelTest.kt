package com.example.sharewhatyoucanproject.auth.login

import org.junit.Assert
import org.junit.Before
import org.junit.Test


internal class LoginViewModelTest {

    lateinit var loginViewModel: LoginViewModel

    @Before
    fun setup() {
        loginViewModel = LoginViewModel(null, null)
    }

    @Test
    fun testGeneratePassword_TwoTimes_returnDifferentPasswords() {
        val password1 = loginViewModel.generatePassword("Harry@gmail.com")
        val password2 = loginViewModel.generatePassword("Oliver@gmail.com")

        Assert.assertNotEquals(password1, password2)
    }

    @Test
    fun testGeneratePassword_ThreeTimes_returnDifferentPasswords() {
        val password1 = loginViewModel.generatePassword("James@gmail.com")
        val password2 = loginViewModel.generatePassword("William@gmail.com")
        val password3 = loginViewModel.generatePassword("Benjamin@gmail.com")

        Assert.assertNotEquals(password1, password2)
        Assert.assertNotEquals(password1, password3)
        Assert.assertNotEquals(password2, password3)
    }

    @Test
    fun testGenerateEmail_TwoTimesWithDifferentId_returnDifferentEmails() {
        val email1 = loginViewModel.generateEmail("123456789")
        val email2 = loginViewModel.generateEmail("987654321")

        Assert.assertNotEquals(email1, email2)
    }

    @Test
    fun testGenerateEmail_ThreeTimesWithDifferentId_returnDifferentEmails() {
        val email1 = loginViewModel.generateEmail("123456789")
        val email2 = loginViewModel.generateEmail("987654321")
        val email3 = loginViewModel.generateEmail("543219876")

        Assert.assertNotEquals(email1, email2)
        Assert.assertNotEquals(email2, email3)
        Assert.assertNotEquals(email1, email3)
    }

    @Test
    fun testRandomName_TwoTimes_returnDifferentNames() {
        val name1 = loginViewModel.getRandomName()
        val name2 = loginViewModel.getRandomName()

        Assert.assertNotEquals(name1, name2)
    }

    @Test
    fun testRandomName_ThreeTimes_returnDifferentNames() {
        val name1 = loginViewModel.getRandomName()
        val name2 = loginViewModel.getRandomName()
        val name3 = loginViewModel.getRandomName()

        Assert.assertNotEquals(name1, name2)
        Assert.assertNotEquals(name1, name3)
        Assert.assertNotEquals(name2, name3)
    }

    @Test
    fun testGetUserMap() {
        loginViewModel.userEmail = "a@gmail.com"
        loginViewModel.userId = "1234"
        loginViewModel.deviceId = "5678"
        val userName = loginViewModel.getRandomName()

        val userMap = loginViewModel.getUserMap(userName)

        Assert.assertEquals(loginViewModel.userEmail, userMap["email"])
        Assert.assertEquals(loginViewModel.userId, userMap["uuid"])
        Assert.assertEquals(loginViewModel.deviceId, userMap["deviceId"])
    }


}
