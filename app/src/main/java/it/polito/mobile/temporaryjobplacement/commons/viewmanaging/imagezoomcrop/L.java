package it.polito.mobile.temporaryjobplacement.commons.viewmanaging.imagezoomcrop;

import android.util.Log;

/**
 * @author GT
 */
public class L {
    private static final String TAG = "ImageZoomCrop";

    public static void e(Throwable e){
        Log.e(TAG,e.getMessage(),e);
    }

    public static void e(String msg){
        Log.e(TAG,msg);
    }

}
