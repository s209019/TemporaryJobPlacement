package it.polito.mobile.temporaryjobplacement.pcompany.activities;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import it.polito.mobile.temporaryjobplacement.R;
import it.polito.mobile.temporaryjobplacement.commons.utils.AccountManager;
import it.polito.mobile.temporaryjobplacement.model.Application;
import it.polito.mobile.temporaryjobplacement.model.Company;
import it.polito.mobile.temporaryjobplacement.model.JobOffer;
import it.polito.mobile.temporaryjobplacement.model.Student;
import it.polito.mobile.temporaryjobplacement.pcompany.fragments.OfferListFragment;
import it.polito.mobile.temporaryjobplacement.pcompany.viewmanaging.DrawerManager;
import it.polito.mobile.temporaryjobplacement.pstudent.activities.ApplicationDetailActivity;

public class OpenPositionsActivity extends ActionBarActivity implements OfferListFragment.Callbacks {

    private DrawerManager drawerManager;
    private Company companyProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_position);



        //Set the custom toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null){
            setSupportActionBar(toolbar);
        }
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        drawerManager=new DrawerManager(this,drawerLayout,toolbar,DrawerManager.SECTION1);
        drawerManager.setDrawer();
    }

    public void onItemSelected(JobOffer jobOffer) {

        Intent detailIntent = new Intent(this, CompanyDetailActivity.class);
        detailIntent.putExtra("SELECTED_OFFER", jobOffer.getObjectId());
        startActivity(detailIntent);

    }

    public void initializeProfile() throws Exception {
        if(companyProfile==null)
            companyProfile = AccountManager.getCurrentCompanyProfile();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_company_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            drawerManager.toggleDrawer();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public ParseQueryAdapter.QueryFactory<JobOffer> getQueryFactory() throws Exception {
        ParseQueryAdapter.QueryFactory<JobOffer> query;
        final Exception[] error={null};

        query= new ParseQueryAdapter.QueryFactory<JobOffer>() {

            public ParseQuery<JobOffer> create() {

                ParseQuery<JobOffer> query=null;
                try {

                    query = JobOffer.getQuery();
                    query.whereEqualTo("company", companyProfile);
                    query.orderByDescending("createdAt");
                    query.setLimit(100);
                } catch (Exception e) {
                    e.printStackTrace();error[0]=e;
                }
                return query;
            }
        };
        if(error[0]!=null)throw error[0];
        return query;
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        this.recreate();


    }


    @Override
    public void onBackPressed(){
        if(drawerManager.isDrawerOpen()){
            drawerManager.toggleDrawer();
            return;
        }
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }
}
