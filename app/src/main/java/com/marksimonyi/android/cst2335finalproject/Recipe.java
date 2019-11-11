package com.marksimonyi.android.cst2335finalproject;

public class Recipe {

    private String title;
    private String image;
    private String url;

    public Recipe(String title, String image, String url) {
        this.title = title;
        this.image = image;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
