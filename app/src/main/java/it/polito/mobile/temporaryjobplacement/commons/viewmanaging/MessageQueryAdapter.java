package it.polito.mobile.temporaryjobplacement.commons.viewmanaging;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.ParseQueryAdapter;

import it.polito.mobile.temporaryjobplacement.R;
import it.polito.mobile.temporaryjobplacement.commons.utils.TimeManager;
import it.polito.mobile.temporaryjobplacement.model.Application;
import it.polito.mobile.temporaryjobplacement.model.Message;

/**
 * Created by Enrico on 11/05/15.
 */
public class MessageQueryAdapter extends ParseQueryAdapter<Message> {

    private int rowLayoutId;
    private boolean loggedAsStudent;  //true in the student part of the application, false in the company part of the applicatio
    private boolean inbox; //true if you want to display RECEIVED messages, false if you want to display SENT messages

    public MessageQueryAdapter(Context context, QueryFactory<Message> queryFactory,int rowLayoutId, boolean loggedAsStudent, boolean inbox) {
        super(context, queryFactory);
        this.rowLayoutId=rowLayoutId;
        this.loggedAsStudent=loggedAsStudent;
        this.inbox=inbox;

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
    public View getItemView(final Message message, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = View.inflate(getContext(), rowLayoutId, null);
        }

        TextView interlocutorTextView = (TextView) convertView.findViewById(R.id.interlocutorTextView);
        TextView timestampTextView = (TextView) convertView.findViewById(R.id.timestampTextView);
        TextView subjectTextView = (TextView) convertView.findViewById(R.id.subjectTextView);


        if(loggedAsStudent)
            interlocutorTextView.setText(message.getCompany().getName());
        else
            interlocutorTextView.setText(message.getStudent().getName());



        if(inbox)
            timestampTextView.setText(TimeManager.getFormattedTimestamp(message.getCreatedAt(), "Received"));
        else
            timestampTextView.setText(TimeManager.getFormattedTimestamp(message.getCreatedAt(), "Sent"));


        subjectTextView.setText(message.getSubject());

        //if not read set all bold
        if(!message.isRead() && inbox){
            interlocutorTextView.setTypeface( interlocutorTextView.getTypeface(), Typeface.BOLD);
            timestampTextView.setTypeface( timestampTextView.getTypeface(),Typeface.BOLD);
            subjectTextView.setTypeface( subjectTextView.getTypeface(),Typeface.BOLD);

            ((ImageView) convertView.findViewById(R.id.mailIcon)).setBackgroundResource(R.drawable.ic_action_email_new);
        }

        return convertView;
    }


}
