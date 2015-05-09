package it.polito.mobile.temporaryjobplacement.commons.viewmanaging;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import it.polito.mobile.temporaryjobplacement.R;
import it.polito.mobile.temporaryjobplacement.model.Message;
import it.polito.mobile.temporaryjobplacement.pstudent.model.Offer;

/**
 * Created by Nicolò on 04/05/2015.
 */
public class MessageArrayAdapter extends ArrayAdapter<Message> {
    private Activity activity;
    private int rowIdLayout;
    private List<Message> messages;


    private InnerButtonManager innerButtonManager;
    public interface InnerButtonManager {
        public void configureButton(final Message message, final ImageButton innerButton);
    }



    class ViewHolder{
        TextView interlocutorTextView;
        TextView timestampTextView;
        TextView objectTextView;
        ImageButton innerButton;
    }




    public MessageArrayAdapter(Activity activity, int rowIdLayout, List<Message> messages, InnerButtonManager innerButtonManager) {
        super(activity, rowIdLayout,messages);
        this.activity=activity;
        this.rowIdLayout=rowIdLayout;
        this.messages=messages;
        this.innerButtonManager=innerButtonManager;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;

        if(convertView == null){
            //if convertView is null, inflate it from xml resource, and save its children in viewHolder
            convertView = activity.getLayoutInflater().inflate(rowIdLayout, parent, false);
            holder = new ViewHolder();
            holder.interlocutorTextView = (TextView) convertView.findViewById(R.id.interlocutorTextView);
            holder.timestampTextView = (TextView) convertView.findViewById(R.id.timestampTextView);
            holder.objectTextView = (TextView) convertView.findViewById(R.id.objectTextView);
            holder.innerButton =(ImageButton) convertView.findViewById(R.id.innerButton);
            convertView.setTag(holder);
        }else{
            //if convertView is not null, get its child from viewHolder associated to the convertView
            holder = (ViewHolder) convertView.getTag();
        }


        final Message message=messages.get(position);
        holder.interlocutorTextView.setText(message.getInterlocutor());
        holder.timestampTextView.setText("12.15");//message.getTimestamp()+"");
        holder.objectTextView.setText(message.getObject());


        //if not read set all bold
        if(!message.isRead() && position==0){
            holder.interlocutorTextView.setTypeface( holder.interlocutorTextView.getTypeface(),Typeface.BOLD);
            holder.timestampTextView.setTypeface( holder.timestampTextView.getTypeface(),Typeface.BOLD);
            holder.objectTextView.setTypeface( holder.objectTextView.getTypeface(),Typeface.BOLD);
        }


        //callback to configure innerbutton
        innerButtonManager.configureButton(message,holder.innerButton);



        return convertView;

    }






}
