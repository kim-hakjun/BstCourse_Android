package com.example.simpleapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.simpleapp.adapter.MovieViewPagerAdapter;
import com.example.simpleapp.R;
import com.example.simpleapp.model.MovieSummaryInfo;

import java.util.ArrayList;

public class MovieListFragment extends Fragment {

    private ViewPager mMovieViewPager;
    private MovieViewPagerAdapter mMovieViewPagerAdapter;

    private ArrayList<MoviePreviewFragment> mMoviePreviewFragmentList = new ArrayList<>();
    ArrayList<MovieSummaryInfo> movieList;

    public static MovieListFragment newInstance(ArrayList<MovieSummaryInfo> movieList) {
        MovieListFragment frag = new MovieListFragment();
        Bundle args = new Bundle();

        args.putParcelableArrayList("movieList", movieList);

        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null) {
            movieList = getArguments().getParcelableArrayList("movieList");
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_movie_list, container, false);

        mMovieViewPager = (ViewPager)rootView.findViewById(R.id.viewpager);

        for(int i = 0; i < movieList.size(); i++) {
            MovieSummaryInfo movie = movieList.get(i);
            mMoviePreviewFragmentList.add(MoviePreviewFragment.newInstance(movie.image, movie.id, movie.title, movie.reservation_rate, movie.grade));
        }
        mMovieViewPager.setOffscreenPageLimit(3);

//        mMovieViewPagerAdapter = new MovieViewPagerAdapter(getActivity().getSupportFragmentManager());
        // getChildFragmentManager : Return a private FragmentManager for placing and managing Fragments inside of a Fragment.
        mMovieViewPagerAdapter = new MovieViewPagerAdapter(this.getChildFragmentManager());
        mMovieViewPagerAdapter.replaceItems(mMoviePreviewFragmentList);
        mMovieViewPager.setAdapter(mMovieViewPagerAdapter);

        return rootView;
    }
}
