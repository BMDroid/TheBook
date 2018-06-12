package com.example.android.thebook;

import android.graphics.Bitmap;
import java.io.Serializable;

/**
 * Created by Jianyuan on 9/28/2016.
 */

public class Book extends Object implements Serializable {

    private String title;
    private String[] authors;
    private String[] categories;
    private double averageRating;
    private double retailPrice;
    private String currencyCode;
    private String infoLink;
    private Bitmap thumbnailBitmap;
    private String thumbnailURL;
    private boolean favorite = false;

    public Book(String aTitle, String[] aAuthors, String[] aCategories, double aAverageRating, double aRetailPrice, String aCurrencyCode, String aInfoLink, String aThumbnail) {
        title = aTitle;
        authors = aAuthors;
        categories = aCategories;
        averageRating = aAverageRating;
        retailPrice = aRetailPrice;
        infoLink = aInfoLink;
        thumbnailURL = aThumbnail;
    }

    public String getTitle() {
        return title;
    }

    public String[] getAuthors() {
        return authors;
    }

    public String[] getCategories() {
        return categories;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public double getRetailPrice() {
        return retailPrice;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public String getInfoLink() {
        return infoLink;
    }

    public Bitmap getThumbnail() {
        return thumbnailBitmap;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public boolean isFavorite(){
        return favorite;
    }

    public void setBitmap(Bitmap aBitmap) {
        thumbnailBitmap = aBitmap;
    }

    public void setFavorite(){
        favorite = true;
    }

    public void setNonFavotite(){
        favorite = false;
    }

    public String toString() {
        String book = "Title: " + title
                + "\nAuthor: " + authors.toString()
                + "\nCategories: " + categories.toString()
                + "\nAverage Rating: " + averageRating
                + "\nRetail Price: " + retailPrice
                + "\nInfo Link: " + infoLink
                + "\nImage Link: " + thumbnailURL;

        return book;
    }
}
