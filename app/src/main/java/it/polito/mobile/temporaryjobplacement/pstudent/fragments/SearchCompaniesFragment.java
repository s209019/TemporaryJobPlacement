package it.polito.mobile.temporaryjobplacement.pstudent.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import it.polito.mobile.temporaryjobplacement.R;
import it.polito.mobile.temporaryjobplacement.commonfragments.MultipleChoiceDialogFragment;
import it.polito.mobile.temporaryjobplacement.commons.utils.FileManager;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.ClearableEditText;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.DialogManager;
import it.polito.mobile.temporaryjobplacement.pstudent.activities.StudentCompanyListActivity;
import it.polito.mobile.temporaryjobplacement.pstudent.activities.StudentOfferListActivity;


public class SearchCompaniesFragment extends Fragment {

    TextView industriesClickableTextView;
    EditText editTextName;
    EditText editTextLocation;


    private OnFragmentInteractionListener mListener;
    public interface OnFragmentInteractionListener {
        public void startSearchCompaniesActivity(Intent intent);
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


    public static Fragment newInstance() {
        SearchCompaniesFragment fragment = new SearchCompaniesFragment();
       /* Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return fragment;
    }

    public SearchCompaniesFragment() {
        // Required empty public constructor
    }




    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putString("editTextNameContent", editTextName.getText().toString());
        outState.putString("editTextLocationContent", editTextLocation.getText().toString());
        outState.putString("industriesTextViewContent", industriesClickableTextView.getText().toString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View rootView= inflater.inflate(R.layout.fragment_search_by_company, container, false);



        //get editText references from ClearableEditTexts
        editTextName=((ClearableEditText)rootView.findViewById(R.id.editCompanyName)).editText();
        editTextLocation=((ClearableEditText)rootView.findViewById(R.id.editLocationName)).editText();
        industriesClickableTextView =(TextView)rootView.findViewById(R.id.industriesClickableTextView);


        if(savedInstanceState!=null){
            editTextName.setText(savedInstanceState.getString("editTextNameContent"));
            editTextLocation.setText(savedInstanceState.getString("editTextLocationContent"));
            industriesClickableTextView.setText(savedInstanceState.getString("industriesTextViewContent"));
        }



        //INDUSTRY FIELD
        //implement interface declared by MultipleChoiceDialogFragment in order to receive
        //industries checked by user and set them in industriesClickableTextView
        final MultipleChoiceDialogFragment.OnAllItemsCheckedListener onIndustriesCheckedListener=
                new MultipleChoiceDialogFragment.OnAllItemsCheckedListener() {
                    @Override
                    public void getAllItemsChecked(ArrayList<String> selectedItems) {
                        industriesClickableTextView.setText(getTextFromList(selectedItems));
                    }
                };
        //launch MultipleChoiceDialogFragment passing items to display, items previously checked and interface implementation
        industriesClickableTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> items = FileManager.readRowsFromFile(getActivity().getApplicationContext(), "industries.dat");
                //get already industries selected from industriesClickableTextView and pass it to MultipleChoiceDialogFragment
                String[] alreadyCheckedIndustries= getItemsFromTextView(industriesClickableTextView);
                DialogFragment df = MultipleChoiceDialogFragment.newInstance("Select one or more industries:", items, alreadyCheckedIndustries, onIndustriesCheckedListener);
                df.show(getActivity().getSupportFragmentManager(), "MyDialog");
            }
        });



        //search button which sends to activity, through interface OnFragmentInteractionListener(implemented by activity),
        //search params
        RelativeLayout searchButton=(RelativeLayout)rootView.findViewById(R.id.buttonSearch);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              if(editTextName.getText().toString().trim().equals("") && editTextLocation.getText().toString().trim().equals("") && industriesClickableTextView.getText().toString().trim().equals("")) {
                  DialogManager.toastMessage(getActivity().getResources().getString(R.string.all_empty_field_message), getActivity());
                  return;
              }
                Intent intent = new Intent(getActivity(), StudentCompanyListActivity.class);

                if(!editTextLocation.getText().toString().trim().equals("")){
                    intent.putExtra("location", editTextLocation.getText().toString().trim().toLowerCase());
                }

                if(!editTextName.getText().toString().trim().equals("")){
                    intent.putExtra("company", editTextName.getText().toString().trim().toLowerCase());
                }

                if(!industriesClickableTextView.getText().toString().trim().equals("")) {
                    intent.putStringArrayListExtra("industries", new ArrayList<String>(Arrays.asList(getItemsFromTextView(industriesClickableTextView))));
                }


                mListener.startSearchCompaniesActivity(intent);
            }
        });

        return rootView;
    }






    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    private String[] getItemsFromTextView(TextView textView){
        String text=textView.getText().toString();
        String[] items = text.split("\n");
        return items;
    }


    private String getTextFromList(ArrayList<String> list){
        String text="";
        for(String industry : list) text=text+industry+"\n";
        if(!text.equals(""))text=text.substring(0,text.length()-1);
        return text;
    }


}
