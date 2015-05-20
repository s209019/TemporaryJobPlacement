package it.polito.mobile.temporaryjobplacement.pstudent.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

import it.polito.mobile.temporaryjobplacement.R;
import it.polito.mobile.temporaryjobplacement.TemporaryJobPlacementApp;
import it.polito.mobile.temporaryjobplacement.commons.utils.AccountManager;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.DialogManager;
import it.polito.mobile.temporaryjobplacement.model.Application;
import it.polito.mobile.temporaryjobplacement.model.Education;
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
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        // Show the Up button in the action bar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final String appId=getIntent().getExtras().getString("SELECTED_APP");

        final RelativeLayout loadingOverlay = (RelativeLayout) findViewById(R.id.loadingOverlay);
        loadingOverlay.setVisibility(View.VISIBLE);
        new AsyncTask<Object, Object, Object>() {
            @Override
            protected Object doInBackground(Object... params) {
                try {
                    profile = AccountManager.getCurrentStudentProfile();
                    application = profile.getApplication(appId);
                    setProfile(profile);
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
                    initializeView();

                } catch (Exception e) {
                    e.fillInStackTrace();
                }
            }
        }.execute();


    }

    private void initializeView() {

        ((TextView) findViewById(R.id.companyAppTextView)).setText(application.getJobOffer().getCompany().getName());
        ((TextView) findViewById(R.id.offerAppTextView)).setText(application.getJobOffer().getName());
        ((TextView) findViewById(R.id.statusAppTextView)).setText(application.getStatus());

        if (!application.getStudentNotes().trim().equals("")) {
            findViewById(R.id.notesLine).setVisibility(View.VISIBLE);
            findViewById(R.id.notesLabel).setVisibility(View.VISIBLE);
            TextView notesTextView = (TextView) findViewById(R.id.notesTextView);
            notesTextView.setVisibility(View.VISIBLE);
            notesTextView.setText(application.getFeedback());
        }
        if (!application.getFeedback().trim().equals("")) {
            findViewById(R.id.feedbackLine).setVisibility(View.VISIBLE);
            findViewById(R.id.feedbackLabel).setVisibility(View.VISIBLE);
            TextView feedTextView = (TextView) findViewById(R.id.feedbackTextView);
            feedTextView.setVisibility(View.VISIBLE);
            feedTextView.setText(application.getFeedback());
        }

        if (application.getStatus().equals("Submitted")) {
             getMenuInflater().inflate(R.menu.menu_application_detail,menu);
        }


    }

    private Menu menu;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_offer_item, menu);
        this.menu=menu;
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
            // Intent i = new Intent(this, StudentMainActivity.class);
            this.onBackPressed();
        }
        if (id == R.id.action_HOME) {
            //setResult(TemporaryJobPlacementApp.exitCode);
            //finish();
            Intent i = new Intent(this, StudentMainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
        if (id == R.id.action_delete) {
            if (getProfile() != null) {
                String title="WITHDRAW APPLICATION";
                String description="Are you sure you want to withdraw application?";
                DialogManager.setDialogWithCancelAndOk(title, description, this, "WITHDRAW", new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ParseUser user = AccountManager.getCurrentUser();
                            final ProgressDialog pd=ProgressDialog.show(ApplicationDetailActivity.this, null, "Loading", true, false);
                            application.deleteInBackground(new DeleteCallback() {
                                @Override
                                public void done(com.parse.ParseException e) {
                                    if (pd != null && pd.isShowing()) pd.dismiss();
                                    ApplicationDetailActivity.this.onBackPressed();
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                });
            }
        }
        return super.onOptionsItemSelected(item);
    }



    public synchronized Student getProfile(){
        return profile;
    }
    public synchronized void setProfile(Student s){
        profile=s;
    }


    public void learnMoreAboutOffer(View v){
        Intent detailIntent = new Intent(this, StudentDetailActivity.class);
        detailIntent.putExtra("SELECTED_OFFER", application.getJobOffer().getObjectId());
        startActivity(detailIntent);
    }


    public void seeResume(View v){
        //TODO see resume
    }


    public void seeCoverLetter(View v){
        //TODO see letter
    }





}
