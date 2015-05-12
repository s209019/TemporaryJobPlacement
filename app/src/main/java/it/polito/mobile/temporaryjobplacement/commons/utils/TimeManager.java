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

        Date now = new Date();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        final long diff = now.getTime() - date.getTime();

        if (diff < MINUTE_MILLIS) {
            return "Just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "A minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "An hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "Yesterday";
        } else if (diff < 25 * DAY_MILLIS ){
            return diff / DAY_MILLIS + " days ago";
        } else {
            return df.format(date);

        }
    }

}