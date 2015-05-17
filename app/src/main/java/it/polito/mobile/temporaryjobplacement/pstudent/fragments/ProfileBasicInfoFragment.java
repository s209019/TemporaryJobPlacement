package it.polito.mobile.temporaryjobplacement.pstudent.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.SaveCallback;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import it.polito.mobile.temporaryjobplacement.R;
import it.polito.mobile.temporaryjobplacement.commons.utils.AccountManager;
import it.polito.mobile.temporaryjobplacement.commons.utils.Connectivity;
import it.polito.mobile.temporaryjobplacement.commons.utils.ExternalIntents;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.DialogManager;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.LargeBarAnimatedManager;
import it.polito.mobile.temporaryjobplacement.model.Application;
import it.polito.mobile.temporaryjobplacement.model.JobOffer;
import it.polito.mobile.temporaryjobplacement.model.Student;
import it.polito.mobile.temporaryjobplacement.pstudent.activities.StudentApplyActivity;

public class ProfileBasicInfoFragment extends Fragment {

    private Student myProfile;


    public static Fragment newInstance() {
        ProfileBasicInfoFragment fragment = new ProfileBasicInfoFragment();
        return fragment;
    }

    public ProfileBasicInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_profile_basic_info, container, false);

        final Student[] myProfile = {null};
        final RelativeLayout loadingOverlay =(RelativeLayout)rootView.findViewById(R.id.loadingOverlay);
        loadingOverlay.setVisibility(View.VISIBLE);
        new AsyncTask<Object, Object, Object>(){
            @Override
            protected Object doInBackground(Object... params) {
                try {
                    myProfile[0] = AccountManager.getCurrentStudentProfile();
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
                initializeView(rootView, myProfile[0]);
            }
        }.execute();


        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void initializeView(final View rootView, final Student myProfile){

        this.myProfile=myProfile;

        final EditText firstNameTextView=(EditText)rootView.findViewById(R.id.firstNameTextView);
        if(myProfile.getFirstName()!=null) {
            firstNameTextView.setText(myProfile.getFirstName());
        }


        firstNameTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String input;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    input = v.getText().toString();
                    updateFirstName(input);
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(firstNameTextView.getWindowToken(), 0);

                    return true;
                }
                return false;
            }
        });
        firstNameTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String input;
                EditText editText;

                if (!hasFocus) {
                    editText = (EditText) v;
                    input = editText.getText().toString();
                    updateFirstName(input);
                }
            }
        });

        final EditText lastNameTextView=(EditText)rootView.findViewById(R.id.lastNameTextView);
        if(myProfile.getLastName()!=null) {
            lastNameTextView.setText(myProfile.getLastName());
        }

        lastNameTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String input;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    input = v.getText().toString();
                    updateLastName(input);
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(lastNameTextView.getWindowToken(), 0);

                    return true;
                }
                return false;
            }
        });
        lastNameTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String input;
                EditText editText;

                if (!hasFocus) {
                    editText = (EditText) v;
                    input = editText.getText().toString();
                    updateLastName(input);
                }
            }
        });


        final EditText keywordsTextView=(EditText)rootView.findViewById(R.id.keywordsTextView);
        if(myProfile.getSkills()!=null) {
            keywordsTextView.setText(myProfile.getSkills());
        }


        keywordsTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String input;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    input = v.getText().toString();
                    updateSkills(input);
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(keywordsTextView.getWindowToken(), 0);

                    return true;
                }
                return false;
            }
        });
        keywordsTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String input;
                EditText editText;

                if (!hasFocus) {
                    editText = (EditText) v;
                    input = editText.getText().toString();
                    updateSkills(input);
                }
            }
        });

        final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        final TextView dateOfBirthTextView = (TextView) rootView.findViewById(R.id.dateOfBirthTextView);
        if(myProfile.getDateOfBirth()!=null) {
            dateOfBirthTextView.setText(df.format(myProfile.getDateOfBirth()));
        } else {
            dateOfBirthTextView.setText("Not specified");
        }
        dateOfBirthTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar currentDate = Calendar.getInstance();
                int year = currentDate.get(Calendar.YEAR);
                int month = currentDate.get(Calendar.MONTH);
                int dayOfMonth = currentDate.get(Calendar.DAY_OF_MONTH);

                if(myProfile.getDateOfBirth()!=null) {

                    GregorianCalendar gc = new GregorianCalendar();
                    gc.setTime(myProfile.getDateOfBirth());
                    year = gc.get(Calendar.YEAR);
                    month = gc.get(Calendar.MONTH);
                    dayOfMonth = gc.get(Calendar.DAY_OF_MONTH);

                }

                DatePickerDialog datePicker;
                datePicker = new DatePickerDialog(getActivity(), DatePickerDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {


                        GregorianCalendar dateOfBirth = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                        dateOfBirthTextView.setText(df.format(dateOfBirth.getTime()));
                        updateDateOfBirth(dateOfBirth.getTime());

                    }
                }, year, month, dayOfMonth);

                datePicker.setTitle("Seleziona il giorno");
                datePicker.show();

            }
        });


    }

    private void updateFirstName(String firstName) {

        if(myProfile.getFirstName()==null || !myProfile.getFirstName().equals(firstName)) {
            myProfile.setFirstName(firstName);
            myProfile.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    DialogManager.toastMessage("First name updated", getActivity(), "center");
                }
            });
        }

    }

    private void updateLastName(String lastName) {

        if(myProfile.getLastName()==null || !myProfile.getLastName().equals(lastName)) {
            myProfile.setLastName(lastName);
            myProfile.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    DialogManager.toastMessage("Last name updated", getActivity(), "center");
                }
            });
        }

    }

    private void updateSkills(String skills) {

        if(myProfile.getSkills()==null || !myProfile.getSkills().equals(skills)) {
            myProfile.setSkills(skills);
            myProfile.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    DialogManager.toastMessage("Skills updated", getActivity(), "center");
                }
            });
        }

    }

    private void updateDateOfBirth(Date dateOfBirth) {

        if(myProfile.getDateOfBirth()==null || !myProfile.getDateOfBirth().equals(dateOfBirth)) {

            myProfile.setDateOfBirth(dateOfBirth);
            myProfile.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    DialogManager.toastMessage("Date of Birth updated", getActivity());
                }
            });
        }

    }





}
