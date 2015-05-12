package it.polito.mobile.temporaryjobplacement.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Message implements Parcelable{
    private String source;
    private String destination;
    private String userS;
    private String userD;
    private String object;
    private String  body;
    private long timestamp;
    private int read;


    public Message(String source, String destination, String userS, String userD, String object, String body, long timestamp) {
        this.source = source;
        this.destination = destination;
        this.userS = userS;
        this.userD = userD;
        this.object = object;
        this.body = body;
        this.timestamp = timestamp;
        read=0;
    }



    @Override
    public int describeContents() {
        return 0;
    }
    /**
     * Storing the Student data to Parcel object
     **/
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(source);
        dest.writeString(destination);
        dest.writeString(userS);
        dest.writeString(userD);
        dest.writeString(object);
        dest.writeString(body);
        dest.writeLong(timestamp);
        dest.writeInt(read);
    }

    /**
     * Retrieving Student data from Parcel object
     * This constructor is invoked by the method createFromParcel(Parcel source) of
     * the object CREATOR
     **/
    private Message(Parcel in){
        this.source = in.readString();
        this.destination = in.readString();
        this.userS=in.readString();
        this.userD=in.readString();
        this.object=in.readString();
        this.body=in.readString();
        this.timestamp=in.readLong();
        this.read=in.readInt();
    }

    public static final Parcelable.Creator<Message> CREATOR = new Parcelable.Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel source) {
            return new Message(source);
        }
        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };


    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getUserS() {
        return userS;
    }

    public void setUserS(String userS) {
        this.userS = userS;
    }

    public String getUserD() {
        return userD;
    }

    public void setUserD(String userD) {
        this.userD = userD;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }




    public String getInterlocutor(){
        //ritorna nome che non coincide con lo user corrent
        return destination;
    }

    public boolean isRead() {
        return read==1;
    }

    public void seatRead() {
        this.read = 1;
    }

    public boolean suck2() {
        return read==1;
    }


}
