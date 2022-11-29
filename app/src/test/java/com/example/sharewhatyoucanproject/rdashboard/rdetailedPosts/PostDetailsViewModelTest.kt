package com.example.sharewhatyoucanproject.rdashboard.rdetailedPosts

import com.example.sharewhatyoucanproject.models.GeoPoint
import com.example.sharewhatyoucanproject.models.PostModel
import org.junit.Assert
import org.junit.Before
import org.junit.Test


internal class PostDetailsViewModelTest {

    lateinit var postDetailsViewModel: PostDetailsViewModel

    @Before
    fun setup() {
        postDetailsViewModel = PostDetailsViewModel(null, null)
    }

    @Test
    fun getRequestMap_PostModel_returnModelOnMap() {
        postDetailsViewModel.postDetailsViewModel = PostModel(
            "www.example.com",
            "Title",
            "Description",
            "UID",
            "Name",
            0,
            "PostID",
            GeoPoint(0.toDouble(), 0.toDouble()),
        )

        val map = postDetailsViewModel.getRequestMap()
        val title = map["for"]
        val status = map["status"] as Int
        val postId = map["postId"]
        val ownerId = map["ownerId"]
        val ownerName = map["ownerName"]

        Assert.assertEquals("Title", title)
        Assert.assertEquals(0, status)
        Assert.assertEquals("PostID", postId)
        Assert.assertEquals("UID", ownerId)
        Assert.assertEquals("Name", ownerName)
    }
}
