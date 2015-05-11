package it.polito.mobile.temporaryjobplacement.pstudent.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Offer implements Parcelable {
    private String title;
    private String position;
    private String company;
    private long timestamp;
    private int favourited;
    private String location;
    private String description;
    private String education;
    private String careerLevel;



    private String stub;


    public Offer(String title, String position, String company, String location,long timestamp,String description, String stub,String education,String careerLevel) {
        this.title = title;
        this.position = position;
        this.company = company;
        this.timestamp = timestamp;
        this.location=location;
        this.description=description;
        this.favourited=0;
        this.education=education;
        this.careerLevel=careerLevel;



        this.stub=stub;
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
        dest.writeString(position);
        dest.writeString(company);
        dest.writeLong(timestamp);
        dest.writeString(location);
        dest.writeString(description);
        dest.writeInt(favourited);
        dest.writeString(education);
        dest.writeString(careerLevel);

        dest.writeString(stub);
    }

    /**
     * Retrieving Student data from Parcel object
     * This constructor is invoked by the method createFromParcel(Parcel source) of
     * the object CREATOR
     **/
    private Offer(Parcel in){
        this.title = in.readString();
        this.position = in.readString();
        this.company = in.readString();
        this.timestamp = in.readLong();
        this.location=in.readString();
        this.description=in.readString();
        this.favourited=in.readInt();
        this.education=in.readString();
        this.careerLevel=in.readString();

        this.stub=in.readString();
    }

    public static final Parcelable.Creator<Offer> CREATOR = new Parcelable.Creator<Offer>() {
        @Override
        public Offer createFromParcel(Parcel source) {
            return new Offer(source);
        }
        @Override
        public Offer[] newArray(int size) {
            return new Offer[size];
        }
    };





    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getDate(){
        return stub;
    }


    public boolean isFavourited() {
        return favourited==1;
    }
    public void setFavourited(boolean favourited) {
        if(favourited)this.favourited = 1;
        else this.favourited = 0;
    }

    public String getLocation() {return location;}

    public void setLocation(String location) {this.location = location;}


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getCareerLevel() {
        return careerLevel;
    }

    public void setCareerLevel(String careerLevel) {
        this.careerLevel = careerLevel;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }
}
