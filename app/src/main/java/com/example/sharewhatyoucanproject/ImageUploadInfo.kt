package com.example.sharewhatyoucanproject

class ImageUploadInfo {
    var imageName: String? = null
    var imageURL: String? = null

    /*public float getLocationButton() {
         return locationButton;
     }*/
    var imageDescription: String? = null

    // public float locationButton;
    constructor() {}
    constructor(name: String?, url: String?, descr: String?) {
        imageName = name
        imageURL = url
        imageDescription = descr
        // this.locationButton = loc;
    }
}
