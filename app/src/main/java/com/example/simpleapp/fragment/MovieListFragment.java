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

import java.util.ArrayList;

public class MovieListFragment extends Fragment {

    private ViewPager mMovieViewPager;
    private MovieViewPagerAdapter mMovieViewPagerAdapter;

    private ArrayList<MoviePreviewFragment> mMoviePreviewFragmentList = new ArrayList<>();

    // not use bundle
    public static MovieListFragment newInstance() {
        MovieListFragment frag = new MovieListFragment();
        Bundle args = new Bundle();
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
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_movie_list, container, false);

        mMovieViewPager = (ViewPager)rootView.findViewById(R.id.viewpager);

        mMoviePreviewFragmentList.add(MoviePreviewFragment.newInstance(R.drawable.image1, 1,"군도"));
        mMoviePreviewFragmentList.add(MoviePreviewFragment.newInstance(R.drawable.image2, 2,"공조"));
        mMoviePreviewFragmentList.add(MoviePreviewFragment.newInstance(R.drawable.image3, 3,"더킹"));
        mMoviePreviewFragmentList.add(MoviePreviewFragment.newInstance(R.drawable.image4, 4,"레지던트 이블"));
        mMoviePreviewFragmentList.add(MoviePreviewFragment.newInstance(R.drawable.image5, 5,"럭키"));
        mMoviePreviewFragmentList.add(MoviePreviewFragment.newInstance(R.drawable.image6, 6,"아수라"));

        mMovieViewPager.setOffscreenPageLimit(3);

//        mMovieViewPagerAdapter = new MovieViewPagerAdapter(getActivity().getSupportFragmentManager());
        // getChildFragmentManager : Return a private FragmentManager for placing and managing Fragments inside of a Fragment.
        mMovieViewPagerAdapter = new MovieViewPagerAdapter(this.getChildFragmentManager());
        mMovieViewPagerAdapter.replaceItems(mMoviePreviewFragmentList);
        mMovieViewPager.setAdapter(mMovieViewPagerAdapter);

        return rootView;
    }
}
