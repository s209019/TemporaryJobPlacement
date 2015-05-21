package it.polito.mobile.temporaryjobplacement.pcompany.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.util.ArrayList;
import java.util.List;

import it.polito.mobile.temporaryjobplacement.R;
import it.polito.mobile.temporaryjobplacement.TemporaryJobPlacementApp;
import it.polito.mobile.temporaryjobplacement.commons.utils.AccountManager;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.FragmentManipulation;
import it.polito.mobile.temporaryjobplacement.model.Company;
import it.polito.mobile.temporaryjobplacement.model.Education;
import it.polito.mobile.temporaryjobplacement.model.Student;
import it.polito.mobile.temporaryjobplacement.pcompany.fragments.EducationsListFragment;
import it.polito.mobile.temporaryjobplacement.pcompany.fragments.StudentDetailFragment;
import it.polito.mobile.temporaryjobplacement.pcompany.fragments.StudentListFragment;
import it.polito.mobile.temporaryjobplacement.pstudent.activities.StudentDetailActivity;
import it.polito.mobile.temporaryjobplacement.pstudent.activities.StudentMainActivity;
import it.polito.mobile.temporaryjobplacement.pstudent.activities.StudentOfferListActivity;
import it.polito.mobile.temporaryjobplacement.pstudent.fragments.AddEducationDialogFragment;
import it.polito.mobile.temporaryjobplacement.pstudent.fragments.CompanyDetailFragment;


public class CompanyStudentListActivity extends ActionBarActivity implements StudentListFragment.Callbacks,StudentDetailFragment.OnFragmentInteractionListener,EducationsListFragment.Callbacks{



    private boolean mTwoPane;
     private Company companyProfile;
    private List<Student> favourites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_student_list);




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
            // large-screen layouts (res/values-large and res/values-sw600
            // If this view is present, then the activity should be in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, list items should be given the 'activated' state when touched.
            ((StudentListFragment)getSupportFragmentManager().findFragmentById(R.id.item_list)).setActivateOnItemClick(true);
            getSupportActionBar().hide();
        }


        //-->HANDSET
    }





    @Override
    public void initializeProfile() throws Exception {
        if(companyProfile==null)

            companyProfile = AccountManager.getCurrentCompanyProfile();

    }

    @Override
    public void onItemSelected(Student student) {
    //IF TABLET
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString("SELECTED_STUDENT", student.getObjectId());
            arguments.putBoolean("IS_FAVOURITED", student.isFavourited());
            StudentDetailFragment fragment = new StudentDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction().replace(R.id.item_detail_container, fragment).commit();

            //IF HANDSET
        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, CompanyDetailActivity.class);
            detailIntent.putExtra("SELECTED_STUDENT", student.getObjectId());
            detailIntent.putExtra("IS_FAVOURITED", student.isFavourited());
            startActivityForResult(detailIntent, 0);
        }

    }

    @Override
    public ParseQueryAdapter.QueryFactory<Student> getQueryFactory() {
        return new ParseQueryAdapter.QueryFactory<Student>() {
            public ParseQuery<Student> create() {
                ParseQuery<Student> query = Student.getQuery();
                query.orderByAscending("lastName");
                query.setLimit(100);
                return query;
            }
        };
    }



    @Override
    public List<Student> getFavouriteStudents() throws ParseException {
       favourites = companyProfile.getFavouriteStudents();
        return favourites;
    }

    @Override
    public void updateFavourites(Student favourite, boolean toBeAdded) {

            if(toBeAdded) {
                companyProfile.getRelation("favouriteStudents").add(favourite); //Remoto
                favourites.add(favourite); //Locale
            } else {
                companyProfile.getRelation("favouriteStudents").remove(favourite);
                favourites.remove(favourite); //Locale

            }

            companyProfile.saveEventually();


    }

    @Override
    public boolean isFavouriteList(){
        return false;
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
            //setResult(TemporaryJobPlacementApp.exitCode);
            //finish();
            Intent i = new Intent(this, StudentMainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==TemporaryJobPlacementApp.exitCode){
            setResult(TemporaryJobPlacementApp.exitCode);
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }




    List<Education> educations=new ArrayList<Education>();
    int currentFragment=0;
    @Override
    public void startEducationsFragment(List<Education> educations) {
        this.educations=educations;
        //start educations fragment
        DialogFragment dialogF = (DialogFragment) EducationsListFragment.newInstance();
        dialogF.show(getSupportFragmentManager(), "MyDialog");

    }

    @Override
    public List<Education> getEducations() {
        return educations;
    }

}