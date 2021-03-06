package it.polito.mobile.temporaryjobplacement.pstudent.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.SaveCallback;

import java.util.Objects;

import it.polito.mobile.temporaryjobplacement.R;
import it.polito.mobile.temporaryjobplacement.TemporaryJobPlacementApp;
import it.polito.mobile.temporaryjobplacement.commons.utils.AccountManager;
import it.polito.mobile.temporaryjobplacement.commons.utils.Connectivity;
import it.polito.mobile.temporaryjobplacement.commons.utils.TimeManager;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.DialogManager;
import it.polito.mobile.temporaryjobplacement.model.Application;
import it.polito.mobile.temporaryjobplacement.model.Company;
import it.polito.mobile.temporaryjobplacement.model.JobOffer;
import it.polito.mobile.temporaryjobplacement.model.Message;
import it.polito.mobile.temporaryjobplacement.model.Student;

public class CoverLetterActivity extends ActionBarActivity {

    private Company company;
    private Student myProfile;
    private Message originalMessage;
    private boolean editMode=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cover_letter);

        //Set the custom toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null){
            setSupportActionBar(toolbar);
        }
        // Show the Up button in the action bar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        boolean displayMode=false;
        if(getIntent().hasExtra("COVER_LETTER_INDEX")) {
            editMode=true;
            ((EditText)findViewById(R.id.coverLetterNameTextView)).setText(getIntent().getStringExtra("COVER_LETTER_NAME"));
            ((EditText)findViewById(R.id.coverLetterContentTextView)).setText(getIntent().getStringExtra("COVER_LETTER_CONTENT"));
            displayMode= getIntent().getBooleanExtra("DISPLAY_MODE",false);
            getSupportActionBar().setTitle("Edit cover letter");
        }



        if(displayMode){
            getSupportActionBar().setTitle(getIntent().getStringExtra("COVER_LETTER_NAME"));
            ((EditText)findViewById(R.id.coverLetterNameTextView)).setEnabled(false);
            ((EditText)findViewById(R.id.coverLetterContentTextView)).setEnabled(false);
            ((Button)findViewById(R.id.saveCoverLetter)).setVisibility(View.INVISIBLE);
        }







    }

    public void save(View v) {
        try {

            EditText coverLetterNameTextView = (EditText) findViewById(R.id.coverLetterNameTextView);
            EditText coverLetterContentTextView = (EditText) findViewById(R.id.coverLetterContentTextView);

            if(coverLetterNameTextView.getText().toString().trim().isEmpty() || coverLetterContentTextView.getText().toString().trim().isEmpty()) {

                //Hide the keyboard
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(coverLetterNameTextView.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(coverLetterContentTextView.getWindowToken(), 0);
                DialogManager.toastMessage("Cover letter name or content missing.", this);

            } else {

                Intent intent = new Intent();
                intent.putExtra("COVER_LETTER_NAME",coverLetterNameTextView.getText().toString());
                intent.putExtra("COVER_LETTER_CONTENT", coverLetterContentTextView.getText().toString());
                if(editMode)
                    intent.putExtra("COVER_LETTER_INDEX", getIntent().getIntExtra("COVER_LETTER_INDEX", -1));

                setResult(Activity.RESULT_OK, intent);
                finish();
            }



        } catch (Exception e) {
            e.printStackTrace();

        }

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
        if(resultCode== TemporaryJobPlacementApp.exitCode){
            setResult(TemporaryJobPlacementApp.exitCode);
            finish();
        } else if (resultCode== TemporaryJobPlacementApp.exitCodeListOffer) {
            setResult(TemporaryJobPlacementApp.exitCodeListOffer);
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }




    public void onBackPressed(){
        super.onBackPressed();
        //Hide the keyboard
        EditText coverLetterNameTextView = (EditText) findViewById(R.id.coverLetterNameTextView);
        EditText coverLetterContentTextView = (EditText) findViewById(R.id.coverLetterContentTextView);
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(coverLetterNameTextView.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(coverLetterContentTextView.getWindowToken(), 0);
        DialogManager.toastMessage("Cover letter name or content missing.", this);
    }

}
