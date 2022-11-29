package com.example.sharewhatyoucanproject.adapters

import com.example.sharewhatyoucanproject.models.RequestModel
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class RequestAdapterTest {

    lateinit var requestAdapter: RequestAdapter

    @Before
    fun setup() {
        requestAdapter = RequestAdapter { _,_ -> }
    }

    @Test
    fun testSetList() {
        val requestModel = RequestModel(
            name = "rName",
            uid = "rId",
            updateid = "rUpdateId",
            status = 0,
            postid = "rPostId",
        )

        Assert.assertEquals(0, requestAdapter.arrayList.size)

        requestAdapter.setRequestList(listOf(requestModel,requestModel,requestModel,requestModel,requestModel))

        Assert.assertEquals(5, requestAdapter.arrayList.size)

        requestAdapter.setRequestList(listOf(requestModel,requestModel,requestModel))

        Assert.assertEquals(3, requestAdapter.arrayList.size)
    }

    @Test
    fun testUpdateList() {
        val requestModel = RequestModel(
            name = "rName",
            uid = "rId",
            updateid = "rUpdateId",
            status = 0,
            postid = "rPostId",
        )

        val requestModel2 = RequestModel(
            name = "rName2",
            uid = "rId2",
            updateid = "rUpdateId2",
            status = 0,
            postid = "rPostId2",
        )

        requestAdapter.setRequestList(listOf(requestModel,requestModel2))
        requestAdapter.updateRequestListStatus(requestModel2.uid)

        Assert.assertEquals(1, requestAdapter.arrayList[1].status)
    }

}
