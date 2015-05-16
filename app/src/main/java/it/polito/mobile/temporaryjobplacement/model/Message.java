package it.polito.mobile.temporaryjobplacement.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by Enrico on 16/05/15.
 */
@ParseClassName("Message")
public class Message extends ParseObject {

    public Student getStudent(){
        return (Student)getParseObject("student");

    }

    public void setStudent(Student value){
        put("student", value);
    }

    public void setCompany(Company value){
        put("company", value);
    }

    public Company getCompany(){
        return (Company)getParseObject("company");

    }

    public String getSender(){
        return getString("sender");

    }

    public void setSender(String value){
        put("sender", value);
    }


    public String getSubject(){
        return getString("subject");

    }

    public void setSubject(String value){
        put("subject", value);
    }

    public String getMessage(){
        return getString("message");

    }

    public void setMessage(String value){
        put("message", value);
    }

    public boolean isRead(){
        return getBoolean("read");

    }

    public Message getOriginalMessage() {

        return (Message)getParseObject("originalMessage");
    }

    public void setRead(boolean value){
        put("read", value);
    }

    public static ParseQuery<Message> getQuery() {
        return ParseQuery.getQuery(Message.class);
    }


}
