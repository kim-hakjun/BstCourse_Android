package com.example.simpleapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.simpleapp.FragmentCallback;
import com.example.simpleapp.R;
import com.example.simpleapp.util.NetworkHelper;

public class MoviePreviewFragment extends Fragment {

    private FragmentCallback callback;

    private ImageView mMoviePosterView;
    private TextView mMovieIdView;
    private TextView mMovieTitleView;
    private Button mShowDetailBtn;
    private TextView mReservationRateView;
    private TextView mMovieGradeView;

    private String mMoviePoster;
    private int mMovieId;
    private String mMovieTitle;
    private float mReservationRate;
    private int mGrade;

    public MoviePreviewFragment() {

    }

    public static MoviePreviewFragment newInstance(String mMoviePoster, int mMovieId, String mMovieTitle, float mReservation_rate, int mGrade) {
        MoviePreviewFragment mMoviePreviewFragment = new MoviePreviewFragment();

        Bundle args = new Bundle();
        args.putString("param_poster", mMoviePoster);
        args.putInt("param_id", mMovieId);
        args.putString("param_title", mMovieTitle);
        args.putFloat("param_reservationRate", mReservation_rate);
        args.putInt("param_grade", mGrade);
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
            mMoviePoster = getArguments().getString("param_poster");
            mMovieId = getArguments().getInt("param_id");
            mMovieTitle = getArguments().getString("param_title");
            mReservationRate = getArguments().getFloat("param_reservationRate");
            mGrade = getArguments().getInt("param_grade");
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
        mReservationRateView = (TextView)rootView.findViewById(R.id.movie_reservationRate);
        mMovieGradeView = (TextView)rootView.findViewById(R.id.movie_grade);

        // setting
        Glide.with(getActivity()).load(mMoviePoster).into(mMoviePosterView);
        mMovieIdView.setText(Integer.toString(mMovieId));
        mMovieTitleView.setText(mMovieTitle);
        mReservationRateView.setText(Float.toString(mReservationRate));
        mMovieGradeView.setText(Integer.toString(mGrade));

        mShowDetailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onDetailSelected(mMovieId);
            }
        });

        return rootView;
    }
}
