package com.example.simpleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.simpleapp.R;
import com.example.simpleapp.ReviewItem;

public class ReviewWriteActivity extends AppCompatActivity {
    TextView mRv_movieTitle;
    RatingBar mRv_rating;
    EditText mRv_comment;
    Button mRv_saveBtn, mRv_cancelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_write);

        init();

        Intent intent = getIntent();
        String cmt_movieTitle_val = intent.getStringExtra("movieTitle");
        mRv_movieTitle.setText(cmt_movieTitle_val);

        mRv_saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goMain();
            }
        });

        mRv_cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void init() {
        mRv_movieTitle = (TextView) findViewById(R.id.rv_movieTitle);
        mRv_rating = (RatingBar) findViewById(R.id.rv_rating);
        mRv_comment = (EditText) findViewById(R.id.rv_comment);
        mRv_saveBtn = (Button) findViewById(R.id.rv_saveBtn);
        mRv_cancelBtn = (Button) findViewById(R.id.rv_cancelBtn);
    }

    public void goMain() {
        String comment_res = mRv_comment.getText().toString();
        float rating_res = mRv_rating.getRating();

        // using parcelable
        Intent intent = new Intent();
        ReviewItem new_Review = new ReviewItem(R.drawable.user_image, "UserID", "xx분전", rating_res, comment_res, 1);
        intent.putExtra("new_Review", new_Review);

        setResult(RESULT_OK, intent);
        finish();
    }

}
