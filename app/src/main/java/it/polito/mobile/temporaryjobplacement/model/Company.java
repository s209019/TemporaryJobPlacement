package it.polito.mobile.temporaryjobplacement.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by Enrico on 10/05/15.
 */
@ParseClassName("CompanyProfile")
public class Company extends ParseObject {

    private boolean favourited=false;

    //String name

    public String getName() {
        return getString("name");
    }

    public void setName(String value) {
        put("name", value);
    }






    public boolean isFavourited() {
        return favourited;
    }

    public void setFavourited(boolean favourited) {
        this.favourited=favourited;
    }


    public static ParseQuery<Company> getQuery() {
        return ParseQuery.getQuery(Company.class);
    }

}
