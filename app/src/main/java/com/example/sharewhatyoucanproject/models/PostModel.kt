package com.example.sharewhatyoucanproject.models

import kotlinx.serialization.Serializable

@Serializable
data class PostModel(
    var image: String,
    var title: String,
    var desc: String,
    var uid: String,
    var name: String,
    var status: Int,
    var postid: String,
    var location: GeoPoint? = null,
)

@Serializable
data class GeoPoint(val latitude: Double, val longitude: Double)
