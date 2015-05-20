package it.polito.mobile.temporaryjobplacement.model;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.view.View;

import com.parse.DeleteCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import it.polito.mobile.temporaryjobplacement.R;
import it.polito.mobile.temporaryjobplacement.commons.utils.AccountManager;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.DialogManager;
import it.polito.mobile.temporaryjobplacement.pstudent.activities.StudentProfileActivity;

/**
 * Created by Enrico on 10/05/15.
 */
@ParseClassName("StudentProfile")
public class Student extends ParseObject {

    private Bitmap photo;

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

    public ArrayList<ParseObject> getCoverLetters() {
        return (ArrayList<ParseObject>)get("coverLetters");
    }



    public ArrayList<String> getLanguageSkills(){
        String languagesText=(String)this.getString("languages");
        if(languagesText==null || languagesText.trim().equals(""))return  new ArrayList<String>();
        String[] languages=languagesText.split("\n");

        return  new ArrayList<String>(Arrays.asList(languages));
    }
    public void setLanguageSkills(String value){
        put("languages",value);
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



    public void updatePhoto(Bitmap bitImage, SaveCallback saveCallback) {
        ParseObject photo = new ParseObject("Photo");
        photo.put("name", "photoProfile");
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitImage.compress(Bitmap.CompressFormat.PNG, 60, stream);
        byte[] byteArray = stream.toByteArray();
        photo.put("photo", new ParseFile(byteArray));

        if(this.has("photoProfile"))
            this.getParseObject("photoProfile").deleteInBackground();

        this.put("photoProfile", photo);
        this.saveInBackground(saveCallback);

    }

    public void removePhoto(SaveCallback saveCallback) {
        final ParseObject photo = (ParseObject)this.get("photoProfile");
        if(photo==null)return;
        photo.deleteInBackground();
        this.remove("photoProfile");
        this.saveInBackground(saveCallback);

    }


    public Bitmap getPhoto(Context context) throws com.parse.ParseException {

        ParseObject photo = (ParseObject)this.get("photoProfile");
        if(photo==null)return null;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Photo");
        byte[]  data = query.get(photo.getObjectId()).getParseFile("photo").getData();
        if(data==null)return null;
        Bitmap bitmap = BitmapFactory.decodeByteArray( data, 0, data.length);
        return bitmap;
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



    public List<Education> getEducations() throws com.parse.ParseException {
        ParseRelation<Education> relation = getRelation("educations");
        List<Education> educations= relation.getQuery().find();
        Collections.sort(educations);
        return educations;
    }

    public void deleteEducation(Education education,SaveCallback saveCallback)   {
        ParseRelation<Education> relation = getRelation("educations");
        relation.remove(education);
        this.saveInBackground(saveCallback);
        education.deleteEventually();
    }


    public void addEducation(final Education education, final SaveCallback saveCallback) {
        education.saveInBackground(new SaveCallback() {
            @Override
            public void done(com.parse.ParseException e) {
                ParseRelation<Education> relation = getRelation("educations");
                relation.add(education);
                Student.this.saveInBackground(saveCallback);
            }
        });

    }


    public void updateEducation(Education education, SaveCallback saveCallback) {
        education.saveInBackground(saveCallback);
    }

    public void clearProfile(final ParseUser user, final SaveCallback saveCallback) {

       this.deleteInBackground(new DeleteCallback() {
           @Override
           public void done(com.parse.ParseException e) {
               Student student = new Student();
               student.put("user", user);
               student.setFirstName("");
               student.setLastName("");
               student.saveInBackground(saveCallback);
           }
       });

    }


    public void updatePublicFlag(boolean b,SaveCallback saveCallback) {
        put("public", b);
        this.saveInBackground(saveCallback);
    }



    public boolean isPublic(){
        return getBoolean("public");
    }



    public Application getApplication(String appId) throws com.parse.ParseException {
        ParseQuery<Application> query=Application.getQuery();
        query.include("jobOffer").include("jobOffer.company");
        return query.get(appId);


    }
}
