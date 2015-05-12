package it.polito.mobile.temporaryjobplacement.pstudent.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

public class Company extends ParseObject implements Parcelable {

    private String title;
    private String location;
    private List<String> industries;
    private int favourited;
    private String email;
    private String phone;

    public Company(String title, String location, List<String> industries, String email,String phone) {
        this.title = title;
        this.location = location;
        this.industries = industries;
        this.favourited = 0;
        this.email=email;
        this.phone=phone;
    }



    /*PARCELABLE IMPLEMENTATION
     */
    @Override
    public int describeContents() {
        return 0;
    }
    /**
     * Storing the Student data to Parcel object
     **/
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(location);
        dest.writeInt(favourited);
        dest.writeStringList(industries);
        dest.writeString(email);
        dest.writeString(phone);
    }

    /**
     * Retrieving Student data from Parcel object
     * This constructor is invoked by the method createFromParcel(Parcel source) of
     * the object CREATOR
     **/
    private Company(Parcel in){
        this.title = in.readString();
        this.location = in.readString();
        this.favourited=in.readInt();
        this.industries=new ArrayList<String>();
        in.readStringList(this.industries);
        this.email=in.readString();
        this.phone=in.readString();
    }

    public static final Parcelable.Creator<Company> CREATOR = new Parcelable.Creator<Company>() {
        @Override
        public Company createFromParcel(Parcel source) {
            return new Company(source);
        }
        @Override
        public Company[] newArray(int size) {
            return new Company[size];
        }
    };


    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<String> getIndustries() {
        return industries;
    }

    public void setIndustries(List<String> industries) {
        this.industries = industries;
    }

    public boolean isFavourited() {
        return favourited==1;
    }
    public void setFavourited(boolean favourited) {
        if(favourited)this.favourited = 1;
        else this.favourited = 0;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }




}
