package it.polito.mobile.temporaryjobplacement.commons.utils;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.mobile.temporaryjobplacement.TemporaryJobPlacementApp;
import it.polito.mobile.temporaryjobplacement.model.Education;

public class DegreeAnalyzer{


public static String getBestDegree(List<Education> educations){


    try {


        ArrayList<String> allDegrees = TemporaryJobPlacementApp.getAllDegrees();

        ArrayList<String> degrees = new ArrayList<>();
        for (Education e : educations) degrees.add(e.getDegree());
        Map<String, Integer> weights = new HashMap<String, Integer>();
        for (String degree : degrees) {
            weights.put(degree, allDegrees.indexOf(degree));
        }
        String theBest = degrees.get(0);
        for (String degree : degrees) {
            if (weights.get(degree) > weights.get(theBest))
                theBest = degree;

        }
        return theBest;
    }catch(Exception e){
        e.printStackTrace();
       return "";
    }


}




}