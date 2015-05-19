package it.polito.mobile.temporaryjobplacement.commons.viewmanaging.imagezoomcrop;

import android.net.Uri;

import java.io.File;

/**
 * @author GT
 */
public class Utils {

    public static Uri getImageUri(String path) {
        return Uri.fromFile(new File(path));
    }
}
