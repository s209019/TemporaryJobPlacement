package it.polito.mobile.temporaryjobplacement.pstudent.fragments;



import android.app.AlertDialog;
import android.os.Bundle;

import android.support.v4.app.DialogFragment;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import it.polito.mobile.temporaryjobplacement.R;


public class MultipleChoiceDialogFragment extends DialogFragment {


    private static OnAllItemsCheckedListener onAllItemsCheckedListener;
    public interface OnAllItemsCheckedListener {
        public void getAllItemsChecked(ArrayList<String> selectedItems);
    }

    public static MultipleChoiceDialogFragment newInstance(String title, ArrayList<String> items,String[] alreadyCheckedIndustries, OnAllItemsCheckedListener onAllItemsCheckedList) {
        MultipleChoiceDialogFragment fragment = new MultipleChoiceDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putStringArrayList("items", items);
        args.putStringArray("alreadyCheckedIndustries", alreadyCheckedIndustries);
        fragment.setArguments(args);
        onAllItemsCheckedListener=onAllItemsCheckedList;
        return fragment;
    }

    public MultipleChoiceDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public AlertDialog onCreateDialog(Bundle savedInstanceState) {
        String title = null;
        ArrayList<String> items=null;
        String[]alreadyCheckedIndustries=null;
        final ArrayList<String> allItemsChecked=new ArrayList<String>();
        if (getArguments() != null) {
            title = getArguments().getString("title");
            items=getArguments().getStringArrayList("items");
            alreadyCheckedIndustries=getArguments().getStringArray("alreadyCheckedIndustries");
        }

        //internal view -->listview
        View internalView=getActivity().getLayoutInflater().inflate(android.R.layout.list_content,null);
        final ListView listView=(ListView)internalView.findViewById(android.R.id.list);
        final ArrayAdapter adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_multiple_choice,items);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setAdapter(adapter);

        //reset already checked industries
        if(alreadyCheckedIndustries!=null && alreadyCheckedIndustries.length!=0){
            for(int i=0; i<adapter.getCount();i++){
                for(int j=0; j<alreadyCheckedIndustries.length;j++){
                    if(((String)adapter.getItem(i)).equals(alreadyCheckedIndustries[j])) {
                        listView.setItemChecked(i, true);
                    }
                }

            }
        }





        //wrapper dialog
        AlertDialog aDialog=null;
        AlertDialog.Builder alertBuilder=new AlertDialog.Builder(getActivity());
        if(title!=null)alertBuilder.setTitle(title);
        alertBuilder.setCancelable(true);
        alertBuilder.setIcon(android.R.drawable.arrow_up_float);
        alertBuilder.setPositiveButton("OK", new android.content.DialogInterface.OnClickListener() {
            @Override
            public void onClick(android.content.DialogInterface dialog, int which) {

                SparseBooleanArray checkedPositions = listView.getCheckedItemPositions();
                for (int i = 0; i < adapter.getCount(); i++) {
                    //if element with key i exists, get item corresponding to index i
                    if (checkedPositions.indexOfKey(i) != -1) {
                        if (checkedPositions.get(i) == true)
                            allItemsChecked.add((String) adapter.getItem(i));
                    }

                }

                onAllItemsCheckedListener.getAllItemsChecked(allItemsChecked);
            }
        });
        alertBuilder.setNeutralButton("Cancel", new android.content.DialogInterface.OnClickListener() {
            @Override
            public void onClick(android.content.DialogInterface dialog, int which) {

            }
        });
        alertBuilder.setNegativeButton("Clear", new android.content.DialogInterface.OnClickListener() {
            @Override
            public void onClick(android.content.DialogInterface dialog, int which) {
                onAllItemsCheckedListener.getAllItemsChecked(new ArrayList<String>());
            }});

        alertBuilder.setView(internalView);
        aDialog=alertBuilder.create();

        return aDialog;
    }




    @Override
    public void onDetach() {
        super.onDetach();
        onAllItemsCheckedListener = null;
    }



}
