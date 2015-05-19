package it.polito.mobile.temporaryjobplacement.pstudent.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.zip.Inflater;

import it.polito.mobile.temporaryjobplacement.R;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.DialogManager;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.SavableEditText;

public class ProfileEducationFragment extends Fragment   {
  private ImageView V_education;
    private ProgressBar pro_Education;
    ArrayList<LinearLayout> educationLayouts=new ArrayList<LinearLayout>();


    public static Fragment newInstance() {
        ProfileEducationFragment fragment = new ProfileEducationFragment();
        return fragment;
    }

    public ProfileEducationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View rootView=inflater.inflate(R.layout.fragment_profile_education, container, false);

        V_education=(ImageView)rootView.findViewById(R.id.V_Education);
        pro_Education=(ProgressBar)rootView.findViewById(R.id.pro_Education);


        intializeView(rootView,inflater);

        return rootView;
    }



    private void intializeView(final View rootView,final LayoutInflater inflater) {

        final LinearLayout panelEducation=(LinearLayout)rootView.findViewById(R.id.educationsPanel);

        final AddEducationDialogFragment.Callbacks callback =new AddEducationDialogFragment.Callbacks() {
            @Override
            public void onInfoLanguageInserted(String degree, String course, String university, String mark, Date from, Date to) {

                LinearLayout educationLayout=(LinearLayout)inflater.inflate(R.layout.education_layout, null);
                ((TextView)educationLayout.findViewById(R.id.degreeTextView)).setText(degree);
                ((TextView)educationLayout.findViewById(R.id.courseTextView)).setText(course);
                ((TextView)educationLayout.findViewById(R.id.universityTextView)).setText(university);
                ((TextView)educationLayout.findViewById(R.id.markTextView)).setText(mark);
                 SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                ((TextView)educationLayout.findViewById(R.id.periodTextView)).setText(df.format(from)+" - "+ df.format(to));


                //handle delete button
                //(ImageButton)rootView.get


                panelEducation.addView(educationLayout);
                educationLayouts.add(educationLayout);
                addEducation(degree, course, university, mark, from, to);
            }
        };

        Button addEducation=(Button)rootView.findViewById(R.id.addEntry);
        addEducation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment df = AddEducationDialogFragment.newInstance(callback);
                df.show(getActivity().getSupportFragmentManager(), "MyDialog");
            }
        });


    }

    private void addEducation(String degree, String course, String university, String mark, Date from, Date to) {



        V_education.setVisibility(View.GONE);
        pro_Education.setVisibility(View.VISIBLE);
        /*myProfile.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null){
                    DialogManager.toastMessage("First name updated", getActivity(), "center", true);
                    if(pro_Education!=null) pro_Education.setVisibility(View.GONE);
                    if(V_education!=null) V_education.setVisibility(View.VISIBLE);
                } else {
                    DialogManager.toastMessage("" + e.getMessage(), getActivity(), "center", true);
                }
            }
        });*/


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
