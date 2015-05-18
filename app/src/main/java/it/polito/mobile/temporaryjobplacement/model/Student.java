package it.polito.mobile.temporaryjobplacement.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Enrico on 10/05/15.
 */
@ParseClassName("StudentProfile")
public class Student extends ParseObject {

    public String getFirstName() {
        return getString("firstName");
    }

    public void setFirstName(String value) {
        put("firstName", value);
    }

    public String getLastName() {
        return getString("lastName");
    }

    public void setLastName(String value) {
        put("lastName", value);
    }

    public String getSkills() {
        return getString("skills");
    }

    public void setSkills(String value) {
        put("skills", value);
    }

    public Date getDateOfBirth() {
        return getDate("dateOfBirth");
    }

    public void setDateOfBirth(Date value) {
        put("dateOfBirth", value);
    }


    public static ParseQuery<Student> getQuery() {
        return ParseQuery.getQuery(Student.class);
    }

    public List<JobOffer> getFavouritesOffers() throws com.parse.ParseException {
        ParseRelation<JobOffer> relation = getRelation("favouritesOffers");

        return relation.getQuery().find();
    }



    public ArrayList<String> getLanguageSkills(){

        String languagesText=(String)this.getString("languages");
        if(languagesText==null || languagesText.trim().equals(""))return  new ArrayList<String>();
        String[] languages=languagesText.split("\n");

        return  new ArrayList<String>(Arrays.asList(languages));
    }


    public void addLanguageSkill(String lang, final SaveCallback onDone) {

        String languagesText=this.getString("languages");
        if(languagesText==null)languagesText="";
        if(languagesText.trim().equals(""))languagesText=lang;
        else languagesText=languagesText+"\n"+lang;

        this.put("languages",languagesText);
        this.saveInBackground(new SaveCallback() {
            @Override
            public void done(com.parse.ParseException e) {
                onDone.done(e);
            }
        });
    }


    public void removeLanguageSkill(String lang, final SaveCallback onDone) {

        ArrayList<String> list=getLanguageSkills();
        list.remove(lang);
        String languagesText="";
        for(String s:list){
            if(languagesText.trim().equals(""))languagesText=s;
            else languagesText=languagesText+"\n"+s;
        }

        this.put("languages",languagesText);
        this.saveInBackground(new SaveCallback() {
            @Override
            public void done(com.parse.ParseException e) {
                onDone.done(e);
            }
        });
    }



    public ParseQuery<JobOffer> getFavouritesOffersRelationQuery() throws com.parse.ParseException {
        ParseRelation<JobOffer> relation = getRelation("favouritesOffers");
        return relation.getQuery();
    }


    public List<Company> getFavouritesCompanies() throws com.parse.ParseException {
        ParseRelation<Company> relation = getRelation("favouritesCompanies");

        return relation.getQuery().find();
    }

    public ParseQuery<Company> getFavouritesCompaniesRelationQuery() throws com.parse.ParseException {
        ParseRelation<Company> relation = getRelation("favouritesCompanies");
        return relation.getQuery();
    }


}
