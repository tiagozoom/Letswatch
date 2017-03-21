package com.example.tgzoom.letswatch.search;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tgzoom.letswatch.App;
import com.example.tgzoom.letswatch.AppComponent;
import com.example.tgzoom.letswatch.R;

import javax.inject.Inject;

public class SearchFragment extends Fragment implements SearchContract.View
{

    public static final String TAG = "search_fragment";
    @Inject SearchPresenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerSearchComponent.builder()
                .appComponent(((App) getActivity().getApplication()).getmAppComponent())
                .searchPresenterModule(new SearchPresenterModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        mPresenter.loadMovies("logan");

        return rootView;
    }

    @Override
    public void setPresenter(SearchPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setLoadingIndicator(boolean isLoading) {

    }

    @Override
    public void updateMovies(int movieApiId, boolean isFavourite) {

    }

    @Override
    public void showLoadingMoviesError() {

    }

    @Override
    public void hideRefresh() {

    }
}
