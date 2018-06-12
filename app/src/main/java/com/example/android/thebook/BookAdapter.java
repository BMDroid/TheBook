package com.example.android.thebook;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.Locale;


/**
 * Created by Jianyuan on 9/28/2016.
 */

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    public static final String LOG_TAG = BookAdapter.class.getSimpleName();
    private ArrayList<Book> mBooks;
    private static final Currency CURRENCY = Currency.getInstance(Locale.US);

    public static class BookViewHolder extends RecyclerView.ViewHolder {

        public ImageView mThumbnail;
        public TextView mTitle;
        public TextView mCategories;
        public TextView mAuthors;
        public RatingBar mRating;
        public TextView mPrice;

        public BookViewHolder(View v) {
            super(v);
            mThumbnail = (ImageView) v.findViewById(R.id.thumbnail);
            mTitle = (TextView) v.findViewById(R.id.title);
            mCategories = (TextView) v.findViewById(R.id.categories);
            mAuthors = (TextView) v.findViewById(R.id.authors);
            mRating = (RatingBar) v.findViewById(R.id.rating);
            mPrice = (TextView) v.findViewById(R.id.price);
        }
    }

    public BookAdapter(ArrayList<Book> books) {
        mBooks = books;
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_list, parent, false);
        return new BookViewHolder(v);
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {
        final Book mBook = mBooks.get(position);
        if (mBook.getThumbnailURL().equals("")) {
            holder.mThumbnail.setImageResource(R.drawable.image_not_available);
        } else {
            holder.mThumbnail.setImageBitmap(mBook.getThumbnail());
        }
        holder.mTitle.setText(mBook.getTitle());
        holder.mCategories.setText(Arrays.toString(mBook.getCategories()).replace("[", "").replace("]", ""));
        holder.mAuthors.setText(Arrays.toString(mBook.getAuthors()).replace("[", "").replace("]", ""));
        holder.mRating.setRating((float) mBook.getAverageRating());
        holder.mPrice.setText(CURRENCY.getSymbol() + " " + Double.toString(mBook.getRetailPrice()));
    }

    @Override
    public int getItemCount() {
        if (mBooks == null) return 0;
        return mBooks.size();
    }

}
