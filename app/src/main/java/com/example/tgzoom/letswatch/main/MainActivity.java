package com.example.tgzoom.letswatch.main;

import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
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
import com.example.tgzoom.letswatch.util.PreferencesUtils;
import com.facebook.stetho.Stetho;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.bottom_navigation_view) BottomNavigationView mBottomNavigationView;
    @BindView(R.id.appbarLayout) AppBarLayout mBarLayout;

    private enum Menus{
        most_popular,
        top_rated,
        favourites
    }

    private Map<Integer,Menus> menuOptions = new HashMap<Integer,Menus>(){{
        put(R.id.nav_most_popular,Menus.most_popular);
        put(R.id.nav_top_rated,Menus.top_rated);
        put(R.id.nav_favourites,Menus.favourites);
    }};

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
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        if(fragment == null){
            fragment = new MoviesFragment();
            ActivityUtils.addFragment(fm,fragment,MoviesFragment.TAG,R.id.fragment_container);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        setSelectedMenu(menuOptions.get(item.getItemId()));
        item.setChecked(true);
        return true;
    }

    public void setUpAppBar(String title){
        getSupportActionBar().setTitle(title);
        mBarLayout.setExpanded(true,true);
    }

    public void setSelectedMenu(Menus item){
        FragmentManager fm = getSupportFragmentManager();
        Class fragment = null;
        String fragmentTag = null;
        String appBarTitle = "";
        String movieSort = getString(R.string.pref_order_popularity);

        switch (item) {
            case most_popular:
                fragment = MoviesFragment.class;
                fragmentTag = MoviesFragment.TAG;
                appBarTitle = getString(R.string.drawer_menu_most_popular);
                movieSort = getString(R.string.pref_order_popularity);
                break;

            case top_rated:
                fragment = MoviesFragment.class;
                fragmentTag = MoviesFragment.TAG;
                appBarTitle = getString(R.string.drawer_menu_top_rated);
                movieSort = getString(R.string.pref_order_vote_average);
                break;
            case favourites:
                fragment = FavouritesFragment.class;
                fragmentTag = FavouritesFragment.TAG;
                appBarTitle = getString(R.string.drawer_menu_most_popular);
                break;
            default:
                break;
        }

        Fragment instanciatedFragment = fm.findFragmentByTag(fragmentTag);

        if (instanciatedFragment == null) {
            try {
                ActivityUtils.replaceFragment(fm,(Fragment) fragment.newInstance(),fragmentTag,R.id.fragment_container);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            ((MoviesFragment) instanciatedFragment).setMoviesSort(movieSort);
        }

        setUpAppBar(appBarTitle);
    }
}
