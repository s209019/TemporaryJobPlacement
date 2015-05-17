package it.polito.mobile.temporaryjobplacement.pstudent.viewmanaging;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.ParseQueryAdapter;

import it.polito.mobile.temporaryjobplacement.R;
import it.polito.mobile.temporaryjobplacement.commons.utils.TimeManager;
import it.polito.mobile.temporaryjobplacement.model.Application;

/**
 * Created by Enrico on 11/05/15.
 */
public class ApplicationsQueryAdapter extends ParseQueryAdapter<Application> {

    public ApplicationsQueryAdapter(Context context, QueryFactory<Application> queryFactory) {
        super(context, queryFactory);
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
    public View getItemView(final Application application, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = View.inflate(getContext(), R.layout.application_list_item, null);
        }

        TextView titleTextView = (TextView) convertView.findViewById(R.id.nameTextView);
        TextView companyTextView = (TextView) convertView.findViewById(R.id.companyTextView);
        TextView positionTextView = (TextView) convertView.findViewById(R.id.positionTextView);
        TextView timeAgoTextView = (TextView) convertView.findViewById(R.id.timeAgoTextView);
        TextView locationTextView=(TextView) convertView.findViewById(R.id.locationTextView);
        TextView statusTextView=(TextView) convertView.findViewById(R.id.statusTextView);

        titleTextView.setText(application.getJobOffer().getName());
        positionTextView.setText(application.getJobOffer().getContract());
        companyTextView.setText(application.getJobOffer().getCompany().getName());
        timeAgoTextView.setText((TimeManager.getFormattedDate(application.getCreatedAt(),"Applied")));
        locationTextView.setText(application.getJobOffer().getCompactLocation());
        statusTextView.setText(application.getStatus());

        return convertView;
    }


}
