package it.polito.mobile.temporaryjobplacement.pstudent.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.util.ArrayList;
import java.util.List;

import it.polito.mobile.temporaryjobplacement.R;
import it.polito.mobile.temporaryjobplacement.commons.utils.AccountManager;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.DialogManager;
import it.polito.mobile.temporaryjobplacement.model.Application;
import it.polito.mobile.temporaryjobplacement.model.JobOffer;
import it.polito.mobile.temporaryjobplacement.model.Student;
import it.polito.mobile.temporaryjobplacement.pstudent.fragments.ApplicationListFragment;
import it.polito.mobile.temporaryjobplacement.pstudent.fragments.CompanyDetailFragment;
import it.polito.mobile.temporaryjobplacement.pstudent.fragments.CompanyListFragment;
import it.polito.mobile.temporaryjobplacement.pstudent.fragments.OfferDetailFragment;
import it.polito.mobile.temporaryjobplacement.pstudent.fragments.OfferListFragment;
import it.polito.mobile.temporaryjobplacement.pstudent.viewmanaging.DrawerManager;


public class StudentMyApplicationsActivity extends ActionBarActivity implements ApplicationListFragment.Callbacks {

    DrawerManager drawerManager;
    private Student studentProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_history);


        //Set the custom toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null){
            setSupportActionBar(toolbar);
        }
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        drawerManager=new DrawerManager(this,drawerLayout,toolbar,DrawerManager.SECTION4);
        drawerManager.setDrawer();

    }

    @Override
    public void onItemSelected(Application application) {

            Intent detailIntent = new Intent(this, ApplicationDetailActivity.class);
            detailIntent.putExtra("SELECTED_APP", application.getObjectId());
            startActivity(detailIntent);

    }

    @Override
    public void initializeProfile() throws Exception {
        if(studentProfile==null)
            studentProfile = AccountManager.getCurrentStudentProfile();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_student_main, menu);
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


    public ParseQueryAdapter.QueryFactory<Application> getQueryFactory() throws Exception {
        ParseQueryAdapter.QueryFactory<Application> query;
        final Exception[] error={null};

        query= new ParseQueryAdapter.QueryFactory<Application>() {

            public ParseQuery<Application> create() {

                ParseQuery<Application> query=null;
                try {

                    query = Application.getQuery();
                    query.whereEqualTo("student", studentProfile);
                    query.include("jobOffer");
                    query.include("jobOffer.company");
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
    public void onDeleteApplicationPressed(Application application) {

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