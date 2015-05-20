package it.polito.mobile.temporaryjobplacement.pstudent.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import it.polito.mobile.temporaryjobplacement.R;
import it.polito.mobile.temporaryjobplacement.TemporaryJobPlacementApp;
import it.polito.mobile.temporaryjobplacement.commons.utils.AccountManager;
import it.polito.mobile.temporaryjobplacement.model.Application;
import it.polito.mobile.temporaryjobplacement.model.Student;

public class ApplicationDetailActivity extends ActionBarActivity {

    Student profile;
    Application application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_detail);



        //Set the custom toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null){
            setSupportActionBar(toolbar);
        }
        // Show the Up button in the action bar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        final RelativeLayout loadingOverlay = (RelativeLayout) findViewById(R.id.loadingOverlay);
        loadingOverlay.setVisibility(View.VISIBLE);
        new AsyncTask<Object, Object, Object>() {
            @Override
            protected Object doInBackground(Object... params) {
                try {
                    profile= AccountManager.getCurrentStudentProfile();
                    application=profile.getApplication( );

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
                return new Object();
            }
            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                try {
                    loadingOverlay.setVisibility(View.GONE);

                } catch (Exception e) {
                    e.fillInStackTrace();
                }
            }}.execute();



                }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_application_detail, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            backToTheList(null);
            return true;
        }if(id==R.id.action_HOME){
            //setResult(TemporaryJobPlacementApp.exitCode);
            //finish();
            Intent i = new Intent(this, StudentMainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }



    /*
        Intent detailIntent = new Intent(this, StudentDetailActivity.class);
            detailIntent.putExtra("SELECTED_OFFER", application.getJobOffer().getObjectId());
            startActivity(detailIntent);
     */





    public void backToTheList(View v){

        setResult(TemporaryJobPlacementApp.exitCodeListOffer);
        finish();

    }
}
