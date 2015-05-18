package it.polito.mobile.temporaryjobplacement.pstudent.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Layout;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

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



    private EditText firstNameTextView;
    private EditText lastNameTextView;

    private EditText keywordsTextView;


    ArrayList<String> languages;







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
                    languages=myProfile[0].getLanguageSkills();
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







        firstNameTextView = ((SavableEditText)rootView.findViewById(R.id.firstNameTextView)).editText();
        if (myProfile.getFirstName() != null) {
            ((SavableEditText)firstNameTextView.getParent()).setSavedText(myProfile.getFirstName());
            firstNameTextView.setText(myProfile.getFirstName());
        }

        ((SavableEditText)firstNameTextView.getParent()).setButtonSaveListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateFirstName(myProfile, firstNameTextView.getText().toString());
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(firstNameTextView.getWindowToken(), 0);
            }
        });
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
                    updateFirstName(myProfile, input);
                }
            }
        });



       lastNameTextView = ((SavableEditText) rootView.findViewById(R.id.lastNameTextView)).editText();
        if (myProfile.getLastName() != null) {
            ((SavableEditText) lastNameTextView.getParent()).setSavedText(myProfile.getLastName());
            lastNameTextView.setText(myProfile.getLastName());
        }
        ((SavableEditText)lastNameTextView.getParent()).setButtonSaveListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateLastName(myProfile, lastNameTextView.getText().toString());
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(lastNameTextView.getWindowToken(), 0);
            }
        });
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
                    updateLastName(myProfile, input);
                }
            }
        });




       keywordsTextView = ((SavableEditText) rootView.findViewById(R.id.keywordsTextView)).editText();
        if (myProfile.getSkills() != null) {
            ((SavableEditText) keywordsTextView.getParent()).setSavedText(myProfile.getSkills());
            keywordsTextView.setText(myProfile.getSkills());
        }
        ((SavableEditText) keywordsTextView.getParent()).setButtonSaveListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSkills(myProfile, keywordsTextView.getText().toString());
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow( keywordsTextView.getWindowToken(), 0);
            }
        });
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










        final Button addLanguageSkillsButton=(Button)rootView.findViewById(R.id.addLanguageSkillsButton);
        final LinearLayout[] languageLayouts={
                (LinearLayout)rootView.findViewById(R.id.languageLayout1),
                (LinearLayout)rootView.findViewById(R.id.languageLayout2),
                (LinearLayout)rootView.findViewById(R.id.languageLayout3),
                (LinearLayout)rootView.findViewById(R.id.languageLayout4),
                (LinearLayout)rootView.findViewById(R.id.languageLayout5)};


        //handle delete button
        for(int position=0;position<languageLayouts.length;position++){
            final int finalPosition = position;
            ((ImageButton)languageLayouts[position].getChildAt(1)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeLanguageSkills(myProfile, finalPosition,languageLayouts,languages,addLanguageSkillsButton);
                }
            });
        }



        //populate languages textView
        int i=0;
        for(String language: languages){
            if(i==languageLayouts.length)break;
            languageLayouts[i].setVisibility(View.VISIBLE);
            ((TextView)languageLayouts[i].getChildAt(0)).setText(language);
            i++;
            if(i==languageLayouts.length)addLanguageSkillsButton.setVisibility(View.INVISIBLE);
        }


                final InsertLanguageDialogFragment.Callbacks callbacks=new InsertLanguageDialogFragment.Callbacks() {
                    @Override
                    public void onLanguageInserted(String language) {

                        if(languages.size()<languageLayouts.length){
                            languages.add(language);
                            languageLayouts[languages.size()-1].setVisibility(View.VISIBLE);
                            ((TextView)languageLayouts[languages.size() - 1].getChildAt(0)).setText(language);
                            if(languages.size()==languageLayouts.length)addLanguageSkillsButton.setVisibility(View.INVISIBLE);
                            addLanguageSkills(myProfile,language);
                        }
                    }
                };
        addLanguageSkillsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment df = InsertLanguageDialogFragment.newInstance("Insert language and level:", callbacks);
                df.show(getActivity().getSupportFragmentManager(), "MyDialog");
            }
        });


    }

    private void updateFirstName(final Student myProfile, final String firstName) {

        if (myProfile.getFirstName() == null || !myProfile.getFirstName().equals(firstName)) {
            myProfile.setFirstName(firstName);

            V_firstName.setVisibility(View.GONE);
            pro_firstName.setVisibility(View.VISIBLE);
            myProfile.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e==null){
                    DialogManager.toastMessage("First name updated", getActivity(), "center", true);
                    if(pro_firstName!=null) pro_firstName.setVisibility(View.GONE);
                    if(V_firstName!=null) V_firstName.setVisibility(View.VISIBLE);
                    if(firstNameTextView!=null)(
                            (SavableEditText)firstNameTextView.getParent()).setSavedText(myProfile.getFirstName());
                    }
                }
            });
        }

    }

    private void updateLastName(final Student myProfile,String lastName) {

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
                        if(V_lastName!=null) V_lastName.setVisibility(View.VISIBLE);
                        if(lastNameTextView!=null)
                            ((SavableEditText) lastNameTextView.getParent()).setSavedText(myProfile.getLastName());
                    }
                }
            });
        }

    }

    private void updateSkills(final Student myProfile,String skills) {

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
                        if(V_skills!=null) V_skills.setVisibility(View.VISIBLE);
                        if(keywordsTextView!=null)
                            ((SavableEditText) keywordsTextView.getParent()).setSavedText(myProfile.getSkills());
                    }
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


    private void addLanguageSkills(Student myProfile,String language) {
        V_languageSkills.setVisibility(View.GONE);
        pro_languageSkills.setVisibility(View.VISIBLE);
        myProfile.addLanguageSkill(language, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    DialogManager.toastMessage("Language skill added", getActivity(), "center", true);
                    if (pro_languageSkills != null) pro_languageSkills.setVisibility(View.GONE);
                    if ( V_languageSkills != null)  V_languageSkills.setVisibility(View.VISIBLE);
                }else{
                    DialogManager.toastMessage(""+e.getMessage(), getActivity(), "center", true);
                }
            }});
    }

    private void removeLanguageSkills(Student myProfile, final int position, final LinearLayout[] languageLayouts, final ArrayList<String> languages, final Button addLanguageSkillsButton) {

        V_languageSkills.setVisibility(View.GONE);
        pro_languageSkills.setVisibility(View.VISIBLE);
        myProfile.removeLanguageSkill(languages.get(position), new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    DialogManager.toastMessage("Language skill removed", getActivity(), "center", true);
                    if (pro_languageSkills != null) pro_languageSkills.setVisibility(View.GONE);
                    if (V_languageSkills != null) V_languageSkills.setVisibility(View.VISIBLE);

                    languages.remove(position);
                    for(LinearLayout l: languageLayouts)l.setVisibility(View.GONE);
                    //repopulate languages textView
                    int i=0;
                    for(String language: languages){
                        if(i==languageLayouts.length)break;
                        languageLayouts[i].setVisibility(View.VISIBLE);
                        ((TextView)languageLayouts[i].getChildAt(0)).setText(language);
                        i++;
                        if(i==languageLayouts.length)
                            addLanguageSkillsButton.setVisibility(View.GONE);
                        else addLanguageSkillsButton.setVisibility(View.VISIBLE);
                    }


                } else {
                    DialogManager.toastMessage("" + e.getMessage(), getActivity(), "center", true);
                }
            }
        });



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