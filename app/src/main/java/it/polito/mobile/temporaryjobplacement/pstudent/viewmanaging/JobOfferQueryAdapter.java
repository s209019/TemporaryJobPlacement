package it.polito.mobile.temporaryjobplacement.pstudent.viewmanaging;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.ParseQueryAdapter;

import it.polito.mobile.temporaryjobplacement.R;
import it.polito.mobile.temporaryjobplacement.commons.utils.TimeManager;
import it.polito.mobile.temporaryjobplacement.model.JobOffer;

/**
 * Created by Enrico on 11/05/15.
 */
public class JobOfferQueryAdapter extends ParseQueryAdapter<JobOffer> {

    private InnerButtonManager innerButtonManager;
    private int rowLayoutId;
    private boolean noResultsFound=true;
    private boolean firstTime=true;
    private Intent intentFilter;

    public boolean isNoResultsFound() {
        return noResultsFound;
    }

    public interface InnerButtonManager {
        void configureButton(final JobOffer offer,final ImageButton innerButton);
    }


    public JobOfferQueryAdapter(Context context, ParseQueryAdapter.QueryFactory<JobOffer> queryFactory, InnerButtonManager innerButtonManager,int rowLayoutId) {
        super(context, queryFactory);
        this.innerButtonManager=innerButtonManager;
        this.rowLayoutId=rowLayoutId;
    }

    public JobOfferQueryAdapter(Context context, ParseQueryAdapter.QueryFactory<JobOffer> queryFactory, InnerButtonManager innerButtonManager,int rowLayoutId, Intent intentFilter) {
        super(context, queryFactory);
        this.innerButtonManager=innerButtonManager;
        this.rowLayoutId=rowLayoutId;
        this.intentFilter=intentFilter;
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
                ((TextView) textView).setText("LOADING MORE...");
                progressBar.setVisibility(View.VISIBLE);
                arrowImage.setVisibility(View.GONE);

                finalV.performClick();

            }
        });

        noResultsFound=false;
        firstTime=true;

        return v;
    }

    //Inflate della vista del singolo item
    @Override
    public View getItemView(final JobOffer jobOffer, View convertView, ViewGroup parent) {

        if (convertView == null || !(convertView instanceof LinearLayout)) {
            convertView = View.inflate(getContext(), rowLayoutId, null);
        }

        if(firstTime){
            noResultsFound=true;
            firstTime=false;
        }

        if(intentFilter!=null) {
            if(intentFilter.hasExtra("keywords")) {
                for(String keyword: intentFilter.getStringArrayListExtra("keywords")){
                    if(!jobOffer.getKeywords().contains(keyword))
                        return new View(getContext());
                }
            }

            if(intentFilter.hasExtra("industries")) {
                boolean matchTrovato=false;

                for(String industry: intentFilter.getStringArrayListExtra("industries")){
                    if(jobOffer.getIndustries().contains(industry)) {
                        matchTrovato=true;
                        break;
                    }
                }

                if(!matchTrovato)
                    return new View(getContext());
            }

            if(intentFilter.hasExtra("education")) {
                boolean matchTrovato=false;

                for(String education: intentFilter.getStringArrayListExtra("education")){
                    if(jobOffer.getEducation().contains(education)) {
                        matchTrovato=true;
                        break;
                    }
                }

                if(!matchTrovato)
                    return new View(getContext());

            }

            if(intentFilter.hasExtra("careerLevel")) {
                boolean matchTrovato=false;

                for(String careerLevel: intentFilter.getStringArrayListExtra("careerLevel")){
                    if(jobOffer.getCareerLevel().contains(careerLevel)) {
                        matchTrovato=true;
                        break;
                    }
                }

                if(!matchTrovato)
                    return new View(getContext());

            }

            if(intentFilter.hasExtra("contract")) {
                boolean matchTrovato=false;

                for(String contract: intentFilter.getStringArrayListExtra("contract")){
                    if(jobOffer.getContract().contains(contract)) {
                        matchTrovato=true;
                        break;
                    }
                }

                if(!matchTrovato)
                    return new View(getContext());

            }


        }

        noResultsFound=false;

        TextView titleTextView = (TextView) convertView.findViewById(R.id.nameTextView);
        TextView companyTextView = (TextView) convertView.findViewById(R.id.companyTextView);
        TextView positionTextView = (TextView) convertView.findViewById(R.id.positionTextView);
        TextView timeAgoTextView = (TextView) convertView.findViewById(R.id.timeAgoTextView);
        TextView locationTextView=(TextView) convertView.findViewById(R.id.locationTextView);
        final ImageButton innerButton =(ImageButton) convertView.findViewById(R.id.innerButton);

        titleTextView.setText(jobOffer.getName());
        positionTextView.setText(jobOffer.getContract());
        companyTextView.setText(jobOffer.getCompany().getName());
        timeAgoTextView.setText((TimeManager.getFormattedDate(jobOffer.getCreatedAt())));
        locationTextView.setText(jobOffer.getCompactLocation());

        innerButtonManager.configureButton(jobOffer, innerButton);

        return convertView;
    }


}
