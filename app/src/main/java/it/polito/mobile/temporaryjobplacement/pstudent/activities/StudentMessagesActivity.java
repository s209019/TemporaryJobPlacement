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

import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.util.ArrayList;

import it.polito.mobile.temporaryjobplacement.R;
import it.polito.mobile.temporaryjobplacement.commons.utils.AccountManager;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.DialogManager;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.googlelibtabview.SlidingTabLayout;
import it.polito.mobile.temporaryjobplacement.model.Message;
import it.polito.mobile.temporaryjobplacement.model.Student;
import it.polito.mobile.temporaryjobplacement.pstudent.fragments.MessageListFragment;
import it.polito.mobile.temporaryjobplacement.pstudent.viewmanaging.DrawerManager;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.TabsPagerAdapter;


public class StudentMessagesActivity extends ActionBarActivity implements MessageListFragment.Callbacks {

    DrawerManager drawerManager;
    private Student studentProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_messages);


        //Set the custom toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null){
            setSupportActionBar(toolbar);
        }
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        drawerManager=new DrawerManager(this,drawerLayout,toolbar,DrawerManager.SECTION3);
        drawerManager.setDrawer();


        //set tabViews
        ArrayList<Fragment> fragmentList=new ArrayList<Fragment>();
        fragmentList.add(MessageListFragment.newInstance(true));
        fragmentList.add(MessageListFragment.newInstance(false));
        String titles[] ={"INBOX","SENT"};
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
    public void initializeProfile() {
        try {
            studentProfile = AccountManager.getCurrentStudentProfile();
        } catch (Exception e) {
            e.printStackTrace();
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


    public ParseQueryAdapter.QueryFactory<Message> getQueryFactory(boolean inbox) {

        if(inbox) {



            return new ParseQueryAdapter.QueryFactory<Message>() {

                public ParseQuery<Message> create() {

                    ParseQuery<Message> query=null;
                    try {

                        query = Message.getQuery();
                        query.whereEqualTo("student", studentProfile);
                        query.whereEqualTo("sender", "company");
                        query.include("company");
                        query.orderByDescending("createdAt");
                        query.setLimit(100);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return query;
                }
            };



        } else {

            return new ParseQueryAdapter.QueryFactory<Message>() {

                public ParseQuery<Message> create() {

                    ParseQuery<Message> query=null;
                    try {

                        query = Message.getQuery();
                        query.whereEqualTo("student", studentProfile);
                        query.whereEqualTo("sender", "student");
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


    @Override
    public void onItemSelected(Message message, boolean inbox) {
        Intent i = new Intent(this, ShowMessageActivity.class);
        i.putExtra("SELECTED_MESSAGE", message.getObjectId());
        startActivityForResult(i, 0);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }


}
