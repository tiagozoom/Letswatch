package com.example.tgzoom.letswatch.movies;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.example.tgzoom.letswatch.App;
import com.example.tgzoom.letswatch.R;
import com.example.tgzoom.letswatch.data.Movie;
import com.example.tgzoom.letswatch.dialog.SortDialogFragment;
import com.example.tgzoom.letswatch.dialog.SortDialogListener;
import com.example.tgzoom.letswatch.listener.MoviesItemListener;
import com.example.tgzoom.letswatch.moviedetail.MovieDetailActivity;
import com.example.tgzoom.letswatch.util.EndlessRecyclerViewScrollListener;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoviesFragment extends Fragment implements MoviesContract.View,SwipeRefreshLayout.OnRefreshListener,SortDialogListener{

    public final static String TAG = "MoviesFragment";
    private int mCurrentPage = 1;
    private MovieAdapter mMovieAdapter;
    private SortDialogFragment mSortDialogFragment;
    private static final String PARCELABLE_MOVIE_LIST = "parcelable_movie_list";
    private static final String CURRENT_PAGE_INDEX = "current_page_index";
    public static final int MOVIES_FRAGMENT_ID = 105;
    private Snackbar mSnackbar;

    @BindView(R.id.movies_recyclerview) RecyclerView mRecyclerView;
    @BindView(R.id.movies_swipe_refresh) SwipeRefreshLayout mSwipeRefreshLayout;
    @Inject MoviesPresenter mPresenter;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_movies, container, false);
        ButterKnife.bind(this,rootView);
        setHasOptionsMenu(true);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), Integer.valueOf(getString(R.string.gridlayout_span_count)));
        mSortDialogFragment = new SortDialogFragment();

        mSortDialogFragment.setTargetFragment(MoviesFragment.this, MOVIES_FRAGMENT_ID);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(mMovieAdapter);

        mRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager,mCurrentPage) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                mPresenter.loadMovies(false,page);
                mCurrentPage = page;
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(this);
        return rootView;
    }

    public void registerBroadastReceiver(){
        IntentFilter internetConnectionIntent = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        BroadcastReceiver mInternetConnectionReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mPresenter.testConnectivity();
            }
        };

        getContext().registerReceiver(mInternetConnectionReceiver,internetConnectionIntent);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaggerMoviesComponent.builder()
                .moviesRepositoryComponent(((App) getActivity().getApplication()).getMoviesRepositoryComponent())
                .moviesPresenterModule(new MoviesPresenterModule(this))
                .build()
                .inject(this);

        mMovieAdapter = new MovieAdapter(new ArrayList<Movie>(),mMoviesItemListener);

        if(savedInstanceState != null){
            List<Movie> movies = savedInstanceState.<Movie>getParcelableArrayList(PARCELABLE_MOVIE_LIST);
            mMovieAdapter.swapArrayList(movies);
            mCurrentPage  = savedInstanceState.getInt(CURRENT_PAGE_INDEX);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        registerBroadastReceiver();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState == null){
            mPresenter.start();
        }
    }

    @Override
    public void setLoadingIndicator(boolean isActive) {
        mSwipeRefreshLayout.setRefreshing(isActive);
    }

    @Override
    public void showMovies(List<Movie> movies) {
        if(mCurrentPage <= 1){
            mMovieAdapter.swapArrayList(movies);
        }else{
            mMovieAdapter.addArrayList(movies);
        }
    }

    @Override
    public void showLoadingMoviesError() {}

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
    public boolean isActive() {
        return false;
    }

    @Override
    public void showMarkedAsFavouriteMessage() {
        showMessage(getString(R.string.marked_as_favourite_message),Snackbar.LENGTH_SHORT);
    }

    @Override
    public void showUnmarkedAsFavouriteMessage() {
        showMessage(getString(R.string.unmarked_as_favourite_message),Snackbar.LENGTH_SHORT);
    }

    @Override
    public void showNoConnectivityMessage() {
        showMessage(getString(R.string.no_connection_message),Snackbar.LENGTH_INDEFINITE);
    }

    @Override
    public void showMovieDetails(Movie movie) {
        Intent intent = new Intent(getContext(), MovieDetailActivity.class);
        intent.setAction(Intent.ACTION_VIEW);
        intent.putExtra(MovieDetailActivity.MOVIE_OBJECT, movie);
        getContext().startActivity(intent);
    }

    @Override
    public void hideMessage() {
        if(mSnackbar != null){
            mSnackbar.dismiss();
        }
    }

    private void showMessage(String message,int duration){
        mSnackbar = Snackbar.make(getView(),message,duration);
        mSnackbar.show();
    }

    @Override
    public void setPresenter(MoviesContract.Presenter presenter) {
        if(presenter != null){
            mPresenter = (MoviesPresenter) presenter;
        }
    }

    @Override
    public void onRefresh() {
        mMovieAdapter.clear();
        mPresenter.start();
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
    public void onDestroy() {
        super.onDestroy();
        setLoadingIndicator(false);
    }

    @Override
    public void onSortChange() {
        onRefresh();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_movies, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_sort:
                mSortDialogFragment.show(getFragmentManager(), TAG);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
