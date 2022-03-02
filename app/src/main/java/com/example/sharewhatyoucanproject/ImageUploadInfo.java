package com.example.sharewhatyoucanproject;

public class ImageUploadInfo {

    public String imageName;

    public String imageURL;
    public String  imageDescription;
    //public float locationButton;

    public ImageUploadInfo() {

    }

    public ImageUploadInfo(String name, String url, String descr) {

        this.imageName = name;
        this.imageURL= url;
        this.imageDescription= descr;
        //this.locationButton = loc;

    }

    public String getImageName() {
        return imageName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getImageDescription() {
        return imageDescription;
    }

    /*public float getLocationButton() {
        return locationButton;
    }*/
}