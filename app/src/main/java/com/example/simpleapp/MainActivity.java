package com.example.simpleapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ScrollView mScrollView;
    Button mThumbUpBtn, mThumbDownBtn, mReviewWriteTextBtn, mViewAllReviewBtn;
    ImageButton mReviewWriteBtn;
    TextView mMovieTitleText, mThumbUpCntText, mThumbDownCntText;
    ListView mReviewListView;

    com.example.simpleapp.ReviewListViewAdapter mAdapter;
    ArrayList<com.example.simpleapp.ReviewItem> mReviewList = new ArrayList<>();

    boolean mThumbUpState, mThumbDownState;

    public enum reqType {
        COMMENTING(101), viewAllReview(102);

        private final int value;

        reqType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        // thumbUpBtn Pressed
        mThumbUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thumbUpBtnClicked();
            }
        });

        // thumbDownBtn pressed
        mThumbDownBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thumbDownBtnClicked();
            }
        });


        // add temp Item
        mReviewList.add(new com.example.simpleapp.ReviewItem(R.drawable.user_image, "tempID", "xx분전", 3.5f, "TEMP DATA", 1));
        mReviewList.add(new com.example.simpleapp.ReviewItem(R.drawable.user_image, "tempID", "xx분전", 3.5f, "TEMP DATA", 1));

        // reviewListView setting
        mAdapter = new com.example.simpleapp.ReviewListViewAdapter(mReviewList);
        mReviewListView.setAdapter(mAdapter);

        // To solve Scroll problem betwwn listView and ScrollView
        mReviewListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mScrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        // reviewWriteBtn Pressed
        mReviewWriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goCommenting();
            }
        });

        // reviewWriteTextBtn Pressed
        mReviewWriteTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goCommenting();
            }
        });

        // viewAllReviewBtn Pressed
        mViewAllReviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goAllReview();
            }
        });
    }

    public void init() {
        // get layout
        mScrollView = (ScrollView) findViewById(R.id.mainScrollView);
        mReviewListView = (ListView) findViewById(R.id.reviewListView);

        // get Button
        mThumbUpBtn = (Button) findViewById(R.id.thumbUpBtn);
        mThumbDownBtn = (Button) findViewById(R.id.thumbDownBtn);
        mReviewWriteBtn = (ImageButton) findViewById(R.id.reviewWriteBtn);
        mReviewWriteTextBtn = (Button) findViewById(R.id.reviewWriteTextBtn);
        mViewAllReviewBtn = (Button) findViewById(R.id.viewAllReviewBtn);

        // get TextView
        mMovieTitleText = (TextView) findViewById(R.id.movieTitle);
        mThumbUpCntText = (TextView) findViewById(R.id.thumbUpCntText);
        mThumbDownCntText = (TextView) findViewById(R.id.thumbDownCntText);
    }

    public void thumbUpBtnClicked() {
        if (mThumbUpState) {
            thumbUpCntFunc(true);
        } else {
            if (mThumbDownState) {
                thumbDownCntFunc(true);
            }
            thumbUpCntFunc(false);
        }
    }

    public void thumbDownBtnClicked() {
        if (mThumbDownState) {
            thumbDownCntFunc(true);
        } else {
            if (mThumbUpState) {
                thumbUpCntFunc(true);
            }
            thumbDownCntFunc(false);
        }
    }

    public void thumbUpCntFunc(boolean type) {  // true: 원래 눌려있을 경우, false: 안눌려있을 경우
        int curCnt = Integer.parseInt(mThumbUpCntText.getText().toString());

        if (type) {
            curCnt -= 1;
            mThumbUpBtn.setBackgroundResource(R.drawable.thumb_up_selector);
        } else {
            curCnt += 1;
            mThumbUpBtn.setBackgroundResource(R.drawable.ic_thumb_up_selected);
        }

        mThumbUpCntText.setText(String.valueOf(curCnt));
        mThumbUpState = !mThumbUpState;
    }

    public void thumbDownCntFunc(boolean type) {
        int curCnt = Integer.parseInt(mThumbDownCntText.getText().toString());

        if (type) {
            curCnt -= 1;
            mThumbDownBtn.setBackgroundResource(R.drawable.thumb_down_selector);
        } else {
            curCnt += 1;
            mThumbDownBtn.setBackgroundResource(R.drawable.ic_thumb_down_selected);
        }

        mThumbDownCntText.setText(String.valueOf(curCnt));
        mThumbDownState = !mThumbDownState;
    }

    // 리뷰 작성 화면
    public void goCommenting() {
        Intent rv_intent = new Intent(getApplicationContext(), com.example.simpleapp.ReviewWriteActivity.class);
        rv_intent.putExtra("movieTitle", mMovieTitleText.getText().toString());
        startActivityForResult(rv_intent, reqType.COMMENTING.getValue());
    }

    // 리뷰 모두 보기 화면
    public void goAllReview() {
        Intent allReview_intent = new Intent(getApplicationContext(), AllReviewActivity.class);
        allReview_intent.putExtra("movieTitle", mMovieTitleText.getText().toString());
        allReview_intent.putParcelableArrayListExtra("reviewList", mReviewList);
        startActivityForResult(allReview_intent, reqType.viewAllReview.getValue());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == reqType.COMMENTING.getValue()) {
            if (data != null) {
                com.example.simpleapp.ReviewItem item = (com.example.simpleapp.ReviewItem) data.getParcelableExtra("new_Review");
                mReviewList.add(item);
                mAdapter.notifyDataSetChanged();
            }
        }else if(requestCode == reqType.viewAllReview.getValue()) {
            if(data != null) {
                mReviewList.clear();
                mReviewList = data.getParcelableArrayListExtra("reviewList");
                mAdapter.replaceList(mReviewList);
                mAdapter.notifyDataSetChanged();
            }
        }
    }

}