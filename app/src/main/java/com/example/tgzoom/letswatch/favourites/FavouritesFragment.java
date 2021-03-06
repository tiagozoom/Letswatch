package com.example.tgzoom.letswatch.favourites;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.tgzoom.letswatch.App;
import com.example.tgzoom.letswatch.R;
import com.example.tgzoom.letswatch.data.Movie;
import com.example.tgzoom.letswatch.main.MainActivity;
import com.example.tgzoom.letswatch.moviedetail.MovieDetailActivity;
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
    private FavouriteAdapter mFavouriteAdapter;
    private Snackbar mSnackbar;

    @BindView(R.id.favourites_recyclerview) RecyclerView mRecyclerView;
    @BindView(R.id.favourites_swipe_refresh) SwipeRefreshLayout mSwipeRefreshLayout;
    @Inject FavouritesPresenter mPresenter;

    private MoviesItemListener mFavouritesItemListener = new MoviesItemListener() {
        @Override
        public void onClick(Movie movie) {
            mPresenter.openDetails(movie);
        }

        @Override
        public void onCardMenuClick(View view, Movie movie) {
            mFavouriteAdapter.onCardMenuClick(view,movie);
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
        View rootView = inflater.inflate(R.layout.fragment_favourites,container,false);
        ButterKnife.bind(this,rootView);
        ((MainActivity) getActivity()).setTitle(getString(R.string.fragment_favourites_title));
        
        mRecyclerView.setAdapter(mFavouriteAdapter);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        final GridLayoutManager gridLayoutManager = (GridLayoutManager) mRecyclerView.getLayoutManager();
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (mFavouriteAdapter.getItemViewType(position)){
                    case FavouriteAdapter.ITEM_VIEW:
                        return 1;
                    case FavouriteAdapter.NO_ITEMS:
                        return getResources().getInteger(R.integer.gridlayout_span_count);
                    default:
                        return gridLayoutManager.getSpanCount();
                }
            }
        });
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(mFavouriteAdapter != null){
            ArrayList<Movie> movieList = (ArrayList<Movie>) mFavouriteAdapter.getArrayList();
            outState.putParcelableArrayList(PARCELABLE_FAVOURITES_LIST, movieList);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerFavouritesComponent.builder()
                .appComponent(((App) getActivity().getApplication()).getmAppComponent())
                .favouritesPresenterModule(new FavouritesPresenterModule(this))
                .build()
                .inject(this);

        mFavouriteAdapter = new FavouriteAdapter(mFavouritesItemListener);

        if(savedInstanceState != null){
            List<Movie> movies = savedInstanceState.<Movie>getParcelableArrayList(PARCELABLE_FAVOURITES_LIST);
            mFavouriteAdapter.swapArrayList(movies);
        }

        mPresenter.start(true);
    }

    @Override
    public void setLoadingIndicator(boolean active) {}

    @Override
    public void showMovies(List<Movie> movies) {
        mFavouriteAdapter.swapArrayList(movies);
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
        mFavouriteAdapter.addItem(null);
    }

    @Override
    public void hideLoadingBar() {
        mFavouriteAdapter.getArrayList().remove(1);
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
        mFavouriteAdapter.clear();
        mPresenter.start(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter = null;
    }
}
