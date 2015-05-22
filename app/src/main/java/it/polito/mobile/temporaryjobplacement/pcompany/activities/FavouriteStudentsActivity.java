package it.polito.mobile.temporaryjobplacement.pcompany.activities;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.util.List;

import it.polito.mobile.temporaryjobplacement.R;
import it.polito.mobile.temporaryjobplacement.commons.utils.AccountManager;
import it.polito.mobile.temporaryjobplacement.model.Company;
import it.polito.mobile.temporaryjobplacement.model.Student;
import it.polito.mobile.temporaryjobplacement.pcompany.fragments.StudentDetailFragment;
import it.polito.mobile.temporaryjobplacement.pcompany.fragments.StudentListFragment;
import it.polito.mobile.temporaryjobplacement.pcompany.viewmanaging.DrawerManager;

public class FavouriteStudentsActivity extends ActionBarActivity implements StudentListFragment.Callbacks {

    private DrawerManager drawerManager;
    private Company companyProfile;
    private List<Student> favourites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_students);



        //Set the custom toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null){
            setSupportActionBar(toolbar);
        }
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        drawerManager=new DrawerManager(this,drawerLayout,toolbar,DrawerManager.SECTION4);
        drawerManager.setDrawer();




        StudentListFragment fragment = new StudentListFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_company_profile, menu);
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
    protected void onRestart() {
        super.onRestart();

        this.recreate();


    }



    @Override
    public void onItemSelected(Student student) {
        // In single-pane mode, simply start the detail activity
        // for the selected item ID.
        Intent detailIntent = new Intent(this, CompanyDetailActivity.class);
        detailIntent.putExtra("SELECTED_STUDENT", student.getObjectId());
        detailIntent.putExtra("IS_FAVOURITED",true);
        startActivityForResult(detailIntent, 0);
    }

    @Override
    public ParseQueryAdapter.QueryFactory<Student> getQueryFactory() throws Exception {
        final Exception[] error={null};
        ParseQueryAdapter.QueryFactory<Student> query= new ParseQueryAdapter.QueryFactory<Student>() {
            public ParseQuery<Student> create() {
                ParseQuery<Student> query=null;
                try {
                    query = companyProfile.getFavouriteStudentsRelationQuery();
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
    public List<Student> getFavouriteStudents() throws ParseException {
        return null;
    }

    @Override
    public void updateFavourites(Student favourite, boolean toBeAdded) {

    }

    @Override
    public boolean isFavouriteList() {
        return true;
    }

    @Override
    public void initializeProfile() throws Exception {
        if(companyProfile==null)
            companyProfile = AccountManager.getCurrentCompanyProfile();
    }
}
