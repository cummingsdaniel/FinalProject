package com.marksimonyi.android.cst2335finalproject;


/**
 * Recipe class for holding recipes
 */
public class Recipe {

    private String title;
    private String image;
    private String url;

    public boolean isFav() {
        return fav;
    }

    public void setFav(boolean fav) {
        this.fav = fav;
    }

    private boolean fav;
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
    public Recipe(int id) {
        this.id = id;
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

    /**
     * equals method for recipes.
     * @param o
     * @return true if the id is the same, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (o.getClass() == this.getClass()) {
            Recipe r = (Recipe)o;
            return r.getId() == this.getId();
        } else {
            return false;
        }
    }
}
