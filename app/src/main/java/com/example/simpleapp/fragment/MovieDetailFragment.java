package com.example.simpleapp.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.simpleapp.R;
import com.example.simpleapp.ReviewItem;
import com.example.simpleapp.adapter.ReviewListViewAdapter;
import com.example.simpleapp.activity.AllReviewActivity;
import com.example.simpleapp.activity.ReviewWriteActivity;

import java.util.ArrayList;

public class MovieDetailFragment extends Fragment {

    private ScrollView mScrollView;
    private Button mThumbUpBtn, mThumbDownBtn, mReviewWriteTextBtn, mViewAllReviewBtn;
    private ImageButton mReviewWriteBtn;
    private TextView mMovieTitleText, mThumbUpCntText, mThumbDownCntText;
    private ListView mReviewListView;

    private ReviewListViewAdapter mAdapter;
    private ArrayList<ReviewItem> mReviewList = new ArrayList<>();

    private boolean mThumbUpState, mThumbDownState;

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
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_movie_detail, container, false);

        init(rootView);

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
        mReviewList.add(new ReviewItem(R.drawable.user_image, "tempID", "xx분전", 3.5f, "TEMP DATA", 1));
        mReviewList.add(new ReviewItem(R.drawable.user_image, "tempID", "xx분전", 3.5f, "TEMP DATA", 1));

        // reviewListView setting
        mAdapter = new ReviewListViewAdapter(mReviewList);
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
        return rootView;
    }

    public void init(ViewGroup root) {
        // get layout
        mScrollView = (ScrollView) root.findViewById(R.id.mainScrollView);
        mReviewListView = (ListView) root.findViewById(R.id.reviewListView);

        // get Button
        mThumbUpBtn = (Button) root.findViewById(R.id.thumbUpBtn);
        mThumbDownBtn = (Button) root.findViewById(R.id.thumbDownBtn);
        mReviewWriteBtn = (ImageButton) root.findViewById(R.id.reviewWriteBtn);
        mReviewWriteTextBtn = (Button) root.findViewById(R.id.reviewWriteTextBtn);
        mViewAllReviewBtn = (Button) root.findViewById(R.id.viewAllReviewBtn);

        // get TextView
        mMovieTitleText = (TextView) root.findViewById(R.id.movieTitle);
        mThumbUpCntText = (TextView) root.findViewById(R.id.thumbUpCntText);
        mThumbDownCntText = (TextView) root.findViewById(R.id.thumbDownCntText);
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
        Intent rv_intent = new Intent(getActivity(), ReviewWriteActivity.class);
        rv_intent.putExtra("movieTitle", mMovieTitleText.getText().toString());
        startActivityForResult(rv_intent, reqType.COMMENTING.getValue());
    }

    // 리뷰 모두 보기 화면
    public void goAllReview() {
        Intent allReview_intent = new Intent(getActivity(), AllReviewActivity.class);
        allReview_intent.putExtra("movieTitle", mMovieTitleText.getText().toString());
        allReview_intent.putParcelableArrayListExtra("reviewList", mReviewList);
        startActivityForResult(allReview_intent, reqType.viewAllReview.getValue());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == reqType.COMMENTING.getValue()) {
            if (data != null) {
                ReviewItem item = (ReviewItem) data.getParcelableExtra("new_Review");
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
