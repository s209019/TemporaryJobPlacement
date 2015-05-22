package it.polito.mobile.temporaryjobplacement.pcompany.activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import it.polito.mobile.temporaryjobplacement.R;
import it.polito.mobile.temporaryjobplacement.commonfragments.MultipleChoiceDialogFragment;
import it.polito.mobile.temporaryjobplacement.commons.utils.AccountManager;
import it.polito.mobile.temporaryjobplacement.commons.utils.Connectivity;
import it.polito.mobile.temporaryjobplacement.commons.utils.FileManager;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.DialogManager;
import it.polito.mobile.temporaryjobplacement.model.Application;
import it.polito.mobile.temporaryjobplacement.model.Company;
import it.polito.mobile.temporaryjobplacement.model.JobOffer;
import it.polito.mobile.temporaryjobplacement.model.Student;
import it.polito.mobile.temporaryjobplacement.pcompany.viewmanaging.DrawerManager;

public class PostJobOfferActivity extends ActionBarActivity {

    private DrawerManager drawerManager;
    private EditText nameEditText;
    private EditText descriptionEditText;
    private EditText addressEditText;
    private EditText countryEditText;
    private EditText zipCodeEditText;
    private EditText cityEditText;
    private EditText responsibilitiesEditText;
    private EditText minimumQualificationsEditText;
    private EditText preferredQualificationsEditText;
    private Spinner contractSpinner;
    private Spinner educationSpinner;
    private Spinner careerLevelSpinner;
    private TextView industriesTextView;
    private boolean editMode;
    private JobOffer jobOffer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_job_offer);

        //Set the custom toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null){
            setSupportActionBar(toolbar);
        }
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        editMode=false;
        if(getIntent().hasExtra("SELECTED_OFFER")){
            editMode=true;
            findViewById(R.id.loadingOverlay).setVisibility(View.VISIBLE);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            drawerManager=new DrawerManager(this,drawerLayout,toolbar,DrawerManager.SECTION2);
            drawerManager.setDrawer();
        }

        nameEditText = (EditText) findViewById(R.id.nameTextView);
        descriptionEditText = (EditText) findViewById(R.id.descriptionTextView);
        addressEditText = (EditText) findViewById(R.id.addressTextView);
        countryEditText = (EditText) findViewById(R.id.countryTextView);
        zipCodeEditText = (EditText) findViewById(R.id.zipCodeTextView);
        cityEditText = (EditText) findViewById(R.id.cityTextView);
        responsibilitiesEditText = (EditText) findViewById(R.id.responsibilitiesTextView);
        minimumQualificationsEditText = (EditText) findViewById(R.id.minimumQualificationsTextView);
        preferredQualificationsEditText = (EditText) findViewById(R.id.preferredQualificationsTextView);
        contractSpinner = (Spinner) findViewById(R.id.contractSpinner);
        educationSpinner = (Spinner) findViewById(R.id.educationSpinner);
        careerLevelSpinner = (Spinner) findViewById(R.id.careerLevelSpinner);
        industriesTextView = (TextView) findViewById(R.id.industriesTextView);

        //INDUSTRY FIELD
        //implement interface declared by MultipleChoiceDialogFragment in order to receive
        //industries checked by user and set them in industriesClickableTextView
        final MultipleChoiceDialogFragment.OnAllItemsCheckedListener onIndustriesCheckedListener=
                new MultipleChoiceDialogFragment.OnAllItemsCheckedListener() {
                    @Override
                    public void getAllItemsChecked(ArrayList<String> selectedItems) {
                        industriesTextView.setText(getTextFromList(selectedItems));
                    }
                };
        //launch MultipleChoiceDialogFragment passing items to display, items previously checked and interface implementation
        industriesTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> items = FileManager.readRowsFromFile(getApplicationContext(), "industries.dat");
                //get already industries selected from industriesClickableTextView and pass it to MultipleChoiceDialogFragment
                String[] alreadyCheckedIndustries = getItemsFromTextView(industriesTextView);
                DialogFragment df = MultipleChoiceDialogFragment.newInstance("Select one or more industries:", items, alreadyCheckedIndustries, onIndustriesCheckedListener);
                df.show(getSupportFragmentManager(), "MyDialog");
            }
        });

        //education Spinner
        final List<String> listEducation = new ArrayList<String>();
        listEducation.add("");
        listEducation.addAll(FileManager.readRowsFromFile(this, "educations.dat"));

        final ArrayAdapter<String> dataAdapterEducation = new ArrayAdapter<String>(this, R.layout.spinner_item , listEducation);
        dataAdapterEducation.setDropDownViewResource(R.layout.spinner_item_dropdown);
        educationSpinner.setAdapter(dataAdapterEducation);

        //careerLevel Spinner
        final List<String> listCareerLevel = new ArrayList<String>();
        listCareerLevel.add("");
        listCareerLevel.addAll(FileManager.readRowsFromFile(this, "career_levels.dat"));

        final ArrayAdapter<String> dataAdapterCareerLevels = new ArrayAdapter<String>(this, R.layout.spinner_item , listCareerLevel);
        dataAdapterCareerLevels.setDropDownViewResource(R.layout.spinner_item_dropdown);
        careerLevelSpinner.setAdapter(dataAdapterCareerLevels);

        //careerLevel Spinner
        final List<String> listContract = new ArrayList<String>();
        listContract.add("");
        listContract.addAll(FileManager.readRowsFromFile(this, "positions.dat"));

        final ArrayAdapter<String> dataAdapterContract = new ArrayAdapter<String>(this, R.layout.spinner_item , listContract);
        dataAdapterContract.setDropDownViewResource(R.layout.spinner_item_dropdown);
        contractSpinner.setAdapter(dataAdapterContract);

        if(editMode){
            //getting jobOfferID
            final String jobOfferId=getIntent().getStringExtra("SELECTED_OFFER");
            final JobOffer[] offer = {null};
            new AsyncTask<Object, Object, Object>(){
                @Override
                protected Object doInBackground(Object... params) {
                    try {
                        offer[0] = JobOffer.getQuery().get(jobOfferId);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                    return new Object();

                }

                @Override
                protected void onPostExecute(Object o) {
                    try {
                        super.onPostExecute(o);
                        if (o == null) {
                            Connectivity.connectionError(PostJobOfferActivity.this);
                            return;
                        }

                        jobOffer = offer[0];
                        nameEditText.append(jobOffer.getName());
                        descriptionEditText.append(jobOffer.getDescription());
                        addressEditText.append(jobOffer.getAddress());
                        countryEditText.append(jobOffer.getCountry());
                        zipCodeEditText.append(jobOffer.getZipCode());
                        cityEditText.append(jobOffer.getCity());
                        responsibilitiesEditText.append(jobOffer.getResponsibilities());
                        minimumQualificationsEditText.append(jobOffer.getMinimumQualifications());
                        preferredQualificationsEditText.append(jobOffer.getPreferredQualifications());
                        contractSpinner.setSelection(listContract.indexOf(jobOffer.getContract()));
                        educationSpinner.setSelection(listEducation.indexOf(jobOffer.getEducation()));
                        careerLevelSpinner.setSelection(listCareerLevel.indexOf(jobOffer.getCareerLevel()));
                        industriesTextView.setText(jobOffer.getIndustries());

                        ((Button)findViewById(R.id.postJobOfferButton)).setText("EDIT OFFER");

                        findViewById(R.id.loadingOverlay).setVisibility(View.GONE);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }.execute();        }

    }

    private String[] getItemsFromTextView(TextView textView){
        String text=textView.getText().toString();
        String[] items = text.split("\n");
        return items;
    }


    private String getTextFromList(ArrayList<String> list){
        String text="";
        for(String industry : list) text=text+industry+"\n";
        if(!text.equals(""))text=text.substring(0,text.length()-1);
        return text;
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

            if(editMode)
                this.onBackPressed();
            else
                drawerManager.toggleDrawer();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }

        if(!editMode && drawerManager.isDrawerOpen()){
            drawerManager.toggleDrawer();
            return;
        }
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }

    public void postJobOffer(View v) {

        StringBuilder sb= new StringBuilder();
        if(nameEditText.getText().toString().trim().equals(""))
            sb.append("Position offered field empty\n");
        if(descriptionEditText.getText().toString().trim().equals(""))
            sb.append("Job description field empty\n");
        if(addressEditText.getText().toString().trim().equals(""))
            sb.append("Address field empty\n");
        if(countryEditText.getText().toString().trim().equals(""))
            sb.append("Country field empty\n");
        if(zipCodeEditText.getText().toString().trim().equals(""))
            sb.append("Zip code field empty\n");
        if(cityEditText.getText().toString().trim().equals(""))
            sb.append("City field empty\n");
        if(responsibilitiesEditText.getText().toString().trim().equals(""))
            sb.append("Responsibilities field empty\n");
        if(minimumQualificationsEditText.getText().toString().trim().equals(""))
            sb.append("Minimum qualifications field empty\n");
        if(preferredQualificationsEditText.getText().toString().trim().equals(""))
            sb.append("Preferred qualifications field empty\n");
        if(contractSpinner.getSelectedItem().toString().equals(""))
            sb.append("Contract field empty\n");
        if(educationSpinner.getSelectedItem().toString().equals(""))
            sb.append("Education level field empty\n");
        if(careerLevelSpinner.getSelectedItem().toString().equals(""))
            sb.append("Career level field empty\n");
        if(industriesTextView.getText().toString().trim().equals(""))
            sb.append("Industries field empty\n");

        try {
        if(sb.toString().equals("")){

            if(!editMode)
                jobOffer = new JobOffer();

            Company company = AccountManager.getCurrentCompanyProfile(); //TODO: Fare in background?

            jobOffer.setName(nameEditText.getText().toString().trim());
            jobOffer.setDescription(descriptionEditText.getText().toString().trim());
            jobOffer.setCompany(company);
            jobOffer.setEducation(educationSpinner.getSelectedItem().toString());
            jobOffer.setCareerLevel(careerLevelSpinner.getSelectedItem().toString());
            jobOffer.setContract(contractSpinner.getSelectedItem().toString());
            jobOffer.setAddress(addressEditText.getText().toString().trim());
            jobOffer.setCity(cityEditText.getText().toString().trim());
            jobOffer.setZipCode(zipCodeEditText.getText().toString().trim());
            jobOffer.setCountry(countryEditText.getText().toString().trim());
            jobOffer.setMinimumQualifications(minimumQualificationsEditText.getText().toString().trim());
            jobOffer.setPreferredQualifications(preferredQualificationsEditText.getText().toString().trim());
            jobOffer.setResponsibilities(responsibilitiesEditText.getText().toString().trim());
            jobOffer.setIndustries(industriesTextView.getText().toString().trim());
            String location_search = addressEditText.getText().toString().trim().toLowerCase() +" "+
                    cityEditText.getText().toString().trim().toLowerCase() +" "+
                    zipCodeEditText.getText().toString().trim() + " " +
                    countryEditText.getText().toString().trim().toLowerCase();
            jobOffer.setLocationSearch(location_search);

            String keywords_search = nameEditText.getText().toString().trim().toLowerCase() + " " +
                    descriptionEditText.getText().toString().trim().toLowerCase() + " " +
                    company.getName().trim().toLowerCase() + " " +
                    responsibilitiesEditText.getText().toString().trim().toLowerCase() + " " +
                    minimumQualificationsEditText.getText().toString().trim().toLowerCase();

            jobOffer.setKeywords(keywords_search);
            jobOffer.setPublic(true);

            findViewById(R.id.loadingOverlay).setVisibility(View.VISIBLE);

            jobOffer.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {

                    if(!editMode) {
                        Intent i = new Intent(PostJobOfferActivity.this, CompanyDetailActivity.class);
                        i.putExtra("SELECTED_OFFER", jobOffer.getObjectId());
                        startActivity(i);
                    }
                    finish();

                }
            });
        } else {
            //Mostra dialog con errori
            DialogManager.toastMessage("ERROR! All fields must be non-empty!\n\n" + sb.toString(), this, "center", false);
        }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
