package it.polito.mobile.temporaryjobplacement.pcompany.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.util.ArrayList;
import java.util.List;

import it.polito.mobile.temporaryjobplacement.R;
import it.polito.mobile.temporaryjobplacement.TemporaryJobPlacementApp;
import it.polito.mobile.temporaryjobplacement.commons.utils.AccountManager;
import it.polito.mobile.temporaryjobplacement.model.Application;
import it.polito.mobile.temporaryjobplacement.model.Company;
import it.polito.mobile.temporaryjobplacement.model.Education;
import it.polito.mobile.temporaryjobplacement.model.JobOffer;
import it.polito.mobile.temporaryjobplacement.model.Student;
import it.polito.mobile.temporaryjobplacement.pcompany.fragments.ApplicationListFragmentForCompany;


public class ApplicationsListActivity extends ActionBarActivity implements ApplicationListFragmentForCompany.Callbacks {


    private Company companyProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_list);




        //Set the custom toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null){
            setSupportActionBar(toolbar);
        }

        final String jobOfferName=getIntent().getStringExtra("SELECTED_OFFER_NAME");
        getSupportActionBar().setTitle("Applications for:");
        getSupportActionBar().setSubtitle(jobOfferName);




        // Show the Up button in the action bar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ApplicationListFragmentForCompany fragment = new ApplicationListFragmentForCompany();
        getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();





    }





    @Override
    public void initializeProfile() throws Exception {
        if(companyProfile==null)
            companyProfile = AccountManager.getCurrentCompanyProfile();

    }

    @Override
    public void onItemSelected(Application application) {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, ApplicationDetailActivity.class);
            detailIntent.putExtra("SELECTED_APP", application.getObjectId());
            startActivityForResult(detailIntent, 0);
    }

    @Override
    public ParseQueryAdapter.QueryFactory<Application> getQueryFactory() {

        final String jobOfferId=getIntent().getStringExtra("SELECTED_OFFER");
        return new ParseQueryAdapter.QueryFactory<Application>() {
            public ParseQuery<Application> create() {
                ParseQuery innerQuery = JobOffer.getQuery();
                innerQuery.whereEqualTo("objectId", jobOfferId);

                ParseQuery<Application> query = Application.getQuery();
                query.orderByDescending("createdAt");
                query.setLimit(100);
                query.include("jobOffer");
                query.include("student");
                query.whereMatchesQuery("jobOffer", innerQuery);
                return query;
            }
        };
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
            Intent i = new Intent(this, CompanyMainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==TemporaryJobPlacementApp.exitCode){
            setResult(TemporaryJobPlacementApp.exitCode);
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }




}