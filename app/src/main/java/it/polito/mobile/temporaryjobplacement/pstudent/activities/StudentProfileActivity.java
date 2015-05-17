package it.polito.mobile.temporaryjobplacement.pstudent.activities;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import it.polito.mobile.temporaryjobplacement.commons.utils.AccountManager;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.TabsPagerAdapter;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.googlelibtabview.SlidingTabLayout;
import it.polito.mobile.temporaryjobplacement.model.Student;
import it.polito.mobile.temporaryjobplacement.pstudent.fragments.ProfileBasicInfoFragment;
import it.polito.mobile.temporaryjobplacement.pstudent.fragments.ProfileCVFragment;
import it.polito.mobile.temporaryjobplacement.pstudent.fragments.ProfileEducationFragment;
import it.polito.mobile.temporaryjobplacement.pstudent.viewmanaging.DrawerManager;
import it.polito.mobile.temporaryjobplacement.R;


public class StudentProfileActivity extends ActionBarActivity implements  ProfileBasicInfoFragment.Callbacks  {
DrawerManager drawerManager;
    Student studentProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);




        //Set the custom toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null){
            setSupportActionBar(toolbar);
        }
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        drawerManager=new DrawerManager(this,drawerLayout,toolbar,DrawerManager.SECTION1);
        drawerManager.setDrawer();

        //set tabViews
        ArrayList<Fragment> fragmentList=new ArrayList<Fragment>();
        fragmentList.add(ProfileBasicInfoFragment.newInstance());
        fragmentList.add(ProfileCVFragment.newInstance());
        fragmentList.add(ProfileEducationFragment.newInstance());
        String titles[] ={"BASIC INFO","RESUMES / COVER LETTERS", "EDUCATION"};
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_student_profile, menu);
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
    public void onBackPressed(){
        if(drawerManager.isDrawerOpen()){
            drawerManager.toggleDrawer();
            return;
        }
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }

    @Override
    public synchronized Student getProfile() throws Exception {
        if(studentProfile==null) studentProfile= AccountManager.getCurrentStudentProfile();
        return studentProfile;
    }
}
