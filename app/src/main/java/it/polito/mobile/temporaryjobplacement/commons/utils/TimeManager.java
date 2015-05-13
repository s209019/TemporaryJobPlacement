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
            return "Published just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "Published a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return "Published " + diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "Published an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return "Published " + diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "Published yesterday";
        } else if (diff < 24 * DAY_MILLIS ){
            return "Published " + diff / DAY_MILLIS + " days ago";
        } else {
            return "Published on "+df.format(date);

        }
    }

}
