package it.polito.mobile.temporaryjobplacement.pstudent.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import it.polito.mobile.temporaryjobplacement.R;
import it.polito.mobile.temporaryjobplacement.commons.utils.AccountManager;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.googlelibtabview.SlidingTabLayout;
import it.polito.mobile.temporaryjobplacement.model.Student;
import it.polito.mobile.temporaryjobplacement.pstudent.viewmanaging.DrawerManager;
import it.polito.mobile.temporaryjobplacement.pstudent.fragments.SearchCompaniesFragment;
import it.polito.mobile.temporaryjobplacement.commonfragments.SearchOffersFragment;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.TabsPagerAdapter;


public class StudentMainActivity extends ActionBarActivity implements SearchOffersFragment.OnFragmentInteractionListener,SearchCompaniesFragment.OnFragmentInteractionListener {
    DrawerManager drawerManager;
    private ProgressDialog progressDialog;

    Student profile;
    String user="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);










        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        //progressDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);




        //Set the custom toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null){
            setSupportActionBar(toolbar);
        }
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        drawerManager=new DrawerManager(this,drawerLayout,toolbar,DrawerManager.SECTION0);
        drawerManager.setDrawer();
        drawerManager.toggleDrawer();



        //manage first time uncompleted profile
        try {
            user=AccountManager.getCurrentUser().getUsername();
        } catch (Exception e) {
            e.printStackTrace();
        }
        SharedPreferences prefs = getSharedPreferences("INITIAL_DIALOG", MODE_PRIVATE);
        boolean doNotShow= prefs.getBoolean(user+"noThanks", false);
        if(!doNotShow) {
            new AsyncTask<Object, Object, Object>() {
                @Override
                protected Object doInBackground(Object... params) {
                    try {
                        profile = AccountManager.getCurrentStudentProfile();
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

                        if (profile.getLastName().equals("")) {
                            AlertDialog aDialog = null;
                            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(StudentMainActivity.this);

                            alertBuilder.setTitle("PROFILE NOT YET CREATED");
                            alertBuilder.setMessage("In order to fully exploit this application, we suggest you to create a profile");
                            alertBuilder.setCancelable(false);
                            alertBuilder.setPositiveButton("CREATE PROFILE", new android.content.DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(android.content.DialogInterface dialog, int which) {
                                    drawerManager.goToSectionProfile();
                                }
                            });
                            alertBuilder.setNegativeButton("NO, THANKS", new android.content.DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(android.content.DialogInterface dialog, int which) {
                                    SharedPreferences.Editor editor = getSharedPreferences("INITIAL_DIALOG", MODE_PRIVATE).edit();
                                    editor.putBoolean(user+"noThanks", true);
                                    editor.commit();
                                }
                            });

                            aDialog = alertBuilder.create();
                            aDialog.show();

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.execute();
        }




        //set tabViews

        ArrayList<Fragment> fragmentList=new ArrayList<Fragment>();
        fragmentList.add(SearchOffersFragment.newInstance());
        fragmentList.add(SearchCompaniesFragment.newInstance());
        String titles[] ={"JOB OFFERS","COMPANIES"};
        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        TabsPagerAdapter tabsAdapter =  new TabsPagerAdapter(getSupportFragmentManager(),titles,fragmentList);

        // Assigning ViewPager View and setting the adapter
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
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
    protected void onResume() {
        super.onResume();
        if(progressDialog.isShowing()) progressDialog.dismiss();
        //EXIT
        if(getIntent().getExtras()!=null && getIntent().getExtras().get("EXIT")!=null){
            this.finish();
        }

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
    public void startSearchOffersActivity(Intent i) {
        //start activity and pass it searching params
       // DialogManager.toastMessage("starting searching:\n"+params,this);
        progressDialog.show();
        startActivity(i);


    }


    @Override
    public void startSearchCompaniesActivity(Intent i) {
        //start activity and pass it searching params
        //DialogManager.toastMessage("starting searching:\n"+params,this);
        progressDialog.show();
        startActivity(i);

    }


    @Override
    public void onBackPressed(){
        if(drawerManager.isDrawerOpen()){
            drawerManager.toggleDrawer();
            return;
        }
        super.onBackPressed();
    }
}
