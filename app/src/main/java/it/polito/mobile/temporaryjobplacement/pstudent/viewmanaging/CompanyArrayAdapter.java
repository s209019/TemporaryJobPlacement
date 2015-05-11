package it.polito.mobile.temporaryjobplacement.pstudent.viewmanaging;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import it.polito.mobile.temporaryjobplacement.R;
import it.polito.mobile.temporaryjobplacement.pstudent.model.Company;

public class CompanyArrayAdapter extends ArrayAdapter<Company> {
    private Activity activity;
    private int rowIdLayout;
    private List<Company> companies;

    private InnerButtonManager innerButtonManager;
    public interface InnerButtonManager {
        public void configureButton(final Company company,final ImageButton innerButton);
    }



    class ViewHolder{
        TextView titleTextView;
        ImageButton innerButton;
    }




    public CompanyArrayAdapter(Activity activity, int rowIdLayout, List<Company> companies,InnerButtonManager innerButtonManager) {
        super(activity, rowIdLayout,companies);
        this.activity=activity;
        this.rowIdLayout=rowIdLayout;
        this.companies=companies;
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
            holder.innerButton =(ImageButton) convertView.findViewById(R.id.innerButton);
            convertView.setTag(holder);
        }else{
            //if convertView is not null, get its child from viewHolder associated to the convertView
            holder = (ViewHolder) convertView.getTag();
        }


        final Company company=companies.get(position);
        holder.titleTextView.setText(company.getTitle());

        innerButtonManager.configureButton(company,holder.innerButton);






        return convertView;

    }

}
