package it.polito.mobile.temporaryjobplacement.pstudent.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import it.polito.mobile.temporaryjobplacement.R;
import it.polito.mobile.temporaryjobplacement.TemporaryJobPlacementApp;
import it.polito.mobile.temporaryjobplacement.commons.utils.AccountManager;
import it.polito.mobile.temporaryjobplacement.model.Application;
import it.polito.mobile.temporaryjobplacement.model.JobOffer;
import it.polito.mobile.temporaryjobplacement.model.Student;

public class StudentApplyActivity extends ActionBarActivity {

    private JobOffer offer;
    private Student myProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_apply);

        //Set the custom toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null){
            setSupportActionBar(toolbar);
        }
        // Show the Up button in the action bar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        final String jobOfferId=getIntent().getStringExtra("SELECTED_OFFER");
        final JobOffer[] offer = {null};
        final Student[] myProfile = {null};
        final RelativeLayout loadingOverlay =(RelativeLayout) findViewById(R.id.loadingOverlay);
        loadingOverlay.setVisibility(View.VISIBLE);
        new AsyncTask<Object, Object, Object>(){
            @Override
            protected Object doInBackground(Object... params) {
                try {
                    offer[0] = JobOffer.getQuery().include("company").get(jobOfferId);
                    myProfile[0] = AccountManager.getCurrentStudentProfile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                loadingOverlay.setVisibility(View.GONE);
                initializeView(offer[0], myProfile[0]);
            }
        }.execute();


    }

    private void initializeView(JobOffer offer, Student myProfile) {

        this.offer=offer;
        this.myProfile=myProfile;

        //Position stands for job name
        TextView positionTextView=(TextView) findViewById(R.id.jobOfferNameTextView);
        positionTextView.setText(offer.getName());

        TextView companyTextView=(TextView) findViewById(R.id.companyTextView);
        companyTextView.setText(offer.getCompany().getName());

        TextView contractTextView=(TextView) findViewById(R.id.contractTextView);
        contractTextView.setText(offer.getContract());

        TextView educationTextView=(TextView) findViewById(R.id.educationTextView);
        educationTextView.setText(offer.getEducation());

        TextView careerLevelTextView=(TextView) findViewById(R.id.careerLevelTextView);
        careerLevelTextView.setText(offer.getCareerLevel());

        TextView industriesTextView =(TextView) findViewById(R.id.industriesTextView);
        industriesTextView.setText(offer.getIndustries());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_offer_item, menu);
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
            this.onBackPressed();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode== TemporaryJobPlacementApp.exitCode){
            setResult(TemporaryJobPlacementApp.exitCode);
            finish();
        } else if (resultCode== TemporaryJobPlacementApp.exitCodeListOffer) {
            setResult(TemporaryJobPlacementApp.exitCodeListOffer);
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void apply(View v) {
        try {
            Application application = new Application();
            application.setStudent(myProfile);
            application.setJobOffer(offer);
            application.setStatus("Submitted");

            EditText studentNotesEditText = (EditText)findViewById(R.id.studentNotesEditText);

            application.setStudentNotes(studentNotesEditText.getText().toString());
            application.saveInBackground();

            Intent intent = new Intent(this, ApplicationSentActivity.class);
            startActivityForResult(intent, 0);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
