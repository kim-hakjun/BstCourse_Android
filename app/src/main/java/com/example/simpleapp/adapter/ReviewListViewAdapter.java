package com.example.simpleapp.adapter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.simpleapp.R;
import com.example.simpleapp.model.Review;
import com.example.simpleapp.ReviewItemView;
import com.example.simpleapp.util.RequestHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReviewListViewAdapter extends BaseAdapter {
    ArrayList<Review> mItems = new ArrayList<Review>();

    TextView recommendText;

    public ReviewListViewAdapter(ArrayList<Review> items) {
        this.mItems = items;
    }

    public void addItem(Review item) {
        mItems.add(item);
    }

    public void replaceList(ArrayList<Review> items) {
        this.mItems = items;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ReviewItemView view;

        if(convertView == null) {
            view = new ReviewItemView(parent.getContext());
        }
        else {
            view = (ReviewItemView)convertView;
        }

        final Review item = mItems.get(position);

        view.setmUserImage(R.drawable.user_image);
        view.setmUserId(Integer.toString(item.id));
        view.setmWriteTime(item.time);
        view.setmRating(item.rating);
        view.setmUserReview(item.contents);
        view.setmRecommendCnt(item.recommend);
        // + need report button

        recommendText = view.findViewById(R.id.recommendText);

        recommendText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                processRecommend(item.id);
            }
        });

        return view;
    }

    public void processRecommend(final int review_id) {
        String url = "http://" + RequestHelper.host + ":" + RequestHelper.port + "/movie/increaseRecommend";

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
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("review_id", review_id +"");
                return params;
            }
        };

        request.setShouldCache(false);
        RequestHelper.requestQueue.add(request);
    }

}