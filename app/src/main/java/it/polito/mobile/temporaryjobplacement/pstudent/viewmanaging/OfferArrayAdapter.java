package it.polito.mobile.temporaryjobplacement.pstudent.viewmanaging;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

public class OfferArrayAdapter{

}


/*

import it.polito.mobile.temporaryjobplacement.R;
import it.polito.mobile.temporaryjobplacement.pstudent.model.Offer;

public class OfferArrayAdapter extends ArrayAdapter<Offer> {
    private Activity activity;
    private int rowIdLayout;
    private List<Offer> offers;


    private InnerButtonManager innerButtonManager;
    public interface InnerButtonManager {
        public void configureButton(final Offer offer,final ImageButton innerButton);
    }



    class ViewHolder{
        TextView titleTextView;
        TextView companyTextView;
        TextView timeAgoTextView;
        TextView locationTextView;
        TextView positionTextView;
        ImageButton innerButton;
    }




    public OfferArrayAdapter(Activity activity,int rowIdLayout, List<Offer> offers,InnerButtonManager innerButtonManager) {
        super(activity, rowIdLayout,offers);
        this.activity=activity;
        this.rowIdLayout=rowIdLayout;
        this.offers=offers;
        this.innerButtonManager=innerButtonManager;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;

        if(convertView == null){
            //if convertView is null, inflate it from xml resource, and save its children in viewHolder
            convertView = activity.getLayoutInflater().inflate(rowIdLayout, parent, false);
            holder = new ViewHolder();
            holder.titleTextView = (TextView) convertView.findViewById(R.id.nameTextView);
            holder.companyTextView = (TextView) convertView.findViewById(R.id.companyTextView);
            holder.positionTextView = (TextView) convertView.findViewById(R.id.positionTextView);
            holder.timeAgoTextView = (TextView) convertView.findViewById(R.id.timeAgoTextView);
            holder.locationTextView=(TextView) convertView.findViewById(R.id.locationTextView);
            holder.innerButton =(ImageButton) convertView.findViewById(R.id.innerButton);
            convertView.setTag(holder);
        }else{
            //if convertView is not null, get its child from viewHolder associated to the convertView
            holder = (ViewHolder) convertView.getTag();
        }


        final Offer offer=offers.get(position);
        holder.titleTextView.setText(offer.getTitle());
        holder.positionTextView.setText(offer.getPosition());
        holder.companyTextView.setText(offer.getCompany());
        holder.timeAgoTextView.setText(offer.getDate());
        holder.locationTextView.setText(offer.getLocation());


        //callback to configure innerbutton
        innerButtonManager.configureButton(offer,holder.innerButton);



        return convertView;

    }






}
*/