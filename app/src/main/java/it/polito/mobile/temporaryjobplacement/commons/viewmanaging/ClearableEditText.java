package it.polito.mobile.temporaryjobplacement.commons.viewmanaging;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

/*
* NOTE:
* ClearableEditText  must contain exclusively EditText"
*/





public class ClearableEditText extends RelativeLayout {

    private static int EDIT_TEXT_PADDING_RIGHT=40;
    private static int BUTTON_MARGIN_RIGHT =3;
    private static int BUTTON_SIZE=37;
    private LayoutInflater inflater = null;
    private AutoCompleteTextView edit_text;
    private Button btn_clear;
    private Context ctx;
    private boolean viewCompleted;



    public ClearableEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        ctx=context;

    }

    public ClearableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        ctx=context;


    }



    public ClearableEditText(Context context) {
        super(context);
        ctx=context;
    }


    private void completeView() {
        setEditText();
        buildAndSetButtonClear(ctx);
        setListeners();
        viewCompleted=true;
    }


    private void setEditText() {

        //AutoCompleteTextView must be already inserted inside ClearableEditText, in xml file
        edit_text=(AutoCompleteTextView)ClearableEditText.this.getChildAt(0);
        edit_text.setPadding(edit_text.getPaddingLeft(),edit_text.getPaddingTop(),DP2PX(EDIT_TEXT_PADDING_RIGHT),edit_text.getPaddingBottom());
    }



    private void buildAndSetButtonClear(Context context) {
        /*     android:layout_width="30dip"
                    android:layout_height="30dip"
                    android:layout_alignParentRight="true"
                    android:layout_centerVertical="true"
                    android:background="@android:drawable/ic_delete"
                    android:layout_marginRight="5dip"/>
        */
        btn_clear=new Button(context);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(DP2PX(BUTTON_SIZE), DP2PX(BUTTON_SIZE));
        layoutParams.setMargins(0,0, DP2PX(BUTTON_MARGIN_RIGHT),0);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        btn_clear.setLayoutParams(layoutParams);
        btn_clear.setBackgroundResource(android.R.drawable.ic_menu_close_clear_cancel);
        btn_clear.setVisibility(View.INVISIBLE);
        this.addView(btn_clear);

    }




    private void setListeners(){
        //clear edit_text when btn_clear is pressed
        btn_clear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_text.setText("");
            }
        });

        //hide/show btn_clear
        edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0)
                    btn_clear.setVisibility(View.VISIBLE);
                else
                    btn_clear.setVisibility(View.INVISIBLE);
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }


    private int DP2PX(float dp){
        int px = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
        return px;
    }



    public EditText editText(){
        if(!viewCompleted) completeView();
        return edit_text;
    }




}