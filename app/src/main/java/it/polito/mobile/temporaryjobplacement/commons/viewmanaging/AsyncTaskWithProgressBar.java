package it.polito.mobile.temporaryjobplacement.commons.viewmanaging;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.view.WindowManager;

public class AsyncTaskWithProgressBar extends AsyncTask<Void, String, String>{  
	private ProgressDialog dialog; 
	protected Activity activity;
	protected final String OK_VALUE="OK";
	protected final String DONTCARE_VALUE="OK";
	private boolean blackBackground;
	
	public AsyncTaskWithProgressBar(Activity act) {
		this.activity=act;
		blackBackground=true;
	}
	public AsyncTaskWithProgressBar(Activity act,boolean blackBackground) {
		this.activity=act;
		this.blackBackground=blackBackground;
	}




    @Override
	protected void onPreExecute() { 
		 dialog = ProgressDialog.show(activity,null,"Loading",true, false);
		 if(!blackBackground)dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
	}  
	@Override
	protected String doInBackground(Void... params) { 
		String resultMessage=OK_VALUE;
		//override this method to perform background operation
		return resultMessage;
	} 
	@Override
	protected void onPostExecute(String resultMessage) {
		try{	
			if (dialog.isShowing())  dialog.dismiss();
		}catch(Exception e){ e.printStackTrace();}

        if(resultMessage!=null)
            DialogManager.toastMessage(resultMessage, activity);
		
	   //override this method to perform operation after that background operation finishes
		
 }

 }