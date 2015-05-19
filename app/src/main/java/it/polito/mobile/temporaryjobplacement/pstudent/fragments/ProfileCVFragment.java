package it.polito.mobile.temporaryjobplacement.pstudent.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ipaulpro.afilechooser.utils.FileUtils;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import it.polito.mobile.temporaryjobplacement.R;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.DialogManager;
import it.polito.mobile.temporaryjobplacement.model.Student;
import it.polito.mobile.temporaryjobplacement.pstudent.activities.StudentProfileActivity;

public class ProfileCVFragment extends Fragment {

    int numberOfResumes;

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

        View rootView = inflater.inflate(R.layout.fragment_profile_cv, container, false);

        if(callbacks.getProfile()==null) {
            FragmentTransaction fragTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragTransaction.remove(this);
            fragTransaction.commit();

        } else {

        Student studentProfile = callbacks.getProfile();


        rootView.findViewById(R.id.addResumeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create the ACTION_GET_CONTENT Intent
                Intent getContentIntent = FileUtils.createGetContentIntent();

                Intent intent = Intent.createChooser(getContentIntent, "Select a file");
                getActivity().startActivityForResult(intent, StudentProfileActivity.REQUEST_CHOOSER_ID);
                //callbacks.detachAllFragments();

            }
        });
        ((StudentProfileActivity) getActivity()).setNumberOfResumes(0);

        try {


            if (studentProfile.has("cv1") && ((ParseObject) studentProfile.get("cv1")).get("name") != null) {
                showResume("cv1", rootView);
                ((StudentProfileActivity) getActivity()).setNumberOfResumes(1);
            }
            if (studentProfile.has("cv2") && ((ParseObject) studentProfile.get("cv2")).get("name") != null) {
                Log.d("PROVA", "Entrato cv2");
                showResume("cv2", rootView);
                ((StudentProfileActivity) getActivity()).setNumberOfResumes(2);
            }
            if (studentProfile.has("cv3") && ((ParseObject) studentProfile.get("cv3")).get("name") != null) {
                showResume("cv3", rootView);
                ((StudentProfileActivity) getActivity()).setNumberOfResumes(3);
            }
            if (studentProfile.has("cv4") && ((ParseObject) studentProfile.get("cv4")).get("name") != null) {
                showResume("cv4", rootView);
                ((StudentProfileActivity) getActivity()).setNumberOfResumes(4);
            }
            if (studentProfile.has("cv5") && ((ParseObject) studentProfile.get("cv5")).get("name") != null) {
                showResume("cv5", rootView);
                ((StudentProfileActivity) getActivity()).setNumberOfResumes(5);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
        // Inflate the layout for this fragment
        return rootView;
    }




    public void showResume(final String resumeStringId, View rootView) {

        try {
            final ParseObject resume = (ParseObject)((StudentProfileActivity) getActivity()).getProfile().get(resumeStringId);

            Log.d("DEBUG",resumeStringId);
            String resumeName= (String) resume.get("name");
            TextView resumeTextView = ((TextView)rootView.findViewById(getActivity().getResources().getIdentifier(resumeStringId + "NameTextView", "id", getActivity().getPackageName())));
            resumeTextView.setText(resumeName);
            rootView.findViewById(getActivity().getResources().getIdentifier(resumeStringId + "Layout", "id", getActivity().getPackageName())).setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.progress_Resume).setVisibility(View.GONE);
            if(!resumeStringId.equals("cv5")){
                rootView.findViewById(R.id.addResumeButton).setVisibility(View.VISIBLE);
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


