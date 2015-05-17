package it.polito.mobile.temporaryjobplacement.commons.viewmanaging;

import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import it.polito.mobile.temporaryjobplacement.R;
import it.polito.mobile.temporaryjobplacement.model.Company;

/*
* NOTE:
* ClearableEditText  must contain exclusively EditText"
*/


public class SavableEditText extends RelativeLayout {

    private static int EDIT_TEXT_PADDING_RIGHT=40;
    private static int BUTTON_MARGIN_RIGHT =3;
    private static int BUTTON_SIZE=37;
    private LayoutInflater inflater = null;
    private EditText edit_text;
    private ImageButton btn_save;
    private Context ctx;
    private boolean viewCompleted;

    private View.OnClickListener btn_save_listener=null;
    private String savedText="STUB";


    public SavableEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        ctx=context;

    }

    public SavableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        ctx=context;


    }



    public SavableEditText(Context context) {
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

        //EditText must be already inserted inside ClearableEditText, in xml file
        edit_text=(EditText)SavableEditText.this.getChildAt(0);
        edit_text.setPadding(edit_text.getPaddingLeft(), edit_text.getPaddingTop(), DP2PX(EDIT_TEXT_PADDING_RIGHT), edit_text.getPaddingBottom());
    }



    private void buildAndSetButtonClear(Context context) {
        /*     android:layout_width="30dip"
                    android:layout_height="30dip"
                    android:layout_alignParentRight="true"
                    android:layout_centerVertical="true"
                    android:background="@android:drawable/ic_delete"
                    android:layout_marginRight="5dip"/>
        */
        btn_save=new ImageButton(context);
        btn_save.setBackgroundResource(R.drawable.button_drawable2);
        LayoutParams layoutParams = new LayoutParams(DP2PX(BUTTON_SIZE), DP2PX(BUTTON_SIZE));
        layoutParams.setMargins(0, 0, DP2PX(BUTTON_MARGIN_RIGHT),0);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        btn_save.setLayoutParams(layoutParams);
        btn_save.setImageResource(R.drawable.ic_action_save);
        btn_save.setVisibility(View.INVISIBLE);
        this.addView(btn_save);

    }




    private void setListeners(){



        //hide/show btn_save
        edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
                //DialogManager.toastMessage(savedText+";"+text,getContext());

                if (!savedText.equals(text.toString())) {
                    btn_save.setVisibility(View.VISIBLE);
                } else {
                    btn_save.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
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

    public void setButtonSaveListener(View.OnClickListener listener){
        this.btn_save_listener=listener;
        btn_save.setOnClickListener(btn_save_listener);
    }

    public  void setSavedText(String savedText){
        this.savedText=savedText;
        this.btn_save.setVisibility(View.INVISIBLE);
    }




}