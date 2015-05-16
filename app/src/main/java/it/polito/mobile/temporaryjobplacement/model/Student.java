package it.polito.mobile.temporaryjobplacement.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.text.ParseException;
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
