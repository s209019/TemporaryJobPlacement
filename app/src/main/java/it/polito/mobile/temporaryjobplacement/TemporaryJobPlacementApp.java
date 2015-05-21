package it.polito.mobile.temporaryjobplacement;

import android.app.Application;
import android.content.Context;

import com.parse.Parse;
import com.parse.ParseObject;

import java.util.ArrayList;

import it.polito.mobile.temporaryjobplacement.commons.utils.FileManager;
import it.polito.mobile.temporaryjobplacement.model.Company;
import it.polito.mobile.temporaryjobplacement.model.Education;
import it.polito.mobile.temporaryjobplacement.model.JobOffer;
import it.polito.mobile.temporaryjobplacement.model.Message;
import it.polito.mobile.temporaryjobplacement.model.Student;

public class TemporaryJobPlacementApp extends Application {

    public final static int exitCode=2; //HOME
    public final static int exitCodeListOffer=5; //Go back to the job offers list after the application
    public final  static int objectsForPage=2;

    public final static int TIMEOUT_ITERATIONS=15;
    public final static int TIMEOUT_MILLIS=500;
    public static Context ctx;



    public static ArrayList<String> getAllDegrees() {
        if(ctx==null)return new ArrayList<String>();
       return FileManager.readRowsFromFile(ctx, "educations.dat");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(getApplicationContext());
        ParseObject.registerSubclass(JobOffer.class);
        ParseObject.registerSubclass(Company.class);
        ParseObject.registerSubclass(Student.class);
        ParseObject.registerSubclass(Message.class);
        ParseObject.registerSubclass(Education.class);
        ParseObject.registerSubclass(it.polito.mobile.temporaryjobplacement.model.Application.class);
        Parse.initialize(getApplicationContext(), "YfmdTSE9tXlIhIsnPZmZOjQMt8tu8nq7pXZ9mA0G", "Y2GtjfVYqbavbI4KeKvoBGM4EXq4Qs9wWfCzMpQS");

        ctx=getApplicationContext();

        }





}
