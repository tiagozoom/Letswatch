package com.example.tgzoom.letswatch.favourites;

import com.example.tgzoom.letswatch.data.Movie;
import com.example.tgzoom.letswatch.util.schedulers.BaseScheduler;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by tgzoom on 1/5/17.
 */

public class FavouriteObservableImp implements FavouriteObservable {
    BaseScheduler mScheduler;

    private static final PublishSubject<FavouriteClickEvent> FAVORITE_CLICK_EVENT_PUBLISH_SUBJECT = PublishSubject.create();

    public FavouriteObservableImp(BaseScheduler scheduler){
        mScheduler = scheduler;
    }

    @Override
    public Observable<FavouriteClickEvent> getFavoriteClickEventObservable() {
        return FAVORITE_CLICK_EVENT_PUBLISH_SUBJECT.asObservable().observeOn(mScheduler.ui());
    }

    @Override
    public void markAsFavourite(Movie movie) {
        FAVORITE_CLICK_EVENT_PUBLISH_SUBJECT.onNext(new FavouriteClickEvent(movie.getApi_movie_id(),true));
    }

    @Override
    public void unmarkAsFavourite(int movieApiId) {
        FAVORITE_CLICK_EVENT_PUBLISH_SUBJECT.onNext(new FavouriteClickEvent(movieApiId,false));
    }

    public static class FavouriteClickEvent {
        public int movieApiId;
        public boolean isFavorite;

        private FavouriteClickEvent(int movieApiId, boolean isFavorite) {
            this.movieApiId = movieApiId;
            this.isFavorite = isFavorite;
        }
    }
}
