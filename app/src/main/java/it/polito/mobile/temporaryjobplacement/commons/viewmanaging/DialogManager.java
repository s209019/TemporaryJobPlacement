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

                if(activity==null)return;

                DialogFragment newFragment = MyAlertDialogFragment.newInstance(null,description,"OK",null);
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

                if(activity==null)return;
                DialogFragment newFragment = MyAlertDialogFragment.newInstance(title,description, "OK",null);
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



    /*simple alert dialog with title*/
    public  static  void setDialog(String title,String description,FragmentActivity activity,String OK_BUTTON, final Runnable task,boolean useFragment){
        if(activity==null)return;
        if(useFragment) {
            DialogFragment newFragment = MyAlertDialogFragment.newInstance(title, description, OK_BUTTON, task);
            newFragment.show(activity.getSupportFragmentManager(), "dialog");
        }else{
            AlertDialog aDialog=null;
            final AlertDialog.Builder alertBuilder=new AlertDialog.Builder(activity);

            if(title!=null)alertBuilder.setTitle(title);
            alertBuilder.setMessage(description);
            alertBuilder.setCancelable(false);
            alertBuilder.setPositiveButton(OK_BUTTON,new android.content.DialogInterface.OnClickListener() {
                @Override
                public void onClick(android.content.DialogInterface dialog, int which) {
                    if(task!=null){
                        task.run();
                    }
                }});
            aDialog=alertBuilder.create();
            aDialog.show();

        }


    }


    /*simple alert dialog with title*/
    public  static  void setDialogWithCancelAndOk(String title,String description,FragmentActivity activity,String OK_BUTTON, final Runnable task ){
        if(activity==null)return;

            AlertDialog aDialog=null;
            final AlertDialog.Builder alertBuilder=new AlertDialog.Builder(activity);

            if(title!=null)alertBuilder.setTitle(title);
            alertBuilder.setMessage(description);
            alertBuilder.setCancelable(false);
            alertBuilder.setPositiveButton(OK_BUTTON, new android.content.DialogInterface.OnClickListener() {
                @Override
                public void onClick(android.content.DialogInterface dialog, int which) {
                    if (task != null) {
                        task.run();
                    }
                }
            });
        alertBuilder.setNegativeButton("CANCEL",new android.content.DialogInterface.OnClickListener() {
            @Override
            public void onClick(android.content.DialogInterface dialog, int which) {

            }});
            aDialog=alertBuilder.create();
            aDialog.show();



    }



			
			//toast message
			public static  void toastMessage(String string,Context ctx){
                if(ctx==null)return;
                Toast.makeText(ctx, string, Toast.LENGTH_LONG).show();

			}

    //toast message
    public static void toastMessage(String string, Context ctx, String position) {
        if(ctx==null)return;
        int gravity = Gravity.NO_GRAVITY;

        if (position.equals("center")) {
            gravity = Gravity.CENTER;
        }

        Toast toast = Toast.makeText(ctx, string, Toast.LENGTH_LONG);
        toast.setGravity(gravity, 0, 0);
        toast.show();

    }

    //toast message
    public static void toastMessage(String string, Context ctx, String position, boolean shortDuration) {
        if(ctx==null)return;
        int gravity = Gravity.NO_GRAVITY;

        if (position.equals("center")) {
            gravity = Gravity.CENTER;
        }
        if (shortDuration) {
            Toast toast = Toast.makeText(ctx, string, Toast.LENGTH_SHORT);
            toast.setGravity(gravity, 0, 0);
            toast.show();
        } else {
            Toast toast = Toast.makeText(ctx, string, Toast.LENGTH_LONG);
            toast.setGravity(gravity, 0, 0);
            toast.show();
        }


    }




    //toast message
			public static  void toastMessage(String string,Context ctx,int duration){
                if(ctx==null)return;
                Toast.makeText(ctx, string, duration).show();
			
			}







    public static class MyAlertDialogFragment extends DialogFragment {

        private static Runnable mTask;


        public static MyAlertDialogFragment newInstance(String title, String description,String OK_BUTTON, Runnable task) {

            MyAlertDialogFragment frag = new MyAlertDialogFragment();
            Bundle args = new Bundle();
            args.putString("title", title);
            args.putString("description", description);
            args.putString("OK_BUTTON", OK_BUTTON);
            frag.setArguments(args);
            mTask=task;
            return frag;

        }



        @Override
        public AlertDialog onCreateDialog(Bundle savedInstanceState) {
            String title = getArguments().getString("title");
            String description = getArguments().getString("description");
            String OK_BUTTON = getArguments().getString("OK_BUTTON");

            setCancelable(false);

            AlertDialog aDialog=null;
            final AlertDialog.Builder alertBuilder=new AlertDialog.Builder(getActivity());

            if(title!=null)alertBuilder.setTitle(title);
            alertBuilder.setMessage(description);
            alertBuilder.setCancelable(false);
            alertBuilder.setPositiveButton(OK_BUTTON,new android.content.DialogInterface.OnClickListener() {
                @Override
                public void onClick(android.content.DialogInterface dialog, int which) {
                    if(mTask!=null){
                        mTask.run();
                    }
                }});
            aDialog=alertBuilder.create();




            return aDialog;



        }
    }
			
			
}
