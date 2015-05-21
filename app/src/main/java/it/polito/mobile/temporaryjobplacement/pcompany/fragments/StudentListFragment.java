package it.polito.mobile.temporaryjobplacement.pcompany.fragments;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseQueryAdapter;

import java.util.List;

import it.polito.mobile.temporaryjobplacement.R;
import it.polito.mobile.temporaryjobplacement.TemporaryJobPlacementApp;
import it.polito.mobile.temporaryjobplacement.commons.utils.Connectivity;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.DialogManager;
import it.polito.mobile.temporaryjobplacement.model.JobOffer;
import it.polito.mobile.temporaryjobplacement.model.Student;
import it.polito.mobile.temporaryjobplacement.pcompany.viewmanaging.StudentQueryAdapter;
import it.polito.mobile.temporaryjobplacement.pstudent.viewmanaging.JobOfferQueryAdapter;

/**
 * A list fragment representing a list of Items. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {}.
 * <p/>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class StudentListFragment extends ListFragment {
    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;



    private Callbacks callbacks;
    /* A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activties to be notified of item
     * selections.
     */
    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        public void onItemSelected(Student student);

        /*
        *Callback to get the query factory to be used
        */
        public ParseQueryAdapter.QueryFactory<Student> getQueryFactory() throws Exception;

        /*
        *Callback to get favourites
        */
        public List<Student> getFavouriteStudents() throws ParseException;



        /*
         *Callback to update favourite
        */
        public void updateFavourites(Student favourite, boolean toBeAdded);

        /*
        *Callback to check if it is a favourite list
        */
        public boolean isFavouriteList();


        /*
        *Initialize profile
        */
        public void initializeProfile() throws Exception;

    }

    private ParseQueryAdapter<Student> studentsQueryAdapter;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public StudentListFragment() {
    }



    public static Fragment newInstance() {
        StudentListFragment fragment = new StudentListFragment();

        return fragment;
    }



 private List<Student> favourites = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Boolean isFavouriteList= callbacks.isFavouriteList();
        final ParseQueryAdapter.QueryFactory<Student>[] query= new ParseQueryAdapter.QueryFactory[]{null};


        new AsyncTask<Object, Object, Object>(){
            @Override
            protected Object doInBackground(Object... params) {

                try {
                    callbacks.initializeProfile();
                    favourites = callbacks.getFavouriteStudents();
                    query[0]=callbacks.getQueryFactory();
                } catch (Exception e) {
                    e.printStackTrace();
                    return  null;
                }
                return new Object();
            }
            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);

                try {

                    if (o == null) {
                        Connectivity.connectionError(getActivity());
                        return;
                    }
                    StudentQueryAdapter.InnerButtonManager innerButtonManager = null;
                    int row_layout_id = 0;
                    if (isFavouriteList) {
                        row_layout_id = R.layout.student_favourite_list_item;
                        innerButtonManager = new StudentQueryAdapter.InnerButtonManager() {
                            @Override
                            public void configureButton(final Student student, final ImageButton innerButton) {
                                innerButton.setVisibility(View.GONE); //Cambiare se si vuole mettere la x
                                innerButton.setBackgroundResource(android.R.drawable.ic_delete);
                                innerButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        DialogManager.toastMessage("delete", getActivity());
                                    }
                                });
                            }
                        };

                    } else {
                        row_layout_id = R.layout.student_list_item;
                        innerButtonManager = new StudentQueryAdapter.InnerButtonManager() {
                            @Override
                            public void configureButton(final Student student, final ImageButton innerButton) {
                                try {

                                    if (!favourites.contains(student)) {
                                        student.setFavourited(false);
                                        innerButton.setBackgroundResource(R.drawable.ic_action_not_important);
                                    } else {
                                        student.setFavourited(true);
                                        innerButton.setBackgroundResource(R.drawable.ic_action_important);
                                    }


                                    innerButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (!student.isFavourited()) {
                                               student.setFavourited(true);
                                                innerButton.setBackgroundResource(R.drawable.ic_action_important);
                                                callbacks.updateFavourites(student, true);

                                            } else {
                                                student.setFavourited(false);
                                                innerButton.setBackgroundResource(R.drawable.ic_action_not_important);
                                                callbacks.updateFavourites(student, false);
                                            }
                                        }
                                    });

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        };
                    }

                    studentsQueryAdapter = new StudentQueryAdapter(getActivity(), query[0], innerButtonManager, row_layout_id);
                    studentsQueryAdapter.setObjectsPerPage(TemporaryJobPlacementApp.objectsForPage);


                    studentsQueryAdapter.addOnQueryLoadListener(new ParseQueryAdapter.OnQueryLoadListener<Student>() {
                        @Override
                        public void onLoading() {
                        }

                        @Override
                        public void onLoaded(List<Student> list, Exception e) {
                            try {
                                setListShown(true);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
                    setListAdapter(studentsQueryAdapter);
                    setListShown(false);
                    firstTime = false;
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }.execute();








    }


    boolean firstTime=true;
    @Override
    public void onResume() {
        super.onResume();
        //PROBLEMA DELLA SINCRONIZZAZIONE DELLE STELLE
        //SE UN ITEM NON E' PREFERITO, POI CLICCO SU DI ESSO E LO AGGIUNGO AI PREFERITI;
        //CHE SUCCEDE QUANDO TORNO INDIETRO(OnResume)
        //OGNI ITEM DEVE ESSERE SINCRONIZZATO

        if(!firstTime) {
            new AsyncTask<Object, Object, Object>() {
                @Override
                protected Object doInBackground(Object... params) {
                    //reload remote favourites
                    try {
                        favourites = callbacks.getFavouriteStudents();
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                    return new Object();
                }

                @Override
                protected void onPostExecute(Object o) {
                    try {
                        super.onPostExecute(o);
                        if (o == null) {
                            Connectivity.connectionError(getActivity());
                            return;
                        }
                        //refresh listview
                        studentsQueryAdapter.notifyDataSetChanged();
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }.execute();


        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }

        view.setBackgroundColor(getActivity().getResources().getColor(R.color.foregroundColor));
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setEmptyView(buildEmptyTextView("No student found"));



    }
    private TextView buildEmptyTextView(String text) {
        TextView emptyView = new TextView(getActivity());
        emptyView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));
        emptyView.setTextColor(getResources().getColor(R.color.labelTextColor));
        emptyView.setText(text);
        emptyView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.textLabelSize));
        emptyView.setVisibility(View.GONE);
        emptyView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        //Add the view to the list view. This might be what you are missing
        ((ViewGroup) getListView().getParent()).addView(emptyView);

        return emptyView;
    }




    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
        callbacks.onItemSelected((Student) getListAdapter().getItem(position));
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



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        getListView().setChoiceMode(activateOnItemClick ? ListView.CHOICE_MODE_SINGLE : ListView.CHOICE_MODE_NONE);

    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }

}
