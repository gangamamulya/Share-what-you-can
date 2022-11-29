package com.example.sharewhatyoucanproject.utils

import com.example.sharewhatyoucanproject.models.UserType
import org.junit.Assert
import org.junit.Test

internal class UtilsTest {

    @Test
    fun testRandomRoom_TwoTimes_returnDifferentRooms() {
        val room1 = randomRoom()
        val room2 = randomRoom()

        Assert.assertNotEquals(room1, room2)
    }

    @Test
    fun testRandomRoom_ThreeTimes_returnDifferentRooms() {
        val room1 = randomRoom()
        val room2 = randomRoom()
        val room3 = randomRoom()

        Assert.assertNotEquals(room1, room2)
        Assert.assertNotEquals(room1, room3)
        Assert.assertNotEquals(room2, room3)
    }

    @Test
    fun testGetNameFromDonorType() {
        val name = getNameFromType(UserType.DONOR)
        Assert.assertEquals("Donor", name)
    }

    @Test
    fun testGetNameFromReceiverType() {
        val name = getNameFromType(UserType.RECEIVER)
        Assert.assertEquals("Receiver", name)
    }

}
