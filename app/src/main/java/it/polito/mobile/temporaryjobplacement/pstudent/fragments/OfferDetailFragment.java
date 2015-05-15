package it.polito.mobile.temporaryjobplacement.pstudent.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.ActionProvider;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

import it.polito.mobile.temporaryjobplacement.R;
import it.polito.mobile.temporaryjobplacement.commons.utils.AccountManager;
import it.polito.mobile.temporaryjobplacement.commons.utils.ExternalIntents;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.CreateMenuItem;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.DialogManager;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.LargeBarAnimatedManager;
import it.polito.mobile.temporaryjobplacement.model.JobOffer;
import it.polito.mobile.temporaryjobplacement.model.Student;
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



        final String jobOfferId=getArguments().getString("SELECTED_OFFER");
        boolean isFavourited=getArguments().getBoolean("IS_FAVOURITED");

        try {
            final JobOffer offer = JobOffer.getQuery().include("company").get(jobOfferId);
            offer.setFavourited(isFavourited);

        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(offer.getName());
        ((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle(offer.getCompany().getName());

        ImageButton shareButton=largeBarAnimatedManager.getShareButton();
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExternalIntents.share(getActivity(),offer.getName(),
                        offer.getCompany().getName()+" offers the following position:\n"+ offer.getDescription()+"\n\ncontact:"+offer.getCompany().getEmail());

            }
        });

        final RelativeLayout favouriteButton=largeBarAnimatedManager.getFavouriteButton();
          final Student myProfile = AccountManager.getCurrentStudentProfile();

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



        TextView titleTextView=largeBarAnimatedManager.getTitleTextView();
        titleTextView.setText(offer.getName());

        TextView companyTextView=largeBarAnimatedManager.getSubTitleTextView();
        companyTextView.setText(offer.getCompany().getName().toUpperCase());
            companyTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.startCompanyActivity(offer.getCompany().getObjectId());
                }
            });


            TextView learnMoreButton =(TextView)rootView.findViewById(R.id.learnMoreTextView);
            learnMoreButton.setText("Learn more about "+offer.getCompany().getName());
            learnMoreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.startCompanyActivity(offer.getCompany().getObjectId());
                }
            });

        TextView locationTextView=(TextView)rootView.findViewById(R.id.locationTextView);
        locationTextView.setText(offer.getFullLocation());

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

        } catch (Exception e) {
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
