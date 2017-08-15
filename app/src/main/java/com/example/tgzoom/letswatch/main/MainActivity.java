package com.example.tgzoom.letswatch.main;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import com.example.tgzoom.letswatch.R;
import com.example.tgzoom.letswatch.favourites.FavouritesFragment;
import com.example.tgzoom.letswatch.movies.MoviesFragment;
import com.example.tgzoom.letswatch.util.ActivityUtils;
import com.facebook.stetho.Stetho;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.bottom_navigation_view) BottomNavigationView mBottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        Stetho.initializeWithDefaults(this);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);

        if(fragment == null){
            fragment = new MoviesFragment();
            ActivityUtils.addFragment(fm,fragment,MoviesFragment.TAG,R.id.fragment_container);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        FragmentManager fm = getSupportFragmentManager();
        Class fragment = null;
        String fragmentTag = null;

        switch (item.getItemId()) {
            case R.id.nav_movies:
                fragment = MoviesFragment.class;
                fragmentTag = MoviesFragment.TAG;
                break;
            case R.id.nav_favorites:
                fragment = FavouritesFragment.class;
                fragmentTag = FavouritesFragment.TAG;
                break;
            default:
                break;
        }

        if (fm.findFragmentByTag(fragmentTag) == null) {
            try {
                ActivityUtils.replaceFragment(fm,(Fragment) fragment.newInstance(),fragmentTag,R.id.fragment_container);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        item.setChecked(true);
        return true;
    }
}
