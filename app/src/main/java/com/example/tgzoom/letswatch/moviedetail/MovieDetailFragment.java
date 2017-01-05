package com.example.tgzoom.letswatch.moviedetail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.tgzoom.letswatch.R;
import com.example.tgzoom.letswatch.data.Movie;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetailFragment extends Fragment implements MovieDetailContract.View{

    public static final String TAG = "movie_detail_fragment";

    private MovieDetailContract.Presenter mMovieDetailPresenter;

    public static MovieDetailFragment newInstance(Movie movie) {
        MovieDetailFragment detailFragment = new MovieDetailFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable(MovieDetailActivity.MOVIE_OBJECT, movie);
        detailFragment.setArguments(arguments);
        return detailFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_movies, container, false);

        Bundle arguments = getArguments();

        if (arguments != null) {
            Movie movie = (Movie) arguments.get(MovieDetailActivity.MOVIE_OBJECT);
        }

        return rootView;
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showMovie(Movie movies) {

    }

    @Override
    public void setPresenter(MovieDetailContract.Presenter presenter) {

    }
}
