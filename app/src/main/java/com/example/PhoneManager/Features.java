package com.example.PhoneManager;

public class Features {
    private String name;
    private int imageId;
    public Features(String name, int imageId) {
        this.name = name;
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public int getImageId() {
        return imageId;
    }
}
