package it.polito.mobile.temporaryjobplacement.pstudent.fragments;

import android.app.Activity;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.zip.Inflater;

import it.polito.mobile.temporaryjobplacement.R;
import it.polito.mobile.temporaryjobplacement.TemporaryJobPlacementApp;
import it.polito.mobile.temporaryjobplacement.commons.utils.TimeManager;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.DialogManager;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.SavableEditText;
import it.polito.mobile.temporaryjobplacement.model.Education;
import it.polito.mobile.temporaryjobplacement.model.Message;
import it.polito.mobile.temporaryjobplacement.model.Student;

public class ProfileEducationFragment extends Fragment   {
  private ImageView V_education;
    private ProgressBar pro_Education;
    Student studentProfile;
    List<Education> educations;


    private ProfileEducationFragment.Callbacks callbacks = null;
    public interface Callbacks {
        /*
        *get profile
        */
        Student getProfile();
        List<Education> getEducations();
    }


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
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       final View rootView=inflater.inflate(R.layout.fragment_profile_education, container, false);

        V_education=(ImageView)rootView.findViewById(R.id.V_Education);
        pro_Education=(ProgressBar)rootView.findViewById(R.id.pro_Education);

        //PROGRESSIVE WAIT IF NECESSARY
        new AsyncTask<Object, Object, Object>() {
            @Override
            protected Object doInBackground(Object... params) {
                //max 30s timeout(maggiore di quello di parse)
                for(int i=1;i< TemporaryJobPlacementApp.TIMEOUT_ITERATIONS;i++ ){
                    studentProfile=callbacks.getProfile();
                    if( studentProfile!=null) return new Object();
                    try { Thread.sleep (TemporaryJobPlacementApp.TIMEOUT_MILLIS*i); } catch (InterruptedException e) { }
                }
                return null;
            }
            @Override
            protected void onPostExecute(Object o) {
                try {
                    if(o==null)return;
                    educations= callbacks.getEducations();
                    intializeView(rootView, inflater);
                }catch (Exception e){
                    e.printStackTrace();
                }


            }}.execute();
        return rootView;
    }



    private void intializeView(final View rootView,final LayoutInflater inflater) {

        final ListView listView=(ListView)rootView.findViewById(R.id.educationList);
        ArrayAdapter<Education> educationArrayAdapter=new ArrayAdapter<Education>(getActivity(),R.layout.education_layout){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = View.inflate(getContext(),R.layout.education_layout, null);
                }

                final Education education=this.getItem(position);
                ((TextView)convertView.findViewById(R.id.degreeTextView)).setText(education.getDegree());
                ((TextView)convertView.findViewById(R.id.courseTextView)).setText(education.getCourse());
                ((TextView)convertView.findViewById(R.id.universityTextView)).setText(education.getUniversity());
                ((TextView)convertView.findViewById(R.id.markTextView)).setText(education.getMark());
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                ((TextView)convertView.findViewById(R.id.periodTextView)).setText(df.format(education.getFromDate())+" - "+ df.format(education.getToDate()));

                //handle delete button
                ((ImageButton)convertView.findViewById(R.id.deleteButton)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteEducation(education,listView);
                    }
                });


                return convertView;
            }
        };


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                final Education education = (Education) listView.getAdapter().getItem(position);
                final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                DialogFragment dialogF = AddEducationDialogFragment.newInstance(education.getDegree(), education.getCourse(), education.getUniversity(), education.getMark(), df.format(education.getFromDate()), df.format(education.getToDate()),"EDIT"
                        ,new AddEducationDialogFragment.Callbacks() {
                            @Override
                            public void onInfoLanguageInserted(final String updatedDegree, final String updatedCourse, final String updatedUniversity, final String updatedMark, final Date updatedFrom, final Date updatedTo) {
                                ((TextView) view.findViewById(R.id.degreeTextView)).setText(updatedDegree);
                                ((TextView) view.findViewById(R.id.courseTextView)).setText(updatedCourse);
                                ((TextView) view.findViewById(R.id.universityTextView)).setText(updatedUniversity);
                                ((TextView) view.findViewById(R.id.markTextView)).setText(updatedMark);
                                ((TextView) view.findViewById(R.id.periodTextView)).setText(df.format(updatedFrom) + " - " + df.format(updatedTo));

                                education.setDegree(updatedDegree);
                                education.setCourse(updatedCourse);
                                education.setUniversity(updatedUniversity);
                                education.setMark(updatedMark);
                                education.setFromDate(updatedFrom);
                                education.setToDate(updatedTo);


                                ((ArrayAdapter<Education>) listView.getAdapter()).notifyDataSetChanged();



                                updateEducation(education);
                                listView.setSmoothScrollbarEnabled(true);
                                listView.smoothScrollToPosition(position);


                            }
                        });

                dialogF.show(getActivity().getSupportFragmentManager(), "MyDialog");

            }


        });

        educationArrayAdapter.addAll(educations);
        listView.setAdapter(educationArrayAdapter);
        listView.setSelection(0);


        final RelativeLayout addEducation=(RelativeLayout)rootView.findViewById(R.id.addButton);
        addEducation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment df = AddEducationDialogFragment.newInstance("ADD",new AddEducationDialogFragment.Callbacks() {
                    @Override
                    public void onInfoLanguageInserted(String degree, String course, String university, String mark, Date from, Date to) {
                        Education education = new Education();
                        education.setDegree(degree);
                        education.setCourse(course);
                        education.setUniversity(university);
                        education.setMark(mark);
                        education.setFromDate(from);
                        education.setToDate(to);
                        ((ArrayAdapter<Education>) listView.getAdapter()).add(education);

                        addEducation(education, listView);


                    }
                });
                df.show(getActivity().getSupportFragmentManager(), "MyDialog");
            }
        });




    }

    private void addEducation(Education education, final ListView listView) {

        V_education.setVisibility(View.GONE);
        pro_Education.setVisibility(View.VISIBLE);
        studentProfile.addEducation(education,new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    DialogManager.toastMessage("education added", getActivity(), "center", true);
                    if (pro_Education != null) pro_Education.setVisibility(View.GONE);
                    if (V_education != null) V_education.setVisibility(View.VISIBLE);
                    if (((ArrayAdapter<Education>)listView.getAdapter())!=null){
                        ((ArrayAdapter<Education>)listView.getAdapter()).notifyDataSetChanged();
                    }

                } else {
                    DialogManager.toastMessage("" + e.getMessage(), getActivity(), "center", true);
                }
            }
        });

    }

    private void deleteEducation(final Education education, final ListView listView) {

        V_education.setVisibility(View.GONE);
        pro_Education.setVisibility(View.VISIBLE);
        studentProfile.deleteEducation(education, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    DialogManager.toastMessage("education deleted", getActivity(), "center", true);
                    if (pro_Education != null) pro_Education.setVisibility(View.GONE);
                    if (V_education != null) V_education.setVisibility(View.VISIBLE);
                    if (((ArrayAdapter<Education>) listView.getAdapter()) != null) {
                        ((ArrayAdapter<Education>) listView.getAdapter()).remove(education);
                        ((ArrayAdapter<Education>) listView.getAdapter()).notifyDataSetChanged();
                    }

                } else {
                    DialogManager.toastMessage("" + e.getMessage(), getActivity(), "center", true);
                }
            }
        });
    }


    private void updateEducation(Education education) {

        V_education.setVisibility(View.GONE);
        pro_Education.setVisibility(View.VISIBLE);
        studentProfile.updateEducation(education, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    DialogManager.toastMessage("education updated", getActivity(), "center", true);
                    if (pro_Education != null) pro_Education.setVisibility(View.GONE);
                    if (V_education != null) V_education.setVisibility(View.VISIBLE);
                } else {
                    DialogManager.toastMessage("" + e.getMessage(), getActivity(), "center", true);
                }
            }
        });

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
