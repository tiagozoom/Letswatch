package com.example.tgzoom.letswatch.search;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.tgzoom.letswatch.R;
import com.example.tgzoom.letswatch.util.ActivityUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.activity_search_toolbar) Toolbar toolbar;
    @BindView(R.id.menu_search_activity_search) SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        mSearchView.requestFocus();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleSearch(intent);
    }

    public void handleSearch(Intent intent){
        if(Intent.ACTION_SEARCH.equals(intent.getAction())){
            String typedSearch = intent.getStringExtra(SearchManager.QUERY);
            SearchFragment searchFragment = new SearchFragment();
            ActivityUtils.replaceFragment(getSupportFragmentManager(),searchFragment,SearchFragment.TAG,R.id.search_fragment_container);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
