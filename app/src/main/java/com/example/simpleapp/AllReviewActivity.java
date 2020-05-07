package com.example.simpleapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class AllReviewActivity extends AppCompatActivity {
    TextView mAr_movieTitle;
    ImageButton mReviewWriteBtn;
    Button mReviewWriteTextBtn;
    ListView mReviewListView;

    com.example.simpleapp.ReviewListViewAdapter mAdapter;
    ArrayList<com.example.simpleapp.ReviewItem> mReviewList;

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
                Intent rv_intent = new Intent(getApplicationContext(), com.example.simpleapp.ReviewWriteActivity.class);
                rv_intent.putExtra("movieTitle", mAr_movieTitle.getText().toString());
                rv_intent.putExtra("movieTitle", mAr_movieTitle.getText().toString());
                startActivityForResult(rv_intent, reqType.COMMENTING.getValue());
            }
        });

        mAdapter = new com.example.simpleapp.ReviewListViewAdapter(mReviewList);
        mReviewListView.setAdapter(mAdapter);
    }

    public void init() {
        mAr_movieTitle = (TextView) findViewById(R.id.ar_movieTitle);
        mReviewWriteBtn = (ImageButton) findViewById(R.id.reviewWriteBtn);
        mReviewWriteTextBtn = (Button) findViewById(R.id.reviewWriteTextBtn);
        mReviewListView = (ListView) findViewById(R.id.reviewListView);

        Intent intent = getIntent();
        mAr_movieTitle.setText(intent.getStringExtra("movieTitle"));
        mReviewList = intent.getParcelableArrayListExtra("reviewList");
    }

    public void goMain() {
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra("reviewList", mReviewList);
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

        if (requestCode == reqType.COMMENTING.getValue()) {
            if (data != null) {
                com.example.simpleapp.ReviewItem item = (com.example.simpleapp.ReviewItem) data.getParcelableExtra("new_Review");
                mReviewList.add(item);
                mAdapter.notifyDataSetChanged();
            }
        }
    }

}
