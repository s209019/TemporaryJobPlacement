package it.polito.mobile.temporaryjobplacement.pcompany.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.parse.ParseQueryAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import it.polito.mobile.temporaryjobplacement.R;
import it.polito.mobile.temporaryjobplacement.TemporaryJobPlacementApp;
import it.polito.mobile.temporaryjobplacement.commons.utils.Connectivity;
import it.polito.mobile.temporaryjobplacement.commons.utils.FileManager;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.ClearableEditText;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.DialogManager;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.MessageQueryAdapter;
import it.polito.mobile.temporaryjobplacement.model.Education;
import it.polito.mobile.temporaryjobplacement.model.Message;

/**
 * A list fragment representing a list of Items. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {}.
 * <p/>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class EducationsListFragment extends DialogFragment {



    private Callbacks callbacks;
    /* A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activties to be notified of item
     * selections.
     */
    public interface Callbacks {
         List<Education> getEducations();
    }



    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EducationsListFragment() {
    }



    public static Fragment newInstance() {
        EducationsListFragment fragment = new EducationsListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView=inflater.inflate( R.layout.see_educations_dialog_layout, null);

        List<Education> educations=callbacks.getEducations();

   getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        getDialog().setTitle("Education");

        final ListView listView=(ListView)rootView.findViewById(R.id.educationList);
        ArrayAdapter<Education> educationArrayAdapter=new ArrayAdapter<Education>(getActivity(),R.layout.education_layout2){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = View.inflate(getContext(),R.layout.education_layout2, null);
                }



                final Education education=this.getItem(position);
                ((TextView)convertView.findViewById(R.id.degreeTextView)).setText(education.getDegree());
                ((TextView)convertView.findViewById(R.id.courseTextView)).setText(education.getCourse());
                ((TextView)convertView.findViewById(R.id.universityTextView)).setText(education.getUniversity());
                ((TextView)convertView.findViewById(R.id.markTextView)).setText(education.getMark());
                 SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                ((TextView)convertView.findViewById(R.id.periodTextView)).setText(df.format(education.getFromDate())+" - "+ df.format(education.getToDate()));

                //handle delete button
                ((ImageButton)convertView.findViewById(R.id.deleteButton)).setVisibility(View.GONE);



                return convertView;
            }
        };

        educationArrayAdapter.addAll(educations);
        listView.setAdapter(educationArrayAdapter);
        listView.setSelection(0);
        return  rootView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }




    @Override
    public void onResume() {
        super.onResume();



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
