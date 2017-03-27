package com.example.tgzoom.letswatch.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.tgzoom.letswatch.App;
import com.example.tgzoom.letswatch.R;
import com.example.tgzoom.letswatch.data.Movie;
import com.example.tgzoom.letswatch.listener.MoviesItemListener;
import com.example.tgzoom.letswatch.moviedetail.MovieDetailActivity;
import com.example.tgzoom.letswatch.util.EndlessRecyclerViewScrollListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchFragment extends Fragment implements SearchContract.View {

    public static final String TAG = "search_fragment";

    @BindView(R.id.search_movies_recyclerview)
    RecyclerView mRecyclerView;
    @Inject
    SearchPresenter mPresenter;
    private SearchAdapter mSearchAdapter;
    private EndlessRecyclerViewScrollListener mEndlessRecyclerViewScrollListener;
    private int mCurrentPage = 1;
    private Snackbar mSnackbar;
    private String mSearchString;
    private static final String PARCELABLE_MOVIE_LIST = "parcelable_search_list";
    private static final String CURRENT_PAGE_INDEX = "current_page_index";
    private static final String CURRENT_SEARCH_STRING = "current_search_string";

    private MoviesItemListener mMoviesItemListener = new MoviesItemListener() {
        @Override
        public void onClick(Movie movie) {
            mPresenter.openDetails(movie);
        }

        @Override
        public void onCardMenuClick(View view, Movie movie) {
            mSearchAdapter.onCardMenuClick(view, movie);
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
    public void showLoadingBar() {
        mSearchAdapter.addItem(null);
    }

    @Override
    public void hideLoadingBar() {
        List<Movie> movies = mSearchAdapter.getArrayList();
        int indexOfLoadingItem = movies.indexOf(null);
        if (indexOfLoadingItem > -1) {
            Movie movie = movies.get(indexOfLoadingItem);
            movies.remove(movie);
            mSearchAdapter.notifyItemRemoved(indexOfLoadingItem);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerSearchComponent.builder()
                .appComponent(((App) getActivity().getApplication()).getmAppComponent())
                .searchPresenterModule(new SearchPresenterModule(this))
                .build()
                .inject(this);

        mSearchAdapter = new SearchAdapter(new ArrayList<Movie>(),mMoviesItemListener);

        if(savedInstanceState != null){
            List<Movie> movies = savedInstanceState.<Movie>getParcelableArrayList(PARCELABLE_MOVIE_LIST);
            mSearchAdapter.swapArrayList(movies);
            mCurrentPage  = savedInstanceState.getInt(CURRENT_PAGE_INDEX);
            mSearchString = savedInstanceState.getString(CURRENT_SEARCH_STRING);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        ButterKnife.bind(this, rootView);

        mRecyclerView.setAdapter(mSearchAdapter);
        mEndlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                mPresenter.loadMovies(mSearchString, page);
                mCurrentPage = page;
            }
        };

        final GridLayoutManager gridLayoutManager = (GridLayoutManager) mRecyclerView.getLayoutManager();
        mEndlessRecyclerViewScrollListener.setLayoutManager(gridLayoutManager);
        gridLayoutManager.setSpanCount(1);

        mEndlessRecyclerViewScrollListener.setLoading(false);
        mEndlessRecyclerViewScrollListener.setCurrentPage(mCurrentPage);
        mRecyclerView.addOnScrollListener(mEndlessRecyclerViewScrollListener);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(mSearchAdapter != null){
            ArrayList<Movie> movieList = (ArrayList<Movie>) mSearchAdapter.getArrayList();
            outState.putParcelableArrayList(PARCELABLE_MOVIE_LIST, movieList);
            outState.putString(CURRENT_SEARCH_STRING,mSearchString);
            outState.putInt(CURRENT_PAGE_INDEX, mCurrentPage);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void setPresenter(SearchPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setLoadingIndicator(boolean isLoading) {
        mEndlessRecyclerViewScrollListener.setLoading(isLoading);
    }

    @Override
    public void updateMovies(int movieApiId, boolean isFavourite) {
        int count = mSearchAdapter.getItemCount();
        for (int position = 0; position < count; position++) {
            if (mSearchAdapter.getItemId(position) == movieApiId) {
                mSearchAdapter.getArrayList().get(position).setFavourite(isFavourite);
                mSearchAdapter.notifyItemChanged(position);
                break;
            }
        }
    }

    @Override
    public void showLoadingMoviesError() {
    }

    @Override
    public void showMovies(List<Movie> movies) {
        for (Movie movie : movies) {
            mSearchAdapter.addItem(movie);
        }
    }

    @Override
    public void showMarkedAsFavouriteMessage() {
        showMessage(getString(R.string.marked_as_favourite_message), Snackbar.LENGTH_SHORT, null);
    }

    @Override
    public void showMovieDetails(Movie movie) {
        Intent intent = new Intent(getContext(), MovieDetailActivity.class);
        intent.setAction(Intent.ACTION_VIEW);
        intent.putExtra(MovieDetailActivity.MOVIE_OBJECT, movie);
        getContext().startActivity(intent);
    }

    @Override
    public void showUnmarkedAsFavouriteMessage() {
        showMessage(getString(R.string.unmarked_as_favourite_message), Snackbar.LENGTH_SHORT, null);
    }

    private void showMessage(String message, int duration, View.OnClickListener clickListener) {
        mSnackbar = Snackbar.make(getView(), message, duration);
        if (clickListener != null) {
            mSnackbar.setAction(R.string.retry, clickListener);
        }
        mSnackbar.show();
    }

    @Override
    public void loadMovies(String searchString) {
        mSearchAdapter.clear();
        mCurrentPage =1;
        mRecyclerView.addOnScrollListener(mEndlessRecyclerViewScrollListener);
        mSearchString = searchString;
        mPresenter.loadMovies(mSearchString, mCurrentPage);
    }

    @Override
    public void setEndOfList() {
        mRecyclerView.clearOnScrollListeners();
    }
}