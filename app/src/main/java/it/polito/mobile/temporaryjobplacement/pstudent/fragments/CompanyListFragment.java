package it.polito.mobile.temporaryjobplacement.pstudent.fragments;

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
import it.polito.mobile.temporaryjobplacement.model.Company;
import it.polito.mobile.temporaryjobplacement.pstudent.viewmanaging.CompanyQueryAdapter;

/**
 * A list fragment representing a list of Items. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {}.
 * <p/>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class CompanyListFragment extends ListFragment {

    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";
    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;



    private Callbacks mCallbacks;
    /* A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activties to be notified of item
     * selections.
     */
    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        public void onItemSelected(Company company);

        /*
       *Callback to get the query factory to be used
       */
        public ParseQueryAdapter.QueryFactory<Company> getCompanyQueryFactory() throws Exception;
        /*
       *Callback to get favourites
       */
        public List<Company> getFavouritesCompanies() throws ParseException;

        /*
         *Callback to update favourite
        */
        public void updateFavourites(Company favourite, boolean toBeAdded);

        /*
       *Callback to check if it is a favourite list
       */
        public boolean isFavouriteList();

        /*
       *Callback for when the delete inner button is presser
       */
        public void onDeleteButtonCompanyPressed(Company company);

        /*
        *Callback to check if it is a favourite list
        */
        public void onFavouriteButtonCompanyPressed(Company company);

        /*
       *Initialize profile
       */
        public void initializeProfile() throws Exception;

    }



    private ParseQueryAdapter<Company> companiesQueryAdapter;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CompanyListFragment() {
    }



    public static Fragment newInstance() {
        CompanyListFragment fragment = new CompanyListFragment();
       /* Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return fragment;
    }



    private List<Company> favourites = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Boolean isFavouriteList=mCallbacks.isFavouriteList();
        final ParseQueryAdapter.QueryFactory<Company>[] query= new ParseQueryAdapter.QueryFactory[]{null};

 ;
        new AsyncTask<Object, Object, Object>() {
            @Override
            protected Object doInBackground(Object... params) {
                try {
                    mCallbacks.initializeProfile();
                    favourites = mCallbacks.getFavouritesCompanies();
                    query[0]=mCallbacks.getCompanyQueryFactory();
                }catch(Exception e){
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

                CompanyQueryAdapter.InnerButtonManager innerButtonManager = null;
                if (isFavouriteList) {
                    innerButtonManager = new CompanyQueryAdapter.InnerButtonManager() {
                        @Override
                        public void configureButton(Company company, ImageButton innerButton) {
                            innerButton.setVisibility(View.VISIBLE);
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
                    innerButtonManager = new CompanyQueryAdapter.InnerButtonManager() {
                        @Override
                        public void configureButton(final Company company, final ImageButton innerButton) {
                            try {
                                if (!favourites.contains(company)) {
                                    company.setFavourited(false);
                                    innerButton.setBackgroundResource(R.drawable.ic_action_not_important);
                                } else {
                                    company.setFavourited(true);
                                    innerButton.setBackgroundResource(R.drawable.ic_action_important);
                                }

                                innerButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (!company.isFavourited()) {
                                            company.setFavourited(true);
                                            innerButton.setBackgroundResource(R.drawable.ic_action_important);
                                            mCallbacks.updateFavourites(company, true);

                                        } else {
                                            company.setFavourited(false);
                                            innerButton.setBackgroundResource(R.drawable.ic_action_not_important);
                                            mCallbacks.updateFavourites(company, false);
                                        }
                                    }
                                });

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };

                }


                companiesQueryAdapter = new CompanyQueryAdapter(getActivity(), query[0], innerButtonManager, R.layout.company_list_item);
                companiesQueryAdapter.setObjectsPerPage(TemporaryJobPlacementApp.objectsForPage);

                companiesQueryAdapter.addOnQueryLoadListener(new ParseQueryAdapter.OnQueryLoadListener<Company>() {
                    @Override
                    public void onLoading() {
                    }

                    @Override
                    public void onLoaded(List<Company> list, Exception e) {
                        try{
                            setListShown(true);
                        }catch(Exception ex){
                            e.printStackTrace();
                        }
                    }
                });
                setListAdapter(companiesQueryAdapter);
                setListShown(false);
                firstTime=false;


            }
        }.execute();
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
        getListView().setEmptyView(buildEmptyTextView("No Company found"));


    }
    private TextView buildEmptyTextView(String text) {
        TextView emptyView = new TextView(getActivity());
        emptyView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        emptyView.setTextColor(getResources().getColor(R.color.labelTextColor));
        emptyView.setText(text);
        emptyView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.textLabelSize));
        emptyView.setVisibility(View.GONE);
        emptyView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        //Add the view to the list view. This might be what you are missing
        ((ViewGroup) getListView().getParent()).addView(emptyView);

        return emptyView;
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
                        favourites = mCallbacks.getFavouritesCompanies();
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
                    //refresh listview
                    companiesQueryAdapter.notifyDataSetChanged();

                }
            }.execute();



        }
    }








    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
        mCallbacks.onItemSelected((Company) getListAdapter().getItem(position));
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
