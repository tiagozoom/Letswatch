package com.example.tgzoom.letswatch.search;

import android.view.View;

import com.example.tgzoom.letswatch.AppModule;
import com.example.tgzoom.letswatch.data.source.MoviesRepositoryModule;
import com.example.tgzoom.letswatch.network.NetworkModule;

import dagger.Module;
import dagger.Provides;

/**
 * Created by tgzoom on 3/20/17.
 */
@Module
public class SearchPresenterModule {
    private SearchContract.View mView;

    public SearchPresenterModule(SearchContract.View view) {
        mView = view;
    }

    @Provides
    public SearchContract.View provideSearchView() {
        return mView;
    }
}
