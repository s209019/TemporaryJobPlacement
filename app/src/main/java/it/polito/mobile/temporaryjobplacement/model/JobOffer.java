package it.polito.mobile.temporaryjobplacement.model;

import android.os.Parcelable;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

/**
 * Created by Enrico on 10/05/15.
 */
@ParseClassName("JobOffer")
public class JobOffer  extends ParseObject {

    private boolean favourited=false;
    private boolean applicationDone=false;

    //String name

    public String getName() {
        return getString("name");
    }

    public void setName(String value) {
        put("name", value);
    }

    public String getContract() {
        return getString("contract");
    }

    public void setContract(String value) {
        put("contract", value);
    }

    public String getCompactLocation() {

        return getString("city")+" ("+getString("country")+")";
    }

    public String getFullLocation() {

        return getString("address")+"\n"+getString("city")+" ("+getString("country")+")"+" - "+getString("zipCode");
    }

    public String getResponsibilities() {
        return getString("responsibilities");
    }
    public String getMinimumQualifications() {
        return getString("minimumQualifications");
    }
    public String getPreferredQualifications() {
        return getString("preferredQualifications");
    }

    public void setCity(String value) {
        put("city", value);
    }

    public void setZipCode(String value) {
        put("zipCode", value);
    }


    public void setCountry(String value) {
        put("country", value);
    }

    public void setAddress(String value) {
        put("address", value);
    }

    public void setResponsibilities(String value) {
        put("responsibilities", value);
    }

    public void setMinimumQualifications(String value) {
        put("minimumQualifications", value);
    }
    public void setPreferredQualifications(String value) {
        put("preferredQualifications", value);
    }



    public String getEducation() {
        return getString("education");
    }

    public void setEducation(String value) {
        put("education", value);
    }

    public String getCareerLevel() {
        return getString("careerLevel");
    }

    public void setCareerLevel(String value) {
        put("careerLevel", value);
    }

    public Company getCompany() {
        return (Company)getParseObject("company"); //TODO: Controllare
    }

    public void setCompany(Company value) {
        put("company", value);
    }

    public String getDescription() {
        return getString("description");
    }

    public void setDescription(String value) {
        put("description", value);
    }

    public String getIndustries() {
        return getString("industry");
    }

    public void setIndustries(String value) {
        put("industry", value);
    }

    public void setPublic(boolean value) {
        put("public", value);
    }



    public boolean isFavourited() {
        return favourited;
    }

    public void setFavourited(boolean favourited) {
        this.favourited=favourited;
    }

    public boolean isApplicationDone() {
        return applicationDone;
    }

    public void setApplicationDone(boolean applicationDone) {
        this.applicationDone = applicationDone;
    }


    public void updatePublicFlag(boolean b,SaveCallback saveCallback) {
        put("public", b);
        this.saveInBackground(saveCallback);
    }



    public boolean isPublic(){
        return getBoolean("public");
    }




    public static ParseQuery<JobOffer> getQuery() {
        return ParseQuery.getQuery(JobOffer.class);
    }
}
