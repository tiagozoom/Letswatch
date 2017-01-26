package com.example.tgzoom.letswatch.favourites;


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
import android.view.View;
import android.view.ViewGroup;
import com.example.tgzoom.letswatch.App;
import com.example.tgzoom.letswatch.R;
import com.example.tgzoom.letswatch.data.Movie;
import com.example.tgzoom.letswatch.moviedetail.MovieDetailActivity;
import com.example.tgzoom.letswatch.movies.MovieAdapter;
import com.example.tgzoom.letswatch.movies.MoviesContract;
import com.example.tgzoom.letswatch.listener.MoviesItemListener;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavouritesFragment extends Fragment implements FavouritesContract.View,SwipeRefreshLayout.OnRefreshListener {

    public final static String TAG = "FavouritesFragment";
    private static final String PARCELABLE_FAVOURITES_LIST = "parcelable_favourites_list";
    private MovieAdapter mMovieAdapter;
    private Snackbar mSnackbar;

    @Inject FavouritesPresenter mPresenter;
    @BindView(R.id.favourites_recyclerview) RecyclerView mRecyclerView;
    @BindView(R.id.favourites_swipe_refresh) SwipeRefreshLayout mSwipeRefreshLayout;

    private MoviesItemListener mFavouritesItemListener = new MoviesItemListener() {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_favourites, container, false);
        ButterKnife.bind(this,rootView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), Integer.valueOf(getString(R.string.gridlayout_span_count)));
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(mMovieAdapter);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState == null){
            mPresenter.start(true);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(mMovieAdapter != null){
            ArrayList<Movie> movieList = (ArrayList<Movie>) mMovieAdapter.getArrayList();
            outState.putParcelableArrayList(PARCELABLE_FAVOURITES_LIST, movieList);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerFavouritesComponent.builder()
                .moviesRepositoryComponent(((App) getActivity().getApplication()).getMoviesRepositoryComponent())
                .favouritesPresenterModule(new FavouritesPresenterModule(this))
                .build()
                .inject(this);

        mMovieAdapter = new MovieAdapter(new ArrayList<Movie>(),mFavouritesItemListener);

        if(savedInstanceState != null){
            List<Movie> movies = savedInstanceState.<Movie>getParcelableArrayList(PARCELABLE_FAVOURITES_LIST);
            mMovieAdapter.swapArrayList(movies);
        }
    }

    @Override
    public void setLoadingIndicator(boolean active) {
    }

    @Override
    public void showMovies(List<Movie> movies) {
        mMovieAdapter.swapArrayList(movies);
    }

    @Override
    public void showLoadingMoviesError() {}

    private void showMessage(String message, int duration){
        mSnackbar = Snackbar.make(getView(),message,duration);
        mSnackbar.show();
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
    public void showMovieDetails(Movie movie) {
        Intent intent = new Intent(getContext(), MovieDetailActivity.class);
        intent.setAction(Intent.ACTION_VIEW);
        intent.putExtra(MovieDetailActivity.MOVIE_OBJECT, movie);
        getContext().startActivity(intent);
    }

    @Override
    public void updateMovies(int movieApiId, boolean isFavourite) {
        //No need to worry about updating the movie list here because the sqlbrite observable list is already been watched by FavouritesPresenter
    }

    @Override
    public void showNoConnectivityMessage() {
        showMessage(getString(R.string.no_connection_message),Snackbar.LENGTH_INDEFINITE);
    }

    @Override
    public void showLoadingBar() {
        mMovieAdapter.addItem(null);
    }

    @Override
    public void hideLoadingBar() {
        mMovieAdapter.getArrayList().remove(1);
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

    @Override
    public void setPresenter(FavouritesContract.Presenter presenter) {
        if(presenter != null){
            mPresenter = (FavouritesPresenter) presenter;
        }
    }

    @Override
    public void onRefresh() {
        mMovieAdapter.clear();
        mPresenter.start(false);
    }
}
