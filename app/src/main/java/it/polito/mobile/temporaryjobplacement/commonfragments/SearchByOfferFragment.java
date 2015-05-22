package it.polito.mobile.temporaryjobplacement.commonfragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.polito.mobile.temporaryjobplacement.R;
import it.polito.mobile.temporaryjobplacement.commons.utils.FileManager;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.ClearableEditText;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.DialogManager;
import it.polito.mobile.temporaryjobplacement.pstudent.activities.StudentOfferListActivity;


public class SearchByOfferFragment extends Fragment {

    TextView industriesClickableTextView;
    TextView positionClickableTextView;
    TextView carerLevelClickableTextView;
    TextView educationClickableTextView;
    EditText editTextCompanyName;
    EditText editTextLocation;
    EditText editTextKeywords;
    LinearLayout moreOptionsPanel;
    Spinner postingDateSpinner;




    private OnFragmentInteractionListener mListener;
    public interface OnFragmentInteractionListener {
        void startSearchOffersActivity(String params);
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
        SearchByOfferFragment fragment = new SearchByOfferFragment();
       /* Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return fragment;
    }

    public SearchByOfferFragment() {
        // Required empty public constructor
    }




    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putString("editTextNameContent", editTextCompanyName.getText().toString());
        outState.putString("editTextLocationContent", editTextLocation.getText().toString());
        outState.putString("industriesTextViewContent", industriesClickableTextView.getText().toString());
        outState.putString("editTextKeywordsContent", editTextKeywords.getText().toString());
        outState.putBoolean("jobInfoPanelVisibility", moreOptionsPanel.getVisibility() == View.VISIBLE);
        outState.putInt("postingDateSpinnerSelected", postingDateSpinner.getSelectedItemPosition());
        outState.putString("educationClickableTextViewContent", educationClickableTextView.getText().toString());
        outState.putString("carerLevelClickableTextViewContent", carerLevelClickableTextView.getText().toString());
        outState.putString("positionClickableTextViewContent",  positionClickableTextView.getText().toString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_search_by_offer, container, false);

        final ScrollView scrollView=(ScrollView)rootView.findViewById(R.id.scrollView1);


        //get editText references from ClearableEditTexts
        editTextKeywords=((ClearableEditText)rootView.findViewById(R.id.editKeyword)).editText();
        editTextCompanyName =((ClearableEditText)rootView.findViewById(R.id.editCompanyName)).editText();
        editTextLocation=((ClearableEditText)rootView.findViewById(R.id.editLocationName)).editText();
        industriesClickableTextView =(TextView)rootView.findViewById(R.id.industriesClickableTextView);
        educationClickableTextView =(TextView)rootView.findViewById(R.id.educationClickableTextView);
        carerLevelClickableTextView =(TextView)rootView.findViewById(R.id.carerLevelClickableTextView);
        positionClickableTextView =(TextView)rootView.findViewById(R.id.positionClickableTextView);
        postingDateSpinner = (Spinner) rootView.findViewById(R.id.postingDateSpinner);






        //MORE/LESS OPTION
        moreOptionsPanel =(LinearLayout)rootView.findViewById(R.id.panelJobInformation);
        final LinearLayout moreOptionsPanelButton=(LinearLayout)rootView.findViewById(R.id.showJobInformation);
        final ImageView arrowImage=(ImageView)rootView.findViewById(R.id.arrow);
        moreOptionsPanel.setVisibility(View.GONE);
        moreOptionsPanelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (moreOptionsPanel.getVisibility() == View.GONE) {
                    moreOptionsPanel.setVisibility(View.VISIBLE);
                    scrollView.post(new Runnable() {
                        public void run() {
                            scrollView.smoothScrollTo(0, moreOptionsPanelButton.getTop());
                            turnUP(arrowImage);
                        }
                    });
                    //((Button)moreOptionsPanelButton.getChildAt(0)).setText("Fewer options");

                } else {
                    moreOptionsPanel.setVisibility(View.GONE);
                    turnDOWN(arrowImage);
                    //((Button)moreOptionsPanelButton.getChildAt(0)).setText("More options");
                }
            }
        });

        //POSTING DATE
        List<String> list = FileManager.readRowsFromFile(getActivity(),"posting_times.dat");
        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item , list);
        dataAdapter.setDropDownViewResource(R.layout.spinner_item_dropdown);
        postingDateSpinner.setAdapter(dataAdapter);


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
        industriesClickableTextView.setTag(onIndustriesCheckedListener);
        //EDUCATION FIELD
        //implement interface declared by MultipleChoiceDialogFragment in order to receive
        //educations checked by user and set them in educationClickableTextView
        final MultipleChoiceDialogFragment.OnAllItemsCheckedListener onEducationsCheckedListener=
                new MultipleChoiceDialogFragment.OnAllItemsCheckedListener() {
                    @Override
                    public void getAllItemsChecked(ArrayList<String> selectedItems) {
                        educationClickableTextView.setText(getTextFromList(selectedItems));
                    }
                };
        educationClickableTextView.setTag(onEducationsCheckedListener);
        //CAREER LEVEL FIELD
        //implement interface declared by MultipleChoiceDialogFragment in order to receive
        //career levels checked by user and set them in carrerLevelClickableTextView
        final MultipleChoiceDialogFragment.OnAllItemsCheckedListener onCareerLevelsCheckedListener=
                new MultipleChoiceDialogFragment.OnAllItemsCheckedListener() {
                    @Override
                    public void getAllItemsChecked(ArrayList<String> selectedItems) {
                        carerLevelClickableTextView.setText(getTextFromList(selectedItems));
                    }
                };
        carerLevelClickableTextView.setTag(onCareerLevelsCheckedListener);
        //POSITION FIELD
        //implement interface declared by MultipleChoiceDialogFragment in order to receive
        //positions checked by user and set them in carrerLevelClickableTextView
        final MultipleChoiceDialogFragment.OnAllItemsCheckedListener onPositionsCheckedListener=
                new MultipleChoiceDialogFragment.OnAllItemsCheckedListener() {
                    @Override
                    public void getAllItemsChecked(ArrayList<String> selectedItems) {
                        positionClickableTextView.setText(getTextFromList(selectedItems));
                    }
                };
        positionClickableTextView.setTag(onPositionsCheckedListener);
        //LISTENERS FOR FIELDS INDUSTRY, EDUCATION, CAREER LEVEL, POSITION
        //launch MultipleChoiceDialogFragment passing items to display, items previously checked and interface implementation
        View.OnClickListener onFieldPressed= new View.OnClickListener() {
            @Override
            public void onClick(View viewClicked) {
                String assetFileName="";
                if(viewClicked.equals(industriesClickableTextView)){
                    assetFileName="industries.dat";
                }else if(viewClicked.equals(educationClickableTextView)){
                    assetFileName="educations.dat";
                }else if(viewClicked.equals(carerLevelClickableTextView)){
                    assetFileName="career_levels.dat";
                }else if(viewClicked.equals(carerLevelClickableTextView)){
                    assetFileName="career_levels.dat";
                }else if(viewClicked.equals(positionClickableTextView)){
                    assetFileName="positions.dat";
                }
                //get items from asset file
                ArrayList<String> itemsToDisplay = FileManager.readRowsFromFile(getActivity().getApplicationContext(), assetFileName);
                //get already item selected from clickableTextView and pass it to MultipleChoiceDialogFragment
                String[] alreadyCheckedItems= getItemsFromTextView((TextView)viewClicked);
                DialogFragment df = MultipleChoiceDialogFragment.newInstance(
                        "Select one or more items:",
                        itemsToDisplay,
                        alreadyCheckedItems,
                        (MultipleChoiceDialogFragment.OnAllItemsCheckedListener)viewClicked.getTag());
                df.show(getActivity().getSupportFragmentManager(), "MyDialog");
            }
        };


        industriesClickableTextView.setOnClickListener(onFieldPressed);
        educationClickableTextView.setOnClickListener(onFieldPressed);
        carerLevelClickableTextView.setOnClickListener(onFieldPressed);
        positionClickableTextView.setOnClickListener(onFieldPressed);




        //RESTORE PREVIOUS STATE
        if(savedInstanceState!=null){
            editTextCompanyName.setText(savedInstanceState.getString("editTextNameContent"));
            editTextLocation.setText(savedInstanceState.getString("editTextLocationContent"));
            industriesClickableTextView.setText(savedInstanceState.getString("industriesTextViewContent"));
            editTextKeywords.setText(savedInstanceState.getString("editTextKeywordsContent"));
            if(savedInstanceState.getBoolean("jobInfoPanelVisibility")==true) moreOptionsPanel.setVisibility(View.VISIBLE);
            postingDateSpinner.setSelection(savedInstanceState.getInt("postingDateSpinnerSelected"));
            educationClickableTextView.setText(savedInstanceState.getString("educationClickableTextViewContent"));
            carerLevelClickableTextView.setText(savedInstanceState.getString("carerLevelClickableTextViewContent"));
            positionClickableTextView.setText(savedInstanceState.getString("positionClickableTextViewContent"));
        }




        //search button which sends to activity, through interface OnFragmentInteractionListener(implemented by activity),
        //search params
        RelativeLayout searchButton=(RelativeLayout)rootView.findViewById(R.id.buttonSearch);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editTextCompanyName.getText().toString().trim().equals("") &&
                        editTextLocation.getText().toString().trim().equals("") &&
                        industriesClickableTextView.getText().toString().trim().equals("") &&
                        educationClickableTextView.getText().toString().trim().equals("") &&
                        carerLevelClickableTextView.getText().toString().trim().equals("") &&
                        positionClickableTextView.getText().toString().trim().equals("") &&
                        editTextKeywords.getText().toString().trim().equals("") &&
                        postingDateSpinner.getSelectedItemPosition() == 0
                        ) {
                    DialogManager.toastMessage(getActivity().getResources().getString(R.string.all_empty_field_message), getActivity());
                    return;
                }

                Intent intent = new Intent(getActivity(), StudentOfferListActivity.class);

                if(!editTextKeywords.getText().toString().trim().equals("")) {
                    ArrayList<String> keywordsList = new ArrayList<String>();
                    for (String word : editTextKeywords.getText().toString().trim().split(" ")) {
                        keywordsList.add(word);
                    }
                    intent.putStringArrayListExtra("keywords", keywordsList);
                }

                if(!editTextLocation.getText().toString().trim().equals("")){
                    intent.putExtra("location", editTextLocation.getText().toString().trim());
                }



                //intent.putExtra("postingDate",

                        mListener.startSearchOffersActivity(
                                editTextKeywords.getText() + "\n" +
                                        educationClickableTextView.getText() + "\n" +
                                        carerLevelClickableTextView.getText() + "\n" +
                                        positionClickableTextView.getText() + "\n" +
                                        editTextCompanyName.getText().toString() + "\n" +
                                        editTextLocation.getText() + "\n" +
                                        industriesClickableTextView.getText() + "\n" +
                                        postingDateSpinner.getSelectedItem());
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




     public void turnUP(ImageView image) {
         turn(image,0,180);
    }
    public void turnDOWN(ImageView image) {
        turn(image,180,360);

    }
    public void turn(ImageView image,int a,int b) {
        RotateAnimation anim = new RotateAnimation(a, b,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setDuration(500);
        anim.setFillEnabled(true);
        anim.setFillAfter(true);
        image.startAnimation(anim);

    }


}
