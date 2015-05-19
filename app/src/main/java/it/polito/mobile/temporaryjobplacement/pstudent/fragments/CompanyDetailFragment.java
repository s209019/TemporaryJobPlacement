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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

import it.polito.mobile.temporaryjobplacement.R;
import it.polito.mobile.temporaryjobplacement.commons.utils.AccountManager;
import it.polito.mobile.temporaryjobplacement.commons.utils.Connectivity;
import it.polito.mobile.temporaryjobplacement.commons.utils.ExternalIntents;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.CreateMenuItem;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.DialogManager;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.LargeBarAnimatedManager;
import it.polito.mobile.temporaryjobplacement.model.Company;
import it.polito.mobile.temporaryjobplacement.model.JobOffer;
import it.polito.mobile.temporaryjobplacement.model.Student;
import it.polito.mobile.temporaryjobplacement.pstudent.activities.SendMessageActivity;
import it.polito.mobile.temporaryjobplacement.pstudent.activities.StudentDetailActivity;

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
        public void startOffersActivity(String idCompany);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_company_detail, container, false);


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
        final String companyId=getArguments().getString("SELECTED_COMPANY");
        final Company[] company = {null};
        final Student[] myProfile = {null};
        final RelativeLayout loadingOverlay =(RelativeLayout)rootView.findViewById(R.id.loadingOverlay);
        loadingOverlay.setVisibility(View.VISIBLE);
        new AsyncTask<Object, Object, Object>(){
            @Override
            protected Object doInBackground(Object... params) {
                try {
                    company[0] = Company.getQuery().get(companyId);
                    myProfile[0] = AccountManager.getCurrentStudentProfile();
                    List<Company> favourites=myProfile[0].getFavouritesCompanies();
                    company[0].setFavourited(favourites.contains(company[0]));
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
                return new Object();
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if(o==null){
                    Connectivity.connectionError(getActivity());
                    return;
                }
                loadingOverlay.setVisibility(View.GONE);
                initializeView(rootView, largeBarAnimatedManager, company[0], myProfile[0]);
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




    private String getTextFromList(List<String> list){
        String text="";
        for(String industry : list) text=text+industry+"\n";
        if(!text.equals(""))text=text.substring(0,text.length()-1);
        return text;
    }




    private void initializeView(View rootView, LargeBarAnimatedManager largeBarAnimatedManager, final Company company, final Student student) {
        //populate title and subtitle
        largeBarAnimatedManager.getTitleTextView().setText(company.getName());
        largeBarAnimatedManager.getSubTitleTextView().setText("");

        //handle share button
        ImageButton shareButton=largeBarAnimatedManager.getShareButton();
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExternalIntents.share(getActivity(), company.getName(),
                        company.getDescription() + "\n\n" + company.getWebsite() + "\ncontact:" + company.getEmail());
            }
        });

        //handle favourite button
        final RelativeLayout favouriteButton=largeBarAnimatedManager.getFavouriteButton();
        ((ImageButton)favouriteButton.getChildAt(0)).setImageResource(company.isFavourited() ? R.drawable.ic_action_important : R.drawable.ic_action_not_important);
        final ProgressBar progress=(ProgressBar)favouriteButton.getChildAt(1);
        favouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           if (!company.isFavourited()) {
                        student.getRelation("favouritesCompanies").add(company);
                        progress.setVisibility(View.VISIBLE);
                        new AsyncTask<Object, Object, Boolean>(){
                            @Override protected Boolean doInBackground(Object... params) {
                                try {
                                    student.save();
                                } catch (Exception e) {
                                    e.printStackTrace();return false;}
                                return true;
                            }
                            @Override protected void onPostExecute(Boolean o) {super.onPostExecute(o);
                                if(o==true){
                                progress.setVisibility(View.GONE);
                                    DialogManager.toastMessage("Favourite added", getActivity());
                                company.setFavourited(true);
                                ((ImageButton) favouriteButton.getChildAt(0)).setImageResource(R.drawable.ic_action_important);}
                            }
                        }.execute();

                } else {
                        student.getRelation("favouritesCompanies").remove(company);
                        progress.setVisibility(View.VISIBLE);
                        new AsyncTask<Object, Object, Boolean>(){
                            @Override protected Boolean doInBackground(Object... params) {
                                try {
                                    student.save();
                                } catch (Exception e) {
                                    e.printStackTrace(); return false;}
                                return true;}
                            @Override protected void onPostExecute(Boolean o) {super.onPostExecute(o);
                                if(o==true){
                                progress.setVisibility(View.GONE);
                                DialogManager.toastMessage("Favourite removed", getActivity());
                                company.setFavourited(false);
                                ((ImageButton) favouriteButton.getChildAt(0)).setImageResource(R.drawable.ic_action_not_important);}
                            }
                        }.execute();

                }
            }
        });



        //handle companyTextView
        TextView companyTextView =(TextView)rootView.findViewById(R.id.companyTextView);
        companyTextView.setText(company.getName());


        TextView companyDescriptionTextView=(TextView)rootView.findViewById(R.id.companyDescriptionTextView);
        companyDescriptionTextView.setText(company.getDescription());


        TextView locationTextView=(TextView)rootView.findViewById(R.id.locationTextView);
        locationTextView.setText(company.getFullLocation());

        TextView emailTextView=(TextView)rootView.findViewById(R.id.emailTextView);
        emailTextView.setText(company.getEmail());



        TextView websiteTextView=(TextView)rootView.findViewById(R.id.websiteTextView);
        websiteTextView.setText(company.getWebsite());

        TextView phoneTextView=(TextView)rootView.findViewById(R.id.phoneTextView);
        phoneTextView.setText(company.getPhoneNumber());

        TextView industriesTextView =(TextView)rootView.findViewById(R.id.industriesTextView);
        industriesTextView.setText(company.getIndustries());




        LinearLayout showMapLayout=(LinearLayout)rootView.findViewById(R.id.showMapLayout);
        showMapLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExternalIntents.openGoogleMaps(getActivity(), company.getFullLocation());
            }
        });
        LinearLayout mail_layout=(LinearLayout)rootView.findViewById(R.id.mail_layout);
        mail_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExternalIntents.sendMail(getActivity(), company.getEmail());
            }
        });
        LinearLayout websiteLayout=(LinearLayout)rootView.findViewById(R.id.websiteLayout);
        websiteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExternalIntents.goToWebsite(getActivity(), company.getWebsite());
            }
        });
        LinearLayout phone_layout=(LinearLayout)rootView.findViewById(R.id.phone_layout);
        phone_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExternalIntents.call(getActivity(), company.getPhoneNumber());
            }
        });



        Button seeOffers=(Button)rootView.findViewById(R.id.buttonSeeOffers);
        seeOffers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.startOffersActivity(company.getObjectId());
            }
        });


        RelativeLayout messageButton=(RelativeLayout)rootView.findViewById(R.id.buttonsendMessage);
        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    Intent intent = new Intent(getActivity(), SendMessageActivity.class);
                    intent.putExtra("SELECTED_COMPANY", company.getObjectId());
                    startActivityForResult(intent, 0);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });




    }

}
