package com.example.tgzoom.letswatch.moviedetail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tgzoom.letswatch.R;
import com.example.tgzoom.letswatch.data.Movie;
import com.example.tgzoom.letswatch.data.Trailer;
import com.example.tgzoom.letswatch.util.StringUtils;
import com.example.tgzoom.letswatch.util.URIUtils;
import com.example.tgzoom.letswatch.widget.LinearAdapterLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetailFragment extends Fragment implements MovieDetailContract.View {

    public static final String TAG = "movie_detail_fragment";
    private static final String PARCELABLE_TRAILER_LIST = "trailers";

    @BindView(R.id.movie_title) TextView title_textview;
    @BindView(R.id.trailer_linearlist) LinearAdapterLayout mLinearAdapterLayout;
    @BindView(R.id.movie_cover_imageview) ImageView cover_imageview;
    @BindView(R.id.movie_release_date) TextView release_date_textview;
    @BindView(R.id.movie_duration_textview) TextView movie_title_textview;
    @BindView(R.id.movie_rating_textview) TextView rating_textview;
    @BindView(R.id.movie_overview_textview) TextView overview_textview;

    private Movie movie;

    private MovieDetailContract.Presenter mMovieDetailPresenter;

    private TrailerAdapter mTrailerAdapter;

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
        mMovieDetailPresenter.start();
        showMovieDetailInformation(movie);
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
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        ButterKnife.bind(this, rootView);

        mTrailerAdapter = new TrailerAdapter(getContext(), mMoviesDetailListener);

        mLinearAdapterLayout.setAdapter(mTrailerAdapter);

        if(savedInstanceState != null){

            List<Trailer> trailers = savedInstanceState.getParcelableArrayList(PARCELABLE_TRAILER_LIST);

            mTrailerAdapter.swapArrayList(trailers);

        }

        setHasOptionsMenu(true);

        Bundle arguments = getArguments();

        if (arguments != null) {

            movie = (Movie) arguments.get(MovieDetailActivity.MOVIE_OBJECT);

        }

        return rootView;
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

    @Override
    public void showMovieDetailInformation(Movie movie) {

        title_textview.setText(movie.getTitle());

        String release_year = StringUtils.formatMovieYear(movie.getRelease_date());

        release_date_textview.setText(release_year);

        rating_textview.setText(movie.getVote_average().toString());

        overview_textview.setText(movie.getOverview());

        if (movie.getPoster_path() != null) {

            String poster = URIUtils.buildPosterPath(movie.getPoster_path()).toString();

            Glide.with(getActivity())
                    .load(poster)
                    .placeholder(R.color.colorPrimary)
                    .into(cover_imageview);
        }

        if (movie.getBackdrop_path() != null) {

            String backdrop_path = URIUtils.buildBackDropPath(movie.getBackdrop_path()).toString();

            ((MovieDetailCallback) getActivity()).setBackdropImage(backdrop_path);

        }

        ((MovieDetailCallback) getActivity()).setToolbarTitle(movie.getTitle());
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
