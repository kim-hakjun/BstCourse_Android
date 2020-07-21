package com.example.simpleapp.model;

public class Scene {

    String imageUrl;
    String videoUrl;
    Boolean isImage;

    public Scene(String imageUrl, String videoUrl, Boolean isImage) {
        this.imageUrl = imageUrl;
        this.videoUrl = videoUrl;
        this.isImage = isImage;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Boolean getIsImage() {
        return isImage;
    }

    public void setIsImage(Boolean image) {
        isImage = image;
    }
}
