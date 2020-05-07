package com.example.simpleapp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class ReviewItemView extends LinearLayout {
    ImageView mUserImage;
    TextView mUserId;
    TextView mWriteTime;
    RatingBar mRating;
    TextView mUserReview;
    TextView mRecommend;
    Button mReportBtn;

    public ReviewItemView(Context context) {
        super(context);
        init(context);
    }

    public ReviewItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.review_item_view, this, true);

        mUserImage = (ImageView)findViewById(R.id.userImage);
        mUserId = (TextView)findViewById(R.id.userId);
        mWriteTime = (TextView)findViewById(R.id.writeTime);
        mRating = (RatingBar)findViewById(R.id.rating);
        mUserReview = (TextView)findViewById(R.id.userReview);
        mRecommend = (TextView)findViewById(R.id.recommend);
        mReportBtn = (Button)findViewById(R.id.reportBtn);
    }

    public void setmUserImage(int resId) {
        mUserImage.setImageResource(resId);
    }

    public void setmUserId(String param) {
        mUserId.setText(param);
    }

    public void setmWriteTime(String param) {
        mWriteTime.setText(param);
    }

    public void setmRating(float param) {
        mRating.setRating(param);
    }

    public void setmUserReview(String param) {
        mUserReview.setText(param);
    }

    public void setmRecommend(int param) {
        mRecommend.setText(String.valueOf(param));
    }
}