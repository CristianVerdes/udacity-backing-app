package com.example.cristianverdes.bakingapp.data.repository;

import android.os.AsyncTask;

import com.example.cristianverdes.bakingapp.data.local.AppDatabase;
import com.example.cristianverdes.bakingapp.data.model.Timer;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.reactivex.subjects.PublishSubject;

/**
 * Created by cristian.verdes on 22.03.2018.
 */

public class GetTimerFromDbTask extends AsyncTask<Void, Void, Void> {
    private PublishSubject<Boolean> isDataCached = PublishSubject.create();
    private WeakReference<AppDatabase> appDatabaseWeakReference;
    private static final long ONE_MINUTE_IN_MILLIS = 60000;

    public GetTimerFromDbTask(AppDatabase appDatabaseWeakReference) {
        this.appDatabaseWeakReference = new WeakReference<AppDatabase>(appDatabaseWeakReference);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        if (appDatabaseWeakReference.get().timerDao().getEntryExists() != 0 && timeForNewCache(appDatabaseWeakReference.get().timerDao().getCreatedAt())) {
            isDataCached.onNext(true);
        } else {
            isDataCached.onNext(false);
        }

        return null;
    }

    private boolean timeForNewCache(Timer lastTimeCache) {
        // Current Date
        Date currentDate = new Date();

        // Get last time
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date lastTimeCacheDate = new Date();
        try {
            lastTimeCacheDate = sdf.parse(lastTimeCache.getCreatedAt());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long timeInMilliseconds = lastTimeCacheDate.getTime();
        Date afterAddingThirtyMinutes = new Date(timeInMilliseconds + (30 * ONE_MINUTE_IN_MILLIS ));

        // Verify if 30 minutes passed from last cached data
        // And if so... send false and load new data from API
        return afterAddingThirtyMinutes.getTime() < currentDate.getTime();
    }

    public PublishSubject<Boolean> getIsDataCached() {
        return isDataCached;
    }
}
