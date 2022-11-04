package com.example.sharewhatyoucanproject.models

import kotlinx.serialization.Serializable

@Serializable
data class RequestModel(
    var name: String,
    var uid: String,
    var updateid: String,
    var status: Int,
    var postid: String,
)
