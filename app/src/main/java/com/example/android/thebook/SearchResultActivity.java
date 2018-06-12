package com.example.android.thebook;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.jar.Attributes;

import static android.R.attr.bitmap;
import static android.media.CamcorderProfile.get;

public class SearchResultActivity extends AppCompatActivity {
    public static final String LOG_TAG = SearchResultActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private TextView textView;
    private Spinner sortSpinner;
    private ArrayList<Book> books;
    private ArrayList<Book> favorites;
    private String search;
    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        books = new ArrayList<>();
        favorites = new ArrayList<>();
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(SearchResultActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        search = (String) getIntent().getExtras().get("search");
        textView = (TextView) findViewById(R.id.header_text);
        textView.setText("You search for \"" + search + "\"");

        sortSpinner = (Spinner) findViewById(R.id.sort_by_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sort_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        sortSpinner.setAdapter(adapter);
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Collections.sort(books, new SortByRating());
                } else if (position == 1) {
                    Collections.sort(books, new SortByPriceLowToHigh());
                } else {
                    Collections.sort(books, new SortByPriceHighToLow());
                }
                BookAdapter mAdapter = new BookAdapter(books);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Another interface callback
            }
        });

        books = (ArrayList<Book>) getIntent().getExtras().getSerializable("books");
        BookAdapter mAdapter = new BookAdapter(books);
        mRecyclerView.setAdapter(mAdapter);
        //Add onItemClick listener to the recycle view
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {
                        Uri uri = Uri.parse(books.get(position).getInfoLink());
                        Intent browser = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(browser);
                    }

                    @Override
                    public void onItemLongPress(View view, int position) {
                        Book favorite = books.get(position);
                        favorite.setFavorite();
                        FavoriteBooks.addToFavorite(favorite);
                        Toast.makeText(SearchResultActivity.this, "\"" + favorite.getTitle() + "\" has been added to favorites", Toast.LENGTH_SHORT).show();
                    }
                })
        );
        BitmapAsyncTask bitmap = new BitmapAsyncTask();
        bitmap.execute();

    }

    private class BitmapAsyncTask extends AsyncTask<ArrayList<Book>, Void, ArrayList<Book>> {
        @Override
        protected ArrayList<Book> doInBackground(ArrayList<Book>... arrayLists) {
            Bitmap bitmap;
            InputStream inputStream;
            URL url;
            try {
                for (int i = 0; i < books.size(); i++) {
                    url = new URL(books.get(i).getThumbnailURL());
                    URLConnection connection = url.openConnection();
                    connection.connect();
                    inputStream = connection.getInputStream();
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                    bitmap = BitmapFactory.decodeStream(bufferedInputStream);
                    books.get(i).setBitmap(bitmap);
                    inputStream.close();
                    bufferedInputStream.close();
                }
                return books;
            } catch (MalformedURLException e) {
                Log.e(LOG_TAG, "Bad bitmap", e);
            } catch (IOException e) {
                Log.e(LOG_TAG, "IOException", e);
            }
            return books;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            BookAdapter mAdapter = new BookAdapter(books);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    protected void onPause() {
        String favorites = gson.toJson(FavoriteBooks.getFavorites());
        super.onPause();
    }

    @Override
    protected void onStop() {
        String favorites = gson.toJson(FavoriteBooks.getFavorites());
        super.onStop();
    }
}
