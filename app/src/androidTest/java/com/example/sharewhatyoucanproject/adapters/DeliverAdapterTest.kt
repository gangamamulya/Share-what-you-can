package com.example.sharewhatyoucanproject.adapters

import com.example.sharewhatyoucanproject.models.GeoPoint
import com.example.sharewhatyoucanproject.models.PostModel
import org.junit.Assert
import org.junit.Before
import org.junit.Test


internal class DeliverAdapterTest {

    lateinit var deliverAdapter: DeliverAdapter

    @Before
    fun setup() {
        deliverAdapter = DeliverAdapter {}
    }

    @Test
    fun testSetList() {
        val postModel = PostModel(
            "www.example.com",
            "Title",
            "Description",
            "UID",
            "Name",
            0,
            "PostID",
            GeoPoint(0.toDouble(), 0.toDouble()),
        )

        Assert.assertEquals(0, deliverAdapter.arrayList.size)

        deliverAdapter.setDeliverList(listOf(postModel, postModel, postModel, postModel, postModel))

        Assert.assertEquals(5, deliverAdapter.arrayList.size)

        deliverAdapter.setDeliverList(listOf(postModel, postModel, postModel))

        Assert.assertEquals(3, deliverAdapter.arrayList.size)
    }

}
