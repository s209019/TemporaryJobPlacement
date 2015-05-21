package it.polito.mobile.temporaryjobplacement.pcompany.activities;

import android.support.v4.app.DialogFragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import it.polito.mobile.temporaryjobplacement.R;
import it.polito.mobile.temporaryjobplacement.commonfragments.MultipleChoiceDialogFragment;
import it.polito.mobile.temporaryjobplacement.commons.utils.AccountManager;
import it.polito.mobile.temporaryjobplacement.commons.utils.FileManager;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.DialogManager;
import it.polito.mobile.temporaryjobplacement.model.JobOffer;
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
        drawerManager=new DrawerManager(this,drawerLayout,toolbar,DrawerManager.SECTION2);
        drawerManager.setDrawer();

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
            JobOffer jobOffer = new JobOffer();
            jobOffer.setName(nameEditText.getText().toString().trim());
            jobOffer.setDescription(descriptionEditText.getText().toString().trim());
            jobOffer.setCompany(AccountManager.getCurrentCompanyProfile()); //TODO: Fare in background?
            jobOffer.setEducation(educationSpinner.getSelectedItem().toString());
            jobOffer.setCareerLevel(careerLevelSpinner.getSelectedItem().toString());
            jobOffer.setContract(contractSpinner.getSelectedItem().toString());

            //Aggiungere location, industries, responsibilities, minimum Qualifications, preferred qualifications
            jobOffer.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    //TODO: Fare
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
