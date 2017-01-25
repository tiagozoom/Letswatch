package com.example.tgzoom.letswatch.moviedetail;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.tgzoom.letswatch.App;
import com.example.tgzoom.letswatch.R;
import com.example.tgzoom.letswatch.movies.MoviesPresenter;
import com.example.tgzoom.letswatch.util.ActivityUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity implements MovieDetailCallback {

    public static final String MOVIE_OBJECT = "movie_object";
    @Inject MovieDetailPresenter mMovieDetailPresenter;
    @BindView(R.id.backdrop_imageview) ImageView mBackdropImageview;
    @BindView(R.id.detail_toolbar) Toolbar mToolbar;
    @BindView(R.id.main_collapsing) CollapsingToolbarLayout mMainCollapsing;
    @BindView(R.id.detail_coordinator_layout) CoordinatorLayout mDetailcoordinatorLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FragmentManager fm = getSupportFragmentManager();
        MovieDetailFragment fragment = (MovieDetailFragment) fm.findFragmentByTag(MovieDetailFragment.TAG);


        if (fragment == null) {
            fragment = new MovieDetailFragment();
            Bundle arguments = new Bundle();
            arguments.putParcelable(MOVIE_OBJECT, getIntent().getParcelableExtra(MOVIE_OBJECT));
            fragment.setArguments(arguments);
            ActivityUtils.addFragment(fm, fragment, MovieDetailFragment.TAG, R.id.detail_fragment_container);

        }

        DaggerMovieDetailComponent.builder()
                .moviesRepositoryComponent(((App) getApplication()).getMoviesRepositoryComponent())
                .movieDetailPresenterModule(new MovieDetailPresenterModule(fragment))
                .build()
                .inject(this);
    }

    @Override
    public void setBackdropImage(String imageUrl) {
        Glide.with(this)
                .load(imageUrl)
                .override(getResources().getInteger(R.integer.movie_detail_backdrop_width), getResources().getInteger(R.integer.movie_detail_backdrop_height))
                .into(mBackdropImageview);
    }

    @Override
    public void setToolbarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
}
