package it.polito.mobile.temporaryjobplacement.pstudent.fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import it.polito.mobile.temporaryjobplacement.R;
import it.polito.mobile.temporaryjobplacement.commons.utils.FileManager;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.ClearableEditText;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.DialogManager;


public class InsertLanguageDialogFragment extends DialogFragment {


    private static Callbacks listener;
    public interface Callbacks {
        public void onLanguageInserted(String language);
    }


    public static InsertLanguageDialogFragment newInstance(String title,String alreadyPresentLanguages, InsertLanguageDialogFragment.Callbacks callback) {
        InsertLanguageDialogFragment fragment = new InsertLanguageDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("items", alreadyPresentLanguages);
        fragment.setArguments(args);
        listener=callback;
        return fragment;
    }

    public InsertLanguageDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public AlertDialog onCreateDialog(Bundle savedInstanceState) {
        String title = null;
         String alreadyPresentLanguages="";
        if (getArguments() != null) {
            title = getArguments().getString("title");
            alreadyPresentLanguages=getArguments().getString("items");
        }
        if(alreadyPresentLanguages==null) alreadyPresentLanguages="";

        //internal view -->listview
        View internalView=getActivity().getLayoutInflater().inflate(R.layout.language_layout_dialog,null);

        //languages
        final Spinner languageSpinner=(Spinner)internalView.findViewById(R.id.languageSpinner);
        List<String> originalList = FileManager.readRowsFromFile(getActivity(), "languages.dat");
        List<String> list0=new ArrayList<String>();
        for(String l :originalList){
            if(!alreadyPresentLanguages.contains(l.toUpperCase()))
                list0.add(l);
        }
        final ArrayAdapter<String> dataAdapter0 = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item , list0);
        dataAdapter0.setDropDownViewResource(R.layout.spinner_item_dropdown);
        languageSpinner.setAdapter(dataAdapter0);

        //LEVELS
        final Spinner spinnerLevels=(Spinner)internalView.findViewById(R.id.levelSpinner);
        List<String> list = FileManager.readRowsFromFile(getActivity(), "language_levels.dat");
        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item , list);
        dataAdapter.setDropDownViewResource(R.layout.spinner_item_dropdown);
        spinnerLevels.setAdapter(dataAdapter);


        //wrapper dialog
        AlertDialog aDialog=null;
        AlertDialog.Builder alertBuilder=new AlertDialog.Builder(getActivity());
        if(title!=null)alertBuilder.setTitle(title);
        alertBuilder.setCancelable(true);
        alertBuilder.setIcon(android.R.drawable.arrow_up_float);
        alertBuilder.setPositiveButton("OK", new android.content.DialogInterface.OnClickListener() {
            @Override
            public void onClick(android.content.DialogInterface dialog, int which) {


                if(listener!=null)
                listener.onLanguageInserted(languageSpinner.getSelectedItem().toString().toUpperCase()+", level "+spinnerLevels.getSelectedItem());
            }
        });

        alertBuilder.setNegativeButton("Cancel", new android.content.DialogInterface.OnClickListener() {
            @Override
            public void onClick(android.content.DialogInterface dialog, int which) {

            }});

        alertBuilder.setView(internalView);
        aDialog=alertBuilder.create();

        return aDialog;
    }




    @Override
    public void onDetach() {
        super.onDetach();
        listener= null;
    }



}
