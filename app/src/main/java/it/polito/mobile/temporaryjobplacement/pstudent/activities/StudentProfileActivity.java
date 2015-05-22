package it.polito.mobile.temporaryjobplacement.pstudent.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import it.polito.mobile.temporaryjobplacement.commons.utils.AccountManager;
import it.polito.mobile.temporaryjobplacement.commons.utils.Connectivity;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.DialogManager;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.TabsPagerAdapter;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.googlelibtabview.SlidingTabLayout;
import it.polito.mobile.temporaryjobplacement.model.Education;
import it.polito.mobile.temporaryjobplacement.model.Student;
import it.polito.mobile.temporaryjobplacement.pstudent.fragments.MessageListFragment;
import it.polito.mobile.temporaryjobplacement.pstudent.fragments.ProfileBasicInfoFragment;
import it.polito.mobile.temporaryjobplacement.pstudent.fragments.ProfileCVFragment;
import it.polito.mobile.temporaryjobplacement.pstudent.fragments.ProfileEducationFragment;
import it.polito.mobile.temporaryjobplacement.pstudent.viewmanaging.DrawerManager;
import it.polito.mobile.temporaryjobplacement.R;


public class StudentProfileActivity extends ActionBarActivity implements  ProfileBasicInfoFragment.Callbacks, ProfileCVFragment.Callbacks,ProfileEducationFragment.Callbacks  {
    DrawerManager drawerManager;
    Student studentProfile;
    Bitmap photoStudentBitmap;
    List<Education> educations;
    ViewPager pager;



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

        //DialogManager.toastMessage("activityRecreated",this);

        final RelativeLayout loadingOverlay = (RelativeLayout) findViewById(R.id.loadingOverlay);
        loadingOverlay.setVisibility(View.VISIBLE);
        new AsyncTask<Object, Object, Object>() {
            @Override
            protected Object doInBackground(Object... params) {
                Student profile;
                try {
                    profile=AccountManager.getCurrentStudentProfile();
                     educations= profile.getEducations();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
               try{
                   if(StudentProfileActivity.this!=null)
                       photoStudentBitmap =profile.getPhoto(StudentProfileActivity.this);
               }catch (Exception e){
                   e.printStackTrace();
                   //set default bitmap
               }


                setProfile(profile);
                return new Object();
            }
            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                try {

                    if (o == null) {
                        Connectivity.connectionError(StudentProfileActivity.this);
                        return;
                    }
                    loadingOverlay.setVisibility(View.GONE);

                    //set tabViews
                    ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
                    //DialogManager.toastMessage("creating fragments", StudentProfileActivity.this);
                    fragmentList.add(ProfileBasicInfoFragment.newInstance());
                    fragmentList.add(ProfileCVFragment.newInstance());
                    fragmentList.add(ProfileEducationFragment.newInstance());
                    String titles[] = {"BASIC INFO", "RESUMES / COVER LETTERS", "EDUCATION"};
                    // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
                    TabsPagerAdapter tabsAdapter = new TabsPagerAdapter(getSupportFragmentManager(), titles, fragmentList);

                    // Assigning ViewPager View and setting the adapter
                    pager = (ViewPager) findViewById(R.id.pager);
                    //set number of fragments beyond which the next fragment is created and the first is destroyed
                    pager.setOffscreenPageLimit(fragmentList.size() - 1);
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
                            return getResources().getColor(R.color.grayTextColor);
                        }

                        @Override
                        public int getDefaultTextColor() {
                            return getResources().getColor(R.color.grayTextColor);
                        }

                        @Override
                        public int getBackgroundColor() {
                            return getResources().getColor(R.color.foregroundColor);
                        }


                    });
                    // Setting the ViewPager For the SlidingTabsLayout
                    tabLayout.setViewPager(pager);




                    String s="PUBLIC PROFILE";
                    if(!getProfile().isPublic())s="PRIVATE PROFILE";
                    getSupportActionBar().setSubtitle(s);



                    final Button publishButton = (Button) findViewById(R.id.buttonPublish);
                    setPublishInfo(publishButton);
                    publishButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DialogManager.setDialogWithCancelAndOk(title, description, StudentProfileActivity.this, ok_button_text, new Runnable() {
                                @Override
                                public void run() {
                                    if(studentProfile.isPublic() ||
                                            ( studentProfile.getFirstName()!=null && !studentProfile.getFirstName().equals("") &&
                                                    studentProfile.getLastName()!=null && !studentProfile.getLastName().equals(""))){

                                        final ProgressDialog pd = ProgressDialog.show(StudentProfileActivity.this, null, "Loading", true, false);
                                        getProfile().updatePublicFlag(!getProfile().isPublic(), new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                if (pd != null && pd.isShowing()) pd.dismiss();
                                                setPublishInfo(publishButton);
                                                String s="PROFILE PUBLISHED";
                                                if(!getProfile().isPublic())s="PROFILE UNPUBLISHED";
                                                DialogManager.toastMessage(s, StudentProfileActivity.this);
                                                s="PUBLIC";
                                                if(!getProfile().isPublic())s="PRIVATE";
                                                getSupportActionBar().setSubtitle(s);
                                            }
                                        });

                                    }else {

                                        DialogManager.setDialog("Profile not completed", "In order to set your profile as public, you first have to set your full name", StudentProfileActivity.this);


                                    }









                                }
                            }) ;

                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.execute();

    }

    String ok_button_text;
    String title,description;
    public void setPublishInfo(Button publishButton) {
        if(!getProfile().isPublic()){
            publishButton.setText("Make your profile public");
            ok_button_text="SET PUBLIC";
            title="Publish your profile";
            description="Do you want to set your profile as public?\n\nSetting your profile as public, companies will be able to access to the information contained in your profile.\n";
        }
        else {
            publishButton.setText("Make your profile private");
            ok_button_text="SET PRIVATE";
            title="Unpublish your profile";
            description="Do you want to set your profile as private?\n\nSetting your profile as private, only companies to which you have sent an application will be able to access your information.\nAll other companies will no longer be able to access to the information contained in your profile!\n";
        }
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_favourite_students, menu);
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
    public synchronized Student getProfile(){
        return studentProfile;
    }

    @Override
    public synchronized List<Education> getEducations() {
        return educations;
    }

    public synchronized void setProfile(Student s){
         studentProfile=s;
    }

    @Override
    public Bitmap getPhotoStudentBitmap() {
        return photoStudentBitmap;
    }



}
