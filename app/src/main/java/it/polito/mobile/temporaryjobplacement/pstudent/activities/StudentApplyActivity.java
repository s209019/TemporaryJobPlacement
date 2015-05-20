package it.polito.mobile.temporaryjobplacement.pstudent.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import it.polito.mobile.temporaryjobplacement.R;
import it.polito.mobile.temporaryjobplacement.TemporaryJobPlacementApp;
import it.polito.mobile.temporaryjobplacement.commons.utils.AccountManager;
import it.polito.mobile.temporaryjobplacement.commons.utils.Connectivity;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.AsyncTaskWithProgressBar;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.DialogManager;
import it.polito.mobile.temporaryjobplacement.model.Application;
import it.polito.mobile.temporaryjobplacement.model.JobOffer;
import it.polito.mobile.temporaryjobplacement.model.Student;

public class StudentApplyActivity extends ActionBarActivity {

    private JobOffer offer;
    private Student myProfile;
    private Map<Integer, String> resumes = new HashMap<>();
    private ParseObject selectedResume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_apply);

        //Set the custom toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null){
            setSupportActionBar(toolbar);
        }
        // Show the Up button in the action bar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        final String jobOfferId=getIntent().getStringExtra("SELECTED_OFFER");
        final JobOffer[] offer = {null};
        final Student[] myProfile = {null};
        final RelativeLayout loadingOverlay =(RelativeLayout) findViewById(R.id.loadingOverlay);
        loadingOverlay.setVisibility(View.VISIBLE);
        new AsyncTask<Object, Object, Object>(){
            @Override
            protected Object doInBackground(Object... params) {
                try {
                    offer[0] = JobOffer.getQuery().include("company").get(jobOfferId);
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
                        Connectivity.connectionError(StudentApplyActivity.this);
                        return;
                    }
                    loadingOverlay.setVisibility(View.GONE);
                    initializeView(offer[0], myProfile[0]);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.execute();


    }

    private void initializeView(JobOffer offer, final Student myProfile) {

        this.offer=offer;
        this.myProfile=myProfile;

        //Position stands for job name
        TextView positionTextView=(TextView) findViewById(R.id.jobOfferNameTextView);
        positionTextView.setText(offer.getName());

        TextView companyTextView=(TextView) findViewById(R.id.companyTextView);
        companyTextView.setText(offer.getCompany().getName());

        TextView contractTextView=(TextView) findViewById(R.id.contractTextView);
        contractTextView.setText(offer.getContract());

        TextView educationTextView=(TextView) findViewById(R.id.educationTextView);
        educationTextView.setText(offer.getEducation());

        TextView careerLevelTextView=(TextView) findViewById(R.id.careerLevelTextView);
        careerLevelTextView.setText(offer.getCareerLevel());

        TextView industriesTextView =(TextView) findViewById(R.id.industriesTextView);
        industriesTextView.setText(offer.getIndustries());

        if (myProfile.has("cv0") && (myProfile.getParseObject("cv0")).get("fileName") != null) {
            resumes.put(0, (myProfile.getParseObject("cv0")).getString("resumeName"));
        }
        if (myProfile.has("cv1") && ((ParseObject) myProfile.get("cv1")).get("fileName") != null) {
            resumes.put(1, (myProfile.getParseObject("cv0")).getString("fileName"));
        }
        if (myProfile.has("cv2") && ((ParseObject) myProfile.get("cv2")).get("fileName") != null) {
            resumes.put(2, (myProfile.getParseObject("cv0")).getString("fileName"));
        }
        if (myProfile.has("cv3") && ((ParseObject) myProfile.get("cv3")).get("fileName") != null) {
            resumes.put(3, (myProfile.getParseObject("cv0")).getString("fileName"));
        }
        if (myProfile.has("cv4") && ((ParseObject) myProfile.get("cv4")).get("fileName") != null) {
            resumes.put(4, (myProfile.getParseObject("cv0")).getString("fileName"));
        }


        final TextView cvClickableTextView = (TextView) findViewById(R.id.cvClickableTextView);
        cvClickableTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(resumes.size()==0)
                {
                    new AlertDialog.Builder(StudentApplyActivity.this).setTitle("No resume available").setMessage("You haven't uploaded any resume in your profile.\nPlease, go to your profile section and add at least a resume.").setNegativeButton("OK", null).create().show();

                } else {
                    final CharSequence[] resumeNames = resumes.values().toArray(new CharSequence[resumes.size()]);

                    AlertDialog.Builder builder = new AlertDialog.Builder(StudentApplyActivity.this);
                    builder.setTitle("Select a resume").setItems(resumeNames, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, final int which) {

                            cvClickableTextView.setText(resumeNames[which]);
                            selectedResume = (ParseObject) myProfile.get("cv" + which);
                            findViewById(R.id.openResumeButton).setVisibility(View.VISIBLE);
                            findViewById(R.id.openResumeButton).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    try {
                                        final ParseObject resume = selectedResume;

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
                                    }
                                }
                            });

                        }
                    });
                    builder.setNegativeButton("Cancel", null);
                    builder.create().show();

                }

            }
        });

        final TextView coverLetterClickableTextView = (TextView) findViewById(R.id.coverLetterClickableTextView);
        final ArrayList<ParseObject> coverLetters =myProfile.getCoverLetters();
        if(coverLetters==null || coverLetters.size()==0)
            coverLetterClickableTextView.setVisibility(View.GONE);
        else {
            coverLetterClickableTextView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    CharSequence[] coverLettersNames = new CharSequence[coverLetters.size()];
                    for(int i=0; i<coverLetters.size(); i++)
                        coverLettersNames[i]=coverLetters.get(i).getString("name");

                    AlertDialog.Builder builder = new AlertDialog.Builder(StudentApplyActivity.this);
                    builder.setTitle("Select a cover letter").setItems(coverLettersNames, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, final int which) {

                            coverLetterClickableTextView.setText(coverLetters.get(which).getString("name"));
                            ((EditText)findViewById(R.id.coverLetterText)).setText("");
                            ((EditText)findViewById(R.id.coverLetterText)).append(coverLetters.get(which).getString("content"));

                        }
                    });
                    builder.setNegativeButton("Cancel", null);
                    builder.create().show();

                }
            });
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

    public void apply(View v) {

        EditText coverLetterText = ((EditText) findViewById(R.id.coverLetterText));

        if(selectedResume==null){

            DialogManager.toastMessage("Please select a resume", this);

        } else {

            final Application application = new Application();
            application.setStudent(myProfile);
            application.setJobOffer(offer);
            application.setStatus("Submitted");
            EditText studentNotesEditText = (EditText) findViewById(R.id.studentNotesEditText);
            application.setStudentNotes(studentNotesEditText.getText().toString());
            application.setResume(selectedResume);
            application.setCoverLetter(coverLetterText.getText().toString());


            new AsyncTaskWithProgressBar(this) {
                @Override
                protected String doInBackground(Void... params) {
                    String resultMessage = OK_VALUE;
                    try {
                        application.save();
                    } catch (Exception e) {
                        e.printStackTrace();
                        resultMessage = KO_VALUE;
                    }
                    return resultMessage;
                }

                @Override
                protected void onPostExecute(String resultMessage) {
                    super.onPostExecute(null);
                    try {
                        Intent intent = new Intent(activity, ApplicationSentActivity.class);
                        startActivityForResult(intent, 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }.execute();

        }



    }
}
