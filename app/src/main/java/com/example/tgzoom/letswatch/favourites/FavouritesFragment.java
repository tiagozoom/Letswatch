package com.example.tgzoom.letswatch.favourites;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.tgzoom.letswatch.App;
import com.example.tgzoom.letswatch.R;
import com.example.tgzoom.letswatch.data.Movie;
import com.example.tgzoom.letswatch.movies.MovieAdapter;
import com.example.tgzoom.letswatch.movies.MoviesContract;
import com.example.tgzoom.letswatch.movies.MoviesItemListener;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavouritesFragment extends Fragment implements FavouritesContract.View {

    public final static String TAG = "FavouritesFragment";

    private MovieAdapter mMovieAdapter;

    @Inject FavouritesPresenter mPresenter;

    private MoviesItemListener mMoviesItemListener = new MoviesItemListener() {
        @Override
        public void onClick(Movie movie) {
            mPresenter.openDetails(movie);
        }

        @Override
        public void onMarkAsFavorite(Movie movie) {
            mPresenter.markAsFavourite(movie);
        }

        @Override
        public void onUnmarAsFavorite(int movieApiId) {
            mPresenter.unmarkAsFavourite(movieApiId);
        }
    };

    @BindView(R.id.favourites_recyclerview) RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_favourites, container, false);
        ButterKnife.bind(this,rootView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), Integer.valueOf(getString(R.string.gridlayout_span_count)));
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(mMovieAdapter);

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState == null){
            mPresenter.start();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaggerFavouritesComponent.builder()
                .moviesRepositoryComponent(((App) getActivity().getApplication()).getMoviesRepositoryComponent())
                .favouritesPresenterModule(new FavouritesPresenterModule(this))
                .build()
                .inject(this);

        mMovieAdapter = new MovieAdapter(new ArrayList<Movie>(),mMoviesItemListener);
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showMovies(List<Movie> movies) {
        mMovieAdapter.addArrayList(movies);
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
    public void showMovieDetails(Movie movie) {

    }

    @Override
    public void updateMovies(int movieApiId, boolean isFavourite) {
        int count = mMovieAdapter.getItemCount();
        for (int position = 0; position < count; position++) {
            if (mMovieAdapter.getItemId(position) == movieApiId) {
                mMovieAdapter.getArrayList().get(position).setFavourite(isFavourite);
                mMovieAdapter.notifyItemChanged(position);
            }
        }
    }

    @Override
    public void setPresenter(MoviesContract.Presenter presenter) {
        if(presenter != null){
            mPresenter = (FavouritesPresenter) presenter;
        }
    }
}
