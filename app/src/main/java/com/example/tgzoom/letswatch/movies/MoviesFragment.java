package com.example.tgzoom.letswatch.movies;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.example.tgzoom.letswatch.App;
import com.example.tgzoom.letswatch.AppModule;
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
    private int mCurrentPage;
    private MovieAdapter mMovieAdapter;
    private SortDialogFragment mSortDialogFragment;
    private static final String PARCELABLE_MOVIE_LIST = "parcelable_movie_list";
    private static final String CURRENT_PAGE_INDEX = "current_page_index";
    public static final int MOVIES_FRAGMENT_ID = 105;

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaggerMoviesComponent.builder()
                .moviesRepositoryComponent(((App) getActivity().getApplication()).getMoviesRepositoryComponent())
                .moviesPresenterModule(new MoviesPresenterModule(this))
                .appModule(new AppModule(getContext()))
                .build()
                .inject(this);

        mMovieAdapter = new MovieAdapter(new ArrayList<Movie>(),mMoviesItemListener);
        mCurrentPage  = 1;

        if(savedInstanceState != null){
            List<Movie> movies = savedInstanceState.<Movie>getParcelableArrayList(PARCELABLE_MOVIE_LIST);
            mMovieAdapter.swapArrayList(movies);
            mCurrentPage  = savedInstanceState.getInt(CURRENT_PAGE_INDEX);
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
        showMessage(getString(R.string.marked_as_favourite_message));
    }

    @Override
    public void showUnmarkedAsFavouriteMessage() {
        showMessage(getString(R.string.unmarked_as_favourite_message));
    }

    @Override
    public void showMovieDetails(Movie movie) {
        Intent intent = new Intent(getContext(), MovieDetailActivity.class);
        intent.setAction(Intent.ACTION_VIEW);
        intent.putExtra(MovieDetailActivity.MOVIE_OBJECT, movie);
        getContext().startActivity(intent);
    }

    private void showMessage(String message){
        Snackbar.make(getView(),message,Snackbar.LENGTH_SHORT).show();
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
