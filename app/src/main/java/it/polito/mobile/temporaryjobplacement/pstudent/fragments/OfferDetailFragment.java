package it.polito.mobile.temporaryjobplacement.pstudent.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
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


import it.polito.mobile.temporaryjobplacement.R;
import it.polito.mobile.temporaryjobplacement.commons.utils.AccountManager;
import it.polito.mobile.temporaryjobplacement.commons.utils.ExternalIntents;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.CreateMenuItem;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.DialogManager;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.LargeBarAnimatedManager;
import it.polito.mobile.temporaryjobplacement.model.Application;
import it.polito.mobile.temporaryjobplacement.model.JobOffer;
import it.polito.mobile.temporaryjobplacement.model.Student;
import it.polito.mobile.temporaryjobplacement.pstudent.activities.StudentApplyActivity;
import it.polito.mobile.temporaryjobplacement.pstudent.activities.StudentDetailActivity;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link }
 * in two-pane mode (on tablets) or a {@link StudentDetailActivity}
 * on handsets.
 */
public class OfferDetailFragment extends Fragment  {

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

        final View rootView = inflater.inflate(R.layout.fragment_offer_detail, container, false);


        //setting largeBarAnimated
        ((ActionBarActivity)getActivity()).getSupportActionBar().hide();
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


        //getting jobOfferID
        final String jobOfferId=getArguments().getString("SELECTED_OFFER");
        final boolean isFavourited=getArguments().getBoolean("IS_FAVOURITED");
        final JobOffer[] offer = {null};
        final Student[] myProfile = {null};
        final RelativeLayout loadingOverlay =(RelativeLayout)rootView.findViewById(R.id.loadingOverlay);
        loadingOverlay.setVisibility(View.VISIBLE);
        new AsyncTask<Object, Object, Object>(){
           @Override
           protected Object doInBackground(Object... params) {
               try {
                   offer[0] = JobOffer.getQuery().include("company").get(jobOfferId);
                   myProfile[0] = AccountManager.getCurrentStudentProfile();
                   boolean applicationDone = Application.getQuery().whereEqualTo("student", myProfile).whereEqualTo("jobOffer", offer[0]).count()!=0;
                   offer[0].setFavourited(isFavourited);
                   offer[0].setApplicationDone(applicationDone);
               } catch (Exception e) {
                   e.printStackTrace();
               }
                return null;
           }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                loadingOverlay.setVisibility(View.GONE);
                initializeView(rootView, largeBarAnimatedManager, offer[0], myProfile[0]);
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






    private void initializeView(final View rootView,LargeBarAnimatedManager largeBarAnimatedManager,final JobOffer offer, final Student myProfile){
        //populate title and subtitle
        largeBarAnimatedManager.getTitleTextView().setText(offer.getName());
        largeBarAnimatedManager.getSubTitleTextView().setText(offer.getCompany().getName());

        //handle share button
        ImageButton shareButton=largeBarAnimatedManager.getShareButton();
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExternalIntents.share(getActivity(), offer.getName(),
                        offer.getCompany().getName() + " offers the following position:\n" +
                                offer.getDescription() + "\n\ncontact:" + offer.getCompany().getEmail());
            }
        });

        //handle favourite button
        final RelativeLayout favouriteButton=largeBarAnimatedManager.getFavouriteButton();
        ((ImageButton)favouriteButton.getChildAt(0)).setImageResource(offer.isFavourited() ? R.drawable.ic_action_important : R.drawable.ic_action_not_important);favouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!offer.isFavourited()) {
                    offer.setFavourited(true);
                    ((ImageButton) favouriteButton.getChildAt(0)).setImageResource(R.drawable.ic_action_important);
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
                    ((ImageButton) favouriteButton.getChildAt(0)).setImageResource(R.drawable.ic_action_not_important);
                    try {
                        myProfile.getRelation("favouritesOffers").remove(offer);
                        myProfile.saveEventually();
                        DialogManager.toastMessage("PREFERITO RIMOSSO", getActivity());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });



        //handle learn more button
        TextView learnMoreButton =(TextView)rootView.findViewById(R.id.learnMoreTextView);
        learnMoreButton.setText("Learn more about "+offer.getCompany().getName());
        learnMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.startCompanyActivity(offer.getCompany().getObjectId());
            }
        });


        TextView companyTextView=(TextView)rootView.findViewById(R.id.companyTextView);
        companyTextView.setText(offer.getCompany().getName());

        TextView locationTextView=(TextView)rootView.findViewById(R.id.locationTextView);
        locationTextView.setText(offer.getFullLocation());

        TextView positionTextView=(TextView)rootView.findViewById(R.id.positionTextView);
        positionTextView.setText(offer.getContract());

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

        TextView industriesTextView =(TextView)rootView.findViewById(R.id.industriesTextView);
        industriesTextView.setText(offer.getIndustries());


        TextView showMoreTextView =(TextView)rootView.findViewById(R.id.showMoreTextView);
        showMoreTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LinearLayout)rootView.findViewById(R.id.hiddenLayout)).setVisibility(View.VISIBLE);
                v.setVisibility(View.GONE);

            }
        });


        LinearLayout showMapLayout=(LinearLayout)rootView.findViewById(R.id.showMapLayout);
        showMapLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExternalIntents.openGoogleMaps(getActivity(),offer.getFullLocation());
            }
        });

        RelativeLayout applyButton=(RelativeLayout)rootView.findViewById(R.id.buttonApply);
        if(offer.isApplicationDone()) {
            applyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(OfferDetailFragment.this.getActivity(), StudentApplyActivity.class);
                    i.putExtra("SELECTED_OFFER", offer.getObjectId());
                    startActivityForResult(i, 0);
                }
            });
        } else {
            applyButton.setClickable(false);
            Button applyButtonText = (Button)rootView.findViewById(R.id.buttonApplyText);
            applyButtonText.setText("ALREADY APPLIED");
        }


    }





}
