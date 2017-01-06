package com.example.tgzoom.letswatch.favourites;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tgzoom.letswatch.R;
import com.example.tgzoom.letswatch.data.Movie;
import com.example.tgzoom.letswatch.movies.MoviesContract;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavouritesFragment extends Fragment implements FavouritesContract.View {


    public static final String TAG = "FavouritesFragment";

    public FavouritesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourites, container, false);
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showMovies(List<Movie> movies) {

    }

    @Override
    public void showLoadingMoviesError() {

    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void showMarkedAsFavouriteMessage() {

    }

    @Override
    public void showUnmarkedAsFavouriteMessage() {

    }

    @Override
    public void setPresenter(MoviesContract.Presenter presenter) {

    }
}
