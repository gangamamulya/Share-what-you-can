package com.example.sharewhatyoucanproject.deliverfood

import com.example.sharewhatyoucanproject.models.GeoPoint
import com.example.sharewhatyoucanproject.models.PostModel
import com.example.sharewhatyoucanproject.models.RequestModel
import org.junit.Assert
import org.junit.Before
import org.junit.Test


internal class DeliverViewModelTest {

    lateinit var deliverViewModel: DeliverViewModel

    @Before
    fun setup() {
        deliverViewModel = DeliverViewModel(null)
    }

    @Test
    fun testGenerateMap() {
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

        val requestModel = RequestModel(
            name = "rName",
            uid = "rId",
            updateid = "rUpdateId",
            status = 0,
            postid = "rPostId",
        )

        val donationMap =
            deliverViewModel.getDonationMap(
                postModel = postModel,
                requestModel = requestModel,
                displayName = "DisplayName",
                uid = "UID",
            )

        Assert.assertEquals("DisplayName", donationMap["donor"])
        Assert.assertEquals(requestModel.uid, donationMap["donatedTo"])
        Assert.assertEquals(requestModel.name, donationMap["receiverName"])
        Assert.assertEquals(postModel.title, donationMap["title"])
        Assert.assertEquals(postModel.image, donationMap["img"])
        Assert.assertEquals(postModel.desc, donationMap["desc"])
        Assert.assertEquals("UID", donationMap["uid"])
    }

}
