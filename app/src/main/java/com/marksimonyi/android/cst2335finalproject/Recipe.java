package com.marksimonyi.android.cst2335finalproject;


/**
 * Recipe class for holding recipes
 */
public class Recipe {

    private String title;
    private String image;
    private String url;
    /**
     * tracks the database id of
     */
    private long id;
    /**
     * tracks whether this recipe is in the favourites list
     */
    private boolean isFave;

    /**
     * Constructor; creates the Recipe object with all properties.
     * @param title
     * @param image
     * @param url
     */
    public Recipe(String title, String image, String url) {
        this.title = title;
        this.image = image;
        this.url = url;
    }

    /**
     * getter for the recipe title.
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * setter for the title
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * getter for the image path
     * @return image path
     */
    public String getImage() {
        return image;
    }

    /**
     * setter for the image
     * @param image
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * getter for url
     * @return
     */
    public String getUrl() {
        return url;
    }

    /**
     * setter for url
     * @param url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * setter for ID
     * @param setId
     */
    protected void setId(long setId ) { id = setId; }

    /**
     * getter for ID
     * @return
     */
    public long getId() { return id; }

    /**
     * getter for isFave
     * @return
     */
    public boolean isFave() {
        return isFave;
    }

    /**
     * setter for isFave
     * @param fave
     */
    public void setFave(boolean fave) {
        isFave = fave;
    }
}
