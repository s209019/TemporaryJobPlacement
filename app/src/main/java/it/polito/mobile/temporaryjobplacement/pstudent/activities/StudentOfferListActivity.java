package it.polito.mobile.temporaryjobplacement.pstudent.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import it.polito.mobile.temporaryjobplacement.R;
import it.polito.mobile.temporaryjobplacement.TemporaryJobPlacementApp;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.DialogManager;
import it.polito.mobile.temporaryjobplacement.pstudent.fragments.CompanyDetailFragment;
import it.polito.mobile.temporaryjobplacement.pstudent.fragments.CompanyListFragment;
import it.polito.mobile.temporaryjobplacement.pstudent.fragments.OfferDetailFragment;
import it.polito.mobile.temporaryjobplacement.pstudent.fragments.OfferListFragment;
import it.polito.mobile.temporaryjobplacement.pstudent.model.Company;
import it.polito.mobile.temporaryjobplacement.pstudent.model.Offer;


public class StudentOfferListActivity extends ActionBarActivity implements OfferListFragment.Callbacks, OfferDetailFragment.OnFragmentInteractionListener {



    private boolean mTwoPane;

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
        }


        //-->HANDSET
    }


    //CALBACKS IMPLEMENTATION FOR OFFERLISTFRAGMENT
    @Override
    public List<Offer> getOffersToDisplay() {
        //get search info from intent and search offers
        ArrayList<Offer> offers=new ArrayList<Offer>();
        offers.add(new Offer("Web developer", "Full time", "TELECOM ITALIA","contrada birgi nivaloro 7/b",13121,"sajdnkjasndksandkjasndk","3 minutes ago","Master's degree","Experienced Professional"));
        offers.add(new Offer("Mobile developer", "Part time", "TELECOM ITALIA","Turin (Italy)",13121,"sajdnkjasndksandkjasndk","15 minutes ago","Master's degree","Experienced Professional"));
        offers.add(new Offer("Web developer3", "Full time", "REPLY SPA", "Rome (Italy)", 13121, "sajdnkjasndksandkjasndk", "12/08/15", "Master's degree", "Experienced Professional"));
        offers.add(new Offer("Web developer4", "Full time", "ACCENTURE","sssasda", 13121,"sajdnkjasndksandkjasndk","12/07/15","Master's degree","Experienced Professional"));
        return offers;
    }
    @Override
    public void onItemSelected(Offer offer) {
        //IF TABLET
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putParcelable("SELECTED_OFFER", offer);
            OfferDetailFragment fragment = new OfferDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction().replace(R.id.item_detail_container, fragment).commit();

        //IF HANDSET
        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, StudentDetailActivity.class);
            detailIntent.putExtra("SELECTED_OFFER", offer);
            startActivityForResult(detailIntent,0);
        }
    }

    @Override
    public boolean isFavouriteList(){
        return false;
    }

    @Override
    public void onDeleteButtonOfferPressed(Offer offer) {
        return;
    }

    @Override
    public void onFavouriteButtonOfferPressed(Offer offer) {
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
            setResult(TemporaryJobPlacementApp.exitCode);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }







    @Override
    public void startCompanyActivity(String companyName) {
        Intent detailIntent = new Intent(this, StudentDetailActivity.class);
        //search company whose name is companyName
        Company company=new Company("companydisplayed","Turin (Italy)",new ArrayList<String>(),"jdfsdj@bbbb.it","0113432423");
        detailIntent.putExtra("SELECTED_COMPANY", company);
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