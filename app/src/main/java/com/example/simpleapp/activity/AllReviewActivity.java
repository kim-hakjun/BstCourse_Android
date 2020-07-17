package com.example.simpleapp.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.simpleapp.R;
import com.example.simpleapp.model.MovieDetailsInfo;
import com.example.simpleapp.model.ResponseReviewInfo;
import com.example.simpleapp.model.Review;
import com.example.simpleapp.adapter.ReviewListViewAdapter;
import com.example.simpleapp.model.ReviewList;
import com.example.simpleapp.util.RequestHelper;
import com.google.gson.Gson;

import java.util.ArrayList;

public class AllReviewActivity extends AppCompatActivity {
    TextView mAr_movieTitle, mAr_rating, mAr_reviewerNum;
    RatingBar mRatingBar;
    ImageButton mReviewWriteBtn;
    Button mReviewWriteTextBtn;
    ListView mReviewListView;

    ReviewListViewAdapter mAdapter;
    ArrayList<Review> mReviewList;

    MovieDetailsInfo movieInfo;

    public enum reqType {
        COMMENTING(101);

        private final int value;

        reqType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_review);

        // init
        init();

        // go review Write page
        mReviewWriteTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent rv_intent = new Intent(getApplicationContext(), ReviewWriteActivity.class);
                rv_intent.putExtra("movieInfo", movieInfo);
                startActivityForResult(rv_intent, reqType.COMMENTING.getValue());
            }
        });

    }

    @SuppressLint("SetTextI18n")
    public void init() {
        mAr_movieTitle = (TextView) findViewById(R.id.ar_movieTitle);
        mAr_rating = (TextView)findViewById(R.id.ratingInAllReview);
        mAr_reviewerNum = (TextView)findViewById(R.id.reviewerNum);
        mRatingBar = (RatingBar)findViewById(R.id.ratingBarInAllReview);
        mReviewWriteBtn = (ImageButton) findViewById(R.id.reviewWriteBtn);
        mReviewWriteTextBtn = (Button) findViewById(R.id.reviewWriteTextBtn);
        mReviewListView = (ListView) findViewById(R.id.reviewListView);

        Intent intent = getIntent();
        movieInfo = intent.getParcelableExtra("movieInfo");
        mReviewList = intent.getParcelableArrayListExtra("reviewList");

        mAr_movieTitle.setText(movieInfo.title);
        mRatingBar.setRating(movieInfo.user_rating);
        mAr_rating.setText(Float.toString(movieInfo.user_rating * 2));
        mAr_reviewerNum.setText(Integer.toString(mReviewList.size()));

        mAdapter = new ReviewListViewAdapter(mReviewList);
        mReviewListView.setAdapter(mAdapter);

    }

    public void goMain() {
        Intent intent = new Intent();

        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        goMain();
//        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                goMain();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            requestReviewList();
        }

        if (requestCode == reqType.COMMENTING.getValue()) {
            if (data != null) {
                Review item = (Review) data.getParcelableExtra("new_Review");
                mReviewList.add(item);
                mAdapter.notifyDataSetChanged();
            }
        }
    }


    public void requestReviewList() {
        String url = "http://" + RequestHelper.host + ":" + RequestHelper.port +
                "/movie/readCommentList?id=";
        url += movieInfo.id + "&length=" + 100;

        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        processReviewList(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        request.setShouldCache(false);
        RequestHelper.requestQueue.add(request);
    }

    public void processReviewList(String response) {
        Gson gson = new Gson();
        ResponseReviewInfo info = gson.fromJson(response, ResponseReviewInfo.class);
        if (info.code == 200) {
            ReviewList reviewList = gson.fromJson(response, ReviewList.class);

            mReviewList.clear();
            mReviewList.addAll(reviewList.result);
            // reviewListView setting
            mAdapter = new ReviewListViewAdapter(mReviewList);
            mReviewListView.setAdapter(mAdapter);
        }
    }

}
