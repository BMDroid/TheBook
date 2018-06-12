package com.example.android.thebook;

import java.util.Comparator;

/**
 * Created by Jianyuan on 9/28/2016.
 */

public class SortByRating implements Comparator<Book> {
    @Override
    public int compare(Book book, Book t1) {
        return Double.compare(t1.getAverageRating(), book.getAverageRating());
    }
}
