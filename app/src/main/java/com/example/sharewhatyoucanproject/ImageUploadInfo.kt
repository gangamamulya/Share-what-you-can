package com.example.sharewhatyoucanproject

class ImageUploadInfo {
    var imageName: String? = null
    var imageURL: String? = null


    var imageDescription: String? = null


    constructor(name: String?, url: String?, descr: String?) {
        imageName = name
        imageURL = url
        imageDescription = descr

    }
}