package com.example.android.thebook;

import java.util.ArrayList;

/**
 * Created by Jianyuan on 9/30/2016.
 */

public class FavoriteBooks {

    private static ArrayList<Book> favorites = new ArrayList<>();

    private FavoriteBooks(){
    }

    public static void addToFavorite(Book b){
        favorites.add(b);
    }

    public static ArrayList<Book> getFavorites(){
        return favorites;
    }
}
