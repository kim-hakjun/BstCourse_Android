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
import com.example.simpleapp.db.DBHelper;
import com.example.simpleapp.model.MovieDetailsInfo;
import com.example.simpleapp.model.ResponseReviewInfo;
import com.example.simpleapp.model.Review;
import com.example.simpleapp.adapter.ReviewListViewAdapter;
import com.example.simpleapp.model.ReviewList;
import com.example.simpleapp.util.NetworkHelper;
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
    ArrayList<Review> mReviewList = new ArrayList<Review>();
    ReviewList reviewList = new ReviewList();

    MovieDetailsInfo movieInfo;

    public final int COMMENTING_NUM = 101;

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
                goCommenting();
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
//        mReviewList = intent.getParcelableArrayListExtra("reviewList");
        requestReviewList();

        mAr_movieTitle.setText(movieInfo.title);
        mRatingBar.setRating(movieInfo.user_rating);
        mAr_rating.setText(Float.toString(movieInfo.user_rating * 2));
//        mAr_reviewerNum.setText(Integer.toString(mReviewList.size()));
//        mAdapter = new ReviewListViewAdapter(mReviewList);
//        mReviewListView.setAdapter(mAdapter);

    }

    public void goCommenting() {
        if(NetworkHelper.getConnectivityStatus(getApplicationContext()) != NetworkHelper.TYPE_NOT_CONNECTED) {
            Intent rv_intent = new Intent(getApplicationContext(), ReviewWriteActivity.class);
            rv_intent.putExtra("movieInfo", movieInfo);
            startActivityForResult(rv_intent, COMMENTING_NUM);
        }else {
            Toast.makeText(getApplicationContext(),"작성 불가(네트워크 연결 없음)", Toast.LENGTH_LONG).show();
        }
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

        if (requestCode == COMMENTING_NUM) {
            if (data != null) {
                Review item = (Review) data.getParcelableExtra("new_Review");
                mReviewList.add(item);
                mAdapter.notifyDataSetChanged();
            }
        }
    }


    public void requestReviewList() {
        if(NetworkHelper.getConnectivityStatus(getApplicationContext()) != NetworkHelper.TYPE_NOT_CONNECTED) {
            String url = "http://" + RequestHelper.host + ":" + RequestHelper.port +
                    "/movie/readCommentList?id=";
            url += movieInfo.id + "&length=" + 50;

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
        }else {
            Toast.makeText(getApplicationContext(), "네트워크 연결없음(DB에서 리뷰 불러옴)", Toast.LENGTH_LONG).show();
            reviewList.result.addAll(DBHelper.selectReviews(movieInfo.id));

            if(reviewList.result.size() == 0) {
                Toast.makeText(getApplicationContext(), "저장된 데이터 없음", Toast.LENGTH_LONG).show();
            }else {
                showReviewListView();
            }

        }

    }

    public void processReviewList(String response) {
        Gson gson = new Gson();
        ResponseReviewInfo info = gson.fromJson(response, ResponseReviewInfo.class);
        if (info.code == 200) {
            reviewList = gson.fromJson(response, ReviewList.class);

            DBHelper.insertReview(reviewList.result);
            showReviewListView();
        }
    }

    public void showReviewListView() {
        if(mReviewList.size() != 0) {
            mReviewList.clear();
        }
        mReviewList.addAll(reviewList.result);
        mAr_reviewerNum.setText(Integer.toString(mReviewList.size()));

        // reviewListView setting
        mAdapter = new ReviewListViewAdapter(mReviewList);
        mReviewListView.setAdapter(mAdapter);
    }

}
