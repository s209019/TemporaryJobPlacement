package it.polito.mobile.temporaryjobplacement.pstudent.activities;

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

public class SendMessageActivity extends ActionBarActivity {

    private Company company;
    private Student myProfile;
    private Message originalMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        //Set the custom toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null){
            setSupportActionBar(toolbar);
        }
        // Show the Up button in the action bar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        final String companyId=getIntent().getStringExtra("SELECTED_COMPANY");
        final String originalMessageId=getIntent().getStringExtra("ORIGINAL_MESSAGE");

        final Company[] company = {null};
        final Message[] message = {null};
        final Student[] myProfile = {null};
        final RelativeLayout loadingOverlay =(RelativeLayout) findViewById(R.id.loadingOverlay);
        loadingOverlay.setVisibility(View.VISIBLE);
        new AsyncTask<Object, Object, Object>(){
            @Override
            protected Object doInBackground(Object... params) {
                try {
                    company[0] = Company.getQuery().get(companyId);
                    if(originalMessageId!=null) {
                        message[0] = Message.getQuery().get(originalMessageId);
                    } else {
                        message[0]=null;
                    }
                    myProfile[0] = AccountManager.getCurrentStudentProfile();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;

                }
                return new Object();
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                try {
                    if (o == null) {
                        Connectivity.connectionError(SendMessageActivity.this);
                        return;
                    }

                    loadingOverlay.setVisibility(View.GONE);
                    initializeView(company[0], message[0], myProfile[0]);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.execute();


    }

    private void initializeView(Company company, Message originalMessage, Student myProfile) {

        this.company=company;
        this.myProfile=myProfile;
        this.originalMessage=originalMessage;

        getSupportActionBar().setSubtitle(company.getName());

        TextView companyStudentTextView=(TextView) findViewById(R.id.companyStudentTextView);
        companyStudentTextView.setText(company.getName());

        if(originalMessage!=null) {
            EditText subjectTextView = (EditText) findViewById(R.id.subjectTextView);
            subjectTextView.setText("RE: "+originalMessage.getSubject());
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

    public void send(View v) {
        try {

            EditText subjectTextView = (EditText) findViewById(R.id.subjectTextView);
            EditText messageBodyTextView = (EditText) findViewById(R.id.messageBodyTextView);

            if(subjectTextView.getText().toString().trim().isEmpty() || messageBodyTextView.getText().toString().trim().isEmpty()) {

                //Hide the keyboard
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(subjectTextView.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(messageBodyTextView.getWindowToken(), 0);
                DialogManager.toastMessage("Subject or message body missing.", this);

            } else {

                final Message message = new Message();
                message.setStudent(myProfile);
                message.setCompany(company);
                message.setRead(false);
                message.setSender("student");
                message.setSubject(subjectTextView.getText().toString());
                message.setMessage(messageBodyTextView.getText().toString());

                if(originalMessage!=null) {
                    message.setOriginalMessage(originalMessage);
                }

                message.saveEventually(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e==null)DialogManager.toastMessage("Message sent successfully", SendMessageActivity.this,3);
                        else DialogManager.toastMessage("Message send failed ", SendMessageActivity.this,3);
                    }
                });


                DialogManager.toastMessage("Sending in progress... Check SENT tab in message section", SendMessageActivity.this);
                this.onBackPressed();

            }



        } catch (Exception e) {
            e.printStackTrace();

        }

    }
}
