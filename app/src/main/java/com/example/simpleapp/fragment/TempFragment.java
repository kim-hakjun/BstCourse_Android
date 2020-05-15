package com.example.simpleapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.simpleapp.R;


public class TempFragment extends Fragment {

    public static TempFragment newInstance() {
        TempFragment mTempFragment = new TempFragment();

        Bundle args = new Bundle();
        mTempFragment.setArguments(args);
        return mTempFragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_temp, container, false);



        return rootView;
    }
}
