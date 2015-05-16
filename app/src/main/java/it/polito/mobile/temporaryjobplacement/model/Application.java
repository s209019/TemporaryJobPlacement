package it.polito.mobile.temporaryjobplacement.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by Enrico on 15/05/15.
 */
@ParseClassName("Application")
   public class Application extends ParseObject {

    public Student getStudent(){
        return (Student)getParseObject("student");

    }

    public void setStudent(Student value){
        put("student", value);
    }

    public void setJobOffer(JobOffer value){
        put("jobOffer", value);
    }


    public JobOffer getJobOffer(){
        return (JobOffer)getParseObject("jobOffer");

    }

    public String getStatus(){
        return getString("status");

    }

    public void setStatus(String value){
        put("status", value);
    }


    public String getStudentNotes(){
        return getString("studentNotes");

    }

    public void setStudentNotes(String value){
        put("studentNotes", value);
    }

    public String getCompanyNotes(){
        return getString("companyNotes");

    }

    public void setCompanyNotes(String value){
        put("companyNotes", value);
    }

    public String getFeedback(){
        return getString("feedback");

    }

    public void setFeedback(String value){
        put("feedback", value);
    }

    public static ParseQuery<Application> getQuery() {
        return ParseQuery.getQuery(Application.class);
    }


}
