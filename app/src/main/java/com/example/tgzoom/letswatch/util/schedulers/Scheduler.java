package com.example.tgzoom.letswatch.util.schedulers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by tgzoom on 1/2/17.
 */

public class Scheduler implements BaseScheduler {

    @Nullable
    private static Scheduler INSTANCE;

    // Prevent direct instantiation.
    private Scheduler() {
    }

    public static synchronized Scheduler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Scheduler();
        }
        return INSTANCE;
    }

    @NonNull
    @Override
    public rx.Scheduler computation() {
        return Schedulers.computation();
    }

    @NonNull
    @Override
    public rx.Scheduler io() {
        return Schedulers.io();
    }

    @NonNull
    @Override
    public rx.Scheduler ui() {
        return AndroidSchedulers.mainThread();
    }

    @NonNull
    @Override
    public <T> Observable.Transformer<T, T> applySchedulers(){
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> tObservable) {
                return tObservable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
}
