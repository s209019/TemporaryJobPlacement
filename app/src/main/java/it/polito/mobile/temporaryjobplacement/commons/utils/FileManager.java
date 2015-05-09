package it.polito.mobile.temporaryjobplacement.commons.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nicolò on 30/04/2015.
 */
public class FileManager {

    public static ArrayList<String> readRowsFromFile(Context ctx,String pathFile) {


        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(ctx.getAssets().open(pathFile)));

            ArrayList<String> list=new ArrayList<String>();
            String line = null;
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
            br.close();
            return list;
        }catch(Exception e){
            e.printStackTrace();
            return new ArrayList<String>();
        }

    }


}



