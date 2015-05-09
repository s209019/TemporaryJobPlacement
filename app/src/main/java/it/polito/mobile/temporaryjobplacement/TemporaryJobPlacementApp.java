package it.polito.mobile.temporaryjobplacement;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by Nicolò on 21/04/2015.
 */
public class TemporaryJobPlacementApp extends Application {

public static int exitCode=2;

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(getApplicationContext());
        Parse.initialize(getApplicationContext(), "YfmdTSE9tXlIhIsnPZmZOjQMt8tu8nq7pXZ9mA0G", "Y2GtjfVYqbavbI4KeKvoBGM4EXq4Qs9wWfCzMpQS");

        }
}
