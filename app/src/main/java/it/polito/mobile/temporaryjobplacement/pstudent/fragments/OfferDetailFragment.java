package it.polito.mobile.temporaryjobplacement.pstudent.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.parse.ParseException;

import it.polito.mobile.temporaryjobplacement.R;
import it.polito.mobile.temporaryjobplacement.commons.utils.AccountManager;
import it.polito.mobile.temporaryjobplacement.commons.utils.ExternalIntents;
import it.polito.mobile.temporaryjobplacement.commons.utils.TimeManager;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.DialogManager;
import it.polito.mobile.temporaryjobplacement.model.JobOffer;
import it.polito.mobile.temporaryjobplacement.model.Student;
import it.polito.mobile.temporaryjobplacement.pstudent.activities.StudentDetailActivity;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link }
 * in two-pane mode (on tablets) or a {@link StudentDetailActivity}
 * on handsets.
 */
public class OfferDetailFragment extends Fragment {
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public OfferDetailFragment() {
    }



    private OnFragmentInteractionListener mListener;
    public interface OnFragmentInteractionListener {
        public void startCompanyActivity(String companyName);
    }






    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_offer_detail, container, false);

        final String jobOfferId=getArguments().getString("SELECTED_OFFER");
        boolean isFavourited=getArguments().getBoolean("IS_FAVOURITED");

        try {
            final JobOffer offer = JobOffer.getQuery().include("company").get(jobOfferId);
            Log.d("DEBUG", isFavourited+"");
            offer.setFavourited(isFavourited);

        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(offer.getName());

        ImageButton shareButton=(ImageButton)rootView.findViewById(R.id.shareButton);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogManager.toastMessage("share", getActivity());
            }
        });

        final ImageButton favouriteButton=(ImageButton)rootView.findViewById(R.id.favouriteButton);
        favouriteButton.setBackgroundResource(offer.isFavourited() ? R.drawable.ic_action_important : R.drawable.ic_action_not_important);
        favouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!offer.isFavourited()) {
                    offer.setFavourited(true);
                    favouriteButton.setBackgroundResource(R.drawable.ic_action_important);
                    try {
                        Student myProfile = AccountManager.getCurrentStudentProfile();
                        myProfile.getRelation("favouritesOffers").add(offer);
                        myProfile.saveEventually();
                        DialogManager.toastMessage("PREFERITO AGGIUNTO", getActivity());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    offer.setFavourited(false);
                    favouriteButton.setBackgroundResource(R.drawable.ic_action_not_important);
                    try {
                        Student myProfile = AccountManager.getCurrentStudentProfile();
                        myProfile.getRelation("favouritesOffers").remove(offer);
                        myProfile.saveEventually();
                        DialogManager.toastMessage("PREFERITO RIMOSSO", getActivity());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });



        TextView titleTextView=(TextView)rootView.findViewById(R.id.titleTextView);
        titleTextView.setText(offer.getName());

        TextView companyTextView=(TextView)rootView.findViewById(R.id.companyTextView1);
        companyTextView.setText(offer.getCompany().getName().toUpperCase());
        LinearLayout companyLayout=(LinearLayout)rootView.findViewById(R.id.company_layout);
        companyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.startCompanyActivity(offer.getCompany().getName());
            }
        });

        TextView locationTextView=(TextView)rootView.findViewById(R.id.locationTextView);
        locationTextView.setText(offer.getFullLocation());

        TextView timeAgoTextView=(TextView)rootView.findViewById(R.id.timeAgoTextView);
        timeAgoTextView.setText(TimeManager.getFormattedDate(offer.getCreatedAt()));

        TextView positionTextView=(TextView)rootView.findViewById(R.id.positionTextView);
        positionTextView.setText(offer.getPosition());

        TextView educationTextView=(TextView)rootView.findViewById(R.id.educationTextView);
        educationTextView.setText(offer.getEducation());

        TextView careerLevelTextView=(TextView)rootView.findViewById(R.id.careerLevelTextView);
        careerLevelTextView.setText(offer.getCareerLevel());

        TextView descriptionTextView=(TextView)rootView.findViewById(R.id.descriptionTextView);
        descriptionTextView.setText(offer.getDescription());

        TextView responsibilitiesTextView =(TextView)rootView.findViewById(R.id.responsibilitiesTextView);
        responsibilitiesTextView.setText(offer.getResponsibilities());

        TextView minimumQualificationsTextView =(TextView)rootView.findViewById(R.id.minimumQualificationsTextView);
            minimumQualificationsTextView.setText(offer.getMinimumQualifications());

        TextView preferredQualificationsTextView =(TextView)rootView.findViewById(R.id.preferredQualificationsTextView);
            preferredQualificationsTextView.setText(offer.getPreferredQualifications());


        LinearLayout displayPositionButton=(LinearLayout)rootView.findViewById(R.id.location_layout);
        displayPositionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExternalIntents.openGoogleMaps(getActivity(),offer.getFullLocation());
            }
        });

        RelativeLayout applyButton=(RelativeLayout)rootView.findViewById(R.id.buttonApply);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Student myProfile = AccountManager.getCurrentStudentProfile();
                    myProfile.getRelation("jobsApplied").add(offer);
                    myProfile.saveEventually();
                    DialogManager.toastMessage("APPLY", getActivity());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        } catch (ParseException e) {
            e.printStackTrace();
        }



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

}
