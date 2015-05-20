package it.polito.mobile.temporaryjobplacement.commonactivities;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import it.polito.mobile.temporaryjobplacement.R;
import it.polito.mobile.temporaryjobplacement.commons.utils.AccountManager;
import it.polito.mobile.temporaryjobplacement.commons.utils.Connectivity;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.AsyncTaskWithProgressBar;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.ClearableEditText;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.DialogManager;


public class SignUpActivity extends ActionBarActivity {
    private EditText userText, passText;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Set the custom toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null){
            setSupportActionBar(toolbar);
        }

        userText = ((ClearableEditText) findViewById(R.id.editTextUser)).editText();
        passText = ((ClearableEditText) findViewById(R.id.editTextPass)).editText();
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);


        // Show the Up button in the action bar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }






    public void signUp(View v) {

        if(!Connectivity.hasNetworkConnection(getApplicationContext())){
          DialogManager.setDialog(getResources().getString(R.string.no_connectivity_messagge), this);
            return;
        }

        if(userText.getText().toString().trim().equals("") || passText.getText().toString().trim().equals("")){
            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(userText.getWindowToken(), 0);
            mgr.hideSoftInputFromWindow(passText.getWindowToken(), 0);
            DialogManager.toastMessage(getResources().getString(R.string.empty_field_message), this);
            return;
        }


        if(radioGroup.getCheckedRadioButtonId()==-1){
           InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(userText.getWindowToken(), 0);
            mgr.hideSoftInputFromWindow(passText.getWindowToken(), 0);
            DialogManager.toastMessage(getResources().getString(R.string.no_user_type_message), this);
            return;
        }






        RadioButton radioButton=(RadioButton)findViewById(radioGroup.getCheckedRadioButtonId());
        final String userType=radioButton.getText().toString();



        new AsyncTaskWithProgressBar(this) {
            @Override
            protected String doInBackground(Void... params) {
                String resultMessage = activity.getResources().getString(R.string.signedup_message);

                try {
                    AccountManager.signup(userText.getText().toString(), passText.getText().toString(),userType);
                } catch (Exception e) {
                    resultMessage = "Error occurred:\n" + e.getMessage();
                    e.printStackTrace();
                }

                return resultMessage;
            }

            @Override
            protected void onPostExecute(String resultMessage) {
                super.onPostExecute(resultMessage);
                activity.onBackPressed();
            }


        }.execute();

    }








}
