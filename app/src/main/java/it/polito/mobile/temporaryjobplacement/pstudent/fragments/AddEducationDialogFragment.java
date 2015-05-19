package it.polito.mobile.temporaryjobplacement.pstudent.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import it.polito.mobile.temporaryjobplacement.R;
import it.polito.mobile.temporaryjobplacement.commons.utils.FileManager;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.ClearableEditText;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.DialogManager;


public class AddEducationDialogFragment extends DialogFragment {


    private static Callbacks listener;
    public interface Callbacks {
        public void onInfoLanguageInserted(String degree,String course,String university,String mark,Date from, Date to);
    }

    public static AddEducationDialogFragment newInstance(AddEducationDialogFragment.Callbacks callback) {
        AddEducationDialogFragment fragment = new AddEducationDialogFragment();
        Bundle args = new Bundle();
        //args.putString("title", title);
        //args.putStringArrayList("items", items);
        //args.putStringArray("alreadyCheckedIndustries", alreadyCheckedIndustries);
        fragment.setArguments(args);
        listener=callback;
        return fragment;
    }

    public AddEducationDialogFragment() {
        // Required empty public constructor
    }







    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        /*String title = null;
        if (getArguments() != null) {
            title = getArguments().getString("title");
        }*/


        View internalView=inflater.inflate(R.layout.education_layout_dialog, null);
        getDialog().setTitle("Add a new education");
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));



        //LEVELS
        final Spinner spinnerDegree=(Spinner)internalView.findViewById(R.id.degreeSpinner);
        List<String> list = FileManager.readRowsFromFile(getActivity(), "educations.dat");
        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item , list);
        dataAdapter.setDropDownViewResource(R.layout.spinner_item_dropdown);
        spinnerDegree.setAdapter(dataAdapter);

        final EditText courseEditText=((ClearableEditText)internalView.findViewById(R.id.courseName)).editText();
        final EditText universityEditText=((ClearableEditText)internalView.findViewById(R.id.universityName)).editText();
        final EditText markEditText=((ClearableEditText)internalView.findViewById(R.id.markTextView)).editText();


        //manage date of birth
        final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        final TextView fromTextView = (TextView) internalView.findViewById(R.id.fromClickableTextView);
        /*
        if (myProfile.getDateOfBirth() != null) {
            dateOfBirthTextView.setText(df.format(myProfile.getDateOfBirth()));
        } else {
            dateOfBirthTextView.setText("Not specified");
        }*/
        fromTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar currentDate = Calendar.getInstance();
                int year = currentDate.get(Calendar.YEAR);
                int month = currentDate.get(Calendar.MONTH);
                int dayOfMonth = currentDate.get(Calendar.DAY_OF_MONTH);
                /*
                if (myProfile.getDateOfBirth() != null) {
                    GregorianCalendar gc = new GregorianCalendar();
                    gc.setTime(myProfile.getDateOfBirth());
                    year = gc.get(Calendar.YEAR);
                    month = gc.get(Calendar.MONTH);
                    dayOfMonth = gc.get(Calendar.DAY_OF_MONTH);
                }*/
                DatePickerDialog datePicker;
                datePicker = new DatePickerDialog(getActivity(), DatePickerDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        GregorianCalendar fromDate = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                        fromTextView.setText(df.format(fromDate.getTime()));
                        //updateDateOfBirth(myProfile,dateOfBirth.getTime());


                    }
                }, year, month, dayOfMonth);
                datePicker.setTitle("From:");
                datePicker.show();
            }
        });



        final TextView toTextView = (TextView) internalView.findViewById(R.id.toClickableTextView);
        /*
        if (myProfile.getDateOfBirth() != null) {
            dateOfBirthTextView.setText(df.format(myProfile.getDateOfBirth()));
        } else {
            dateOfBirthTextView.setText("Not specified");
        }*/
        toTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar currentDate = Calendar.getInstance();
                int year = currentDate.get(Calendar.YEAR);
                int month = currentDate.get(Calendar.MONTH);
                int dayOfMonth = currentDate.get(Calendar.DAY_OF_MONTH);
                /*
                if (myProfile.getDateOfBirth() != null) {
                    GregorianCalendar gc = new GregorianCalendar();
                    gc.setTime(myProfile.getDateOfBirth());
                    year = gc.get(Calendar.YEAR);
                    month = gc.get(Calendar.MONTH);
                    dayOfMonth = gc.get(Calendar.DAY_OF_MONTH);
                }*/
                DatePickerDialog datePicker;
                datePicker = new DatePickerDialog(getActivity(), DatePickerDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        GregorianCalendar toDate = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                        toTextView.setText(df.format(toDate.getTime()));
                        //updateDateOfBirth(myProfile,dateOfBirth.getTime());

                    }
                }, year, month, dayOfMonth);
                datePicker.setTitle("To:");
                datePicker.show();
            }
        });






        Button addButton =(Button)internalView.findViewById(R.id.add);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(courseEditText.getText().toString().trim().equals("") ||
                        universityEditText.getText().toString().trim().equals("") ||
                       markEditText.getText().toString().trim().equals("") ||
                        fromTextView.getText().toString().trim().equals("") ||
                        toTextView.getText().toString().trim().equals("")){
                    DialogManager.toastMessage("One ore more fields not filled", getActivity(), "center", true);
                    return;
                }

                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                Calendar fromC = Calendar.getInstance();
                Calendar toC = Calendar.getInstance();

                try {
                    fromC.setTime(df.parse(fromTextView.getText().toString()));
                    toC.setTime(df.parse(toTextView.getText().toString()));
                } catch (ParseException e) {  e.printStackTrace();}


                if(toC.before(fromC)){
                    DialogManager.toastMessage("Time interval not valid", getActivity(), "center", true);
                    return;
                }


                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                 imm.hideSoftInputFromWindow(courseEditText.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(universityEditText.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(markEditText.getWindowToken(), 0);

                listener.onInfoLanguageInserted(
                        spinnerDegree.getSelectedItem().toString(),
                        courseEditText.getText().toString().trim(),
                        universityEditText.getText().toString().trim(),
                        markEditText.getText().toString().trim(),
                        fromC.getTime(),
                        toC.getTime());
                getDialog().dismiss();

            }
        });





        Button cancelButton =(Button)internalView.findViewById(R.id.cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        return internalView;
    }





    @Override
    public void onDetach() {
        super.onDetach();
        // Reset the active callbacks interface to the dummy implementation.
        listener = null;
    }


}
