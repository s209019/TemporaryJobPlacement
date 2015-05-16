package it.polito.mobile.temporaryjobplacement.pstudent.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import it.polito.mobile.temporaryjobplacement.R;
import it.polito.mobile.temporaryjobplacement.TemporaryJobPlacementApp;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.DialogManager;
import it.polito.mobile.temporaryjobplacement.pstudent.fragments.CompanyDetailFragment;
import it.polito.mobile.temporaryjobplacement.pstudent.fragments.OfferDetailFragment;


/**
 * An activity representing a single Item detail screen. This
 * activity is only used on handset devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {}.
 * <p/>
 * This activity is mostly just a 'shell' activity containing nothing
 * more than a {@link OfferDetailFragment}.
 */
public class StudentDetailActivity extends ActionBarActivity implements OfferDetailFragment.OnFragmentInteractionListener,CompanyDetailFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_detail);

        //Set the custom toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null){
            setSupportActionBar(toolbar);
        }
        // Show the Up button in the action bar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            if( getIntent().getStringExtra("SELECTED_OFFER")!=null){
                Bundle arguments = new Bundle();
                arguments.putString("SELECTED_OFFER",  getIntent().getStringExtra("SELECTED_OFFER"));
                arguments.putBoolean("IS_FAVOURITED",  getIntent().getBooleanExtra("IS_FAVOURITED", false));
                OfferDetailFragment fragment = new OfferDetailFragment();
                fragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction().add(R.id.item_detail_container, fragment).commit();
            }else if( getIntent().getStringExtra("SELECTED_COMPANY")!=null){
                Bundle arguments = new Bundle();
                arguments.putString("SELECTED_COMPANY",  getIntent().getStringExtra("SELECTED_COMPANY"));
                arguments.putBoolean("IS_FAVOURITED",  getIntent().getBooleanExtra("IS_FAVOURITED", false));
                CompanyDetailFragment fragment = new CompanyDetailFragment();
                fragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction().add(R.id.item_detail_container, fragment).commit();
            }

        }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==TemporaryJobPlacementApp.exitCode){
            setResult(TemporaryJobPlacementApp.exitCode);
            finish();
        } else if (resultCode==TemporaryJobPlacementApp.exitCodeListOffer) {
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void startOffersActivity(String companyName) {
        //pass to StudentOfferListActivity params companyName so that it can display all company's offers
        Intent intent=new Intent(this,StudentOfferListActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    public void startCompanyActivity(String companyName) {
        Intent detailIntent = new Intent(this, StudentDetailActivity.class);
        detailIntent.putExtra("SELECTED_COMPANY", companyName);
        startActivityForResult(detailIntent, 0);
    }
}
