package it.polito.mobile.temporaryjobplacement.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

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

    public String getAddress() {
        return getString("address");
    }

    public void setAddress(String value) {
        put("address", value);
    }

    public String getCity() {
        return getString("city");
    }

    public void setCity(String value) {
        put("city", value);
    }


    public String getCountry() {
        return getString("country");
    }

    public void setCountry(String value) {
        put("country", value);
    }

    public String getZipCode() {
        return getString("zipCode");
    }

    public void setZipCode(String value) {
        put("zipCode", value);
    }


    public String getPhoneNumber() {
        return getString("phoneNumber");
    }
    public void setPhoneNumber(String phoneNumber) {
        put("phoneNumber", phoneNumber);
    }

    public String getEmail() {
        return getString("email");
    }
    public void setEmail(String email) {
        put("email", email);
    }


    public String getWebsite() {
         return getString("website");
    }
    public void setWebsite(String website) {
        put("website", website);
    }



    public String getDescription() {
         return getString("description");
    }
    public void setDescription(String description) {
        put("description", description);
    }


    public String getCompactLocation() {

        return getString("city")+" ("+getString("country")+")";
    }

    public String getFullLocation() {

        return getString("address")+"\n"+getString("city")+" ("+getString("country")+")"+" - "+getString("zipCode");
    }


    public String getIndustries() {
        return getString("industry");
    }

    public void setIndustries(String value) {
        put("industry", value);
    }

    public ArrayList<ParseObject> getDefaultMessages() {
        return (ArrayList<ParseObject>)get("defaultMessages");
    }




    public void updatePhoto(Bitmap bitImage, SaveCallback saveCallback) {
        ParseObject logo = new ParseObject("Photo");
        logo.put("name", "companyLogo");
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitImage.compress(Bitmap.CompressFormat.PNG, 60, stream);
        byte[] byteArray = stream.toByteArray();
        logo.put("photo", new ParseFile(byteArray));

        if(this.has("logo"))
            this.getParseObject("logo").deleteInBackground();

        this.put("logo", logo);
        this.saveInBackground(saveCallback);

    }

    public void removePhoto(SaveCallback saveCallback) {
        final ParseObject logo = (ParseObject)this.get("logo");
        if(logo==null)return;
        logo.deleteInBackground();
        this.remove("logo");
        this.saveInBackground(saveCallback);

    }


    public Bitmap getPhoto(Context context) throws com.parse.ParseException {

        ParseObject logo = (ParseObject)this.get("logo");
        if(logo==null)return null;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Photo");
        byte[]  data = query.get(logo.getObjectId()).getParseFile("photo").getData();
        if(data==null)return null;
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        return bitmap;
    }

    public void updatePublicFlag(boolean b,SaveCallback saveCallback) {
        put("public", b);
        this.saveInBackground(saveCallback);
    }



    public boolean isPublic(){
        return getBoolean("public");
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
