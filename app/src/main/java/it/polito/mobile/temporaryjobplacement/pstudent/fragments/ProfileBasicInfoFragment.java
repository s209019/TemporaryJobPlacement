package it.polito.mobile.temporaryjobplacement.pstudent.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.SaveCallback;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import it.polito.mobile.temporaryjobplacement.R;
import it.polito.mobile.temporaryjobplacement.commons.utils.BitmapManager;
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
    ImageView V_yourPhoto ;
    ProgressBar pro_firstName ;
    ProgressBar pro_lastName ;
    ProgressBar pro_dateOfBirthName ;
    ProgressBar pro_languageSkills ;
    ProgressBar pro_skills ;
    ProgressBar pro_yourPhoto;



    private EditText firstNameTextView;
    private EditText lastNameTextView;
    private EditText keywordsTextView;


    public static final int REQUEST_CAMERA=2,SELECT_FILE=3;
    private ImageView imageView;
    private ImageButton buttonDelete;
    private RelativeLayout photoBox, photoButton;



    private Student profile;






    private ProfileBasicInfoFragment.Callbacks callbacks = null;


    public interface Callbacks {

        /*
        *get profile
        */
        Student getProfile();
        Bitmap getPhotoStudentBitmap();
        //void detachAllFragments();

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

        //DialogManager.toastMessage("fragmetRecreated",getActivity());



        profile=callbacks.getProfile();
        ArrayList<String> languages = profile.getLanguageSkills();
        Bitmap bitImage=callbacks.getPhotoStudentBitmap();


        initializeView(rootView, profile, languages, bitImage);


        return rootView;
    }



    private void initializeView(final View rootView, final Student myProfile, final ArrayList<String> languages, final Bitmap bitImage) {


        V_firstName=(ImageView)rootView.findViewById(R.id.V_firstName);
        V_lastName=(ImageView)rootView.findViewById(R.id.V_LastName);
        V_dateOfBirthName=(ImageView)rootView.findViewById(R.id.V_DateOfBirth);
        V_languageSkills=(ImageView)rootView.findViewById(R.id.V_Language);
        V_skills=(ImageView)rootView.findViewById(R.id.V_skills);
        V_yourPhoto=(ImageView)rootView.findViewById(R.id.V_yourPhoto);
        pro_firstName=(ProgressBar)rootView.findViewById(R.id.progress_firstName);
        pro_lastName=(ProgressBar)rootView.findViewById(R.id.progress_LastName);
        pro_dateOfBirthName=(ProgressBar)rootView.findViewById(R.id.progress_DateOfBirth);
        pro_languageSkills=(ProgressBar)rootView.findViewById(R.id.progress_Language);
        pro_yourPhoto=(ProgressBar)rootView.findViewById(R.id.progress_yourPhoto);







        //manage first name editText
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

        //manage last name editText
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



        //manage last name editText
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
                imm.hideSoftInputFromWindow(keywordsTextView.getWindowToken(), 0);
            }
        });
        keywordsTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String input;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    input = v.getText().toString();
                    updateSkills(myProfile, input);
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


        //manage date of birth
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
                datePicker.setTitle("Select date:");
                datePicker.show();
            }
        });




        //manage language skills input
        final Button addLanguageSkillsButton=(Button)rootView.findViewById(R.id.addLanguageSkillsButton);
        final LinearLayout[] languageLayouts={
                (LinearLayout)rootView.findViewById(R.id.languageLayout1),
                (LinearLayout)rootView.findViewById(R.id.languageLayout2),
                (LinearLayout)rootView.findViewById(R.id.languageLayout3),
                (LinearLayout)rootView.findViewById(R.id.languageLayout4),
                (LinearLayout)rootView.findViewById(R.id.languageLayout5)};

        //handle delete button of each language
        for(int position=0;position<languageLayouts.length;position++){
            final int finalPosition = position;
            ((ImageButton)languageLayouts[position].getChildAt(1)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeLanguageSkills(myProfile, finalPosition,languageLayouts,languages,addLanguageSkillsButton);
                }
            });
        }
        //populate languages textViews
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


        //manage your photo input
        imageView=(ImageView)rootView.findViewById(R.id.photo);
        photoButton=(RelativeLayout)rootView.findViewById(R.id.buttonPhoto);
        photoBox=(RelativeLayout)rootView.findViewById(R.id.fotoBOx);
        buttonDelete=(ImageButton)rootView.findViewById(R.id.buttonDelete);
        if(bitImage!=null){
            photoButton.setVisibility(View.GONE);
            photoBox.setVisibility(View.VISIBLE);
            imageView.setImageBitmap(bitImage);
        }
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPhoto();
            }
        });
        photoBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPhoto();
            }
        });
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePhoto(myProfile);
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
                    if (V_languageSkills != null) V_languageSkills.setVisibility(View.VISIBLE);
                } else {
                    DialogManager.toastMessage("" + e.getMessage(), getActivity(), "center", true);
                }
            }
        });
    }

    private void removeLanguageSkills(Student myProfile, final int position, final LinearLayout[] languageLayouts, final ArrayList<String> languages, final Button addLanguageSkillsButton) {
        V_languageSkills.setVisibility(View.GONE);
        pro_languageSkills.setVisibility(View.VISIBLE);
        ((ImageButton)languageLayouts[position].getChildAt(1)).setClickable(false);
        myProfile.removeLanguageSkill(languages.get(position), new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    ((ImageButton) languageLayouts[position].getChildAt(1)).setClickable(true);
                    DialogManager.toastMessage("Language skill removed", getActivity(), "center", true);
                    if (pro_languageSkills != null) pro_languageSkills.setVisibility(View.GONE);
                    if (V_languageSkills != null) V_languageSkills.setVisibility(View.VISIBLE);

                    languages.remove(position);
                    for (LinearLayout l : languageLayouts) l.setVisibility(View.GONE);
                    //repopulate languages textView
                    int i = 0;
                    for (String language : languages) {
                        if (i == languageLayouts.length) break;
                        languageLayouts[i].setVisibility(View.VISIBLE);
                        ((TextView) languageLayouts[i].getChildAt(0)).setText(language);
                        i++;
                        if (i == languageLayouts.length)
                            addLanguageSkillsButton.setVisibility(View.GONE);
                        else addLanguageSkillsButton.setVisibility(View.VISIBLE);
                    }


                } else {
                    DialogManager.toastMessage("" + e.getMessage(), getActivity(), "center", true);
                }
            }
        });



    }

    private void updatePhoto(Student myProfile,Bitmap bitImage) {
        V_yourPhoto.setVisibility(View.GONE);
        pro_yourPhoto.setVisibility(View.VISIBLE);
        myProfile.updatePhoto(bitImage, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    DialogManager.toastMessage("Photo added", getActivity(), "center", true);
                    if (pro_yourPhoto != null) pro_yourPhoto.setVisibility(View.GONE);
                    if (V_yourPhoto != null) V_yourPhoto.setVisibility(View.VISIBLE);
                } else {
                    DialogManager.toastMessage("" + e.getMessage(), getActivity(), "center", true);
                }
            }
        });
    }
    public void deletePhoto(Student myProfile){
        photoButton.setVisibility(View.VISIBLE);
        photoBox.setVisibility(View.GONE);
        V_yourPhoto.setVisibility(View.GONE);
        pro_yourPhoto.setVisibility(View.VISIBLE);
        myProfile.removePhoto(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    DialogManager.toastMessage("Photo added", getActivity(), "center", true);
                    if (pro_yourPhoto != null) pro_yourPhoto.setVisibility(View.GONE);
                    if (V_yourPhoto != null) V_yourPhoto.setVisibility(View.VISIBLE);
                } else {
                    DialogManager.toastMessage("" + e.getMessage(), getActivity(), "center", true);
                }
            }
        });
    }






    /********select photo from gallery or camera***********************/
    public void selectPhoto() {
        //Creo cartella se non esiste
        BitmapManager.creaCartella(getActivity(), "myjob_photos");
        final CharSequence[] items = { "Take a picture", "Choose from gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Insert your photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take a picture")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "myjob_photos/temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                     startActivityForResult(intent, REQUEST_CAMERA);

                } else if (items[item].equals("Choose from gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Select app"), SELECT_FILE);
                }
                //callbacks.detachAllFragments();

            }
        });
        builder.show();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("recreated", true);
    }

    //handle data returning from camera or gallery
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_OK) {
            Bitmap bitImage=null;
            if (requestCode == REQUEST_CAMERA) {

                bitImage = BitmapManager.getBitmap(getActivity(), "temp.jpg", "myjob_photos/", true);
                if (bitImage == null) {
                    DialogManager.toastMessage("Error occurred", getActivity());
                    return;
                }
                photoButton.setVisibility(View.GONE);
                photoBox.setVisibility(View.VISIBLE);
                imageView.setImageBitmap(bitImage);

            } else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                String path = BitmapManager.getPath(selectedImageUri, getActivity());
                bitImage = BitmapManager.getBitmap(getActivity(), path, true);
                photoButton.setVisibility(View.GONE);
                photoBox.setVisibility(View.VISIBLE);
                imageView.setImageBitmap(bitImage);

            }
           /* try {
                BitmapManager.memorizzaImmagine(bitImage, "temp1.jpg", "myjob_photos/", 20);
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            updatePhoto(profile,bitImage);

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