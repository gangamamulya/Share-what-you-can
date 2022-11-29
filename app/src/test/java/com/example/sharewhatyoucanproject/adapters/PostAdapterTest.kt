package com.example.sharewhatyoucanproject.adapters

import com.example.sharewhatyoucanproject.models.GeoPoint
import com.example.sharewhatyoucanproject.models.PostModel
import org.junit.Assert
import org.junit.Before
import org.junit.Test


internal class PostAdapterTest {

    lateinit var postAdapter: PostAdapter

    @Before
    fun setup() {
        postAdapter = PostAdapter {}
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

        Assert.assertEquals(0, postAdapter.arrayList.size)

        postAdapter.setPostsList(listOf(postModel, postModel, postModel, postModel, postModel))

        Assert.assertEquals(5, postAdapter.arrayList.size)

        postAdapter.setPostsList(listOf(postModel, postModel, postModel))

        Assert.assertEquals(3, postAdapter.arrayList.size)
    }

}

