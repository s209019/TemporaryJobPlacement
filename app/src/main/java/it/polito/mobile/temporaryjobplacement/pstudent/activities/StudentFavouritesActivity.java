package it.polito.mobile.temporaryjobplacement.pstudent.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.googlelibtabview.SlidingTabLayout;
import it.polito.mobile.temporaryjobplacement.model.Company;
import it.polito.mobile.temporaryjobplacement.model.JobOffer;
import it.polito.mobile.temporaryjobplacement.model.Student;
import it.polito.mobile.temporaryjobplacement.pstudent.fragments.CompanyListFragment;
import it.polito.mobile.temporaryjobplacement.pstudent.fragments.OfferListFragment;
import it.polito.mobile.temporaryjobplacement.pstudent.viewmanaging.DrawerManager;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.TabsPagerAdapter;


public class StudentFavouritesActivity extends ActionBarActivity implements OfferListFragment.Callbacks,CompanyListFragment.Callbacks {
    DrawerManager drawerManager;
    private Student studentProfile;
    ViewPager pager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_favourites);


        //Set the custom toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null){
            setSupportActionBar(toolbar);
        }
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        drawerManager=new DrawerManager(this,drawerLayout,toolbar,DrawerManager.SECTION2);
        drawerManager.setDrawer();


        //set tabViews
        ArrayList<Fragment> fragmentList=new ArrayList<Fragment>();
        fragmentList.add(OfferListFragment.newInstance());
        fragmentList.add(CompanyListFragment.newInstance());
        String titles[] ={"OFFERS","COMPANIES"};
        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        TabsPagerAdapter tabsAdapter =  new TabsPagerAdapter(getSupportFragmentManager(),titles,fragmentList);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        //set number of fragments beyond which the next fragment is created and the first is destroyed
        pager.setOffscreenPageLimit(fragmentList.size()-1);
        pager.setAdapter(tabsAdapter);


        // Assigning the Sliding Tab Layout View
        SlidingTabLayout tabLayout = (SlidingTabLayout) findViewById(R.id.tabLayout);
        tabLayout.setDistributeEvenly(true); // This makes the tabs Space Evenly in Available width
        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.primaryColor);
            }

            @Override
            public int getDividerColor(int position) {
                return getResources().getColor( R.color.grayTextColor);
            }

            @Override
            public int getDefaultTextColor() {
                return getResources().getColor( R.color.grayTextColor);
            }

            @Override
            public int getBackgroundColor() {
                return getResources().getColor( R.color.foregroundColor);
            }



        });
        // Setting the ViewPager For the SlidingTabsLayout
        tabLayout.setViewPager(pager);
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



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }






    @Override
    public void onItemSelected(Company company) {
        Intent detailIntent = new Intent(this, StudentDetailActivity.class);
        detailIntent.putExtra("SELECTED_COMPANY", company.getObjectId());
        startActivity(detailIntent);
    }

    @Override
    public ParseQueryAdapter.QueryFactory<Company> getCompanyQueryFactory() throws Exception {
        final Exception[] error={null};
        ParseQueryAdapter.QueryFactory<Company> query= new ParseQueryAdapter.QueryFactory<Company>() {
            public ParseQuery<Company> create() {
                ParseQuery<Company> query=null;
                try {
                    query = studentProfile.getFavouritesCompaniesRelationQuery();
                    query.orderByDescending("createdAt");
                    query.setLimit(100);
                } catch (Exception e) {
                    e.printStackTrace();
                    error[0]=e;
                }
                return query;
            }
        };
        if(error[0]!=null)throw error[0];
        return query;
    }

    
    @Override
    public void onItemSelected(JobOffer offer) {
        Intent detailIntent = new Intent(this, StudentDetailActivity.class);
        detailIntent.putExtra("SELECTED_OFFER", offer.getObjectId());
        detailIntent.putExtra("IS_FAVOURITED", true);
        startActivity(detailIntent);
    }

    @Override
    public ParseQueryAdapter.QueryFactory<JobOffer> getQueryFactory() throws Exception {

        final Exception[] error = {null};
        ParseQueryAdapter.QueryFactory<JobOffer> query=new ParseQueryAdapter.QueryFactory<JobOffer>() {
            public ParseQuery<JobOffer> create()  {
                ParseQuery<JobOffer> query=null;
                try {
                    query = studentProfile.getFavouritesOffersRelationQuery();
                    query.orderByDescending("createdAt");
                    query.include("company");
                    query.setLimit(100);
                } catch (Exception e) {
                    e.printStackTrace();
                   error[0] =e;
                }
                return query;
            }
        };

        if(error[0]!=null) throw error[0];
        return query;
    }




/*
    @Override
    public List<Offer> getOffersToDisplay() {

        return null;
    }*/

    @Override
    public List<JobOffer> getFavouritesOffers() {
        return null;
    }

    @Override
    public void updateFavourites(JobOffer favourite, boolean toBeAdded) {

    }

    @Override
    public boolean isFavouriteList(){
        return true;
    }

    @Override
    public void onDeleteButtonCompanyPressed(Company company) {
        return  ;
    }

    @Override
    public void onFavouriteButtonCompanyPressed(Company company) {
        return ;
    }

    @Override
    public void onDeleteButtonOfferPressed(JobOffer offer) {
        return  ;
    }

    @Override
    public void onFavouriteButtonOfferPressed(JobOffer offer) {
        return  ;
    }

    @Override
    public void initializeProfile() throws Exception {
        if(studentProfile==null)
            studentProfile = AccountManager.getCurrentStudentProfile();

    }


    @Override
    public List<Company> getFavouritesCompanies() {
        return null;
    }

    @Override
    public void updateFavourites(Company favourite, boolean toBeAdded) {

    }


    public void deleteAllFavourites(View v){
        DialogManager.setDialog("Delete all favourites?",this);
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
