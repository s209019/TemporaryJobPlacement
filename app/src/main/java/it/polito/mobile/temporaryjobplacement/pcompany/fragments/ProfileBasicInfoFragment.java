package it.polito.mobile.temporaryjobplacement.pcompany.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.atomic.AtomicInteger;

import it.polito.mobile.temporaryjobplacement.R;
import it.polito.mobile.temporaryjobplacement.TemporaryJobPlacementApp;
import it.polito.mobile.temporaryjobplacement.commonfragments.MultipleChoiceDialogFragment;
import it.polito.mobile.temporaryjobplacement.commons.utils.BitmapManager;
import it.polito.mobile.temporaryjobplacement.commons.utils.FileManager;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.DialogManager;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.SavableEditText;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.imagezoomcrop.GOTOConstants;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.imagezoomcrop.ImageCropActivity;
import it.polito.mobile.temporaryjobplacement.model.Company;
import it.polito.mobile.temporaryjobplacement.model.Student;

public class ProfileBasicInfoFragment extends Fragment {

    ImageView V_name;
    ImageView V_Email;
    ImageView V_description ;
    ImageView V_Website ;
    ImageView V_Number ;
    ImageView V_Headquarters ;
    ImageView V_Industries ;
    ProgressBar progress_Industries ;
    ProgressBar progress_Headquarters ;
    ProgressBar progress_name ;
    ProgressBar progress_Website ;
    ProgressBar progress_Number ;
    ProgressBar progress_Email ;
    ProgressBar progress_Description ;
    ProgressBar progress_yourPhoto;



    private EditText nameTextView;
    private EditText emailTextView;
    private EditText addressTextView;
    private EditText CountryTextView;
    private EditText cityTextView;
    private EditText zipCodeTextView;
    private EditText descriptionEditText;
    private EditText websiteTextView;
    private EditText phoneNumberTextView;
    private TextView industriesTextView;

    ImageView logoPicture;
    TextView logoPictureTextview;
    Button deleteLogoPictureButton;


    private Company profile;

    private AtomicInteger viewInitialized= new AtomicInteger(0);

    private String[] picMode = {GOTOConstants.PicModes.GALLERY};

    public static final String TEMP_PHOTO_FILE_NAME = "temp_logo.jpg";
    public static final int REQUEST_CODE_UPDATE_PIC = 0x1;





    private ProfileBasicInfoFragment.Callbacks callbacks = null;


    public interface Callbacks {

        /*
        *get profile
        */
        Company getProfile();
        Bitmap getLogoBitmap();
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

        final View rootView = inflater.inflate(R.layout.fragment_company_profile_basic_info, container, false);


        //PROGRESSIVE WAIT IF NECESSARY
        new AsyncTask<Object, Object, Object>() {
            @Override
            protected Object doInBackground(Object... params) {
                //max 30s timeout(maggiore di quello di parse)
                for(int i=1;i< TemporaryJobPlacementApp.TIMEOUT_ITERATIONS;i++ ){
                    profile=callbacks.getProfile();
                    if(profile!=null) return new Object();
                    try { Thread.sleep (TemporaryJobPlacementApp.TIMEOUT_MILLIS*i); } catch (InterruptedException e) { }
                }
                return null;
            }
            @Override
            protected void onPostExecute(Object o) {
                try {
                    super.onPostExecute(o);
                    if (o == null) return;
                    Bitmap bitImage = callbacks.getLogoBitmap();
                    initializeView(rootView, profile, bitImage);

                    viewInitialized.set(1);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }}.execute();





        return rootView;
    }



    private void initializeView(final View rootView, final Company myProfile, final Bitmap bitImage) {

        V_name=(ImageView)rootView.findViewById(R.id.V_name);
        V_Email=(ImageView)rootView.findViewById(R.id.V_Email);
        V_description=(ImageView)rootView.findViewById(R.id.V_description);
        V_Website=(ImageView)rootView.findViewById(R.id.V_Website);
        V_Number=(ImageView)rootView.findViewById(R.id.V_Number);
        V_Headquarters=(ImageView)rootView.findViewById(R.id.V_Headquarters);
        V_Industries=(ImageView)rootView.findViewById(R.id.V_Industries);
        progress_name=(ProgressBar)rootView.findViewById(R.id.progress_name);
        progress_Number=(ProgressBar)rootView.findViewById(R.id.progress_Number);
        progress_Website=(ProgressBar)rootView.findViewById(R.id.progress_Website);
        progress_Headquarters=(ProgressBar)rootView.findViewById(R.id.progress_Headquarters);
        progress_Industries=(ProgressBar)rootView.findViewById(R.id.progress_Industries);
        progress_Email=(ProgressBar)rootView.findViewById(R.id.progress_Email);
        progress_Description=(ProgressBar)rootView.findViewById(R.id.progress_Description);
        progress_yourPhoto=(ProgressBar)rootView.findViewById(R.id.progress_yourPhoto);


        //manage name editText
        nameTextView = ((SavableEditText)rootView.findViewById(R.id.nameTextView)).editText();
        if (myProfile.getName() != null) {
            ((SavableEditText)nameTextView.getParent()).setSavedText(myProfile.getName());
            nameTextView.setText(myProfile.getName());
        }
        ((SavableEditText)nameTextView.getParent()).setButtonSaveListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateName(myProfile, nameTextView.getText().toString());
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(nameTextView.getWindowToken(), 0);
            }
        });
        nameTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String input;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    input = v.getText().toString();
                    updateName(myProfile, input);
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(nameTextView.getWindowToken(), 0);

                    return true;
                }
                return false;
            }
        });
        nameTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String input;
                EditText editText;
                if (!hasFocus) {
                    editText = (EditText) v;
                    input = editText.getText().toString();
                    updateName(myProfile, input);
                }
            }
        });

        //manage description editText
        descriptionEditText = ((SavableEditText) rootView.findViewById(R.id.descriptionEditText)).editText();
        if (myProfile.getDescription() != null) {
            ((SavableEditText) descriptionEditText.getParent()).setSavedText(myProfile.getDescription());
            descriptionEditText.setText(myProfile.getDescription());
        }
        ((SavableEditText)descriptionEditText.getParent()).setButtonSaveListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDescription(myProfile, descriptionEditText.getText().toString());
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(descriptionEditText.getWindowToken(), 0);
            }
        });
        descriptionEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String input;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    input = v.getText().toString();
                    updateDescription(myProfile, input);
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(descriptionEditText.getWindowToken(), 0);

                    return true;
                }
                return false;
            }
        });
        descriptionEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String input;
                EditText editText;

                if (!hasFocus) {
                    editText = (EditText) v;
                    input = editText.getText().toString();
                    updateDescription(myProfile, input);
                }
            }
        });



        //manage email editText
        emailTextView = ((SavableEditText) rootView.findViewById(R.id.emailTextView)).editText();
        if (myProfile.getEmail() != null) {
            ((SavableEditText) emailTextView.getParent()).setSavedText(myProfile.getEmail());
            emailTextView.setText(myProfile.getEmail());
        }
        ((SavableEditText) emailTextView.getParent()).setButtonSaveListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateEmail(myProfile, emailTextView.getText().toString());
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(emailTextView.getWindowToken(), 0);
            }
        });
        emailTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String input;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    input = v.getText().toString();
                    updateEmail(myProfile, input);
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(emailTextView.getWindowToken(), 0);

                    return true;
                }
                return false;
            }
        });
        emailTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String input;
                EditText editText;

                if (!hasFocus) {
                    editText = (EditText) v;
                    input = editText.getText().toString();
                    updateEmail(myProfile, input);
                }
            }
        });


        //manage website editText
        websiteTextView = ((SavableEditText) rootView.findViewById(R.id.websiteTextView)).editText();
        if (myProfile.getWebsite() != null) {
            ((SavableEditText) websiteTextView.getParent()).setSavedText(myProfile.getWebsite());
            websiteTextView.setText(myProfile.getWebsite());
        }
        ((SavableEditText) websiteTextView.getParent()).setButtonSaveListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateWebsite(myProfile, websiteTextView.getText().toString());
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(websiteTextView.getWindowToken(), 0);
            }
        });
        websiteTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String input;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    input = v.getText().toString();
                    updateWebsite(myProfile, input);
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(websiteTextView.getWindowToken(), 0);

                    return true;
                }
                return false;
            }
        });
        websiteTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String input;
                EditText editText;

                if (!hasFocus) {
                    editText = (EditText) v;
                    input = editText.getText().toString();
                    updateWebsite(myProfile, input);
                }
            }
        });


        //manage phone number editText
        phoneNumberTextView = ((SavableEditText) rootView.findViewById(R.id.phoneNumberTextView)).editText();
        if (myProfile.getPhoneNumber() != null) {
            ((SavableEditText) phoneNumberTextView.getParent()).setSavedText(myProfile.getPhoneNumber());
            phoneNumberTextView.setText(myProfile.getPhoneNumber());
        }
        ((SavableEditText) phoneNumberTextView.getParent()).setButtonSaveListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePhoneNumber(myProfile, phoneNumberTextView.getText().toString());
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(phoneNumberTextView.getWindowToken(), 0);
            }
        });
        phoneNumberTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String input;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    input = v.getText().toString();
                    updatePhoneNumber(myProfile, input);
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(phoneNumberTextView.getWindowToken(), 0);

                    return true;
                }
                return false;
            }
        });
        phoneNumberTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String input;
                EditText editText;

                if (!hasFocus) {
                    editText = (EditText) v;
                    input = editText.getText().toString();
                    updatePhoneNumber(myProfile, input);
                }
            }
        });

        //manage address editText
        addressTextView = ((SavableEditText) rootView.findViewById(R.id.addressTextView)).editText();
        if (myProfile.getAddress() != null) {
            ((SavableEditText) addressTextView.getParent()).setSavedText(myProfile.getAddress());
            addressTextView.setText(myProfile.getAddress());
        }
        ((SavableEditText) addressTextView.getParent()).setButtonSaveListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAddress(myProfile, addressTextView.getText().toString());
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(addressTextView.getWindowToken(), 0);
            }
        });
        addressTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String input;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    input = v.getText().toString();
                    updateAddress(myProfile, input);
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(addressTextView.getWindowToken(), 0);

                    return true;
                }
                return false;
            }
        });
        addressTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String input;
                EditText editText;

                if (!hasFocus) {
                    editText = (EditText) v;
                    input = editText.getText().toString();
                    updateAddress(myProfile, input);
                }
            }
        });


        //manage country editText
        CountryTextView = ((SavableEditText) rootView.findViewById(R.id.CountryTextView)).editText();
        if (myProfile.getCountry() != null) {
            ((SavableEditText) CountryTextView.getParent()).setSavedText(myProfile.getCountry());
            CountryTextView.setText(myProfile.getCountry());
        }
        ((SavableEditText) CountryTextView.getParent()).setButtonSaveListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCountry(myProfile, CountryTextView.getText().toString());
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(CountryTextView.getWindowToken(), 0);
            }
        });
        CountryTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String input;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    input = v.getText().toString();
                    updateCountry(myProfile, input);
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(CountryTextView.getWindowToken(), 0);

                    return true;
                }
                return false;
            }
        });
        CountryTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String input;
                EditText editText;

                if (!hasFocus) {
                    editText = (EditText) v;
                    input = editText.getText().toString();
                    updateCountry(myProfile, input);
                }
            }
        });

        //manage city editText
        cityTextView = ((SavableEditText) rootView.findViewById(R.id.cityTextView)).editText();
        if (myProfile.getCity() != null) {
            ((SavableEditText) cityTextView.getParent()).setSavedText(myProfile.getCity());
            cityTextView.setText(myProfile.getCity());
        }
        ((SavableEditText) cityTextView.getParent()).setButtonSaveListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCity(myProfile, cityTextView.getText().toString());
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(cityTextView.getWindowToken(), 0);
            }
        });
        cityTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String input;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    input = v.getText().toString();
                    updateCity(myProfile, input);
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(cityTextView.getWindowToken(), 0);

                    return true;
                }
                return false;
            }
        });
        cityTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String input;
                EditText editText;

                if (!hasFocus) {
                    editText = (EditText) v;
                    input = editText.getText().toString();
                    updateCity(myProfile, input);
                }
            }
        });



        //manage zip code editText
        zipCodeTextView = ((SavableEditText) rootView.findViewById(R.id.zipCodeTextView)).editText();
        if (myProfile.getZipCode() != null) {
            ((SavableEditText) zipCodeTextView.getParent()).setSavedText(myProfile.getZipCode());
            zipCodeTextView.setText(myProfile.getZipCode());
        }
        ((SavableEditText) zipCodeTextView.getParent()).setButtonSaveListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateZipCode(myProfile, zipCodeTextView.getText().toString());
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(zipCodeTextView.getWindowToken(), 0);
            }
        });
        zipCodeTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String input;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    input = v.getText().toString();
                    updateZipCode(myProfile, input);
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(zipCodeTextView.getWindowToken(), 0);

                    return true;
                }
                return false;
            }
        });
        zipCodeTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String input;
                EditText editText;

                if (!hasFocus) {
                    editText = (EditText) v;
                    input = editText.getText().toString();
                    updateZipCode(myProfile, input);
                }
            }
        });


        industriesTextView = (TextView) rootView.findViewById(R.id.industriesTextView);
        if (myProfile.getIndustries() != null) {
            industriesTextView.setText(myProfile.getIndustries());
        }

        //INDUSTRY FIELD
        //implement interface declared by MultipleChoiceDialogFragment in order to receive
        //industries checked by user and set them in industriesClickableTextView
        final MultipleChoiceDialogFragment.OnAllItemsCheckedListener onIndustriesCheckedListener=
                new MultipleChoiceDialogFragment.OnAllItemsCheckedListener() {
                    @Override
                    public void getAllItemsChecked(ArrayList<String> selectedItems) {
                        industriesTextView.setText(getTextFromList(selectedItems));
                        updateIndustries(myProfile, industriesTextView.getText().toString());
                    }
                };
        //launch MultipleChoiceDialogFragment passing items to display, items previously checked and interface implementation
        industriesTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> items = FileManager.readRowsFromFile(getActivity().getApplicationContext(), "industries.dat");
                //get already industries selected from industriesClickableTextView and pass it to MultipleChoiceDialogFragment
                String[] alreadyCheckedIndustries = getItemsFromTextView(industriesTextView);
                DialogFragment df = MultipleChoiceDialogFragment.newInstance("Select one or more industries:", items, alreadyCheckedIndustries, onIndustriesCheckedListener);
                df.show(getActivity().getSupportFragmentManager(), "MyDialog");
            }
        });




        //manage your photo input
        logoPicture = (ImageView)rootView.findViewById(R.id.logoPicture);
        logoPictureTextview = (TextView)rootView.findViewById(R.id.logoPictureTextview);
        deleteLogoPictureButton = (Button)rootView.findViewById(R.id.deleteLogoPictureButton);

        if(bitImage!=null){
            logoPictureTextview.setVisibility(View.GONE);
            deleteLogoPictureButton.setVisibility(View.VISIBLE);
            logoPicture.setImageBitmap(bitImage);
        }

        logoPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPhoto();
            }
        });
        deleteLogoPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePhoto(myProfile);
            }
        });





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


    private void updateName(final Company myProfile, final String name) {

        if(myProfile.getName() != null && !myProfile.getName().equals("") && nameTextView.getText().toString().trim().equals("")){
            DialogManager.toastMessage("Company name cannot be empty", getActivity(), "center", true);
            nameTextView.setText(myProfile.getName());
        } else if (myProfile.getName() == null || !myProfile.getName().equals(name)) {
            myProfile.setName(name);

            V_name.setVisibility(View.GONE);
            progress_name.setVisibility(View.VISIBLE);
            myProfile.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        DialogManager.toastMessage("Company name updated", getActivity(), "center", true);
                        if (progress_name != null) progress_name.setVisibility(View.GONE);
                        if (V_name != null) V_name.setVisibility(View.VISIBLE);
                        if (nameTextView != null) (
                                (SavableEditText) nameTextView.getParent()).setSavedText(myProfile.getName());
                    } else {
                        DialogManager.toastMessage("" + e.getMessage(), getActivity(), "center", true);
                    }
                }
            });
        }

    }

    private void updateDescription(final Company myProfile,String description) {
        if (myProfile.getDescription() == null || !myProfile.getDescription().equals(description)) {
            myProfile.setDescription(description);
            V_description.setVisibility(View.GONE);
            progress_Description.setVisibility(View.VISIBLE);
            myProfile.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e==null){
                        DialogManager.toastMessage("Description updated", getActivity(), "center",true);
                        if(progress_Description!=null) progress_Description.setVisibility(View.GONE);
                        if(V_description!=null) V_description.setVisibility(View.VISIBLE);
                        if(descriptionEditText!=null)
                            ((SavableEditText) descriptionEditText.getParent()).setSavedText(myProfile.getDescription());
                    } else {
                        DialogManager.toastMessage("" + e.getMessage(), getActivity(), "center", true);
                    }
                }
            });
        }

    }

    private void updateEmail(final Company myProfile,String email) {
        try {
            if (myProfile.getEmail() == null || !myProfile.getEmail().equals(email)) {
                myProfile.setEmail(email);
                V_Email.setVisibility(View.GONE);
                progress_Email.setVisibility(View.VISIBLE);
                myProfile.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            DialogManager.toastMessage("Email updated", getActivity(), "center", true);
                            if (progress_Email != null) progress_Email.setVisibility(View.GONE);
                            if (V_Email != null) V_Email.setVisibility(View.VISIBLE);
                            if (emailTextView != null)
                                ((SavableEditText) emailTextView.getParent()).setSavedText(myProfile.getEmail());
                        } else {
                            DialogManager.toastMessage("" + e.getMessage(), getActivity(), "center", true);
                        }
                    }
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void updateWebsite(final Company myProfile,String website) {
        if (myProfile.getWebsite() == null || !myProfile.getWebsite().equals(website)) {
            myProfile.setWebsite(website);
            V_Website.setVisibility(View.GONE);
            progress_Website.setVisibility(View.VISIBLE);
            myProfile.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e==null){
                        DialogManager.toastMessage("Website updated", getActivity(), "center",true);
                        if(progress_Website!=null) progress_Website.setVisibility(View.GONE);
                        if(V_Website!=null) V_Website.setVisibility(View.VISIBLE);
                        if(websiteTextView!=null)
                            ((SavableEditText) websiteTextView.getParent()).setSavedText(myProfile.getWebsite());
                    } else {
                        DialogManager.toastMessage("" + e.getMessage(), getActivity(), "center", true);
                    }
                }
            });
        }

    }

    private void updatePhoneNumber(final Company myProfile,String phoneNumber) {
        if (myProfile.getPhoneNumber() == null || !myProfile.getPhoneNumber().equals(phoneNumber)) {
            myProfile.setPhoneNumber(phoneNumber);
            V_Number.setVisibility(View.GONE);
            progress_Number.setVisibility(View.VISIBLE);
            myProfile.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e==null){
                        DialogManager.toastMessage("Phone number updated", getActivity(), "center",true);
                        if(progress_Number!=null) progress_Number.setVisibility(View.GONE);
                        if(V_Number!=null) V_Number.setVisibility(View.VISIBLE);
                        if(phoneNumberTextView!=null)
                            ((SavableEditText) phoneNumberTextView.getParent()).setSavedText(myProfile.getPhoneNumber());
                    } else {
                        DialogManager.toastMessage("" + e.getMessage(), getActivity(), "center", true);
                    }
                }
            });
        }

    }

    private void updateAddress(final Company myProfile,String address) {
        if (myProfile.getAddress() == null || !myProfile.getAddress().equals(address)) {
            myProfile.setAddress(address);
            V_Headquarters.setVisibility(View.GONE);
            progress_Headquarters.setVisibility(View.VISIBLE);
            myProfile.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e==null){
                        DialogManager.toastMessage("Address updated", getActivity(), "center",true);
                        if(progress_Headquarters!=null) progress_Headquarters.setVisibility(View.GONE);
                        if(V_Headquarters!=null) V_Headquarters.setVisibility(View.VISIBLE);
                        if(addressTextView!=null)
                            ((SavableEditText) addressTextView.getParent()).setSavedText(myProfile.getAddress());
                    } else {
                        DialogManager.toastMessage("" + e.getMessage(), getActivity(), "center", true);
                    }
                }
            });
        }

    }

    private void updateCountry(final Company myProfile, final String country) {
        if (myProfile.getCountry() == null || !myProfile.getCountry().equals(country)) {
            myProfile.setCountry(country);
            V_Headquarters.setVisibility(View.GONE);
            progress_Headquarters.setVisibility(View.VISIBLE);
            myProfile.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e==null){
                        DialogManager.toastMessage("Country updated", getActivity(), "center",true);
                        if(progress_Headquarters!=null) progress_Headquarters.setVisibility(View.GONE);
                        if(V_Headquarters!=null) V_Headquarters.setVisibility(View.VISIBLE);
                        if(CountryTextView!=null)
                            ((SavableEditText) CountryTextView.getParent()).setSavedText(myProfile.getCountry());
                    } else {
                        DialogManager.toastMessage("" + e.getMessage(), getActivity(), "center", true);
                    }
                }
            });
        }

    }


    private void updateCity(final Company myProfile,String city) {
        if (myProfile.getCity() == null || !myProfile.getCity().equals(city)) {
            myProfile.setCity(city);
            V_Headquarters.setVisibility(View.GONE);
            progress_Headquarters.setVisibility(View.VISIBLE);
            myProfile.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e==null){
                        DialogManager.toastMessage("City updated", getActivity(), "center",true);
                        if(progress_Headquarters!=null) progress_Headquarters.setVisibility(View.GONE);
                        if(V_Headquarters!=null) V_Headquarters.setVisibility(View.VISIBLE);
                        if(cityTextView!=null)
                            ((SavableEditText) cityTextView.getParent()).setSavedText(myProfile.getCity());
                    } else {
                        DialogManager.toastMessage("" + e.getMessage(), getActivity(), "center", true);
                    }
                }
            });
        }

    }


    private void updateZipCode(final Company myProfile,String zipCode) {
        if (myProfile.getZipCode() == null || !myProfile.getZipCode().equals(zipCode)) {
            myProfile.setZipCode(zipCode);
            V_Headquarters.setVisibility(View.GONE);
            progress_Headquarters.setVisibility(View.VISIBLE);
            myProfile.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e==null){
                        DialogManager.toastMessage("ZIP code updated", getActivity(), "center",true);
                        if(progress_Headquarters!=null) progress_Headquarters.setVisibility(View.GONE);
                        if(V_Headquarters!=null) V_Headquarters.setVisibility(View.VISIBLE);
                        if(zipCodeTextView!=null)
                            ((SavableEditText) zipCodeTextView.getParent()).setSavedText(myProfile.getZipCode());
                    } else {
                        DialogManager.toastMessage("" + e.getMessage(), getActivity(), "center", true);
                    }
                }
            });
        }

    }

    private void updateIndustries(final Company myProfile,String industries) {
        if (myProfile.getIndustries() == null || !myProfile.getIndustries().equals(industries)) {
            myProfile.setIndustries(industries);
            V_Industries.setVisibility(View.GONE);
            progress_Industries.setVisibility(View.VISIBLE);
            myProfile.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e==null){
                        DialogManager.toastMessage("Industries updated", getActivity(), "center",true);
                        if(progress_Industries!=null) progress_Industries.setVisibility(View.GONE);
                        if(V_Industries!=null) V_Industries.setVisibility(View.VISIBLE);
                    } else {
                        DialogManager.toastMessage("" + e.getMessage(), getActivity(), "center", true);
                    }
                }
            });
        }

    }



    private void updatePhoto(Company myProfile,Bitmap bitImage) {
        myProfile.updatePhoto(bitImage, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    DialogManager.toastMessage("Photo added", getActivity(), "center", true);
                    if (progress_yourPhoto != null){
                        progress_yourPhoto.setVisibility(View.GONE);
                        logoPictureTextview.setText("Tap on the photo to choose a profile picture.");
                        logoPictureTextview.setVisibility(View.GONE);
                        deleteLogoPictureButton.setVisibility(View.VISIBLE);
                    }
                } else {
                    DialogManager.toastMessage("" + e.getMessage(), getActivity(), "center", true);
                }
            }
        });
    }
    public void deletePhoto(Company myProfile){
        logoPicture.setImageResource(R.drawable.default_logo);
        deleteLogoPictureButton.setVisibility(View.GONE);
        logoPictureTextview.setText("Deleting the photo...");
        logoPictureTextview.setVisibility(View.VISIBLE);
        progress_yourPhoto.setVisibility(View.VISIBLE);
        myProfile.removePhoto(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    DialogManager.toastMessage("Photo deleted", getActivity(), "center", true);
                    if (progress_yourPhoto != null) {
                        progress_yourPhoto.setVisibility(View.GONE);
                        logoPictureTextview.setText("Tap on the photo to choose a profile picture.");
                    }
                } else {
                    DialogManager.toastMessage("" + e.getMessage(), getActivity(), "center", true);
                }
            }
        });
    }






    /********select photo from gallery or camera***********************/
    public void selectPhoto() {

        /*Intent intent = new Intent(getActivity(),ImageCropActivity.class);
        intent.putExtra("ACTION",GOTOConstants.IntentExtras.ACTION_GALLERY);
        startActivityForResult(intent, REQUEST_CODE_UPDATE_PIC);*/
        //Creo cartella se non esiste
        BitmapManager.creaCartella(getActivity(), "JobPlacement");
        BitmapManager.creaCartella(getActivity(), "JobPlacement/camera");
        Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select company logo"), REQUEST_CODE_UPDATE_PIC);


    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("recreated", true);
    }

    //handle data returning from camera or gallery
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_OK) {

            //PROGRESSIVE WAIT IF NECESSARY
            new AsyncTask<Object, Object, Object>() {
                @Override
                protected Object doInBackground(Object... params) {
                    //max 30s timeout(maggiore di quello di parse)
                    for(int i=1;i<TemporaryJobPlacementApp.TIMEOUT_ITERATIONS;i++ ){
                        if(viewInitialized.compareAndSet(1,1))return new Object();
                        try { Thread.sleep (TemporaryJobPlacementApp.TIMEOUT_MILLIS*i); } catch (InterruptedException e) { }
                    }
                    return null;
                }
                @Override
                protected void onPostExecute(Object o) {
                    try {
                        super.onPostExecute(o);
                        if (o == null) return;
                        performOnActivityResult(requestCode, resultCode, data);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }}.execute();

        }

    }


    void performOnActivityResult(int requestCode, int resultCode, Intent data) {


        if (resultCode == getActivity().RESULT_OK) {
             if (requestCode == REQUEST_CODE_UPDATE_PIC) {
                Uri selectedImageUri = data.getData();
                final String path= BitmapManager.getPath(selectedImageUri, getActivity());
                 final Bitmap[] bitmap = new Bitmap[1];
                 if(deleteLogoPictureButton!=null)deleteLogoPictureButton.setVisibility(View.GONE);
                 if(logoPictureTextview!=null)logoPictureTextview.setText("Uploading the photo...");
                 if(logoPictureTextview!=null)logoPictureTextview.setVisibility(View.VISIBLE);
                 if(progress_yourPhoto!=null)progress_yourPhoto.setVisibility(View.VISIBLE);
                 new AsyncTask<Object, Object, Object>() {
                     @Override
                     protected Object doInBackground(Object... params) {
                           bitmap[0] =BitmapManager.getBitmap(getActivity(), path, true);
                         return null;
                     }
                     @Override
                     protected void onPostExecute(Object o) {
                             super.onPostExecute(o);
                          if(logoPicture!=null)logoPicture.setImageBitmap(bitmap[0]);
                              updatePhoto(profile, bitmap[0]);
                     }}.execute();
            }


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