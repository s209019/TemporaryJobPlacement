package it.polito.mobile.temporaryjobplacement.pstudent.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import it.polito.mobile.temporaryjobplacement.R;
import it.polito.mobile.temporaryjobplacement.commons.utils.ExternalIntents;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.CreateMenuItem;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.DialogManager;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.LargeBarAnimatedManager;
import it.polito.mobile.temporaryjobplacement.pstudent.activities.StudentDetailActivity;
import it.polito.mobile.temporaryjobplacement.pstudent.model.Company;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link }
 * in two-pane mode (on tablets) or a {@link StudentDetailActivity}
 * on handsets.
 */
public class CompanyDetailFragment extends Fragment {
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CompanyDetailFragment() {
    }


    private OnFragmentInteractionListener mListener;
    public interface OnFragmentInteractionListener {
        public void startOffersActivity(String companyName);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_company_detail, container, false);

        ((ActionBarActivity)getActivity()).getSupportActionBar().hide();
        LargeBarAnimatedManager largeBarAnimatedManager=new LargeBarAnimatedManager(rootView,(ActionBarActivity)getActivity());

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
        if(true)return rootView;
        final Company  company=getArguments().getParcelable("SELECTED_COMPANY");




        final ImageButton favouriteButton=(ImageButton)rootView.findViewById(R.id.favouriteButton);
        favouriteButton.setImageResource(company.isFavourited() ? R.drawable.ic_action_important : R.drawable.ic_action_not_important);
        favouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!company.isFavourited()) {
                    company.setFavourited(true);
                    favouriteButton.setImageResource(R.drawable.ic_action_important);
                } else {
                    company.setFavourited(false);
                    favouriteButton.setImageResource(R.drawable.ic_action_not_important);
                }
                DialogManager.toastMessage("favourite", getActivity());
            }
        });








        TextView locationTextView=(TextView)rootView.findViewById(R.id.locationTextView);
        locationTextView.setText(company.getLocation());



        TextView emailTextView=(TextView)rootView.findViewById(R.id.emailTextView);
        emailTextView.setText(company.getEmail());

        TextView phoneTextView=(TextView)rootView.findViewById(R.id.phoneTextView);
        phoneTextView.setText(company.getPhone());


        TextView industriesTextView=(TextView)rootView.findViewById(R.id.industriesTextView);
        industriesTextView.setText(getTextFromList(company.getIndustries()));






        LinearLayout sendEmailButton=(LinearLayout)rootView.findViewById(R.id.mail_layout);
        sendEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExternalIntents.sendMail(getActivity(), company.getEmail());
            }
        });
        LinearLayout callButton=(LinearLayout)rootView.findViewById(R.id.phone_layout);
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExternalIntents.call(getActivity(), company.getPhone());
            }
        });
        LinearLayout displayPositionButton=( LinearLayout)rootView.findViewById(R.id.location_layout);
        displayPositionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExternalIntents.openGoogleMaps(getActivity(), company.getLocation());
            }
        });



        Button buttonSeeOffers=(Button)rootView.findViewById(R.id.buttonSeeOffers);
        buttonSeeOffers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.startOffersActivity(company.getTitle());
            }
        });



        RelativeLayout sendMessageButton=(RelativeLayout)rootView.findViewById(R.id.sendMessage);
        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 DialogManager.toastMessage("send Message",getActivity());
            }
        });


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




    private String getTextFromList(List<String> list){
        String text="";
        for(String industry : list) text=text+industry+"\n";
        if(!text.equals(""))text=text.substring(0,text.length()-1);
        return text;
    }

}
