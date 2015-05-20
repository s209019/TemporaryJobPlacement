package it.polito.mobile.temporaryjobplacement.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;

import java.util.Date;
import java.util.List;

/**
 * Created by Nicolò on 20/05/2015.
 */
@ParseClassName("Education")
public class Education extends ParseObject implements Comparable<Education> {
    //String name

    public String getDegree() {
        return getString("degree");
    }
    public void setDegree(String value) {
        put("degree", value);
    }
    public String getCourse() {
        return getString("course");
    }
    public void setCourse(String value) {
        put("course", value);
    }

    public String getUniversity() {
        return getString("university");
    }
    public void setUniversity(String value) {
        put("university", value);
    }

    public String getMark() {
        return getString("mark");
    }
    public void setMark(String value) {
        put("mark", value);
    }


    public Date getFromDate() {
        return getDate("fromDate");
    }
    public void setFromDate(Date value) {
        put("fromDate", value);
    }

    public Date getToDate() {
        return getDate("toDate");
    }
    public void setToDate(Date value) {
        put("toDate", value);
    }


    public static ParseQuery<Education> getQuery() {
        return ParseQuery.getQuery(Education.class);
    }


    @Override
    public int compareTo(Education another) {
        Date thisDate=getFromDate();
        Date anotherDate=another.getFromDate();
        int returnValue=0;
        if(thisDate.getTime()>anotherDate.getTime())returnValue=1;
        else returnValue=-1;
        return -1*returnValue;//decrescente

    }



    public static int compare(Education e1, Education e2){
        Date thisDate=e1.getFromDate();
        Date anotherDate=e2.getFromDate();
        int returnValue=0;
        if(thisDate.getTime()>anotherDate.getTime())returnValue=1;
        else returnValue=-1;
        return -1*returnValue;//decrescente
    }

}
