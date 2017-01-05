package com.example.tgzoom.letswatch.favourites;

import com.example.tgzoom.letswatch.data.Movie;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by tgzoom on 1/5/17.
 */

public interface FavouriteObservable {
    Observable<FavouriteObservableImp.FavouriteClickEvent> getFavoriteClickEventObservable();
    void markAsFavourite(Movie movie);
    void unmarkAsFavourite(int movieApiId);
}
