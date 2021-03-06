package it.polito.mobile.temporaryjobplacement.pcompany.viewmanaging;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseQueryAdapter;

import java.util.Locale;

import it.polito.mobile.temporaryjobplacement.R;
import it.polito.mobile.temporaryjobplacement.commons.utils.DegreeAnalyzer;
import it.polito.mobile.temporaryjobplacement.commons.utils.TimeManager;
import it.polito.mobile.temporaryjobplacement.model.Student;

/**
 * Created by Enrico on 11/05/15.
 */
public class StudentQueryAdapter extends ParseQueryAdapter<Student> {

    private InnerButtonManager innerButtonManager;
    private int rowLayoutId;
    private boolean noResultsFound=true;
    private boolean firstTime=true;
    private Intent intentFilter;



    public interface InnerButtonManager {
        void configureButton(final Student student, final ImageButton innerButton);
    }

    public boolean isNoResultsFound() {
        return noResultsFound;
    }

    public StudentQueryAdapter(Context context, QueryFactory<Student> queryFactory, InnerButtonManager innerButtonManager, int rowLayoutId, Intent intent) {
        super(context, queryFactory);
        this.innerButtonManager=innerButtonManager;
        this.rowLayoutId=rowLayoutId;
        this.intentFilter=intent;
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
    public View getItemView(final Student student, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = View.inflate(getContext(), rowLayoutId, null);
        }

        if(firstTime){
            noResultsFound=true;
            firstTime=false;
        }

        if(intentFilter!=null) {
            if(intentFilter.hasExtra("keywords")) {
                for(String keyword: intentFilter.getStringArrayListExtra("keywords")){
                    if(!((String)student.get("skills_search")).contains(keyword))
                        return new View(getContext());
                }
            }

            if(intentFilter.hasExtra("degree")) {

                if(!DegreeAnalyzer.firstIsBetterOrEqual(student.getBestDegree(), intentFilter.getStringExtra("degree")))
                    return new View(getContext());
            }

            if(intentFilter.hasExtra("languages")) {
                boolean matchTrovato=false;

                for(String language: intentFilter.getStringArrayListExtra("languages")){
                    Log.d("DEBUG", language+"          "+student.getLanguageSkills().toString());
                    if(student.getLanguageSkills().toString().contains(language.toUpperCase())) {
                        matchTrovato=true;
                        break;
                    }
                }

                if(!matchTrovato)
                    return new View(getContext());
            }



        }

        noResultsFound=false;


        TextView nameTextView = (TextView) convertView.findViewById(R.id.nameTextView);
        TextView degreeTextView = (TextView) convertView.findViewById(R.id.companyTextView);
        TextView ageTextView = (TextView) convertView.findViewById(R.id.timeAgoTextView);

        final ImageButton innerButton =(ImageButton) convertView.findViewById(R.id.innerButton);

        nameTextView.setText(student.getLastName().toUpperCase(Locale.ENGLISH)+" "+student.getFirstName().toUpperCase(Locale.ENGLISH));


        try {
            if (student.getBestDegree().equals("")) throw new Exception();
            degreeTextView.setText(student.getBestDegree().toUpperCase());
        }catch (Exception e ){
            degreeTextView.setText("Degree not specified");
        }




        try {
            ageTextView.setText(student.getAge());
        }catch (Exception e){
            ageTextView.setText("Age not specified");
        }


        innerButtonManager.configureButton(student, innerButton);

        return convertView;
    }


}
