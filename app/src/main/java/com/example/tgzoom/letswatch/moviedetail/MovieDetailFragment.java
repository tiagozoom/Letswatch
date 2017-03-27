package com.example.tgzoom.letswatch.moviedetail;

import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.example.tgzoom.letswatch.BR;
import com.example.tgzoom.letswatch.R;
import com.example.tgzoom.letswatch.data.Movie;
import com.example.tgzoom.letswatch.data.Trailer;
import com.example.tgzoom.letswatch.databinding.FragmentMovieDetailBinding;
import com.example.tgzoom.letswatch.util.URIUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetailFragment extends Fragment implements MovieDetailContract.View {

    public static final String TAG = "movie_detail_fragment";
    private static final String PARCELABLE_TRAILER_LIST = "trailers";

    private Movie movie;
    private MovieDetailContract.Presenter mMovieDetailPresenter;
    private TrailerAdapter mTrailerAdapter;
    private ShareActionProvider mShareActionProvider;
    private MovieDetailListener mMoviesDetailListener = new MovieDetailListener() {
        @Override
        public void onMarkAsFavorite(Movie movie) {
            mMovieDetailPresenter.markAsFavourite(movie);
        }
        @Override
        public void onUnmarAsFavorite(int movieApiId) {
            mMovieDetailPresenter.unmarkAsFavourite(movieApiId);
        }
        @Override
        public void onTrailerClick(String trailerkey) {
            mMovieDetailPresenter.openTrailer(trailerkey);
        }
    };

    public static MovieDetailFragment newInstance(Movie movie) {
        MovieDetailFragment detailFragment = new MovieDetailFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable(MovieDetailActivity.MOVIE_OBJECT, movie);
        detailFragment.setArguments(arguments);
        return detailFragment;

    }

    @Override
    public void onStart() {
        super.onStart();
        mMovieDetailPresenter.start(false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(mTrailerAdapter != null){
            ArrayList<Trailer> trailers = (ArrayList<Trailer>) mTrailerAdapter.getList();
            outState.putParcelableArrayList(PARCELABLE_TRAILER_LIST, trailers);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentMovieDetailBinding fragmentMoviesListItemBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_movie_detail, container, false);
        setHasOptionsMenu(true);
        mTrailerAdapter = new TrailerAdapter(getContext(), mMoviesDetailListener);
        if(savedInstanceState != null){
            List<Trailer> trailers = savedInstanceState.getParcelableArrayList(PARCELABLE_TRAILER_LIST);
            mTrailerAdapter.swapArrayList(trailers);
        }
        if (getArguments() != null) {
            movie = (Movie) getArguments().get(MovieDetailActivity.MOVIE_OBJECT);
            fragmentMoviesListItemBinding.setVariable(BR.movie,movie);
        }
        fragmentMoviesListItemBinding.trailersList.setAdapter(mTrailerAdapter);
        return fragmentMoviesListItemBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(mTrailerAdapter.getCount() <= 0){
            mMovieDetailPresenter.loadTrailers(movie.getApi_movie_id());
        }
    }

    @Override
    public void setLoadingIndicator(boolean active) {

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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_movie_detail, menu);
        MenuItem favorite_button = menu.findItem(R.id.favorite_button);
        favorite_button.setIcon((movie.isFavourite()) ? R.drawable.ic_heart_white_24dp : R.drawable.ic_heart_outline_white_24dp);
    }

    @BindingAdapter("bind:coverImage")
    public static void loadCoverImage(ImageView coverImage,String url){
        String posterPath = URIUtils.buildPosterPath(url);
        Glide.with(coverImage.getContext())
                .load(posterPath)
                .centerCrop()
                .priority(Priority.LOW)
                .placeholder(R.color.colorPrimary)
                .into(coverImage);
    }

    @Override
    public void showTrailers(List<Trailer> trailers) {
        mTrailerAdapter.swapArrayList(trailers);
    }

    @Override
    public void openTrailer(Uri trailerUri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, trailerUri);
        getContext().startActivity(intent);
    }

    @Override
    public void shareMovie(Intent shareMovieIntent) {
        startActivity(Intent.createChooser(shareMovieIntent,"Share"));
    }

    private void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                return true;
            case R.id.favorite_button:
                if (movie != null) {
                    if (movie.isFavourite()) {
                        mMovieDetailPresenter.unmarkAsFavourite(movie.getApi_movie_id());
                    } else {
                        mMovieDetailPresenter.markAsFavourite(movie);
                    }
                    movie.setFavourite(!movie.isFavourite());
                    item.setIcon((movie.isFavourite()) ? R.drawable.ic_heart_white_24dp : R.drawable.ic_heart_outline_white_24dp);
                }
                break;
            case R.id.share_movie:
                mMovieDetailPresenter.shareMovie(movie.getApi_movie_id());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setPresenter(MovieDetailContract.Presenter presenter) {
        if (presenter != null) {
            mMovieDetailPresenter = presenter;
        }
    }
}
