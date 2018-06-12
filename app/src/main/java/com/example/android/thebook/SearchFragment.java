package com.example.android.thebook;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    public static final String LOG_TAG = SearchFragment.class.getSimpleName();
    private static final String BOOK_SEARCH_URL = "https://www.googleapis.com/books/v1/volumes?q=";
    private static final String SEARCH_FORTY_BOOKS = "&maxResults=40";
    private String searchURL;
    private ArrayList<Book> books = new ArrayList<>();
    private String editText;
    private EditText editTextView;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        editTextView = (EditText) rootView.findViewById(R.id.search_text);

        Button searchButton = (Button) rootView.findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                editText = editTextView.getText().toString();
                if (editText.equals("")) {
                    Toast.makeText(getContext(), "Please Enter A Valid Keyword!", Toast.LENGTH_SHORT).show();
                }
                try {
                    searchURL = BOOK_SEARCH_URL + URLEncoder.encode(editText, "UTF-8") + SEARCH_FORTY_BOOKS;
                } catch (UnsupportedEncodingException e) {
                    Log.e(LOG_TAG, "Unsupported url", e);
                }
                books = new ArrayList<>();
                BookAsyncTask search = new BookAsyncTask();
                search.execute();
            }
        });
        return rootView;
    }

    /**
     * {@link AsyncTask} to perform the network request on a background thread, and then
     * update the UI with the first earthquake in the response.
     */
    private class BookAsyncTask extends AsyncTask<URL, Void, ArrayList<Book>> {

        @Override
        protected ArrayList<Book> doInBackground(URL... urls) {
            // Create URL object
            URL url = createUrl(searchURL);

            // Perform HTTP request to the URL and receive a JSON response back
            String jsonResponse = "";
            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Http request error", e);
            }
            ArrayList<Book> books = extractFeatureFromJson(jsonResponse);

            return books;
        }

        /**
         * Update the screen with the given earthquake (which was the result of the
         * {@link SearchFragment.BookAsyncTask}).
         */
        @Override
        protected void onPostExecute(ArrayList<Book> books) {
            if (books == null) {
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putSerializable("books", books);
            Intent intent = new Intent(getContext(), SearchResultActivity.class).putExtras(bundle).putExtra("search", editText);
            startActivity(intent);
        }
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Error with creating URL", exception);
            return url;
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        //check the whether the url is null
        if (url == null) return jsonResponse;
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            //HttpURLConnection is a subclass of URLConnection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            //establish the http connection
            urlConnection.connect();
            //check the response code
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.i(LOG_TAG, "Response code is " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "IOException", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return an {@link Book} object by parsing out information
     * about the books from the input bookSearchJSON string.
     */
    private ArrayList<Book> extractFeatureFromJson(String bookSearchJSON) {
        //check the string is null or empty
        if (TextUtils.isEmpty(bookSearchJSON)) return null;
        try {
            JSONObject baseJsonResponse = new JSONObject(bookSearchJSON);
            JSONArray booksArr = baseJsonResponse.getJSONArray("items");

            for (int i = 0; i < booksArr.length(); i++) {
                JSONObject item = booksArr.getJSONObject(i);
                JSONObject volumeInfo = item.getJSONObject("volumeInfo");
                JSONObject saleInfo = item.getJSONObject("saleInfo");

                String title = volumeInfo.getString("title");
                //Get the authors array of the book
                String[] authors = {""};
                if (volumeInfo.has("authors")) {
                    JSONArray authorsArr = volumeInfo.getJSONArray("authors");
                    authors = new String[authorsArr.length()];
                    for (int j = 0; j < authorsArr.length(); j++) {
                        authors[j] = authorsArr.getString(j);
                    }
                }
                //Get the categories array of the book
                String[] categories = {""};
                if (volumeInfo.has("categories")) {
                    JSONArray categoriesArr = volumeInfo.getJSONArray("categories");
                    categories = new String[categoriesArr.length()];
                    for (int j = 0; j < categoriesArr.length(); j++) {
                        categories[j] = categoriesArr.getString(j);
                    }
                }
                //Get the average rating of the book
                double rating = 0.0;
                if (volumeInfo.has("averageRating")) rating = volumeInfo.getDouble("averageRating");

                //Get the thumbnail link of the book
                //Bitmap thumbnail = null;
                String thumbnail = "";
                if (volumeInfo.has("imageLinks")) {
                    JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
                    //thumbnail = getBitmap(imageLinks.getString("thumbnail"));
                    thumbnail = imageLinks.getString("smallThumbnail");
                }

                //Get the infoLink of the book
                String infoLink = volumeInfo.getString("infoLink");

                double retailPrice = 0.0;
                String currencyCode = "USD";
                //String currencyCode = Currency.getInstance(Locale.getDefault()).toString();
                if (saleInfo.has("retailPrice")) {
                    JSONObject retailPriceJSON = saleInfo.getJSONObject("retailPrice");
                    //Get the retail price of the book
                    retailPrice = retailPriceJSON.getDouble("amount");
                    currencyCode = retailPriceJSON.getString("currencyCode");
                }
                books.add(new Book(title, authors, categories, rating, retailPrice, currencyCode, infoLink, thumbnail));
            }
            return books;
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the JSON results", e);
        }
        return null;
    }

    private Bitmap getBitmap(String url) {
        Bitmap bitmap;
        try {
            InputStream inputStream = new URL(url).openStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Bad bitmap", e);
        } catch (IOException e) {
            Log.e(LOG_TAG, "IOException", e);
        }
        return null;
    }
}

