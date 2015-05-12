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
import it.polito.mobile.temporaryjobplacement.model.JobOffer;
import it.polito.mobile.temporaryjobplacement.pstudent.fragments.CompanyDetailFragment;
import it.polito.mobile.temporaryjobplacement.pstudent.fragments.CompanyListFragment;
import it.polito.mobile.temporaryjobplacement.pstudent.fragments.OfferDetailFragment;
import it.polito.mobile.temporaryjobplacement.pstudent.fragments.OfferListFragment;
import it.polito.mobile.temporaryjobplacement.pstudent.model.Company;
import it.polito.mobile.temporaryjobplacement.pstudent.model.Offer;
import it.polito.mobile.temporaryjobplacement.pstudent.viewmanaging.DrawerManager;


public class StudentMyApplicationsActivity extends ActionBarActivity implements OfferListFragment.Callbacks {

    DrawerManager drawerManager;

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


    //CALBACKS IMPLEMENTATION FOR OFFERLISTFRAGMENT
    @Override
    public List<Offer> getOffersToDisplay() {
        //get search info from intent and search offers
        ArrayList<Offer> offers=new ArrayList<Offer>();
        offers.add(new Offer("Web developer", "Full time", "TELECOM ITALIA","Turin (Italy)",13121,"sajdnkjasndksandkjasndk","3 minutes ago","Master's degree","Experienced Professional"));
        offers.add(new Offer("Mobile developer", "Part time", "TELECOM ITALIA","Turin (Italy)",13121,"sajdnkjasndksandkjasndk","15 minutes ago","Master's degree","Experienced Professional"));
        offers.add(new Offer("Web developer3", "Full time", "REPLY SPA", "Rome (Italy)", 13121, "sajdnkjasndksandkjasndk", "12/08/15", "Master's degree", "Experienced Professional"));
        offers.add(new Offer("Web developer4", "Full time", "ACCENTURE","Milan (Italy)", 13121,"sajdnkjasndksandkjasndk","12/07/15","Master's degree","Experienced Professional"));
        return offers;
    }

    @Override
    public List<JobOffer> getFavouritesOffers() {
        return null;
    }

    @Override
    public void updateFavourites(JobOffer favourite, boolean toBeAdded) {

    }

    @Override
    public void onItemSelected(JobOffer offer) {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, StudentDetailActivity.class);
            detailIntent.putExtra("SELECTED_OFFER", offer.getObjectId());
            startActivity(detailIntent);

    }

    @Override
    public boolean isFavouriteList(){
        return true;
    }

    @Override
    public void onDeleteButtonOfferPressed(JobOffer offer) {
        return ;
    }

    @Override
    public void onFavouriteButtonOfferPressed(JobOffer offer) {
        return ;
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




    public void deleteHistory(View v){
        DialogManager.setDialog("Delete history?", this);
    }

    public ParseQueryAdapter.QueryFactory<JobOffer> getQueryFactory() {

        return new ParseQueryAdapter.QueryFactory<JobOffer>() {
            public ParseQuery<JobOffer> create() {
                ParseQuery<JobOffer> query=null;
                try {
                    query = AccountManager.getCurrentStudentProfile().getJobsAppliedRelationQuery();
                    query.include("company");
                    query.orderByDescending("createdAt");
                    query.setLimit(100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return query;
            }
        };
    }
    }