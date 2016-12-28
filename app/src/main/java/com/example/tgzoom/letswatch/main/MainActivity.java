package com.example.tgzoom.letswatch.main;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.example.tgzoom.letswatch.App;
import com.example.tgzoom.letswatch.R;
import com.example.tgzoom.letswatch.data.source.MoviesRepositoryComponent;
import com.example.tgzoom.letswatch.favourites.FavouritesFragment;
import com.example.tgzoom.letswatch.favourites.FavouritesPresenterModule;
import com.example.tgzoom.letswatch.movies.DaggerMoviesComponent;
import com.example.tgzoom.letswatch.movies.MoviesComponent;
import com.example.tgzoom.letswatch.movies.MoviesFragment;
import com.example.tgzoom.letswatch.movies.MoviesPresenterModule;
import com.example.tgzoom.letswatch.network.NetworkModule;
import com.example.tgzoom.letswatch.util.ActivityUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.navigation_view)
    NavigationView mNavigationView;

    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        mNavigationView.setNavigationItemSelectedListener(this);

        FragmentManager fragmentManager = getSupportFragmentManager();

        MoviesFragment moviesFragment = (MoviesFragment) fragmentManager.findFragmentById(R.id.fragment_container);

        if(moviesFragment == null){
            moviesFragment = new MoviesFragment();
            ActivityUtils.addFragment(fragmentManager,moviesFragment,MoviesFragment.TAG,R.id.fragment_container);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_movies:
                break;
            case R.id.nav_favorites:
                break;
            default:
                break;
        }

        mDrawerLayout.closeDrawers();
        return true;
    }
}
