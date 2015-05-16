package it.polito.mobile.temporaryjobplacement.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.text.ParseException;
import java.util.List;

/**
 * Created by Enrico on 10/05/15.
 */
@ParseClassName("StudentProfile")
public class Student extends ParseObject {

    /***
     *
     * String name;
     * String surname;
     *
     */

    public String getName() {
        return getString("name");
    }

    public void setName(String value) {
        put("name", value);
    }

    public String getSurname() {
        return getString("surname");
    }

    public void setSurname(String value) {
        put("surname", value);
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
