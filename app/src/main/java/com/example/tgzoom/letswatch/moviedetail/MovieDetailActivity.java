package com.example.tgzoom.letswatch.moviedetail;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.tgzoom.letswatch.App;
import com.example.tgzoom.letswatch.BR;
import com.example.tgzoom.letswatch.R;
import com.example.tgzoom.letswatch.data.Movie;
import com.example.tgzoom.letswatch.databinding.ActivityMovieDetailBinding;
import com.example.tgzoom.letswatch.movies.MoviesPresenter;
import com.example.tgzoom.letswatch.util.ActivityUtils;
import com.example.tgzoom.letswatch.util.URIUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity{

    public static final String MOVIE_OBJECT = "movie_object";
    @Inject MovieDetailPresenter mMovieDetailPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMovieDetailBinding movieDetailActivitBinding = DataBindingUtil.setContentView(this,R.layout.activity_movie_detail);
        setSupportActionBar(movieDetailActivitBinding.detailToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FragmentManager fm = getSupportFragmentManager();
        MovieDetailFragment fragment = (MovieDetailFragment) fm.findFragmentByTag(MovieDetailFragment.TAG);
        Movie movie = getIntent().getParcelableExtra(MOVIE_OBJECT);
        movieDetailActivitBinding.setMovie(movie);

        if (fragment == null) {
            fragment = new MovieDetailFragment();
            Bundle arguments = new Bundle();
            arguments.putParcelable(MOVIE_OBJECT, movie);
            fragment.setArguments(arguments);
            ActivityUtils.addFragment(fm, fragment, MovieDetailFragment.TAG, R.id.detail_fragment_container);
        }

        DaggerMovieDetailComponent.builder()
                .appComponent(((App) getApplication()).getmAppComponent())
                .movieDetailPresenterModule(new MovieDetailPresenterModule(fragment))
                .build()
                .inject(this);
    }

    @BindingAdapter("bind:backdropImage")
    public static void setBackdropImage(ImageView backdropImageView, String backdrop_path) {
        if(backdrop_path != null){
            String imageUrl = URIUtils.buildBackDropPath(backdrop_path).toString();
            Glide.with(backdropImageView.getContext())
                    .load(imageUrl)
                    .override(backdropImageView.getResources().getInteger(R.integer.movie_detail_backdrop_width), backdropImageView.getResources().getInteger(R.integer.movie_detail_backdrop_height))
                    .into(backdropImageView);
        }
    }
}
