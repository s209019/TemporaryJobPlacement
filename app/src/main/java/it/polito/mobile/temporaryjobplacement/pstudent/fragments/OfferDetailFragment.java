package it.polito.mobile.temporaryjobplacement.pstudent.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.parse.ParseException;

import it.polito.mobile.temporaryjobplacement.R;
import it.polito.mobile.temporaryjobplacement.commons.utils.ExternalIntents;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.DialogManager;
import it.polito.mobile.temporaryjobplacement.model.JobOffer;
import it.polito.mobile.temporaryjobplacement.pstudent.activities.StudentDetailActivity;
import it.polito.mobile.temporaryjobplacement.pstudent.model.Company;
import it.polito.mobile.temporaryjobplacement.pstudent.model.Offer;

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

        try {
            final JobOffer offer = JobOffer.getQuery().include("company").get(jobOfferId);

        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle("Offer by "+offer.getCompany());



        ImageButton shareButton=(ImageButton)rootView.findViewById(R.id.shareButton);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogManager.toastMessage("share", getActivity());
            }
        });

        /*
        final ImageButton favouriteButton=(ImageButton)rootView.findViewById(R.id.favouriteButton);
        favouriteButton.setBackgroundResource(offer.isFavourited() ? R.drawable.ic_action_important : R.drawable.ic_action_not_important);
        favouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!offer.isFavourited()) {
                    offer.setFavourited(true);
                    favouriteButton.setBackgroundResource(R.drawable.ic_action_important);
                } else {
                    offer.setFavourited(false);
                    favouriteButton.setBackgroundResource(R.drawable.ic_action_not_important);
                }
                DialogManager.toastMessage("favourite", getActivity());
            }
        });
        */



        TextView titleTextView=(TextView)rootView.findViewById(R.id.titleTextView);
        titleTextView.setText(offer.getName());

        TextView companyTextView=(TextView)rootView.findViewById(R.id.companyTextView1);
        companyTextView.setText(offer.getCompany().getName());
        LinearLayout companyLayout=(LinearLayout)rootView.findViewById(R.id.company_layout);
        companyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.startCompanyActivity(offer.getCompany().getName());
            }
        });

        TextView locationTextView=(TextView)rootView.findViewById(R.id.locationTextView);
        locationTextView.setText(offer.getLocation());

        TextView timeAgoTextView=(TextView)rootView.findViewById(R.id.timeAgoTextView);
        timeAgoTextView.setText(offer.getCreatedAt().toString());

        TextView positionTextView=(TextView)rootView.findViewById(R.id.positionTextView);
        positionTextView.setText(offer.getPosition());

        TextView educationTextView=(TextView)rootView.findViewById(R.id.educationTextView);
        educationTextView.setText(offer.getEducation());

        TextView careerLevelTextView=(TextView)rootView.findViewById(R.id.careerLevelTextView);
        careerLevelTextView.setText(offer.getCareerLevel());

        TextView descriptionTextView=(TextView)rootView.findViewById(R.id.descriptionTextView);
        descriptionTextView.setText(offer.getDescription());


        LinearLayout displayPositionButton=(LinearLayout)rootView.findViewById(R.id.location_layout);
        displayPositionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExternalIntents.openGoogleMaps(getActivity(),offer.getLocation());
            }
        });







        RelativeLayout applyButton=(RelativeLayout)rootView.findViewById(R.id.buttonApply);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 DialogManager.toastMessage("APPLY",getActivity());
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
