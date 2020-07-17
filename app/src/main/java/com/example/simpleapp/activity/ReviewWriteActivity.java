package com.example.simpleapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.simpleapp.R;
import com.example.simpleapp.model.MovieDetailsInfo;
import com.example.simpleapp.model.Review;
import com.example.simpleapp.util.RequestHelper;

import java.util.HashMap;
import java.util.Map;

public class ReviewWriteActivity extends AppCompatActivity {
    TextView mRv_movieTitle;
    RatingBar mRv_rating;
    EditText mRv_comment;
    Button mRv_saveBtn, mRv_cancelBtn;

    MovieDetailsInfo movieInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_write);

        init();

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

        Intent intent = getIntent();
        movieInfo = intent.getParcelableExtra("movieInfo");

        mRv_movieTitle.setText(movieInfo.title);

    }

    public void goMain() {
        String comment_res = mRv_comment.getText().toString();
        float rating_res = mRv_rating.getRating();

        if(comment_res.length() < 5) {
            Toast.makeText(getApplicationContext(), "최소 5자 작성", Toast.LENGTH_LONG).show();
        }else {
            processMyReview("writer", movieInfo.id, comment_res, rating_res);
            setResult(RESULT_OK);
            finish();
        }
    }

    public void processMyReview(final String writer, final int movieId, final String comment, final float rating) {
        String url = "http://" + RequestHelper.host + ":" + RequestHelper.port + "/movie/createComment";

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        ) {
            // POST
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("writer", writer);
                params.put("id", movieId+"");
                params.put("contents", comment);
                params.put("rating", rating+"");

                return params;
            }
        };

        request.setShouldCache(false);
        RequestHelper.requestQueue.add(request);
    }
}
