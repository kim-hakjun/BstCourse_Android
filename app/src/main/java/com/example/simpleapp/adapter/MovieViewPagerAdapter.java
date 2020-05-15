package com.example.simpleapp.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.simpleapp.fragment.MoviePreviewFragment;

import java.util.ArrayList;

public class MovieViewPagerAdapter extends FragmentPagerAdapter {
    ArrayList<MoviePreviewFragment> items = new ArrayList<MoviePreviewFragment>();

    public MovieViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public void replaceItems(ArrayList<MoviePreviewFragment> param) {
        this.items = param;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getCount() {
        return items.size();
    }
}
