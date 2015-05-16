package it.polito.mobile.temporaryjobplacement.commons.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Enrico on 12/05/15.
 */
public class TimeManager {

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public static String getFormattedDate(Date date) {

        return getFormattedDate(date, "Published");
    }

    public static String getFormattedDate(Date date, String customString) {

        Date now = new Date();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        final long diff = now.getTime() - date.getTime();

        if (diff < MINUTE_MILLIS) {
            return customString+" just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return customString+" a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return customString+" " + diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return customString+" an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return customString+" " + diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return customString+" yesterday";
        } else if (diff < 24 * DAY_MILLIS ){
            return customString+" " + diff / DAY_MILLIS + " days ago";
        } else {
            return customString+" on "+df.format(date);

        }
    }


    public static String getFormattedTimestamp(Date date, String customString) {

        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm");


        final long diff = now.getTime() - date.getTime();

        if (diff < MINUTE_MILLIS) {
            return customString+" just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return customString+" a minute ago";
        } else if (diff < 59 * MINUTE_MILLIS) {
            return customString+" " + diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 61 * MINUTE_MILLIS) {
            return customString+" an hour ago";
        } else {
            return customString+" on "+dateFormat.format(date)+" at "+hourFormat.format(date);

        }
    }

}
