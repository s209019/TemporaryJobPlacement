package it.polito.mobile.temporaryjobplacement.commons.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

public class ExternalIntents {



    public static void sendMail(Activity activity,String mailAddress ){

        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        String aEmailList[] = { mailAddress };
        //String aEmailCCList[] = { "user3@fakehost.com","user4@fakehost.com"};
        //String aEmailBCCList[] = { "user5@fakehost.com" };
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, aEmailList);
        //emailIntent.putExtra(android.content.Intent.EXTRA_CC, aEmailCCList);
        //emailIntent.putExtra(android.content.Intent.EXTRA_BCC, aEmailBCCList);
        //emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "My subject");
        emailIntent.setType("plain/text");
        //emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "My message body.");
        try{
            activity.startActivity(emailIntent);}

        catch(Exception e){
            Toast.makeText(activity, "No Mail Client", Toast.LENGTH_LONG).show();
        }
    }




    public static void call(Activity activity,String number){
        try {
            Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
            activity.startActivity(i);
        }catch (Exception e){
            Toast.makeText(activity, "Wrong phone number", Toast.LENGTH_LONG).show();
        }

    }




    public static void openGoogleMaps(Activity activity, String address){
        //address=address.replace(" ")
        try {
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.co.in/maps?q=" + address));
            activity.startActivity(intent);
        }catch (Exception e){
            Toast.makeText(activity, "Wrong position", Toast.LENGTH_LONG).show();
        }
    }



    public static void share(Activity activity, String shareContent){

    }
}
