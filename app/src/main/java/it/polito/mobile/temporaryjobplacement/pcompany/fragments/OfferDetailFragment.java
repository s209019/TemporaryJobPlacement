package it.polito.mobile.temporaryjobplacement.pcompany.fragments;

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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import it.polito.mobile.temporaryjobplacement.R;
import it.polito.mobile.temporaryjobplacement.commons.utils.AccountManager;
import it.polito.mobile.temporaryjobplacement.commons.utils.Connectivity;
import it.polito.mobile.temporaryjobplacement.commons.utils.ExternalIntents;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.CreateMenuItem;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.DialogManager;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.LargeBarAnimatedManager;
import it.polito.mobile.temporaryjobplacement.model.Application;
import it.polito.mobile.temporaryjobplacement.model.Company;
import it.polito.mobile.temporaryjobplacement.model.JobOffer;
import it.polito.mobile.temporaryjobplacement.model.Student;
import it.polito.mobile.temporaryjobplacement.pcompany.activities.PostJobOfferActivity;
import it.polito.mobile.temporaryjobplacement.pcompany.activities.CompanyDetailActivity;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link }
 * in two-pane mode (on tablets) or a {@link CompanyDetailActivity}
 * on handsets.
 */
public class OfferDetailFragment extends Fragment  {

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public OfferDetailFragment() {
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_my_offer_detail, container, false);


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
        final JobOffer[] offer = {null};
        final Company[] myProfile = {null};
        final RelativeLayout loadingOverlay =(RelativeLayout)rootView.findViewById(R.id.loadingOverlay);
        loadingOverlay.setVisibility(View.VISIBLE);
        new AsyncTask<Object, Object, Object>(){
           @Override
           protected Object doInBackground(Object... params) {
               try {
                   offer[0] = JobOffer.getQuery().get(jobOfferId);
                   myProfile[0] = AccountManager.getCurrentCompanyProfile();
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
                    initializeView(rootView, largeBarAnimatedManager, offer[0], myProfile[0]);
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
    }






    private void initializeView(final View rootView,LargeBarAnimatedManager largeBarAnimatedManager,final JobOffer offer, final Company myProfile){
        //populate title and subtitle
        largeBarAnimatedManager.getTitleTextView().setText(offer.getName());

        //handle share button
        ImageButton shareButton=largeBarAnimatedManager.getShareButton();
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExternalIntents.share(getActivity(), offer.getName(),
                        myProfile.getName() + " offers the following position:\n" +
                                offer.getDescription() + "\n\ncontact:" + myProfile.getEmail());
            }
        });

        //handle favourite button
        largeBarAnimatedManager.getFavouriteButton().setVisibility(View.GONE);



        Button editOfferButton =(Button)rootView.findViewById(R.id.editOfferButton);
        editOfferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), PostJobOfferActivity.class);
                i.putExtra("SELECTED_OFFER", offer.getObjectId());
                startActivity(i);
            }
        });

        final Button closeApplicationsButton =(Button)rootView.findViewById(R.id.closeApplicationsButton);
        closeApplicationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(offer.isPublic()) {
                    offer.setPublic(false);
                    offer.saveInBackground();
                    closeApplicationsButton.setText("RE-OPEN APPLICATIONS");
                } else {
                    offer.setPublic(true);
                    offer.saveInBackground();
                    closeApplicationsButton.setText("CLOSE APPLICATIONS");
                }

            }
        });

        if(offer.isPublic()) {
            closeApplicationsButton.setText("CLOSE APPLICATIONS");
        } else {
            closeApplicationsButton.setText("RE-OPEN APPLICATIONS");
        }


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


            }


        }
