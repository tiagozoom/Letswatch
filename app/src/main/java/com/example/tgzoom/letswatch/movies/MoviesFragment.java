package com.example.tgzoom.letswatch.movies;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.tgzoom.letswatch.R;
import com.example.tgzoom.letswatch.data.Movie;
import com.example.tgzoom.letswatch.util.EndlessRecyclerViewScrollListener;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoviesFragment extends Fragment implements MoviesContract.View,SwipeRefreshLayout.OnRefreshListener{

    public final static String TAG = "MoviesFragment";

    private int mCurrentPage;

    private MovieAdapter mMovieAdapter;

    private MoviesContract.Presenter mPresenter;

    private static final String PARCELABLE_MOVIE_LIST = "parcelable_movie_list";

    private static final String CURRENT_PAGE_INDEX = "current_page_index";

    @BindView(R.id.moviedb_recyclerview) RecyclerView mRecyclerView;

    @BindView(R.id.swipe_refresh) SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_movies, container, false);
        ButterKnife.bind(this,rootView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), Integer.valueOf(getString(R.string.gridlayout_span_count)));
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(mMovieAdapter);
        mRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager,mCurrentPage) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                mPresenter.loadMovies(false,page);
                mCurrentPage = page;
            }
        });

        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){
            mMovieAdapter = new MovieAdapter(savedInstanceState.<Movie>getParcelableArrayList(PARCELABLE_MOVIE_LIST));
            mCurrentPage  = savedInstanceState.getInt(CURRENT_PAGE_INDEX);
        }else{
            mMovieAdapter = new MovieAdapter(new ArrayList<Movie>());
            mCurrentPage  = 1;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState == null){
            mPresenter.start();
        }
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        if(active){
            mSwipeRefreshLayout.setRefreshing(true);
        }else{
            mSwipeRefreshLayout.setRefreshing(false);
        }
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
    public void onSaveInstanceState(Bundle outState) {
        if(mMovieAdapter != null){
            ArrayList<Movie> movieList = (ArrayList<Movie>) mMovieAdapter.getArrayList();
            outState.putParcelableArrayList(PARCELABLE_MOVIE_LIST, movieList);
            outState.putInt(CURRENT_PAGE_INDEX, mCurrentPage);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void showUnmarkedAsFavouriteMessage() {

    }

    @Override
    public void setPresenter(MoviesContract.Presenter presenter) {
        if(presenter != null){
            mPresenter = presenter;
        }
    }

    @Override
    public void onRefresh() {
        mMovieAdapter.clear();
        mPresenter.start();
    }
}
