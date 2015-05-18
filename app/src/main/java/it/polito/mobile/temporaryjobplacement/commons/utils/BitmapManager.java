package it.polito.mobile.temporaryjobplacement.commons.utils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;



import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;
import android.widget.Toast;

public class BitmapManager {
	
	
	static Bitmap bmFromAsync;

	/****************************ESISTE FILE?*/
	public static boolean isFileExisting(String nome_dir,String nome_file){
		File file = new File(Environment.getExternalStorageDirectory().getPath() + "/"+nome_dir+"/"+nome_file);
		return file.exists();
	}
	
	/****************************ESISTE FILE?*/
	public static boolean isFileExisting(String path){
		File file = new File(path);
		return file.exists();
	}
	
	
	
	
	/*************************CREA CARTELLA*/
	public static boolean creaCartella(Activity activity,String nomeDir){
		File destinationFolder = new File(Environment.getExternalStorageDirectory().getPath() + "/"+nomeDir); // cartella co
		
		if (!destinationFolder.exists()){ // se non esiste la cartella
		
			// Creo una nuova cartella per inserire il file
			boolean success = false;
			success = destinationFolder.mkdir();
			return success;}
		 
		//if (destinationFolder.exists()) 
		return true;
	  
	} 
	
	
	
	
	
	
	/*********************** getbitmap from sd  */
	public static Bitmap getBitmap(Activity activity,final String string_url_image,String nomeDir,boolean ridotta) {
		String nomeImage= urlTOname(string_url_image);
		String photoPath=Environment.getExternalStorageDirectory().getPath() + "/"+nomeDir+"/"+nomeImage;
		
		//Il file è presente nella cartella
		if(isFileExisting(nomeDir, nomeImage)){
		/////Toast.makeText(activity, "C'è", Toast.LENGTH_LONG).show();
			BitmapFactory.Options options = new BitmapFactory.Options();
			if(ridotta)options.inSampleSize=2;
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;
			Bitmap bitmap = BitmapFactory.decodeFile(photoPath, options);
			return bitmap;
		} 
	/////Toast.makeText(activity, "nonC'è", Toast.LENGTH_LONG).show();
	return null;	
	}
	
	
	
	/*********************** getbitmap from sd  */
	public static Bitmap getBitmap(Activity activity,final String path,boolean ridotta) {
		
		
		//Il file è presente nella cartella
		if(isFileExisting(path)){
		/////Toast.makeText(activity, "C'è", Toast.LENGTH_LONG).show();
			BitmapFactory.Options options = new BitmapFactory.Options();
			if(ridotta)options.inSampleSize=2;
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;
			Bitmap bitmap = BitmapFactory.decodeFile(path, options);
			return bitmap;
		} 
	/////Toast.makeText(activity, "nonC'è", Toast.LENGTH_LONG).show();
	return null;	
	}






	/*************memorizzo file in sd */
	public static void memorizzaImmagine(Bitmap bm, String string_url_image,
			String dirToSave,int quality0TO100)  throws IOException {
		String nomeImage= urlTOname(string_url_image);
		String photoPath=Environment.getExternalStorageDirectory().getPath() + "/"+dirToSave+"/"+nomeImage;

		BitmapFactory.Options options=new BitmapFactory.Options();  
	    options.inPreferredConfig = Bitmap.Config.ARGB_8888;

	    
	   FileOutputStream fileOutputStream  = new FileOutputStream(photoPath);
	   BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);
	   bm.compress(CompressFormat.JPEG, quality0TO100, bos);

	   bos.flush();
	   bos.close();
  }


	
	//prendo la stringa dal file json
	public static String getJsonFile(Activity activity, String nomeFile) {
		String JsonString = "";
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(activity.getAssets().open(nomeFile), "UTF-8"));

			// do reading, usually loop until end of file reading
			String mLine = reader.readLine();
			while (mLine != null) {
				JsonString = JsonString + mLine;
				mLine = reader.readLine();
			}

			reader.close();
		} catch (IOException e) {
		}

		return JsonString;
	}
	


	
	
	
	
	
	//RICAVA PATH DA URI
	public static String getPath(Uri uri, Activity activity) {
        String[] projection = { MediaColumns.DATA };
        Cursor cursor = activity.managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }



	public static String urlTOname(String url){
		return url.replace("/", "_");
	}
	
	
}


 






