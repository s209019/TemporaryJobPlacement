package it.polito.mobile.temporaryjobplacement.pstudent.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.DialogManager;
import it.polito.mobile.temporaryjobplacement.model.JobOffer;
import it.polito.mobile.temporaryjobplacement.model.Student;
import it.polito.mobile.temporaryjobplacement.pstudent.fragments.CompanyDetailFragment;
import it.polito.mobile.temporaryjobplacement.pstudent.fragments.CompanyListFragment;
import it.polito.mobile.temporaryjobplacement.pstudent.fragments.OfferDetailFragment;
import it.polito.mobile.temporaryjobplacement.pstudent.fragments.OfferListFragment;


public class StudentOfferListActivity extends ActionBarActivity implements OfferListFragment.Callbacks, OfferDetailFragment.OnFragmentInteractionListener {

    private boolean mTwoPane;
    private Student studentProfile;
    private List<JobOffer> favourites;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_offer_list);


        //Set the custom toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null){
            setSupportActionBar(toolbar);
        }


        // Show the Up button in the action bar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //TABLET
        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and res/values-sw600dp).
            // If this view is present, then the activity should be in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, list items should be given the 'activated' state when touched.
            ((OfferListFragment)getSupportFragmentManager().findFragmentById(R.id.item_list)).setActivateOnItemClick(true);
            getSupportActionBar().hide();
        }


        //-->HANDSET
    }

    @Override
    public void initializeProfile() throws Exception {
        if(studentProfile==null)

            studentProfile = AccountManager.getCurrentStudentProfile();

    }
    @Override
    public List<JobOffer> getFavouritesOffers() throws ParseException {

                favourites =studentProfile.getFavouritesOffers();


        return favourites;
    }

    @Override
    public void updateFavourites(JobOffer favourite, boolean toBeAdded) {


            if(toBeAdded) {
                studentProfile.getRelation("favouritesOffers").add(favourite); //Remoto
                favourites.add(favourite); //Locale
            } else {
                studentProfile.getRelation("favouritesOffers").remove(favourite);
                favourites.remove(favourite); //Locale
            }

            studentProfile.saveEventually();



    }

    @Override
    public void onItemSelected(JobOffer offer) {

        Log.d("DEBUG 0", offer.isFavourited()+"");
        //IF TABLET
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString("SELECTED_OFFER", offer.getObjectId());
            arguments.putBoolean("IS_FAVOURITED", offer.isFavourited());
            OfferDetailFragment fragment = new OfferDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction().replace(R.id.item_detail_container, fragment).commit();

        //IF HANDSET
        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, StudentDetailActivity.class);
            detailIntent.putExtra("SELECTED_OFFER", offer.getObjectId());
            detailIntent.putExtra("IS_FAVOURITED", offer.isFavourited());
            startActivityForResult(detailIntent,0);
        }
    }

    @Override
    public ParseQueryAdapter.QueryFactory<JobOffer> getQueryFactory() {
        return new ParseQueryAdapter.QueryFactory<JobOffer>() {
            public ParseQuery<JobOffer> create() {
                ParseQuery<JobOffer> query = JobOffer.getQuery();
                query.include("company");
                query.orderByDescending("createdAt");
                query.setLimit(100);
                return query;
            }
        };
    }

    @Override
    public boolean isFavouriteList(){
        return false;
    }

    @Override
    public void onDeleteButtonOfferPressed(JobOffer offer) {
        return;
    }

    @Override
    public void onFavouriteButtonOfferPressed(JobOffer offer) {
        return;
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
    public void startCompanyActivity(String companyName) {
        Intent detailIntent = new Intent(this, StudentDetailActivity.class);
        detailIntent.putExtra("SELECTED_COMPANY", companyName);
        startActivityForResult(detailIntent, 0);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode== TemporaryJobPlacementApp.exitCode){
            setResult(TemporaryJobPlacementApp.exitCode);
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



}