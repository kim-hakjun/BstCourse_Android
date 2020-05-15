package com.example.simpleapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.simpleapp.FragmentCallback;
import com.example.simpleapp.R;

public class MoviePreviewFragment extends Fragment {

    private FragmentCallback callback;

    private ImageView mMoviePosterView;
    private TextView mMovieIdView;
    private TextView mMovieTitleView;
    private Button mShowDetailBtn;

    private int mMoviePoster;
    private int mMovieId;
    private String mMovieTitle;

    public MoviePreviewFragment() {

    }

    public static MoviePreviewFragment newInstance(int mMoviePoster, int mMovieId, String mMovieTitle) {
        MoviePreviewFragment mMoviePreviewFragment = new MoviePreviewFragment();

        Bundle args = new Bundle();
        args.putInt("param_poster", mMoviePoster);
        args.putInt("param_id", mMovieId);
        args.putString("param_title", mMovieTitle);

        mMoviePreviewFragment.setArguments(args);

        return mMoviePreviewFragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if(context instanceof FragmentCallback) {
            callback = (FragmentCallback)context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // check Bundle
        if(getArguments() != null) {
            mMoviePoster = getArguments().getInt("param_poster");
            mMovieId = getArguments().getInt("param_id");
            mMovieTitle = getArguments().getString("param_title");
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_movie_preview, container, false);

        // init
        mMoviePosterView = (ImageView)rootView.findViewById(R.id.movie_poster);
        mMovieIdView = (TextView)rootView.findViewById(R.id.movie_id);
        mMovieTitleView = (TextView)rootView.findViewById(R.id.movie_title);
        mShowDetailBtn = (Button)rootView.findViewById(R.id.showDetailBtn);

        // setting
        mMoviePosterView.setImageResource(mMoviePoster);
        mMovieIdView.setText(Integer.toString(mMovieId));
        mMovieTitleView.setText(mMovieTitle);

        mShowDetailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onDetailSelected(mMovieId);
            }
        });

        return rootView;
    }
}
