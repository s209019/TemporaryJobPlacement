package it.polito.mobile.temporaryjobplacement.pcompany.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import it.polito.mobile.temporaryjobplacement.R;
import it.polito.mobile.temporaryjobplacement.TemporaryJobPlacementApp;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.DialogManager;
import it.polito.mobile.temporaryjobplacement.model.Company;
import it.polito.mobile.temporaryjobplacement.pcompany.activities.DefaultMessageActivity;

public class ProfileDefaultMessagesFragment extends Fragment {

    public static final int NEW_DEFAULT_MESSAGE_ID = 1286;
    public static final int EDIT_DEFAULT_MESSAGE_ID = 1287;

    Company companyProfile;
    final ArrayList<LinearLayout> defaultMessagesLayouts = new ArrayList<>();
    ArrayList<ParseObject> defaultMessages;


    private AtomicInteger viewInitialized= new AtomicInteger(0);



    private ProfileDefaultMessagesFragment.Callbacks callbacks = null;
    public interface Callbacks {
        /*
        *get profile
        */
        Company getProfile();
        //void detachAllFragments();
    }




    public static Fragment newInstance() {
        ProfileDefaultMessagesFragment fragment = new ProfileDefaultMessagesFragment();
        return fragment;
    }

    public ProfileDefaultMessagesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_company_profile_default_messages, container, false);


        //PROGRESSIVE WAIT IF NECESSARY
        new AsyncTask<Object, Object, Object>() {
            @Override
            protected Object doInBackground(Object... params) {
                //max 30s timeout(maggiore di quello di parse)
                for(int i=1;i<TemporaryJobPlacementApp.TIMEOUT_ITERATIONS;i++ ){
                    companyProfile=callbacks.getProfile();
                    if( companyProfile!=null) return new Object();
                    try { Thread.sleep (TemporaryJobPlacementApp.TIMEOUT_MILLIS*i); } catch (InterruptedException e) { }
                }
                return null;
            }
            @Override
            protected void onPostExecute(Object o) {

                try {
                    if(o==null)return;
                    initializeView(rootView);

                    viewInitialized.set(1);
                }catch (Exception e ){
                    e.printStackTrace();
                }


            }}.execute();




        return rootView;
    }



    private void initializeView(View rootView){

        if(companyProfile.getDefaultMessages()==null || companyProfile.getDefaultMessages().isEmpty() || !companyProfile.getDefaultMessages().get(0).has("name"))
            companyProfile.put("defaultMessages", new ArrayList<ParseObject>());

        defaultMessages = companyProfile.getDefaultMessages();

        for(final ParseObject defaultMessage: defaultMessages) {
            inflateDefaultMessage(rootView, defaultMessage);
        }

        rootView.findViewById(R.id.addDefaultMessageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), DefaultMessageActivity.class);
                startActivityForResult(i, NEW_DEFAULT_MESSAGE_ID);
            }
        });





    }

    public void inflateDefaultMessage(View rootView, final ParseObject defaultMessage){

        final LinearLayout defaultMessageLayout = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.deletable_entry_layout_with_margins, null );
        defaultMessagesLayouts.add(defaultMessageLayout);

        ((ImageButton)((LinearLayout)defaultMessageLayout.getChildAt(0)).getChildAt(1)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeDefaultMessage(defaultMessage, defaultMessageLayout);
            }
        });

        TextView defaultMessageTextView = ((TextView)((LinearLayout)defaultMessageLayout.getChildAt(0)).getChildAt(0));
        defaultMessageTextView.setText(defaultMessage.getString("name"));
        defaultMessageTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DefaultMessageActivity.class);
                intent.putExtra("DEFAULT_MESSAGE_NAME", defaultMessage.getString("name"));
                intent.putExtra("DEFAULT_MESSAGE_CONTENT", defaultMessage.getString("content"));
                intent.putExtra("DEFAULT_MESSAGE_INDEX", defaultMessages.indexOf(defaultMessage));
                startActivityForResult(intent, EDIT_DEFAULT_MESSAGE_ID);

            }
        });

        ((LinearLayout) rootView.findViewById(R.id.defaultMessagesContainer)).addView(defaultMessageLayout);

    }

    public void removeDefaultMessage(final ParseObject defaultMessage, LinearLayout defaultMessageLayout){
        defaultMessages.remove(defaultMessage);
        defaultMessageLayout.setVisibility(View.GONE);
        defaultMessagesLayouts.remove(defaultMessageLayout);
        getView().findViewById(R.id.addDefaultMessageButton).setVisibility(View.GONE);
        getView().findViewById(R.id.progress_defaultMessages).setVisibility(View.VISIBLE);
        companyProfile.put("defaultMessages", defaultMessages);
        companyProfile.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                getView().findViewById(R.id.addDefaultMessageButton).setVisibility(View.VISIBLE);
                getView().findViewById(R.id.progress_defaultMessages).setVisibility(View.GONE);
                defaultMessage.deleteInBackground();
            }
        });
    }



    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //PROGRESSIVE WAIT IF NECESSARY
        new AsyncTask<Object, Object, Object>() {
            @Override
            protected Object doInBackground(Object... params) {
                //max 30s timeout(maggiore di quello di parse)
                for(int i=1;i< TemporaryJobPlacementApp.TIMEOUT_ITERATIONS;i++ ){
                    if(viewInitialized.compareAndSet(1,1))return new Object();
                    try { Thread.sleep (TemporaryJobPlacementApp.TIMEOUT_MILLIS*i); } catch (InterruptedException e) { }
                }
                return null;
            }
            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                try {
                    if(o==null)return;
                    performOnActivityResult(requestCode, resultCode, data);
                }catch (Exception e){
                    e.printStackTrace();
                }


            }}.execute();


    }


    void performOnActivityResult(int requestCode, int resultCode, final Intent data) {
        switch (requestCode) {
            case NEW_DEFAULT_MESSAGE_ID:
                if (resultCode == Activity.RESULT_OK) {

                    getView().findViewById(R.id.addDefaultMessageButton).setVisibility(View.GONE);
                    getView().findViewById(R.id.progress_defaultMessages).setVisibility(View.VISIBLE);
                    final ParseObject defaultMessage = new ParseObject("DefaultMessage");
                    defaultMessage.put("name", data.getStringExtra("DEFAULT_MESSAGE_NAME"));
                    defaultMessage.put("content", data.getStringExtra("DEFAULT_MESSAGE_CONTENT"));
                    defaultMessage.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            companyProfile.getDefaultMessages().add(defaultMessage);
                            companyProfile.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    getView().findViewById(R.id.addDefaultMessageButton).setVisibility(View.VISIBLE);
                                    getView().findViewById(R.id.progress_defaultMessages).setVisibility(View.GONE);
                                    inflateDefaultMessage(getView(), defaultMessage);

                                }

                            });
                        }
                    });
                }
                break;
            case EDIT_DEFAULT_MESSAGE_ID:
                if (resultCode == Activity.RESULT_OK && data.getIntExtra("DEFAULT_MESSAGE_INDEX", -1)!=-1) {

                    getView().findViewById(R.id.addDefaultMessageButton).setVisibility(View.GONE);
                    getView().findViewById(R.id.progress_defaultMessages).setVisibility(View.VISIBLE);
                    final ParseObject defaultMessage = defaultMessages.get(data.getIntExtra("DEFAULT_MESSAGE_INDEX", -1));
                    defaultMessage.put("name", data.getStringExtra("DEFAULT_MESSAGE_NAME"));
                    defaultMessage.put("content", data.getStringExtra("DEFAULT_MESSAGE_CONTENT"));
                    defaultMessage.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            getView().findViewById(R.id.addDefaultMessageButton).setVisibility(View.VISIBLE);
                            getView().findViewById(R.id.progress_defaultMessages).setVisibility(View.GONE);
                            LinearLayout defaultMessageLayout = defaultMessagesLayouts.get(data.getIntExtra("DEFAULT_MESSAGE_INDEX", -1));
                            TextView defaultMessageTextView = ((TextView)((LinearLayout)defaultMessageLayout.getChildAt(0)).getChildAt(0));
                            defaultMessageTextView.setText(defaultMessage.getString("name"));
                        }
                    });

                    }
                    break;
        }
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


