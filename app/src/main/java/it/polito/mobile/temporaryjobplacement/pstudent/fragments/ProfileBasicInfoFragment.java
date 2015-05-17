package it.polito.mobile.temporaryjobplacement.pstudent.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import it.polito.mobile.temporaryjobplacement.R;
import it.polito.mobile.temporaryjobplacement.commons.utils.Connectivity;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.DialogManager;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.SavableEditText;
import it.polito.mobile.temporaryjobplacement.model.Student;

public class ProfileBasicInfoFragment extends Fragment {

    ImageView V_firstName;
    ImageView V_lastName;
    ImageView V_dateOfBirthName;
    ImageView V_languageSkills;
    ImageView V_skills ;
    ProgressBar pro_firstName ;
    ProgressBar pro_lastName ;
    ProgressBar pro_dateOfBirthName ;
    ProgressBar pro_languageSkills ;
    ProgressBar pro_skills ;

    private ProfileBasicInfoFragment.Callbacks callbacks = null;

    public interface Callbacks {

        /*
        *get profile
        */
        public Student getProfile() throws Exception;

    }


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

         V_firstName=(ImageView)rootView.findViewById(R.id.V_firstName);
         V_lastName=(ImageView)rootView.findViewById(R.id.V_LastName);
         V_dateOfBirthName=(ImageView)rootView.findViewById(R.id.V_DateOfBirth);
         V_languageSkills=(ImageView)rootView.findViewById(R.id.V_Language);
         V_skills=(ImageView)rootView.findViewById(R.id.V_skills);
         pro_firstName=(ProgressBar)rootView.findViewById(R.id.progress_firstName);
         pro_lastName=(ProgressBar)rootView.findViewById(R.id.progress_LastName);
          pro_dateOfBirthName=(ProgressBar)rootView.findViewById(R.id.progress_DateOfBirth);
          pro_languageSkills=(ProgressBar)rootView.findViewById(R.id.progress_Language);
          pro_skills=(ProgressBar)rootView.findViewById(R.id.progress_Skills);






        final Student[] myProfile = {null};
        final RelativeLayout loadingOverlay = (RelativeLayout) rootView.findViewById(R.id.loadingOverlay);
        loadingOverlay.setVisibility(View.VISIBLE);
        new AsyncTask<Object, Object, Object>() {
            @Override
            protected Object doInBackground(Object... params) {
                try {
                    myProfile[0] = callbacks.getProfile();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
                return new Object();
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);

                if (o == null) {
                    Connectivity.connectionError(getActivity());
                    return;
                }
                loadingOverlay.setVisibility(View.GONE);
                initializeView(rootView, myProfile[0]);
            }
        }.execute();


        return rootView;
    }



    private void initializeView(final View rootView, final Student myProfile) {


        final EditText firstNameTextView = ((SavableEditText)rootView.findViewById(R.id.firstNameTextView)).editText();
        if (myProfile.getFirstName() != null) {
            firstNameTextView.setText(myProfile.getFirstName());
        }


        firstNameTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String input;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    input = v.getText().toString();
                    updateFirstName(myProfile,input);
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
                    updateFirstName(myProfile,input);
                }
            }
        });

        final EditText lastNameTextView = ((SavableEditText) rootView.findViewById(R.id.lastNameTextView)).editText();
        if (myProfile.getLastName() != null) {
            lastNameTextView.setText(myProfile.getLastName());
        }

        lastNameTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String input;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    input = v.getText().toString();
                    updateLastName(myProfile,input);
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
                    updateLastName(myProfile,input);
                }
            }
        });


        final EditText keywordsTextView = ((SavableEditText) rootView.findViewById(R.id.keywordsTextView)).editText();
        if (myProfile.getSkills() != null) {
            keywordsTextView.setText(myProfile.getSkills());
        }


        keywordsTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String input;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    input = v.getText().toString();
                    updateSkills(myProfile,input);
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
                    updateSkills(myProfile,input);
                }
            }
        });

        final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        final TextView dateOfBirthTextView = (TextView) rootView.findViewById(R.id.dateOfBirthTextView);
        if (myProfile.getDateOfBirth() != null) {
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

                if (myProfile.getDateOfBirth() != null) {

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
                        updateDateOfBirth(myProfile,dateOfBirth.getTime());

                    }
                }, year, month, dayOfMonth);

                datePicker.setTitle("Seleziona il giorno");
                datePicker.show();

            }
        });


    }

    private void updateFirstName(Student myProfile,String firstName) {

        if (myProfile.getFirstName() == null || !myProfile.getFirstName().equals(firstName)) {
            myProfile.setFirstName(firstName);

            V_firstName.setVisibility(View.GONE);
            pro_firstName.setVisibility(View.VISIBLE);
            myProfile.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e==null){
                    DialogManager.toastMessage("First name updated", getActivity(), "center",true);
                    if(pro_firstName!=null) pro_firstName.setVisibility(View.GONE);
                    if(V_firstName!=null) V_firstName.setVisibility(View.VISIBLE);}
                }
            });
        }

    }

    private void updateLastName(Student myProfile,String lastName) {

        if (myProfile.getLastName() == null || !myProfile.getLastName().equals(lastName)) {
            myProfile.setLastName(lastName);
            V_lastName.setVisibility(View.GONE);
            pro_lastName.setVisibility(View.VISIBLE);
            myProfile.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e==null){
                        DialogManager.toastMessage("Last name updated", getActivity(), "center",true);
                        if(pro_lastName!=null) pro_lastName.setVisibility(View.GONE);
                        if(V_lastName!=null) V_lastName.setVisibility(View.VISIBLE);}
                }
            });
        }

    }

    private void updateSkills(Student myProfile,String skills) {

        if (myProfile.getSkills() == null || !myProfile.getSkills().equals(skills)) {
            myProfile.setSkills(skills);
            V_skills.setVisibility(View.GONE);
            pro_skills.setVisibility(View.VISIBLE);
            myProfile.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e==null){
                        DialogManager.toastMessage("Skills updated", getActivity(), "center",true);
                        if(pro_skills!=null) pro_skills.setVisibility(View.GONE);
                        if(V_skills!=null) V_skills.setVisibility(View.VISIBLE);}
                }
            });
        }

    }

    private void updateDateOfBirth(Student myProfile,Date dateOfBirth) {

        if (myProfile.getDateOfBirth() == null || !myProfile.getDateOfBirth().equals(dateOfBirth)) {

            myProfile.setDateOfBirth(dateOfBirth);
            V_dateOfBirthName.setVisibility(View.GONE);
            pro_dateOfBirthName.setVisibility(View.VISIBLE);
            myProfile.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e==null){
                        DialogManager.toastMessage("Date of birth updated", getActivity(), "center",true);
                        if(pro_dateOfBirthName!=null) pro_dateOfBirthName.setVisibility(View.GONE);
                        if(V_dateOfBirthName!=null) V_dateOfBirthName.setVisibility(View.VISIBLE);}
                }
            });
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