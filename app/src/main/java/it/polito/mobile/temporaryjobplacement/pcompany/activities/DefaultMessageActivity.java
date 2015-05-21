package it.polito.mobile.temporaryjobplacement.pcompany.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import it.polito.mobile.temporaryjobplacement.R;
import it.polito.mobile.temporaryjobplacement.TemporaryJobPlacementApp;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.DialogManager;
import it.polito.mobile.temporaryjobplacement.model.Company;
import it.polito.mobile.temporaryjobplacement.model.Message;
import it.polito.mobile.temporaryjobplacement.model.Student;
import it.polito.mobile.temporaryjobplacement.pstudent.activities.StudentMainActivity;

public class DefaultMessageActivity extends ActionBarActivity {

    private Company company;
    private Company myProfile;
    private Message originalMessage;
    private boolean editMode=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_message);

        //Set the custom toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null){
            setSupportActionBar(toolbar);
        }
        // Show the Up button in the action bar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        boolean displayMode=false;
        if(getIntent().hasExtra("DEFAULT_MESSAGE_INDEX")) {
            editMode=true;
            ((EditText)findViewById(R.id.defaultMessageNameTextView)).setText(getIntent().getStringExtra("DEFAULT_MESSAGE_NAME"));
            ((EditText)findViewById(R.id.defaultMessageContentTextView)).setText(getIntent().getStringExtra("DEFAULT_MESSAGE_CONTENT"));
            displayMode= getIntent().getBooleanExtra("DISPLAY_MODE",false);
            getSupportActionBar().setTitle("Edit default message");
        }



        if(displayMode){
            getSupportActionBar().setTitle(getIntent().getStringExtra("DEFAULT_MESSAGE_NAME"));
            ((EditText)findViewById(R.id.defaultMessageNameTextView)).setEnabled(false);
            ((EditText)findViewById(R.id.defaultMessageContentTextView)).setEnabled(false);
            ((Button)findViewById(R.id.saveDefaultMessage)).setVisibility(View.INVISIBLE);
        }







    }

    public void save(View v) {
        try {

            EditText defaultMessageNameTextView = (EditText) findViewById(R.id.defaultMessageNameTextView);
            EditText defaultMessageContentTextView = (EditText) findViewById(R.id.defaultMessageContentTextView);

            if(defaultMessageNameTextView.getText().toString().trim().isEmpty() || defaultMessageContentTextView.getText().toString().trim().isEmpty()) {

                //Hide the keyboard
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(defaultMessageNameTextView.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(defaultMessageContentTextView.getWindowToken(), 0);
                DialogManager.toastMessage("Default message name or content missing.", this);

            } else {

                Intent intent = new Intent();
                intent.putExtra("DEFAULT_MESSAGE_NAME", defaultMessageNameTextView.getText().toString());
                intent.putExtra("DEFAULT_MESSAGE_CONTENT", defaultMessageContentTextView.getText().toString());
                if(editMode)
                    intent.putExtra("DEFAULT_MESSAGE_INDEX", getIntent().getIntExtra("DEFAULT_MESSAGE_INDEX", -1));

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
        EditText defaultMessageNameTextView = (EditText) findViewById(R.id.defaultMessageNameTextView);
        EditText defaultMessageContentTextView = (EditText) findViewById(R.id.defaultMessageContentTextView);
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(defaultMessageNameTextView.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(defaultMessageContentTextView.getWindowToken(), 0);
        DialogManager.toastMessage("Default message name or content missing.", this);
    }

}
