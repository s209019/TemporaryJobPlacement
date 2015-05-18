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

    public static InsertLanguageDialogFragment newInstance(String title, InsertLanguageDialogFragment.Callbacks callback) {
        InsertLanguageDialogFragment fragment = new InsertLanguageDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        //args.putStringArrayList("items", items);
        //args.putStringArray("alreadyCheckedIndustries", alreadyCheckedIndustries);
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
        if (getArguments() != null) {
            title = getArguments().getString("title");
        }

        //internal view -->listview
        View internalView=getActivity().getLayoutInflater().inflate(R.layout.language_layout_dialog,null);
        final EditText languageTextView=((ClearableEditText)internalView.findViewById(R.id.language)).editText();

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

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(languageTextView.getWindowToken(), 0);


                if(languageTextView.getText().toString().trim().equals("")){
                    DialogManager.toastMessage("Language field  cannot be empty",getActivity());
                    return;
                }
                listener.onLanguageInserted(languageTextView.getText().toString().toUpperCase()+", level "+spinnerLevels.getSelectedItem());
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
