package it.polito.mobile.temporaryjobplacement.pcompany.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.parse.DeleteCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import it.polito.mobile.temporaryjobplacement.R;
import it.polito.mobile.temporaryjobplacement.TemporaryJobPlacementApp;
import it.polito.mobile.temporaryjobplacement.commons.utils.AccountManager;
import it.polito.mobile.temporaryjobplacement.commons.utils.FileManager;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.DialogManager;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.SavableEditText;
import it.polito.mobile.temporaryjobplacement.model.Application;
import it.polito.mobile.temporaryjobplacement.model.Company;
import it.polito.mobile.temporaryjobplacement.model.Education;
import it.polito.mobile.temporaryjobplacement.model.Student;

public class ApplicationDetailActivity extends ActionBarActivity {

    Company profile;
    Application application;

    ImageView V_setStatus;
    ImageView V_leaveFeedback;
    ProgressBar pro_setStatus;
    ProgressBar pro_leaveFeedback;
    EditText feedbackEditText;
    Spinner spinnerStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_detail_for_company);


        //Set the custom toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        // Show the Up button in the action bar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final String appId=getIntent().getExtras().getString("SELECTED_APP");

        final RelativeLayout loadingOverlay = (RelativeLayout) findViewById(R.id.loadingOverlay);
        loadingOverlay.setVisibility(View.VISIBLE);
        new AsyncTask<Object, Object, Object>() {
            @Override
            protected Object doInBackground(Object... params) {
                try {
                    profile = AccountManager.getCurrentCompanyProfile();
                    application = (new Student()).getApplication(appId);
                    setProfile(profile);
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
                    loadingOverlay.setVisibility(View.GONE);
                    initializeView();

                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        }.execute();


    }

    private void initializeView() {
        String name=application.getStudent().getFirstName().substring(0,1).toUpperCase()+application.getStudent().getFirstName().substring(1,application.getStudent().getFirstName().length());
        String last_name=application.getStudent().getLastName().substring(0,1).toUpperCase()+application.getStudent().getLastName().substring(1,application.getStudent().getLastName().length());

        ((TextView) findViewById(R.id.applicantAppTextView)).setText(name+" "+last_name);
        ((TextView) findViewById(R.id.offerAppTextView)).setText(application.getJobOffer().getName());
        ((TextView) findViewById(R.id.resumeName)).setText(application.getResume().getString("resumeName"));

        if (application.getStudentNotes()!=null && !application.getStudentNotes().trim().equals("")) {
            findViewById(R.id.notesLine).setVisibility(View.VISIBLE);
            findViewById(R.id.notesLabel).setVisibility(View.VISIBLE);
            TextView notesTextView = (TextView) findViewById(R.id.notesTextView);
            notesTextView.setVisibility(View.VISIBLE);
            notesTextView.setText(application.getStudentNotes());
        }
        /*
        if (application.getFeedback()!=null && !application.getFeedback().trim().equals("")) {
            findViewById(R.id.feedbackLine).setVisibility(View.VISIBLE);
            TextView feedbackLabel = (TextView) findViewById(R.id.feedbackLabel);
            feedbackLabel.setVisibility(View.VISIBLE);
            feedbackLabel.setText(application.getJobOffer().getCompany().getName()+" has left a feedback");
            TextView feedTextView = (TextView) findViewById(R.id.feedbackTextView);
            feedTextView.setVisibility(View.VISIBLE);
            feedTextView.setText(application.getFeedback());
        }*/
        if (application.getCoverLetter()!=null && !application.getCoverLetter().trim().equals("")) {
            findViewById(R.id.coverLetterLine).setVisibility(View.VISIBLE);
            findViewById(R.id.coverLetterLabel).setVisibility(View.VISIBLE);
            TextView feedTextView = (TextView) findViewById(R.id.coverLetterText);
            feedTextView.setVisibility(View.VISIBLE);
            feedTextView.setText(application.getCoverLetter());
        }


       /* if (application.getStatus().equals("Submitted")) {
            getMenuInflater().inflate(R.menu.menu_application_detail, menu);


        } */




        pro_leaveFeedback=(ProgressBar)findViewById(R.id.progress_feedback);
        pro_setStatus=(ProgressBar)findViewById(R.id.progress_setStatus);
        V_leaveFeedback=(ImageView)findViewById(R.id.V_leaveFeedback);
        V_setStatus=(ImageView)findViewById(R.id.V_setStatus);




        feedbackEditText = ((SavableEditText)findViewById(R.id.writeFeedbackTextView)).editText();
        if (application.getFeedback() != null) {
            ((SavableEditText) feedbackEditText.getParent()).setSavedText(application.getFeedback());
            feedbackEditText.setText(application.getFeedback());
        }
        ((SavableEditText)feedbackEditText.getParent()).setButtonSaveListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateFeedback(feedbackEditText.getText().toString());
                InputMethodManager imm = (InputMethodManager) ApplicationDetailActivity.this.getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(feedbackEditText.getWindowToken(), 0);
            }
        });
        feedbackEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String input;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    input = v.getText().toString();
                    updateFeedback(input);
                    InputMethodManager imm = (InputMethodManager) ApplicationDetailActivity.this.getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(feedbackEditText.getWindowToken(), 0);

                    return true;
                }
                return false;
            }
        });
        feedbackEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String input;
                EditText editText;
                if (!hasFocus) {
                    editText = (EditText) v;
                    input = editText.getText().toString();
                    updateFeedback(input);
                }
            }
        });
        final TextView coverLetterClickableTextView = (TextView) findViewById(R.id.coverLetterClickableTextView);
        final ArrayList<ParseObject> defaultMessages =profile.getDefaultMessages();
        if(defaultMessages==null || defaultMessages.size()==0)
            coverLetterClickableTextView.setVisibility(View.GONE);
        else {
            coverLetterClickableTextView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    CharSequence[] coverLettersNames = new CharSequence[defaultMessages.size()];
                    for (int i = 0; i < defaultMessages.size(); i++)
                        coverLettersNames[i] = defaultMessages.get(i).getString("name");

                    AlertDialog.Builder builder = new AlertDialog.Builder(ApplicationDetailActivity.this);
                    builder.setTitle("Select a cover letter").setItems(coverLettersNames, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, final int which) {

                            coverLetterClickableTextView.setText(defaultMessages.get(which).getString("name"));
                            feedbackEditText.setText("");
                            feedbackEditText.append(defaultMessages.get(which).getString("content"));

                        }
                    });
                    builder.setNegativeButton("Cancel", null);
                    builder.create().show();

                }
            });
        }



        //status
        final List<String> list1 = new ArrayList<String>();
        spinnerStatus= (Spinner)findViewById(R.id.statusSpinner);
        list1.addAll(FileManager.readRowsFromFile(this, "app_status.dat"));
        final ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this, R.layout.spinner_item , list1);
        dataAdapter1.setDropDownViewResource(R.layout.spinner_item_dropdown);
        spinnerStatus.setAdapter(dataAdapter1);



        if(application.getStatus()!=null){
            spinnerStatus.setSelection(list1.indexOf(application.getStatus()));
        }else spinnerStatus.setSelection(0);


        spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
               updateStatus(list1.get(position));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });











    }

    private void updateStatus(String s) {


        if(application.getStatus().equals(s))return;


        application.setStatus(s);

        V_setStatus.setVisibility(View.GONE);
        pro_setStatus.setVisibility(View.VISIBLE);
        application.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    DialogManager.toastMessage("Feedback inserted", ApplicationDetailActivity.this, "center", true);
                    if (pro_setStatus != null) pro_setStatus.setVisibility(View.GONE);
                    if (V_setStatus != null) V_setStatus.setVisibility(View.VISIBLE);
                     } else {
                    DialogManager.toastMessage("" + e.getMessage(), ApplicationDetailActivity.this, "center", true);
                }
            }
        });
    }

    private void updateFeedback(String s) {

                application.setFeedback(s);

                V_leaveFeedback.setVisibility(View.GONE);
                pro_leaveFeedback.setVisibility(View.VISIBLE);
               application.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            DialogManager.toastMessage("Feedback inserted", ApplicationDetailActivity.this, "center", true);
                            if (pro_leaveFeedback != null) pro_leaveFeedback.setVisibility(View.GONE);
                            if (V_leaveFeedback != null) V_leaveFeedback.setVisibility(View.VISIBLE);
                            if (feedbackEditText!= null) (
                                    (SavableEditText) feedbackEditText.getParent()).setSavedText(application.getFeedback());
                        } else {
                            DialogManager.toastMessage("" + e.getMessage(), ApplicationDetailActivity.this, "center", true);
                        }
                    }
                });



    }















    private Menu menu;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_offer_item, menu);
        this.menu=menu;
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
            // Intent i = new Intent(this, StudentMainActivity.class);
            this.onBackPressed();
        }
        if (id == R.id.action_HOME) {
            //setResult(TemporaryJobPlacementApp.exitCode);
            //finish();
            Intent i = new Intent(this, CompanyMainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
        if (id == R.id.action_delete) {
            if (getProfile() != null) {
                String title="WITHDRAW APPLICATION";
                String description="Are you sure you want to withdraw application?";
                DialogManager.setDialogWithCancelAndOk(title, description, this, "WITHDRAW", new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ParseUser user = AccountManager.getCurrentUser();
                            final ProgressDialog pd=ProgressDialog.show(ApplicationDetailActivity.this, null, "Loading", true, false);
                            application.deleteInBackground(new DeleteCallback() {
                                @Override
                                public void done(com.parse.ParseException e) {
                                    if (pd != null && pd.isShowing()) pd.dismiss();
                                    ApplicationDetailActivity.this.onBackPressed();
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                });
            }
        }
        return super.onOptionsItemSelected(item);
    }



    public synchronized Company getProfile(){
        return profile;
    }
    public synchronized void setProfile(Company s){
        profile=s;
    }


    public void learnMoreAboutStudent(View v){
        Intent detailIntent = new Intent(this, CompanyDetailActivity.class);
        detailIntent.putExtra("SELECTED_STUDENT", application.getStudent().getObjectId());
        startActivity(detailIntent);
    }


    public void seeResume(View v){
        try {
            final ParseObject resume = application.getResume();

            ((ParseFile) resume.get("curriculum")).getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] bytes, ParseException e) {

                    String filename = resume.getString("fileName");
                    File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filename);

                    try {
                        org.apache.commons.io.FileUtils.writeByteArrayToFile(file, bytes);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                    Uri uri = Uri.fromFile(file);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    if (file.toString().contains(".doc") || file.toString().contains(".docx")) {
                        // Word document
                        intent.setDataAndType(uri, "application/msword");
                    } else if (file.toString().contains(".pdf")) {
                        // PDF file
                        intent.setDataAndType(uri, "application/pdf");
                    } else if (file.toString().contains(".rtf")) {
                        // RTF file
                        intent.setDataAndType(uri, "application/rtf");
                    } else if (file.toString().contains(".txt")) {
                        // Text file
                        intent.setDataAndType(uri, "text/plain");
                    }
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);


                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }    }


}
