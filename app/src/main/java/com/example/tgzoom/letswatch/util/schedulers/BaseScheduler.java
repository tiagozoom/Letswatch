package com.example.tgzoom.letswatch.util.schedulers;

import android.support.annotation.NonNull;

import rx.Scheduler;

/**
 * Created by tgzoom on 1/2/17.
 */

public interface BaseScheduler {
    @NonNull
    Scheduler computation();

    @NonNull
    Scheduler io();

    @NonNull
    Scheduler ui();

}
