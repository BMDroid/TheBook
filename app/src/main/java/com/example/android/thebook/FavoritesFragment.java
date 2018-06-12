package com.example.android.thebook;

import java.lang.reflect.Type;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;

import static com.example.android.thebook.FavoriteBooks.getFavorites;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
public class FavoritesFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private Spinner sortSpinner;
    private ArrayList<Book> books;
    private Gson gson;

    public FavoritesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_favorites, container, false);
        Type type = new TypeToken<ArrayList<Book>>() {}.getType();
        if(getFavorites() == null){
            books = gson.fromJson("favorites", type);
        }else{books = FavoriteBooks.getFavorites();}

        BookAdapter mAdapter = new BookAdapter(books);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        sortSpinner = (Spinner) rootView.findViewById(R.id.sort_by_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.sort_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(adapter);
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(books != null){
                if (position == 0) {
                    Collections.sort(books, new SortByRating());
                } else if (position == 1) {
                    Collections.sort(books, new SortByPriceLowToHigh());
                } else {
                    Collections.sort(books, new SortByPriceHighToLow());
                }
                BookAdapter mAdapter = new BookAdapter(books);
                mRecyclerView.setAdapter(mAdapter);
            }}

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        return rootView;
    }
}
