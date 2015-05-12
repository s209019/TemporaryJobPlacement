package it.polito.mobile.temporaryjobplacement.pstudent.viewmanaging;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.ParseQueryAdapter;

import it.polito.mobile.temporaryjobplacement.R;
import it.polito.mobile.temporaryjobplacement.model.Company;

/**
 *
 */
public class CompanyQueryAdapter extends ParseQueryAdapter<Company> {

    private InnerButtonManager innerButtonManager;
    private int rowLayoutId;


    public interface InnerButtonManager {
        void configureButton(final Company company, final ImageButton innerButton);
    }


    public CompanyQueryAdapter(Context context, QueryFactory<Company> queryFactory, InnerButtonManager innerButtonManager, int rowLayoutId) {
        super(context, queryFactory);
        this.innerButtonManager=innerButtonManager;
        this.rowLayoutId=rowLayoutId;
    }

    @Override
    public View getNextPageView(View v, ViewGroup parent) {
        if (v == null) {
            // R.layout.adapter_next_page contains an ImageView with a custom graphic
            // and a TextView.
            v = View.inflate(getContext(), R.layout.next_page_list_item, null);
        }
        TextView textView = (TextView) v.findViewById(R.id.nextPageTextViewId);
        final ProgressBar progressBar=(ProgressBar)v.findViewById(R.id.progress);
        final ImageView arrowImage=(ImageView)v.findViewById(R.id.arrowImage);
        progressBar.setVisibility(View.GONE);
        arrowImage.setVisibility(View.VISIBLE);
        textView.setText("SHOW MORE");
        textView.setClickable(true);
        final View finalV = v;
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View textView) {
                ((TextView)textView).setText("LOADING MORE...");
                progressBar.setVisibility(View.VISIBLE);
                arrowImage.setVisibility(View.GONE);

                finalV.performClick();

            }
        });

        return v;
    }

    //Inflate della vista del singolo item
    @Override
    public View getItemView(final Company company, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = View.inflate(getContext(), rowLayoutId, null);
        }



        TextView titleTextView = (TextView) convertView.findViewById(R.id.nameTextView);
        ImageButton innerButton =(ImageButton) convertView.findViewById(R.id.innerButton);




        titleTextView.setText(company.getName());

             innerButtonManager.configureButton(company,innerButton);


    return convertView;
    }


}
