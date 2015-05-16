package it.polito.mobile.temporaryjobplacement.model;

import android.os.Parcelable;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

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

    public static ParseQuery<JobOffer> getQuery() {
        return ParseQuery.getQuery(JobOffer.class);
    }
}
