package it.polito.mobile.temporaryjobplacement;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

import it.polito.mobile.temporaryjobplacement.model.Company;
import it.polito.mobile.temporaryjobplacement.model.JobOffer;
import it.polito.mobile.temporaryjobplacement.model.Student;

public class TemporaryJobPlacementApp extends Application {

    public static int exitCode=2; //HOME
    public static int exitCodeListOffer=5; //Go back to the job offers list after the application

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(getApplicationContext());
        ParseObject.registerSubclass(JobOffer.class);
        ParseObject.registerSubclass(Company.class);
        ParseObject.registerSubclass(Student.class);
        ParseObject.registerSubclass(it.polito.mobile.temporaryjobplacement.model.Application.class);
        Parse.initialize(getApplicationContext(), "YfmdTSE9tXlIhIsnPZmZOjQMt8tu8nq7pXZ9mA0G", "Y2GtjfVYqbavbI4KeKvoBGM4EXq4Qs9wWfCzMpQS");

        //PROVA GIT - blabla

        }
}
