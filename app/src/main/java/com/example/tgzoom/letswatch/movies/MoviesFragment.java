package com.example.tgzoom.letswatch.movies;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tgzoom.letswatch.R;
import com.example.tgzoom.letswatch.data.Movie;
import com.example.tgzoom.letswatch.util.StringUtils;
import com.example.tgzoom.letswatch.util.EndlessRecyclerViewScrollListener;
import com.example.tgzoom.letswatch.util.URIUtils;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoviesFragment extends Fragment implements MoviesContract.View{

    public final static String TAG = "MoviesFragment";
    private MovieAdapter mMovieAdapter;
    private EndlessRecyclerViewScrollListener mEndlessRecyclerViewScrollListener;
    private MoviesContract.Presenter mPresenter;

    @BindView(R.id.moviedb_recyclerview) RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh) SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_movies, container, false);
        ButterKnife.bind(this,rootView);

        mMovieAdapter = new MovieAdapter(new ArrayList<Movie>());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), Integer.valueOf(getString(R.string.gridlayout_span_count)));
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(mMovieAdapter);
        mRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager,1) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                mPresenter.loadMovies(false,page);
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setLoadingIndicator(boolean active) {

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
    public void showUnmarkedAsFavouriteMessage() {

    }

    @Override
    public void setPresenter(MoviesContract.Presenter presenter) {
        if(presenter != null){
            mPresenter = presenter;
        }
    }

    public static class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieDBHolder> {
        private List<Movie> mMovieDBArrayList = new ArrayList<Movie>();

        public MovieAdapter(ArrayList<Movie> movieArrayList) {
            mMovieDBArrayList = movieArrayList;
        }

        public static class MovieDBHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.card_image) ImageView cardImage;
            @BindView(R.id.card_title) TextView cardTitle;
            @BindView(R.id.movie_year) TextView movie_year;
            @BindView(R.id.movie_rate) TextView movie_rate;
            @BindView(R.id.card_menu) ImageView card_menu;
            @BindView(R.id.movie_layout) RelativeLayout movie_layout;
            Context mContext;

            public MovieDBHolder(View view, final Context context) {
                super(view);
                ButterKnife.bind(this, view);
                mContext = context;
            }
        }

        @Override
        public MovieDBHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_movies_list_item, parent, false);
            return new MovieDBHolder(view, parent.getContext());
        }

        @Override
        public void onBindViewHolder(final MovieDBHolder holder, final int position) {
            final Movie movie = mMovieDBArrayList.get(position);

            String title = StringUtils.formatMovieTitle(holder.mContext, movie.getTitle());
            holder.cardTitle.setText(title);

            String formated_year = StringUtils.formatMovieYear(movie.getRelease_date());
            holder.movie_year.setText(formated_year);

            if (movie.getPoster_path() != null) {

                String poster_path = URIUtils.buildPosterPath(movie.getPoster_path()).toString();

                Glide.with(holder.mContext)
                        .load(poster_path)
                        .centerCrop()
                        .placeholder(R.color.colorPrimary)
                        .into(holder.cardImage);
            }

            holder.movie_rate.setText(movie.getVote_average().toString());

        }

        @Override
        public int getItemCount() {
            if (mMovieDBArrayList != null) {
                return mMovieDBArrayList.size();
            }
            return 0;
        }

        @Override
        public long getItemId(int position) {
            return mMovieDBArrayList.get(position).getApi_movie_id();
        }

        public void addArrayList(List<Movie> movieDBArrayList){
            if(mMovieDBArrayList.size() <= 0){
                mMovieDBArrayList = movieDBArrayList;
            }else{
                mMovieDBArrayList.addAll(movieDBArrayList);
            }

            notifyDataSetChanged();
        }

    }
}
