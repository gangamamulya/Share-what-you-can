package com.example.sharewhatyoucanproject.models

import kotlinx.serialization.Serializable

@Serializable
data class PostModel(
    val image: String,
    val title: String,
    val desc: String,
    val uid: String,
    val name: String,
    val status: Int,
    val postid: String,
    val location: GeoPoint? = null,
)

@Serializable
data class GeoPoint(val latitude: Double, val longitude: Double)
