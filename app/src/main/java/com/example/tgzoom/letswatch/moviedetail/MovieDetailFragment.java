package com.example.tgzoom.letswatch.moviedetail;

import android.os.Bundle;
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
import com.example.tgzoom.letswatch.util.StringUtils;
import com.example.tgzoom.letswatch.util.URIUtils;
import com.example.tgzoom.letswatch.widget.LinearAdapterLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetailFragment extends Fragment implements MovieDetailContract.View{

    public static final String TAG = "movie_detail_fragment";

    @BindView(R.id.movie_title) TextView title_textview;
    @BindView(R.id.trailer_linearlist) LinearAdapterLayout mLinearAdapterLayout;
    @BindView(R.id.movie_cover_imageview) ImageView cover_imageview;
    @BindView(R.id.movie_release_date) TextView release_date_textview;
    @BindView(R.id.movie_duration_textview) TextView movie_title_textview;
    @BindView(R.id.movie_rating_textview) TextView rating_textview;
    @BindView(R.id.movie_overview_textview) TextView overview_textview;

    private MovieDetailContract.Presenter mMovieDetailPresenter;

    private MovieDetailListener mMoviesDetailListener = new MovieDetailListener() {

        @Override
        public void onMarkAsFavorite(Movie movie) {
            mMovieDetailPresenter.markAsFavourite(movie);
        }

        @Override
        public void onUnmarAsFavorite(int movieApiId) {
            mMovieDetailPresenter.unmarkAsFavourite(movieApiId);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_movie_detail, container, false);

        ButterKnife.bind(this,rootView);

        setHasOptionsMenu(true);

        Bundle arguments = getArguments();

        if (arguments != null) {

            Movie movie = (Movie) arguments.get(MovieDetailActivity.MOVIE_OBJECT);

            showMovie(movie);
        }

        return rootView;
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showMarkedAsFavouriteMessage() {

    }

    @Override
    public void showUnmarkedAsFavouriteMessage() {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_movie_detail, menu);
    }

    @Override
    public void showMovie(Movie movie) {
        title_textview.setText(movie.getTitle());
        String release_year = StringUtils.formatMovieYear(movie.getRelease_date());
        release_date_textview.setText(release_year);
        rating_textview.setText(movie.getVote_average().toString());
        overview_textview.setText(movie.getOverview());

        if(movie.getPoster_path() != null){
            String poster = URIUtils.buildPosterPath(movie.getPoster_path()).toString();
            Glide.with(getActivity())
                    .load(poster)
                    .placeholder(R.color.colorPrimary)
                    .into(cover_imageview);
        }

        if(movie.getBackdrop_path() != null){
            String  backdrop_path = URIUtils.buildBackDropPath(movie.getBackdrop_path()).toString();
            ((MovieDetailCallback) getActivity()).setBackdropImage(backdrop_path);
        }

        ((MovieDetailCallback) getActivity()).setToolbarTitle(movie.getTitle());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                return true;
            case R.id.favorite_button:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setPresenter(MovieDetailContract.Presenter presenter) {

    }
}
