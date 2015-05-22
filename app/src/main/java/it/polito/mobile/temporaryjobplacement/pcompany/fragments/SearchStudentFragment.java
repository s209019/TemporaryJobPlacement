package it.polito.mobile.temporaryjobplacement.pcompany.fragments;

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
import java.util.Arrays;
import java.util.List;

import it.polito.mobile.temporaryjobplacement.R;
import it.polito.mobile.temporaryjobplacement.commonfragments.MultipleChoiceDialogFragment;
import it.polito.mobile.temporaryjobplacement.commons.utils.FileManager;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.ClearableEditText;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.DialogManager;
import it.polito.mobile.temporaryjobplacement.pcompany.activities.CompanyStudentListActivity;


public class SearchStudentFragment extends Fragment {

    EditText editTextKeywords;
    TextView languagesClickableTextView;
    Spinner minAgeSpinner;
    Spinner maxAgeSpinner;
    Spinner degreeSpinner;
    LinearLayout moreOptionsPanel;






    private OnFragmentInteractionListener mListener;
    public interface OnFragmentInteractionListener {
        public void startSearchStudentActivity(Intent intent);


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
        SearchStudentFragment fragment = new SearchStudentFragment();
       /* Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return fragment;
    }

    public SearchStudentFragment() {
        // Required empty public constructor
    }




    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putString("industriesTextViewContent", languagesClickableTextView.getText().toString());
        outState.putString("editTextKeywordsContent", editTextKeywords.getText().toString());
        outState.putBoolean("jobInfoPanelVisibility", moreOptionsPanel.getVisibility() == View.VISIBLE);
        outState.putInt("postingDateSpinnerSelected", minAgeSpinner.getSelectedItemPosition());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_search_by_student, container, false);

        final ScrollView scrollView=(ScrollView)rootView.findViewById(R.id.scrollView1);


        //get editText references from ClearableEditTexts
        editTextKeywords=((ClearableEditText)rootView.findViewById(R.id.editKeyword)).editText();
        languagesClickableTextView =(TextView)rootView.findViewById(R.id.languagesClickableTextView);
        minAgeSpinner = (Spinner) rootView.findViewById(R.id.ageSpinner);
        maxAgeSpinner = (Spinner) rootView.findViewById(R.id.maxAgeSpinner);
        degreeSpinner=(Spinner) rootView.findViewById(R.id.spinnerDegree);






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

        //min age
        final List<String> list = new ArrayList<String>();
        list.add("");
        for(int i=18;i<=100;i++)list.add(i+"");
        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item , list);
        dataAdapter.setDropDownViewResource(R.layout.spinner_item_dropdown);
        minAgeSpinner.setAdapter(dataAdapter);

        //max age
        final ArrayAdapter<String> dataAdapter0 = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item , list);
        dataAdapter0.setDropDownViewResource(R.layout.spinner_item_dropdown);
        maxAgeSpinner.setAdapter(dataAdapter0);


        //degree
         final List<String> list1 = new ArrayList<String>();
        list1.add("");
        list1.addAll(FileManager.readRowsFromFile(getActivity(), "educations.dat"));

        final ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item , list1);
        dataAdapter1.setDropDownViewResource(R.layout.spinner_item_dropdown);
        degreeSpinner.setAdapter(dataAdapter1);




        final MultipleChoiceDialogFragment.OnAllItemsCheckedListener onIndustriesCheckedListener=
                new MultipleChoiceDialogFragment.OnAllItemsCheckedListener() {
                    @Override
                    public void getAllItemsChecked(ArrayList<String> selectedItems) {
                        languagesClickableTextView.setText(getTextFromList(selectedItems));
                    }
                };
        languagesClickableTextView.setTag(onIndustriesCheckedListener);

        //launch MultipleChoiceDialogFragment passing items to display, items previously checked and interface implementation
        View.OnClickListener onFieldPressed= new View.OnClickListener() {
            @Override
            public void onClick(View viewClicked) {
                String assetFileName="";
                if(viewClicked.equals(languagesClickableTextView)){
                    assetFileName="languages.dat";
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
        languagesClickableTextView.setOnClickListener(onFieldPressed);




        //RESTORE PREVIOUS STATE
        if(savedInstanceState!=null){

            languagesClickableTextView.setText(savedInstanceState.getString("industriesTextViewContent"));
            editTextKeywords.setText(savedInstanceState.getString("editTextKeywordsContent"));
            if(savedInstanceState.getBoolean("jobInfoPanelVisibility")==true)
                moreOptionsPanel.setVisibility(View.VISIBLE);
            minAgeSpinner.setSelection(savedInstanceState.getInt("postingDateSpinnerSelected"));

        }




        //search button which sends to activity, through interface OnFragmentInteractionListener(implemented by activity),
        //search params
        RelativeLayout searchButton=(RelativeLayout)rootView.findViewById(R.id.buttonSearch);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (languagesClickableTextView.getText().toString().trim().equals("") &&
                    editTextKeywords.getText().toString().trim().equals("") &&
                    minAgeSpinner.getSelectedItem().equals("")&&
                    maxAgeSpinner.getSelectedItem().equals("")&&
                    degreeSpinner.getSelectedItem().equals("")) {

                    DialogManager.toastMessage(getActivity().getResources().getString(R.string.all_empty_field_message), getActivity());
                    return;
                }

                try {
                if (Integer.parseInt(minAgeSpinner.getSelectedItem().toString()) > Integer.parseInt(maxAgeSpinner.getSelectedItem().toString())) {
                    DialogManager.toastMessage("Age interval not valid", getActivity());
                    return;
                }
                }catch (Exception e){ e.printStackTrace();}

                //if(minAgeSpinner.getSelectedItem().equals(""))minAgeSpinner.setSelection(1);//18
                //if(maxAgeSpinner.getSelectedItem().equals(""))maxAgeSpinner.setSelection(list.size()-1);//100


                Intent intent = new Intent(getActivity(), CompanyStudentListActivity.class);


                if(!editTextKeywords.getText().toString().trim().equals("")) {
                    ArrayList<String> keywordsList = new ArrayList<String>();
                    for (String word : editTextKeywords.getText().toString().trim().split(" ")) {
                        keywordsList.add(word.toLowerCase());
                    }
                    intent.putStringArrayListExtra("keywords", keywordsList);
                }

                if(!degreeSpinner.getSelectedItem().equals("")){
                    intent.putExtra("degree", (String) degreeSpinner.getSelectedItem());
                }

                if(!maxAgeSpinner.getSelectedItem().equals("")){
                    intent.putExtra("maxAge", Integer.parseInt((String) maxAgeSpinner.getSelectedItem()));
                }

                if(!minAgeSpinner.getSelectedItem().equals("")){
                    intent.putExtra("minAge", Integer.parseInt((String) minAgeSpinner.getSelectedItem()));
                }

                if(!languagesClickableTextView.getText().toString().trim().equals("")) {
                    intent.putStringArrayListExtra("languages", new ArrayList<String>(Arrays.asList(getItemsFromTextView(languagesClickableTextView))));
                }


                mListener.startSearchStudentActivity(intent);
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
        turn(image, 180, 360);

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
