package it.polito.mobile.temporaryjobplacement.pstudent.viewmanaging;


import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import it.polito.mobile.temporaryjobplacement.LoginActivity; ;
import it.polito.mobile.temporaryjobplacement.R;
import it.polito.mobile.temporaryjobplacement.commons.utils.AccountManager;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.DialogManager;
import it.polito.mobile.temporaryjobplacement.pstudent.activities.StudentFavouritesActivity;
import it.polito.mobile.temporaryjobplacement.pstudent.activities.StudentMyApplicationsActivity;
import it.polito.mobile.temporaryjobplacement.pstudent.activities.StudentMessagesActivity;
import it.polito.mobile.temporaryjobplacement.pstudent.activities.StudentProfileActivity;


public class DrawerManager {
    public static final int SECTION0=0;
    public static final int SECTION1=1;
    public static final int SECTION2=2;
    public static final int SECTION3=3;
    public static final int SECTION4 =4;
    public static final int SECTION5 =5;


    private ActionBarActivity activity;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private int currentSection;

    public DrawerManager(final ActionBarActivity activity, DrawerLayout drawerLayout, Toolbar toolbar, final int currentSection){
        this.activity=activity;
        this.drawerLayout=drawerLayout;
        this.toolbar=toolbar;
        this.currentSection =currentSection;
    }

    public void setDrawer(){

        final ListView drawerListView=(ListView)drawerLayout.findViewById(R.id.drawerListView);
        String[] itemTitles={"Search filter","Profile","Favourites","Messages","My applications","Logout"};
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(activity, R.layout.drawer_list_item_activated,itemTitles){
            public View getView(int pos, View v, ViewGroup p) {
               TextView view=(TextView)super.getView(pos,v,p);
                switch(pos){
                    case SECTION0:
                        view.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.ic_action_search, 0);
                        break;
                    case SECTION1:
                        view.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_person, 0);
                        break;
                    case SECTION2:
                        view.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_important, 0);
                        break;
                    case SECTION3:
                        view.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_email, 0);
                        break;
                    case SECTION4:
                        view.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_time, 0);
                        break;
                    case SECTION5:
                        view.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_backspace, 0);

            }

                return view;
            }
            };



        drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int positionClicked, long id) {
                //set item clicked position as tag of drawerListView: when the method onDrawerClosed()
                // is invoked the item clicked position is gotten
                drawerListView.setTag(positionClicked);
                toggleDrawer();//close drawer(onDrawerClosed() is then invoked)
            }
        });
        drawerListView.setAdapter(adapter);
        drawerListView.setItemChecked(currentSection, true);
        drawerListView.setTag(currentSection);



        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                activity,
                drawerLayout,
                toolbar,
                R.string.open,
                R.string.close){
            public void onDrawerClosed(View drawerView){
                super.onDrawerClosed(drawerView);
                activity.invalidateOptionsMenu();
                this.syncState();

                int clickedSection=(int)drawerView.getTag();
                startClickedSection(clickedSection,(ListView)drawerView);
            }

            public void onDrawerOpened(View drawerView){
                super.onDrawerOpened(drawerView);
                activity.invalidateOptionsMenu();
                this.syncState();
            }
        };
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        actionBarDrawerToggle.syncState();

    }




    public void startClickedSection(int selectedSection,ListView drawerView){
        switch(selectedSection) {
            case SECTION0://section 0 clicked
                if(currentSection !=SECTION0){//if you are already in selectedSection do nothing
                    //you are now in a second level activity, finish it to come back to section 0(unique first level activity)
                    activity.onBackPressed();
                }
                break;
            case SECTION1://section 1 item clicked
                if(currentSection !=SECTION1){//if you are already in selectedSection do nothing
                   //start selected section
                    Intent intent=new Intent(activity,StudentProfileActivity.class);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    activity.startActivity(intent);
                    activity.overridePendingTransition(0, 0);

                    //if currentSection is the first level activity restore selected item in drawer
                    if(currentSection ==SECTION0){ drawerView.setTag(SECTION0); drawerView.setItemChecked(SECTION0, true);}
                    else activity.finish();
                }
                break;
            case SECTION2://section 2 item clicked
                if(currentSection !=SECTION2){//if you are already in selectedSection do nothing
                    //start selected section
                    Intent intent=new Intent(activity,StudentFavouritesActivity.class);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    activity.startActivity(intent);
                    activity.overridePendingTransition(0,0);

                    //if currentSection is the first level activity restore selected item in drawer
                    if(currentSection ==SECTION0){ drawerView.setTag(SECTION0); drawerView.setItemChecked(SECTION0, true);}
                    else activity.finish();
                }
                break;
            case SECTION3://section 3 item clicked
                if(currentSection !=SECTION3){//if you are already in selectedSection do nothing
                    //start selected section
                    Intent intent=new Intent(activity,StudentMessagesActivity.class);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    activity.startActivity(intent);
                    activity.overridePendingTransition(0,0);

                    //if currentSection is the first level activity restore selected item in drawer
                    if(currentSection ==SECTION0){ drawerView.setTag(SECTION0); drawerView.setItemChecked(SECTION0, true);}
                    else activity.finish();
                }
                break;
            case SECTION4://section 4 item clicked
                if(currentSection !=SECTION4){//if you are already in selectedSection do nothing
                    //start selected section
                    Intent intent=new Intent(activity,StudentMyApplicationsActivity.class);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    activity.startActivity(intent);
                    activity.overridePendingTransition(0,0);

                    //if currentSection is the first level activity restore selected item in drawer
                    if(currentSection ==SECTION0){ drawerView.setTag(SECTION0); drawerView.setItemChecked(SECTION0, true);}
                    else activity.finish();
                }
                break;
            case SECTION5: // 5 item clicked
                AccountManager.logout();
                DialogManager.toastMessage("Logged Out", activity);
                //clear activity stack and start login activity
                Intent intent=new Intent(activity,LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                activity.startActivity(intent);
        }
    }



    public void toggleDrawer(){
        if(drawerLayout.isDrawerOpen(Gravity.START)){
            drawerLayout.closeDrawer(Gravity.START);
        }else drawerLayout.openDrawer(Gravity.START);
    }








}
