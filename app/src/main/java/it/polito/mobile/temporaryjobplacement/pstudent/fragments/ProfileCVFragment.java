package it.polito.mobile.temporaryjobplacement.pstudent.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
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
import it.polito.mobile.temporaryjobplacement.model.Student;
import it.polito.mobile.temporaryjobplacement.pstudent.activities.StudentProfileActivity;

public class ProfileCVFragment extends Fragment {

    private static final int REQUEST_CHOOSER_ID = 1234;
    int numberOfResumes;


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

        rootView.findViewById(R.id.addResumeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create the ACTION_GET_CONTENT Intent
                Intent getContentIntent = FileUtils.createGetContentIntent();

                Intent intent = Intent.createChooser(getContentIntent, "Select a file");
                startActivityForResult(intent, REQUEST_CHOOSER_ID);

            }
        });
        numberOfResumes=0;

        try {
            Student studentProfile = ((StudentProfileActivity) getActivity()).getProfile();

            if(studentProfile.has("cv1") && ((ParseObject)studentProfile.get("cv1")).get("name")!=null) {
                showResume("cv1", rootView);
                numberOfResumes++;
            }
            if(studentProfile.has("cv2") && ((ParseObject)studentProfile.get("cv2")).get("name")!=null) {
                showResume("cv2", rootView);
                numberOfResumes++;
            }
            if(studentProfile.has("cv3") && ((ParseObject)studentProfile.get("cv3")).get("name")!=null) {
                showResume("cv3", rootView);
                numberOfResumes++;
            }
            if(studentProfile.has("cv4") && ((ParseObject)studentProfile.get("cv4")).get("name")!=null) {
                showResume("cv4", rootView);
                numberOfResumes++;
            }
            if(studentProfile.has("cv5") && ((ParseObject)studentProfile.get("cv5")).get("name")!=null) {
                showResume("cv5", rootView);
                numberOfResumes++;
            }

        }catch (Exception e){
            e.printStackTrace();
        }


        // Inflate the layout for this fragment
        return rootView;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
                                final String resumeStringId="cv"+(numberOfResumes+1);
                                ParseObject curriculum = new ParseObject("Curriculum");
                                curriculum.put("name", "CV ITA"+numberOfResumes);
                                curriculum.put("curriculum", new ParseFile(org.apache.commons.io.FileUtils.readFileToByteArray(file)));

                                ((StudentProfileActivity) getActivity()).getProfile().put(resumeStringId, curriculum);
                                getView().findViewById(R.id.addResumeButton).setVisibility(View.GONE);
                                getView().findViewById(R.id.progress_Resume).setVisibility(View.VISIBLE);
                                ((StudentProfileActivity)getActivity()).getProfile().saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        numberOfResumes++;
                                        showResume(resumeStringId, getView());
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

    private void showResume(final String resumeStringId, View rootView) {

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
                                getActivity().startActivity(intent);


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
}


