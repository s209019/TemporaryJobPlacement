package it.polito.mobile.temporaryjobplacement.pstudent.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.ipaulpro.afilechooser.utils.FileUtils;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import it.polito.mobile.temporaryjobplacement.commons.utils.AccountManager;
import it.polito.mobile.temporaryjobplacement.commons.utils.BitmapManager;
import it.polito.mobile.temporaryjobplacement.commons.utils.Connectivity;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.DialogManager;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.TabsPagerAdapter;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.googlelibtabview.SlidingTabLayout;
import it.polito.mobile.temporaryjobplacement.model.Student;
import it.polito.mobile.temporaryjobplacement.pstudent.fragments.MessageListFragment;
import it.polito.mobile.temporaryjobplacement.pstudent.fragments.ProfileBasicInfoFragment;
import it.polito.mobile.temporaryjobplacement.pstudent.fragments.ProfileCVFragment;
import it.polito.mobile.temporaryjobplacement.pstudent.fragments.ProfileEducationFragment;
import it.polito.mobile.temporaryjobplacement.pstudent.viewmanaging.DrawerManager;
import it.polito.mobile.temporaryjobplacement.R;


public class StudentProfileActivity extends ActionBarActivity implements  ProfileBasicInfoFragment.Callbacks, ProfileCVFragment.Callbacks  {

    DrawerManager drawerManager;
    Student studentProfile;
    ViewPager pager;
    private int numberOfResumes;
    Bitmap profilePicture;

    private int activityState;
    private static final int CREATED_FROM_SCRATCH = 0; // activity ricreata da zero
    private static final int DESTROYED_WHILE_WAITING_RESULT = 1; // activity distrutta dal sistema mentre aspettava l'onActivityResult
    private static final int DESTROYED_NORMALLY = 2; // activity distrutta dal sistema mentre NON aspettava l'onActivityResult

    public static final int REQUEST_CHOOSER_ID = 1234;
    public static final int REQUEST_CAMERA=2,SELECT_FILE=3;



    public int getNumberOfResumes() {
        return numberOfResumes;
    }

    public void setNumberOfResumes(int numberOfResumes) {
        this.numberOfResumes = numberOfResumes;
    }

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

            final RelativeLayout loadingOverlay = (RelativeLayout) findViewById(R.id.loadingOverlay);
            loadingOverlay.setVisibility(View.VISIBLE);

        if(savedInstanceState!=null){
            activityState = DESTROYED_NORMALLY; //activity distrutta dal sistema mentre NON aspettava l'onActivityResult (Diventa 1 se viene chiamato l'onActivityResult, altrimenti resta 2)

        } else {

            activityState = CREATED_FROM_SCRATCH; //Activity ricreata da zero

            new AsyncTask<Object, Object, Object>() {
                @Override
                protected Object doInBackground(Object... params) {

                    try {
                        StudentProfileActivity.this.setProfile(AccountManager.getCurrentStudentProfile());
                        if(studentProfile.has("photoProfile") && studentProfile.getParseObject("photoProfile").has("name")) {
                            profilePicture = studentProfile.getPhoto(StudentProfileActivity.this);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                    return new Object();
                }
                @Override
                protected void onPostExecute(Object o) {
                    super.onPostExecute(o);

                    if (o == null) {
                        Connectivity.connectionError(StudentProfileActivity.this);
                        return;
                    }
                    loadingOverlay.setVisibility(View.GONE);

                    //set tabViews
                    ArrayList<Fragment> fragmentList=new ArrayList<Fragment>();
                    //DialogManager.toastMessage("creating fragments", StudentProfileActivity.this);
                    fragmentList.add(ProfileBasicInfoFragment.newInstance());
                    fragmentList.add(ProfileCVFragment.newInstance());
                    fragmentList.add(ProfileEducationFragment.newInstance());
                    String titles[] ={"BASIC INFO","RESUMES / COVER LETTERS", "EDUCATION"};
                    // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
                    TabsPagerAdapter tabsAdapter =  new TabsPagerAdapter(getSupportFragmentManager(),titles,fragmentList);

                    // Assigning ViewPager View and setting the adapter
                    pager  = (ViewPager) findViewById(R.id.pager);
                    //set number of fragments beyond which the next fragment is created and the first is destroyed
                    pager.setOffscreenPageLimit(fragmentList.size() - 1);

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
                    pager.setAdapter(tabsAdapter);
                    tabLayout.setViewPager(pager);

                }
            }.execute();

        }



    }

    @Override
    protected void onResume() {
        super.onResume();
        if(activityState==DESTROYED_NORMALLY) {
            finish();
            Intent i = new Intent(this, StudentProfileActivity.class);
            startActivity(i);
        }
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
    public synchronized Student getProfile(){
        return studentProfile;
    }
    public synchronized void setProfile(Student studentProfile){
        this.studentProfile=studentProfile;
    }


    @Override
    public Bitmap getPhotoStudentBitmap() {
        return profilePicture;
    }


    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        if(activityState==DESTROYED_NORMALLY) {
            // Se l'activity era stata distrutta dal sistema ed entra qua, allora l'activity è stata distrutta dal sistema
            // mentre aspettava l'onActivityResult e bisogna ricaricare il profilo.


            activityState=DESTROYED_WHILE_WAITING_RESULT;
            new AsyncTask<Object, Object, Object>() {
                @Override
                protected Object doInBackground(Object... params) {
                    Object o=null;
                    try {
                        studentProfile= AccountManager.getCurrentStudentProfile();
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                    return new Object();
                }
                @Override
                protected void onPostExecute(Object o) {
                    super.onPostExecute(o);

                    if (o == null) {
                        Connectivity.connectionError(StudentProfileActivity.this);
                        return;
                        //TODO: Che bisogna fare qua???
                    }

                    numberOfResumes = 0;

                    try {


                        if (studentProfile.has("cv1") && ((ParseObject) studentProfile.get("cv1")).get("name") != null) {
                            numberOfResumes++;
                        }
                        if (studentProfile.has("cv2") && ((ParseObject) studentProfile.get("cv2")).get("name") != null) {
                            numberOfResumes++;
                        }
                        if (studentProfile.has("cv3") && ((ParseObject) studentProfile.get("cv3")).get("name") != null) {
                            numberOfResumes++;
                        }
                        if (studentProfile.has("cv4") && ((ParseObject) studentProfile.get("cv4")).get("name") != null) {
                            numberOfResumes++;
                        }
                        if (studentProfile.has("cv5") && ((ParseObject) studentProfile.get("cv5")).get("name") != null) {
                            numberOfResumes++;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    handleActivityResults(requestCode, resultCode, data);
                }
            }.execute();


        } else {

            //L'activity non è stata ricreata, quindi numberOfResumes e profile sono già ok
            handleActivityResults(requestCode, resultCode, data);
        }
    }

    private void handleActivityResults(int requestCode, int resultCode, Intent data) {

        if(resultCode!=Activity.RESULT_OK) {
            if(activityState != CREATED_FROM_SCRATCH) {
                finish();
                Intent i = new Intent(this, StudentProfileActivity.class);
                startActivity(i);
            }
        } else {
            switch (requestCode) {
                case REQUEST_CHOOSER_ID:

                    final Uri uri = data.getData();

                    // Get the File path from the Uri
                    String path = FileUtils.getPath(this, uri);

                    // Alternatively, use FileUtils.getFile(Context, Uri)
                    if (path != null && FileUtils.isLocal(path)) {
                        File file = new File(path);

                        if (file.toString().contains(".doc") ||
                                file.toString().contains(".docx") ||
                                file.toString().contains(".pdf") ||
                                file.toString().contains(".rtf") ||
                                file.toString().contains(".txt")) {

                            try {
                                final String resumeStringId = "cv" + (numberOfResumes + 1);
                                ParseObject curriculum = new ParseObject("Curriculum");
                                curriculum.put("name", "CV ITA" + numberOfResumes);
                                curriculum.put("curriculum", new ParseFile(org.apache.commons.io.FileUtils.readFileToByteArray(file)));

                                getProfile().put(resumeStringId, curriculum);
                                if (activityState == CREATED_FROM_SCRATCH) {
                                    ProfileCVFragment fragmentCV = (ProfileCVFragment) ((TabsPagerAdapter) pager.getAdapter()).getItem(1);
                                    fragmentCV.getView().findViewById(R.id.addResumeButton).setVisibility(View.GONE);
                                    fragmentCV.getView().findViewById(R.id.progress_Resume).setVisibility(View.VISIBLE);

                                }
                                getProfile().saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (activityState == CREATED_FROM_SCRATCH) {
                                            ProfileCVFragment fragmentCV = (ProfileCVFragment) ((TabsPagerAdapter) pager.getAdapter()).getItem(1);
                                            numberOfResumes++;
                                            fragmentCV.showResume(resumeStringId, fragmentCV.getView());

                                        } else {
                                            finish();
                                            Intent i = new Intent(StudentProfileActivity.this, StudentProfileActivity.class);
                                            startActivity(i);

                                        }
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            new AlertDialog.Builder(this).setTitle("ERROR").setMessage("File not allowed!\nFile extensions allowed: doc, docx, pdf, rtf, txt").setNegativeButton("Chiudi", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    if (activityState != 0) {
                                        finish();
                                        Intent i = new Intent(StudentProfileActivity.this, StudentProfileActivity.class);
                                        startActivity(i);
                                    }

                                }
                            }).create().show();
                        }
                    }
                    break;

                case REQUEST_CAMERA:
                    final RelativeLayout loadingOverlay = (RelativeLayout) findViewById(R.id.loadingOverlay);
                    loadingOverlay.setVisibility(View.VISIBLE);
                    Bitmap bitImageFromCamera=null;
                    bitImageFromCamera = BitmapManager.getBitmap(this, "temp.jpg", "myjob_photos/", true);
                    if (bitImageFromCamera == null) {
                        DialogManager.toastMessage("Error occurred", this);
                        return;
                    }
                    try {
                        BitmapManager.memorizzaImmagine(bitImageFromCamera, "temp1.jpg", "myjob_photos/", 20);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    getProfile().updatePhoto(bitImageFromCamera, new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            finish();
                            Intent i = new Intent(StudentProfileActivity.this, StudentProfileActivity.class);
                            startActivity(i);

                        }
                    });
                    break;

                case SELECT_FILE:
                    final RelativeLayout loadingOverlay2 = (RelativeLayout) findViewById(R.id.loadingOverlay);
                    loadingOverlay2.setVisibility(View.VISIBLE);
                    Bitmap bitImageFromFile=null;
                    Uri selectedImageUri = data.getData();
                    String imagePath = BitmapManager.getPath(selectedImageUri,this);
                    bitImageFromFile = BitmapManager.getBitmap(this, imagePath, true);
                    try {
                        BitmapManager.memorizzaImmagine(bitImageFromFile, "temp1.jpg", "myjob_photos/", 20);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    getProfile().updatePhoto(bitImageFromFile, new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            finish();
                            Intent i = new Intent(StudentProfileActivity.this, StudentProfileActivity.class);
                            startActivity(i);

                        }
                    });

                    break;

                default:
                    if(activityState!=0) {
                        finish();
                        Intent i = new Intent(this, StudentProfileActivity.class);
                        startActivity(i);
                    }
            }

        }
    }



}
