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
        public void onInfoLanguageInserted(String degree, String course, String university, String mark, Date from, Date to);
    }

    public static AddEducationDialogFragment newInstance(String OK_BUTTON_TEXT,Callbacks callback) {
        AddEducationDialogFragment fragment = new AddEducationDialogFragment();
        Bundle args = new Bundle();
        args.putString("OK_BUTTON_TEXT", OK_BUTTON_TEXT);
        //args.putString("title", title);
        //args.putStringArrayList("items", items);
        //args.putStringArray("alreadyCheckedIndustries", alreadyCheckedIndustries);
        fragment.setArguments(args);
        listener=callback;
        return fragment;
    }


    public static AddEducationDialogFragment newInstance(String degree,String  course,String university, String mark,String from,String to,String OK_BUTTON_TEXT ,Callbacks callback) {
        AddEducationDialogFragment fragment = new AddEducationDialogFragment();
        Bundle args = new Bundle();
        args.putString("degree", degree);
        args.putString("course", course);
        args.putString("university", university);
        args.putString("mark", mark);
        args.putString("from", from);
        args.putString("OK_BUTTON_TEXT", OK_BUTTON_TEXT);
        args.putString("to", to);


        fragment.setArguments(args);
        listener=callback;
        return fragment;
    }

    public AddEducationDialogFragment() {
        // Required empty public constructor
    }






    String oldDegree=null,  oldCourse=null, oldUniversity=null,   oldMark=null,  oldFrom=null,  oldTo=null,OK_BUTTON_TEXT;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        if (getArguments() != null) {
            oldDegree = getArguments().getString("degree");
            oldCourse = getArguments().getString("course");
            oldUniversity = getArguments().getString("university");
            oldMark = getArguments().getString("mark");
            oldFrom = getArguments().getString("from");
            oldTo = getArguments().getString("to");
            OK_BUTTON_TEXT=getArguments().getString("OK_BUTTON_TEXT");

        }



        View internalView=inflater.inflate(R.layout.education_layout_dialog, null);
        getDialog().setTitle("Add a new education");
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));


        getDialog().setCancelable(false);

        final Spinner spinnerDegree=(Spinner)internalView.findViewById(R.id.degreeSpinner);
        List<String> list = FileManager.readRowsFromFile(getActivity(), "educations.dat");
        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item , list);
        dataAdapter.setDropDownViewResource(R.layout.spinner_item_dropdown);
        spinnerDegree.setAdapter(dataAdapter);
        if(oldDegree!=null)spinnerDegree.setSelection(dataAdapter.getPosition(oldDegree));

        final EditText courseEditText=((ClearableEditText)internalView.findViewById(R.id.courseName)).editText();
        if(oldCourse!=null)courseEditText.setText(oldCourse);
        final EditText universityEditText=((ClearableEditText)internalView.findViewById(R.id.universityName)).editText();
        if(oldUniversity!=null)universityEditText.setText(oldUniversity);
        final EditText markEditText=((ClearableEditText)internalView.findViewById(R.id.markTextView)).editText();
        if(oldMark!=null)markEditText.setText(oldMark);





        final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        final TextView fromTextView = (TextView) internalView.findViewById(R.id.fromClickableTextView);
        if (oldFrom != null) {
            fromTextView.setText(oldFrom);
        }
        fromTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar currentDate = Calendar.getInstance();
                int year = currentDate.get(Calendar.YEAR);
                int month = currentDate.get(Calendar.MONTH);
                int dayOfMonth = currentDate.get(Calendar.DAY_OF_MONTH);

                if (!fromTextView.getText().toString().equals("")) {
                    Calendar gc= Calendar.getInstance();
                    try {
                        gc.setTime(df.parse(fromTextView.getText().toString()));
                    } catch (ParseException e) {  e.printStackTrace();}
                    year = gc.get(Calendar.YEAR);
                    month = gc.get(Calendar.MONTH);
                    dayOfMonth = gc.get(Calendar.DAY_OF_MONTH);
                }



                DatePickerDialog datePicker;
                datePicker = new DatePickerDialog(getActivity(), DatePickerDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        GregorianCalendar fromDate = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                        fromTextView.setText(df.format(fromDate.getTime()));


                    }
                }, year, month, dayOfMonth);
                datePicker.setTitle("From:");
                datePicker.show();
            }
        });



        final TextView toTextView = (TextView) internalView.findViewById(R.id.toClickableTextView);
            if (oldTo != null) {
                toTextView.setText(oldTo);
            }
        toTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar currentDate = Calendar.getInstance();
                int year = currentDate.get(Calendar.YEAR);
                int month = currentDate.get(Calendar.MONTH);
                int dayOfMonth = currentDate.get(Calendar.DAY_OF_MONTH);
                if (!toTextView.getText().toString().equals("")) {
                    Calendar gc= Calendar.getInstance();
                    try {
                        gc.setTime(df.parse(toTextView.getText().toString()));
                    } catch (ParseException e) {  e.printStackTrace();}
                    year = gc.get(Calendar.YEAR);
                    month = gc.get(Calendar.MONTH);
                    dayOfMonth = gc.get(Calendar.DAY_OF_MONTH);
                }
                DatePickerDialog datePicker;
                datePicker = new DatePickerDialog(getActivity(), DatePickerDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        GregorianCalendar toDate = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                        toTextView.setText(df.format(toDate.getTime()));

                    }
                }, year, month, dayOfMonth);
                datePicker.setTitle("To:");
                datePicker.show();
            }
        });






        Button addButton =(Button)internalView.findViewById(R.id.add);
        addButton.setText(OK_BUTTON_TEXT);

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

                if(listener!=null)
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

