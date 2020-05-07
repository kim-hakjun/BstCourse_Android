package com.example.simpleapp;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.simpleapp.ReviewItem;
import com.example.simpleapp.ReviewItemView;

import java.util.ArrayList;

public class ReviewListViewAdapter extends BaseAdapter {
    ArrayList<ReviewItem> mItems = new ArrayList<ReviewItem>();

    public ReviewListViewAdapter(ArrayList<ReviewItem> items) {
        this.mItems = items;
    }

    public void addItem(ReviewItem item) {
        mItems.add(item);
    }

    public void replaceList(ArrayList<ReviewItem> items) {
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
        ReviewItemView view;

        if(convertView == null) {
            view = new ReviewItemView(parent.getContext());
        }
        else {
            view = (ReviewItemView)convertView;
        }

        ReviewItem item = mItems.get(position);

        view.setmUserImage(item.getmUserImage());
        view.setmUserId(item.getmUserId());
        view.setmWriteTime(item.getmWriteTime());
        view.setmRating(item.getmRating());
        view.setmUserReview(item.getmUserReview());
        view.setmRecommend(item.getmRecommend());
        // + need report button

        return view;
    }
}