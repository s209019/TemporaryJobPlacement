package it.polito.mobile.temporaryjobplacement.commons.viewmanaging;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.widget.Toast;

public class DialogManager {

			/*simple alert dialog*/
			public static  void setDialog(String description,FragmentActivity activity){

                DialogFragment newFragment = MyAlertDialogFragment.newInstance(null,description);
                newFragment.show(activity.getSupportFragmentManager(), "dialog");

				/*AlertDialog aDialog=null;
				AlertDialog.Builder alertBuilder=new AlertDialog.Builder(activity);
				
				//alertBuilder.setTitle(string);
				alertBuilder.setMessage(string);
				alertBuilder.setCancelable(false);
				alertBuilder.setPositiveButton("OK",new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(android.content.DialogInterface dialog, int which) {}});
					
				aDialog=alertBuilder.create();
				aDialog.show();*/
			
			}
			
			
			
			
			/*simple alert dialog with title*/
			public static  void setDialog(String title,String description,FragmentActivity activity){

                DialogFragment newFragment = MyAlertDialogFragment.newInstance(title,description);
                newFragment.show(activity.getSupportFragmentManager(), "dialog");


                /*
                AlertDialog aDialog=null;
				AlertDialog.Builder alertBuilder=new AlertDialog.Builder(activity);
				
				alertBuilder.setTitle(title);
				alertBuilder.setMessage(string);
				alertBuilder.setCancelable(false);
				alertBuilder.setPositiveButton("Ok",new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(android.content.DialogInterface dialog, int which) {}});
					
				aDialog=alertBuilder.create();
				aDialog.show();*/
			
			}
			
			
			
			
			//toast message
			public static  void toastMessage(String string,Context ctx){
                Toast.makeText(ctx, string, Toast.LENGTH_LONG).show();

			}

            //toast message
            public static  void toastMessage(String string,Context ctx, String position){

                int gravity = Gravity.NO_GRAVITY;

                if(position.equals("center")) {
                    gravity = Gravity.CENTER;
                }

                Toast toast = Toast.makeText(ctx, string, Toast.LENGTH_LONG);
                toast.setGravity(gravity, 0, 0);
                toast.show();

            }




    //toast message
			public static  void toastMessage(String string,Context ctx,int duration){
				Toast.makeText(ctx, string, duration).show();
			
			}







    public static class MyAlertDialogFragment extends DialogFragment {

        public static MyAlertDialogFragment newInstance(String title,String description) {
            MyAlertDialogFragment frag = new MyAlertDialogFragment();
            Bundle args = new Bundle();
            args.putString("title", title);
            args.putString("description", description);
            frag.setArguments(args);
            return frag;
        }

        @Override
        public AlertDialog onCreateDialog(Bundle savedInstanceState) {
            String title = getArguments().getString("title");
            String description = getArguments().getString("description");

            AlertDialog aDialog=null;
            AlertDialog.Builder alertBuilder=new AlertDialog.Builder(getActivity());

            if(title!=null)alertBuilder.setTitle(title);
            alertBuilder.setMessage(description);
            alertBuilder.setCancelable(false);
            alertBuilder.setPositiveButton("OK",new android.content.DialogInterface.OnClickListener() {
                @Override
                public void onClick(android.content.DialogInterface dialog, int which) {}});

            aDialog=alertBuilder.create();

            return aDialog;



        }
    }
			
			
}
