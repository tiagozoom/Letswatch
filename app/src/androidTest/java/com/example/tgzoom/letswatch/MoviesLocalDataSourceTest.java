package com.example.tgzoom.letswatch;

import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;
import com.example.tgzoom.letswatch.data.Movie;
import com.example.tgzoom.letswatch.data.movie.source.local.MoviesLocalDataSource;
import com.example.tgzoom.letswatch.util.schedulers.ImmediateScheduler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.List;

import rx.observers.TestSubscriber;
import static org.junit.Assert.*;

/**
 * Created by tgzoom on 12/29/16.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MoviesLocalDataSourceTest {

    private MoviesLocalDataSource moviesLocalDataSource;

    private Movie createMovie(){
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

        return movie;
    }

    @Before
    public void setup(){
        ImmediateScheduler immediateScheduler = new ImmediateScheduler();
        moviesLocalDataSource = new MoviesLocalDataSource(InstrumentationRegistry.getTargetContext(), immediateScheduler);
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
    public void getAllMovies(){
        Movie movie = createMovie();
        long movieId = moviesLocalDataSource.markAsFavourite(movie);
        TestSubscriber<List<Movie>> testSubscriber = new TestSubscriber<>();
        moviesLocalDataSource.getFavouriteMovies().subscribe(testSubscriber);
        testSubscriber.assertValueCount(1);
    }

    @Test
    public void saveMovie(){
        Movie movie = createMovie();
        long movieId = moviesLocalDataSource.markAsFavourite(movie);
        assertTrue("Error occurred while insering a new movie",movieId != 0);
        TestSubscriber<Movie> testSubscriber = new TestSubscriber<>();
        moviesLocalDataSource.getMovie(movie.getApi_movie_id()).subscribe(testSubscriber);
        testSubscriber.assertValueCount(1);
        Movie inserted_movie = testSubscriber.getOnNextEvents().get(0);
        assertTrue(inserted_movie.isFavourite());
    }

    @Test
    public void getMoviesIds(){
        Movie movie = createMovie();
        moviesLocalDataSource.markAsFavourite(movie);
        TestSubscriber testSubscriber = new TestSubscriber<>();
        moviesLocalDataSource.getFavouriteMoviesIds().subscribe(testSubscriber);
        testSubscriber.assertValueCount(1);
    }
}
