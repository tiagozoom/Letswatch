package com.example.tgzoom.letswatch;

import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.example.tgzoom.letswatch.data.Movie;
import com.example.tgzoom.letswatch.data.source.local.MoviesLocalDataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.observers.TestSubscriber;

import static org.junit.Assert.*;

/**
 * Created by tgzoom on 12/29/16.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MoviesLocalDataSourceTest {

    private MoviesLocalDataSource moviesLocalDataSource;

    @Before
    public void setup(){
        moviesLocalDataSource = new MoviesLocalDataSource(InstrumentationRegistry.getTargetContext());
    }

    @After
    public void cleanUp() {
        moviesLocalDataSource.deleteAllMovies();
    }

    @Test
    public void testPreConditions() {
        assertNotNull(moviesLocalDataSource);
    }

    @Test
    public void saveMovies(){
        final Movie movie = new Movie();

        int movieApiId = 278;
        String backdropPath = "/xBKGJQsAIeweesB79KC89FpBrVr.jpg";
        String originalTitle = "The Shawshank Redemption";
        String overview = "Framed in the 1940s for the double murder of his wife and her lover, upstanding banker Andy Dufresne begins a new life at the Shawshank prison, where he puts his accounting skills to work for an amoral warden. During his long stretch in prison, Dufresne comes to be admired by the other inmates -- including an older prisoner named Red -- for his integrity and unquenchable sense of hope.";
        Double popularity = 6.206623;
        String posterPath = "/9O7gLzmreU0nGkIB6K3BsJbzvNv.jpg";
        String releaseDate = "1994-09-10";
        String title = "The Shawshank Redemption";
        Double voteAverage = 8.4;
        int voteCount = 5681;
        boolean isFavourite = true;

        movie.setApi_movie_id(movieApiId);
        movie.setBackdrop_path(backdropPath);
        movie.setOriginal_title(originalTitle);
        movie.setOverview(overview);
        movie.setPopularity(popularity);
        movie.setFavourite(isFavourite);
        movie.setRelease_date(releaseDate);
        movie.setVote_average(voteAverage);
        movie.setVote_count(voteCount);
        movie.setPoster_path(posterPath);
        movie.setTitle(title);

        long movieId = moviesLocalDataSource.markAsFavourite(movie);
        movie.setId(movieId);
        assertTrue("Error occurred while insering a new movie",movieId != 0);

        TestSubscriber<Movie> testSubscriber = new TestSubscriber<>();
        moviesLocalDataSource.getMovie(movie.getApi_movie_id());
        testSubscriber.assertCompleted();
        testSubscriber.assertValueCount(1);
    }

}
