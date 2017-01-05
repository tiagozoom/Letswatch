package com.example.tgzoom.letswatch.moviedetail;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.tgzoom.letswatch.R;
import com.example.tgzoom.letswatch.movies.MoviesPresenter;
import com.example.tgzoom.letswatch.util.ActivityUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity {

    public static final String MOVIE_OBJECT = "movie_object";

    @Inject MovieDetailPresenter mMovieDetailPresenter;

    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie_detail);

        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        FragmentManager fm = getSupportFragmentManager();

        Bundle arguments = new Bundle();

        arguments.putParcelable(MOVIE_OBJECT,getIntent().getParcelableExtra(MOVIE_OBJECT));

        MovieDetailFragment fragment = new MovieDetailFragment();

        fragment.setArguments(arguments);

        ActivityUtils.addFragment(fm,fragment,MovieDetailFragment.TAG,R.id.detail_fragment_container);

        DaggerMovieDetailComponent.builder().movieDetailPresenterModule(new MovieDetailPresenterModule(fragment)).build();
    }
}
