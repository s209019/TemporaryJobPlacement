package it.polito.mobile.temporaryjobplacement.pstudent.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ipaulpro.afilechooser.utils.FileUtils;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import it.polito.mobile.temporaryjobplacement.R;
import it.polito.mobile.temporaryjobplacement.model.Student;

public class ProfileCVFragment extends Fragment {

    public static final int REQUEST_CHOOSER_ID = 1234;
    boolean resumes[] = new boolean[5]; //true in posizione 0 se cv1 esiste, false se cv1 non esiste
    Student studentProfile;
    final ArrayList<LinearLayout> resumeLayouts = new ArrayList<>();


    private AtomicInteger viewInitialized= new AtomicInteger(0);



    private ProfileCVFragment.Callbacks callbacks = null;
    public interface Callbacks {
        /*
        *get profile
        */
        Student getProfile();
        //void detachAllFragments();
    }




    public static Fragment newInstance() {
        ProfileCVFragment fragment = new ProfileCVFragment();
        return fragment;
    }

    public ProfileCVFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_profile_cv, container, false);


        //PROGRESSIVE WAIT IF NECESSARY
        new AsyncTask<Object, Object, Object>() {
            @Override
            protected Object doInBackground(Object... params) {
                //max 30s timeout(maggiore di quello di parse)
                for(int i=1;i<11;i++ ){
                    studentProfile=callbacks.getProfile();
                    if( studentProfile!=null) return new Object();
                    try { Thread.sleep (500*i); } catch (InterruptedException e) { }
                }
                return null;
            }
            @Override
            protected void onPostExecute(Object o) {
                if(o==null)return;
                initializeView(rootView);

                viewInitialized.set(1);

            }}.execute();




        return rootView;
    }



    private void initializeView(View rootView){
        rootView.findViewById(R.id.addResumeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create the ACTION_GET_CONTENT Intent
                Intent getContentIntent = FileUtils.createGetContentIntent();

                Intent intent = Intent.createChooser(getContentIntent, "Select a file");
                startActivityForResult(intent, REQUEST_CHOOSER_ID);

            }
        });

        //manage language skills input

        resumeLayouts.add((LinearLayout) rootView.findViewById(R.id.resumeLayout1));
        resumeLayouts.add((LinearLayout) rootView.findViewById(R.id.resumeLayout2));
        resumeLayouts.add((LinearLayout) rootView.findViewById(R.id.resumeLayout3));
        resumeLayouts.add((LinearLayout) rootView.findViewById(R.id.resumeLayout4));
        resumeLayouts.add((LinearLayout) rootView.findViewById(R.id.resumeLayout5));

        //handle delete button of each language
        for(int position=0;position<resumeLayouts.size();position++){
            final int finalPosition = position;
            ((ImageButton)resumeLayouts.get(position).getChildAt(1)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeResume(finalPosition);
                }
            });
        }


/*        final InsertLanguageDialogFragment.Callbacks callbacks=new InsertLanguageDialogFragment.Callbacks() {
            @Override
            public void onLanguageInserted(String language) {
                if(languages.size()<languageLayouts.length){
                    languages.add(language);
                    languageLayouts[languages.size()-1].setVisibility(View.VISIBLE);
                    ((TextView)languageLayouts[languages.size() - 1].getChildAt(0)).setText(language);
                    if(languages.size()==languageLayouts.length)addLanguageSkillsButton.setVisibility(View.INVISIBLE);
                    addLanguageSkills(myProfile,language);
                }
            }
        };
        addLanguageSkillsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment df = InsertLanguageDialogFragment.newInstance("Insert language and level:", callbacks);
                df.show(getActivity().getSupportFragmentManager(), "MyDialog");
            }
        });

*/

        for(int i=0; i<resumes.length; i++)
            resumes[i]=false;


        try {


            if (studentProfile.has("cv0") && (studentProfile.getParseObject("cv0")).get("name") != null) {
                showResume(rootView, 0);
            }
            if (studentProfile.has("cv1") && ((ParseObject) studentProfile.get("cv1")).get("name") != null) {
                showResume(rootView, 1);
            }
            if (studentProfile.has("cv2") && ((ParseObject) studentProfile.get("cv2")).get("name") != null) {
                showResume(rootView, 2);
            }
            if (studentProfile.has("cv3") && ((ParseObject) studentProfile.get("cv3")).get("name") != null) {
                showResume(rootView, 3);
            }
            if (studentProfile.has("cv4") && ((ParseObject) studentProfile.get("cv4")).get("name") != null) {
                showResume(rootView, 4);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void removeResume(final int resumeNumber){

        studentProfile.getParseObject("cv"+resumeNumber).deleteEventually();
        studentProfile.remove("cv" + resumeNumber);
        studentProfile.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                resumes[resumeNumber]=false;
                resumeLayouts.get(resumeNumber).setVisibility(View.GONE);
                getView().findViewById(R.id.addResumeButton).setVisibility(View.VISIBLE);

            }
        });

    }



    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //PROGRESSIVE WAIT IF NECESSARY
        new AsyncTask<Object, Object, Object>() {
            @Override
            protected Object doInBackground(Object... params) {
                //max 30s timeout(maggiore di quello di parse)
                for(int i=1;i<11;i++ ){
                    if(viewInitialized.compareAndSet(1,1))return new Object();
                    try { Thread.sleep (500*i); } catch (InterruptedException e) { }
                }
                return null;
            }
            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if(o==null)return;
                performOnActivityResult(requestCode, resultCode, data);

            }}.execute();


    }


    void performOnActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHOOSER_ID:
                if (resultCode == Activity.RESULT_OK) {

                    final Uri uri = data.getData();

                    // Get the File path from the Uri
                    String path = FileUtils.getPath(getActivity(), uri);

                    // Alternatively, use FileUtils.getFile(Context, Uri)
                    if (path != null && FileUtils.isLocal(path)) {
                        File file = new File(path);

                        if (file.toString().contains(".doc") ||
                                file.toString().contains(".docx") ||
                                file.toString().contains(".pdf") ||
                                file.toString().contains(".rtf") ||
                                file.toString().contains(".txt")) {

                            try {
                                final int resumeNumber = getFirstFreeResumeNumber();
                                final String resumeStringId="cv"+resumeNumber;
                                ParseObject curriculum = new ParseObject("Curriculum");
                                curriculum.put("name", "CV ITA"+resumeNumber);
                                curriculum.put("curriculum", new ParseFile(org.apache.commons.io.FileUtils.readFileToByteArray(file)));

                                studentProfile.put(resumeStringId, curriculum);
                                getView().findViewById(R.id.addResumeButton).setVisibility(View.GONE);
                                getView().findViewById(R.id.progress_Resume).setVisibility(View.VISIBLE);
                                studentProfile.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        getView().findViewById(R.id.addResumeButton).setVisibility(View.VISIBLE);
                                        showResume(getView(),resumeNumber);
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            new AlertDialog.Builder(getActivity()).setTitle("ERROR").setMessage("File not allowed!\nFile extensions allowed: doc, docx, pdf, rtf, txt").setNegativeButton("Chiudi", null).create().show();
                        }


                    }
                }
                break;
        }

    }

    private int getFirstFreeResumeNumber(){
        for(int i=0; i<resumes.length; i++)
            if(!resumes[i])
                return i;

        return -1;
    }

    private void showResume(View rootView, int resumeNumber) {

        try {

            final String resumeStringId="cv"+resumeNumber;

            resumes[resumeNumber]=true;

            final ParseObject resume = (ParseObject)studentProfile.get(resumeStringId);
            LinearLayout linearLayout = resumeLayouts.get(resumeNumber);

            String resumeName= (String) resume.get("name");
            linearLayout.setVisibility(View.VISIBLE);
            ((TextView)linearLayout.getChildAt(0)).setText(resumeName);

            TextView resumeTextView = ((TextView)linearLayout.getChildAt(0));
            resumeTextView.setText(resumeName);
            rootView.findViewById(R.id.progress_Resume).setVisibility(View.GONE);
            if(getFirstFreeResumeNumber()==-1){
                rootView.findViewById(R.id.addResumeButton).setVisibility(View.GONE);
            }
            resumeTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        ((ParseFile) resume.get("curriculum")).getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] bytes, ParseException e) {

                                String filename = resumeStringId + ".pdf";
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
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }
        callbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // Reset the active callbacks interface to the dummy implementation.
        callbacks = null;
    }
}


