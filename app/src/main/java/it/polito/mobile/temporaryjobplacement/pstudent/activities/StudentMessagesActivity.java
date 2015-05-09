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

import java.util.ArrayList;
import java.util.List;

import it.polito.mobile.temporaryjobplacement.R;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.DialogManager;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.googlelibtabview.SlidingTabLayout;
import it.polito.mobile.temporaryjobplacement.model.Message;
import it.polito.mobile.temporaryjobplacement.pstudent.fragments.CompanyListFragment;
import it.polito.mobile.temporaryjobplacement.pstudent.fragments.MessageListFragment;
import it.polito.mobile.temporaryjobplacement.pstudent.fragments.OfferListFragment;
import it.polito.mobile.temporaryjobplacement.pstudent.model.Company;
import it.polito.mobile.temporaryjobplacement.pstudent.model.Offer;
import it.polito.mobile.temporaryjobplacement.pstudent.viewmanaging.DrawerManager;
import it.polito.mobile.temporaryjobplacement.pstudent.fragments.SearchByCompanyFragment;
import it.polito.mobile.temporaryjobplacement.pstudent.fragments.SearchByOfferFragment;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.TabsPagerAdapter;


public class StudentMessagesActivity extends ActionBarActivity implements MessageListFragment.Callbacks {
    DrawerManager drawerManager;


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
        fragmentList.add(MessageListFragment.newInstance(MessageListFragment.INBOX));
        fragmentList.add(MessageListFragment.newInstance(MessageListFragment.SENT));
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
    public void onItemSelected(Message message, int typeOfMessage) {
        DialogManager.toastMessage("pressed",this);
    }

    @Override
    public List<Message> getMessagesToDisplay(int typeOfMessage) {

        if(typeOfMessage==MessageListFragment.INBOX){

        }else if(typeOfMessage==MessageListFragment.SENT){

        }
        List<Message> messages=new ArrayList<Message>();
        messages.add(new Message("Nicolò Foderà","Telecom Italia1","","","Richiesta info1","JNJNKNKJNKJNJK",1));
        messages.add(new Message("Nicolò Foderà","Telecom Italia1","","","Richiesta info2","JNJNKNKJNKJNJK",1));
        messages.add(new Message("Nicolò Foderà","Telecom Italia1","","","Richiesta info3","JNJNKNKJNKJNJK",1));
        messages.add(new Message("Nicolò Foderà","Telecom Italia1","","","Richiesta info4","JNJNKNKJNKJNJK",1));
        messages.add(new Message("Nicolò Foderà","Telecom Italia1","","","Richiesta info5","JNJNKNKJNKJNJK",1));
        messages.add(new Message("Nicolò Foderà","Telecom Italia1","","","Richiesta info6","JNJNKNKJNKJNJK",1));


        return messages;
    }

    @Override
    public void onDeleteButtonMessagePressed(Message message) {
        DialogManager.toastMessage("delete",this);

    }
}
