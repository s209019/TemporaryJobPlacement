package it.polito.mobile.temporaryjobplacement.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Enrico on 10/05/15.
 */
@ParseClassName("CompanyProfile")
public class Company extends ParseObject {

    //String name

    public String getName() {
        return getString("name");
    }

    public void setName(String value) {
        put("name", value);
    }

}
