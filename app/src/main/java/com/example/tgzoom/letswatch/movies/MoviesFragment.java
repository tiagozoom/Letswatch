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
import android.util.Log;
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
import com.example.tgzoom.letswatch.main.MainActivity;
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
    private EndlessRecyclerViewScrollListener mEndlessRecyclerViewScrollListener;
    private Snackbar mSnackbar;
    private BroadcastReceiver mInternetConnectionReceiver;

    @BindView(R.id.movies_recyclerview) RecyclerView mRecyclerView;
    @BindView(R.id.movies_swipe_refresh) SwipeRefreshLayout mSwipeRefreshLayout;
    @Inject MoviesPresenter mPresenter;

    private MoviesItemListener mMoviesItemListener = new MoviesItemListener() {
        @Override
        public void onClick(Movie movie) {
            mPresenter.openDetails(movie);
        }

        @Override
        public void onCardMenuClick(View view, Movie movie) {
            mMovieAdapter.onCardMenuClick(view,movie);
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
        ((MainActivity) getActivity()).setTitle(getString(R.string.fragment_movies_title));

        mSortDialogFragment = new SortDialogFragment();
        mSortDialogFragment.setTargetFragment(MoviesFragment.this, MOVIES_FRAGMENT_ID);
        mRecyclerView.setAdapter(mMovieAdapter);
        mEndlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                mPresenter.loadMovies(page,true);
                mCurrentPage = page;
            }
        };

        final GridLayoutManager gridLayoutManager = (GridLayoutManager) mRecyclerView.getLayoutManager();
        mEndlessRecyclerViewScrollListener.setLayoutManager(gridLayoutManager);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (mMovieAdapter.getItemViewType(position)){
                    case MovieAdapter.VIEW_ITEM:
                        return 1;
                    case MovieAdapter.PROGRESS_BAR:
                        return getResources().getInteger(R.integer.gridlayout_span_count);
                    default:
                        return gridLayoutManager.getSpanCount();
                }
            }
        });

        mEndlessRecyclerViewScrollListener.setLoading(false);
        mEndlessRecyclerViewScrollListener.setCurrentPage(mCurrentPage);
        mRecyclerView.addOnScrollListener(mEndlessRecyclerViewScrollListener);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        return rootView;
    }

    public void registerBroadastReceiver(){
        IntentFilter internetConnectionIntent = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        mInternetConnectionReceiver = new BroadcastReceiver() {
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        registerBroadastReceiver();
        if(savedInstanceState == null){
            mPresenter.start(true);
        }
    }

    @Override
    public void setLoadingIndicator(boolean isLoading) {
        mEndlessRecyclerViewScrollListener.setLoading(isLoading);
    }

    @Override
    public void showLoadingBar() {
        mMovieAdapter.addItem(null);
    }

    @Override
    public void hideLoadingBar() {
        List<Movie> movies = mMovieAdapter.getArrayList();
        if(movies.size() > 0){
            Movie movie = movies.get(movies.size() - 1);
            if(movie == null){
                movies.remove(movie);
            }
        }
    }

    @Override
    public void showMovies(List<Movie> movies) {
        for (Movie movie: movies) {
            mMovieAdapter.addItem(movie);
        }
    }

    @Override
    public void showLoadingMoviesError() {
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
    public void onDestroyView() {
        super.onDestroyView();
        if (mInternetConnectionReceiver != null) {
            getContext().unregisterReceiver(mInternetConnectionReceiver);
        }
    }

    @Override
    public void hideMessage() {
        if(mSnackbar != null){
            mSnackbar.dismiss();
        }
    }

    @Override
    public void hideRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
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
        mPresenter.start(false);
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
    public void onSortChange() {
        mMovieAdapter.clear();
        mPresenter.start(true);
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
