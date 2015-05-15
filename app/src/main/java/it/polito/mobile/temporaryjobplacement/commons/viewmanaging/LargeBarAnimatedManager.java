package it.polito.mobile.temporaryjobplacement.commons.viewmanaging;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

import it.polito.mobile.temporaryjobplacement.R;

/**
 * This class requires inflating xml file fragment_offer_detail_content.xml or fragment_company_detail.xml
 */


public class LargeBarAnimatedManager implements ObservableScrollViewCallbacks {

    private View scrollableBar;
    private ObservableScrollView mScrollView;
    private TextView title;
    private TextView subTitle;
    private View fixedBar;
    private ImageButton homeButton;
    private ImageButton shareButton;
    private RelativeLayout favouriteButton;
    private ImageButton backButton;
    private View rootView;
    private ActionBarActivity activity;
    
    //@color/primaryColorSemiTransparent


    public LargeBarAnimatedManager(View rootView,ActionBarActivity activity) {
        this.rootView = rootView;
        this.activity=activity;

        scrollableBar = rootView.findViewById(R.id.scrollableBar);
        mScrollView = (ObservableScrollView) rootView.findViewById(R.id.scrollView1);
        mScrollView.setScrollViewCallbacks(this);
        mScrollView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        title =(TextView)rootView.findViewById(R.id.titleTextView);
        subTitle =(TextView)rootView.findViewById(R.id.companysubTitleTextView);

        favouriteButton=(RelativeLayout)rootView.findViewById(R.id.favouriteButton);
        fixedBar=rootView.findViewById(R.id.fixedBar);
        homeButton =(ImageButton)rootView.findViewById(R.id.homeButton);
        shareButton =(ImageButton)rootView.findViewById(R.id.shareButton);

        backButton =(ImageButton)rootView.findViewById(R.id.backButton);

    }


    @Override
    public void onScrollChanged(int scrollY, boolean b, boolean b1) {
        scrollableBar.setTranslationY(scrollY / 2);

        //manage horizontal translation of title e subtitle textViews
        title.setTranslationX(scrollY / 2);
        subTitle.setTranslationX(scrollY / 2);

        //manage horizontal translation of homeButton and shareButton
        if(scrollY/2<favouriteButton.getWidth()) {
            homeButton.setTranslationX(-scrollY / 2);
            shareButton.setTranslationX(-scrollY / 2);
        }else{
            homeButton.setTranslationX(-favouriteButton.getWidth());
            shareButton.setTranslationX(-favouriteButton.getWidth());
        }

        //manage vertical translation of favouriteButton
        if (scrollY < scrollableBar.getHeight()*2){
            favouriteButton.setTranslationY(-scrollY / 2);
        }
        else {
            favouriteButton.setTranslationY(-scrollableBar.getHeight());
        }

        //manage background of scrolled
        //the top has been reached
        if(scrollY==0){
           scrollableBar.setBackgroundColor(activity.getResources().getColor(R.color.primaryColor));
        }else{
            scrollableBar.setBackgroundColor(activity.getResources().getColor(R.color.secondaryColor));

        }

        //manage hide/show of title and subtitle textViews
        View view = (View) mScrollView.getChildAt(mScrollView.getChildCount() - 1);
        int diff = (view.getBottom() - (mScrollView.getHeight() + mScrollView.getScrollY()));
        // if diff is zero, then the bottom has been reached
        if (diff == 0) {
            title.setVisibility(View.GONE);
            subTitle.setVisibility(View.GONE);
        }else{
            title.setVisibility(View.VISIBLE);
            subTitle.setVisibility(View.VISIBLE);}


    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }


    public TextView getTitleTextView() {
        return title;
    }



    public TextView getSubTitleTextView() {
        return subTitle;
    }



    public ImageButton getHomeButton() {
        return homeButton;
    }



    public ImageButton getShareButton() {
        return shareButton;
    }


    public ImageButton getBackButton() {
        return backButton;
    }



    public RelativeLayout getFavouriteButton() {
        return favouriteButton;
    }


}
