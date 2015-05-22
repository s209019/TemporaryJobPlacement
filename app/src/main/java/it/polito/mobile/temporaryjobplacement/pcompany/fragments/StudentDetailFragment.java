package it.polito.mobile.temporaryjobplacement.pcompany.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import it.polito.mobile.temporaryjobplacement.R;
import it.polito.mobile.temporaryjobplacement.commons.utils.AccountManager;
import it.polito.mobile.temporaryjobplacement.commons.utils.Connectivity;
import it.polito.mobile.temporaryjobplacement.commons.utils.ExternalIntents;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.CreateMenuItem;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.DialogManager;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.LargeBarAnimatedManager;
import it.polito.mobile.temporaryjobplacement.model.Application;
import it.polito.mobile.temporaryjobplacement.model.Company;
import it.polito.mobile.temporaryjobplacement.model.Education;
import it.polito.mobile.temporaryjobplacement.model.JobOffer;
import it.polito.mobile.temporaryjobplacement.model.Student;
import it.polito.mobile.temporaryjobplacement.pcompany.activities.SendMessageActivity;
import it.polito.mobile.temporaryjobplacement.pstudent.activities.StudentApplyActivity;
import it.polito.mobile.temporaryjobplacement.pstudent.activities.StudentDetailActivity;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link }
 * in two-pane mode (on tablets) or a {@link StudentDetailActivity}
 * on handsets.
 */
public class StudentDetailFragment extends Fragment  {

    Bitmap bitmapImage;
    List<Education> educations;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public StudentDetailFragment() {

    }



    private OnFragmentInteractionListener mListener;
    public interface OnFragmentInteractionListener {
        public void startEducationsFragment(List<Education> educations);
    }






    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_student_detail, container, false);



        //setting largeBarAnimated
                ((ActionBarActivity) getActivity()).getSupportActionBar().hide();
        final LargeBarAnimatedManager largeBarAnimatedManager=new LargeBarAnimatedManager(rootView,(ActionBarActivity)getActivity());

        ImageButton backButton=largeBarAnimatedManager.getBackButton();
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });


        ImageButton homeButton=largeBarAnimatedManager.getHomeButton();
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onOptionsItemSelected(CreateMenuItem.getMenuItem(R.id.action_HOME));
            }
        });


        //getting StudentID
        final String studentId=getArguments().getString("SELECTED_STUDENT");
        final Student[] student = {null};
        final Company[] myProfile = {null};
        final RelativeLayout loadingOverlay =(RelativeLayout)rootView.findViewById(R.id.loadingOverlay);
        loadingOverlay.setVisibility(View.VISIBLE);
        new AsyncTask<Object, Object, Object>(){
            @Override
            protected Object doInBackground(Object... params) {
                try {
                    student[0] = Student.getQuery().get(studentId);
                    myProfile[0] = AccountManager.getCurrentCompanyProfile();
                    boolean applicationDone = (Application.getQuery().whereEqualTo("jobOffer", student[0]).whereEqualTo("student",myProfile[0]).count()!=0);
                    List<Student> favourites=myProfile[0].getFavouriteStudents();
                    student[0].setFavourited(favourites.contains(student[0]));
                    bitmapImage=student[0].getPhoto(getActivity());
                    educations=student[0].getEducations();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
                return new Object();

            }

            @Override
            protected void onPostExecute(Object o) {
                try {
                    super.onPostExecute(o);
                    if (o == null) {
                        Connectivity.connectionError(getActivity());
                        return;
                    }

                    loadingOverlay.setVisibility(View.GONE);
                    initializeView(rootView, largeBarAnimatedManager, student[0], myProfile[0]);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.execute();



        return rootView;
    }




    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }





    private void initializeView(final View rootView,LargeBarAnimatedManager largeBarAnimatedManager,final Student student, final Company myProfile){
        //populate title and subtitle
        largeBarAnimatedManager.getTitleTextView().setText(student.getLastName().toUpperCase(Locale.ENGLISH));
        largeBarAnimatedManager.getSubTitleTextView().setText(student.getFirstName().toUpperCase(Locale.ENGLISH));

        //handle share button
        ImageButton shareButton=largeBarAnimatedManager.getShareButton();
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExternalIntents.share(getActivity(), student.getLastName()+" "+student.getFirstName(),
                        student.getEmail());
            }
        });

        //handle favourite button
        final RelativeLayout favouriteButton=largeBarAnimatedManager.getFavouriteButton();
        ((ImageButton)favouriteButton.getChildAt(0)).setImageResource(student.isFavourited() ? R.drawable.ic_action_important : R.drawable.ic_action_not_important);
        final ProgressBar progress=(ProgressBar)favouriteButton.getChildAt(1);
        favouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!student.isFavourited()) {
                        myProfile.getRelation("favouriteStudents").add(student);
                        progress.setVisibility(View.VISIBLE);
                        new AsyncTask<Object, Object, Boolean>() {
                            @Override
                            protected Boolean doInBackground(Object... params) {
                                try {
                                    myProfile.save();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    return false;
                                }
                                return true;
                            }

                            @Override
                            protected void onPostExecute(Boolean o) {
                                super.onPostExecute(o);
                                if (o == true) {
                                    progress.setVisibility(View.GONE);
                                    DialogManager.toastMessage("Favourite added", getActivity());
                                    student.setFavourited(true);
                                    ((ImageButton) favouriteButton.getChildAt(0)).setImageResource(R.drawable.ic_action_important);
                                }
                            }
                        }.execute();


                    } else {
                        myProfile.getRelation("favouriteStudents").remove(student);
                        progress.setVisibility(View.VISIBLE);
                        new AsyncTask<Object, Object, Boolean>() {
                            @Override
                            protected Boolean doInBackground(Object... params) {
                                try {
                                    myProfile.save();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    return false;
                                }
                                return true;
                            }

                            @Override
                            protected void onPostExecute(Boolean o) {
                                super.onPostExecute(o);
                                if (o == true) {
                                    progress.setVisibility(View.GONE);
                                    DialogManager.toastMessage("Favourite removed", getActivity());
                                    student.setFavourited(false);
                                    ((ImageButton) favouriteButton.getChildAt(0)).setImageResource(R.drawable.ic_action_not_important);
                                }
                            }
                        }.execute();

                    }
                }catch (Exception e){
                    e.printStackTrace();

                }
            }
        });






        TextView firstNameTextView=(TextView)rootView.findViewById(R.id.firstNameTextView);
        String name=student.getFirstName().substring(0,1).toUpperCase()+student.getFirstName().substring(1,student.getFirstName().length());
        firstNameTextView.setText(name);

        TextView lastNameTextView=(TextView)rootView.findViewById(R.id.lastNameTextView);
        name=student.getLastName().substring(0,1).toUpperCase()+student.getLastName().substring(1,student.getLastName().length());
        lastNameTextView.setText(name);

        TextView ageTextView=(TextView)rootView.findViewById(R.id.ageTextView);
        try {
            if (student.getAge().equals("")) throw new Exception();
            ageTextView.setText(student.getAge());
        }catch (Exception e){
            ageTextView.setText("Age not specified");
        }

        TextView degreeTextView=(TextView)rootView.findViewById(R.id.degreeTextView);
        try {
            if (student.getBestDegree().equals("")) throw new Exception();
            degreeTextView.setText(student.getBestDegree());
        }catch (Exception e ){
            degreeTextView.setText("Degree not specified");
        }


        TextView emailTextView=(TextView)rootView.findViewById(R.id.emailTextView);
        try {
            if (student.getEmail().equals("")) throw new Exception();
            emailTextView.setText(student.getEmail());
            ((LinearLayout)emailTextView.getParent()).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ExternalIntents.sendMail(getActivity(), student.getEmail());
                }
            });
        }catch (Exception e ){
            emailTextView.setText("Email not specified");
        }

        TextView phoneTextView=(TextView)rootView.findViewById(R.id.phoneTextView);
        try {
            if (student.getPhoneNumber().equals("")) throw new Exception();
            phoneTextView.setText(student.getPhoneNumber());
            ((LinearLayout)phoneTextView.getParent()).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ExternalIntents.call(getActivity(),student.getPhoneNumber());
                }
            });
        }catch (Exception e ){
            phoneTextView.setText("Phone number not specified");
        }


        TextView languageSkillsTextView=(TextView)rootView.findViewById(R.id.languageSkillsTextView);
        try {
            String text=student.getLanguageSkills().get(0);
            for(int i=1; i<student.getLanguageSkills().size();i++){
                text=text+"\n"+student.getLanguageSkills().get(i);
            }
            languageSkillsTextView.setText(text);
        }catch (Exception e ){
            languageSkillsTextView.setText("Language skills not specified");
        }



        TextView skillsTextView =(TextView)rootView.findViewById(R.id.skillsTextView);
        try {
            if (student.getSkills().equals("")) throw new Exception();
            skillsTextView.setText(student.getSkills());
        }catch (Exception e ){
            skillsTextView.setText("Skills not specified");
        }



        //manage your photo input
        ImageView profilePictureImage = (ImageView)rootView.findViewById(R.id.profilePicture);

        if(bitmapImage!=null){
            profilePictureImage.setImageBitmap(bitmapImage);
        }




        Button messageButton=(Button)rootView.findViewById(R.id.buttonsendMessage);
        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(getActivity(), SendMessageActivity.class);
                    intent.putExtra("SELECTED_STUDENT",student.getObjectId());
                    startActivityForResult(intent, 0);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });



        Button seeEducationsButton=(Button)rootView.findViewById(R.id.buttonSeeEducations);
        seeEducationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(educations==null || educations.size()==0){
                    DialogManager.toastMessage("No education specified",getActivity());
                    return;
                }
                    mListener.startEducationsFragment(educations);


            }
        });







/*
        RelativeLayout applyButton=(RelativeLayout)rootView.findViewById(R.id.buttonApply);
        if(!offer.isApplicationDone()) {

            if(!offer.isPublic()) {
                applyButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {}});
                applyButton.setBackgroundColor(getActivity().getResources().getColor(R.color.primaryColor));
                Button applyButtonText = (Button) rootView.findViewById(R.id.buttonApplyText);
                applyButtonText.setText("OFFER WITHDRAWN");

            }else {
                applyButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(StudentDetailFragment.this.getActivity(), StudentApplyActivity.class);
                        i.putExtra("SELECTED_OFFER", offer.getObjectId());
                        startActivityForResult(i, 0);
                    }
                });
            }

        } else {
            applyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });


            applyButton.setBackgroundColor(getActivity().getResources().getColor(R.color.primaryColor));
            Button applyButtonText = (Button) rootView.findViewById(R.id.buttonApplyText);
            applyButtonText.setText("ALREADY APPLIED");



        }

        */


    }


}
