package com.example.simpleapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.simpleapp.FragmentCallback;
import com.example.simpleapp.fragment.HomeFragment;
import com.example.simpleapp.fragment.MovieDetailFragment;
import com.example.simpleapp.fragment.MovieListFragment;
import com.example.simpleapp.fragment.MoviePreviewFragment;
import com.example.simpleapp.adapter.MovieViewPagerAdapter;
import com.example.simpleapp.R;
import com.example.simpleapp.fragment.TempFragment;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, FragmentCallback {

    private DrawerLayout mDrawer;
    private NavigationView navigationView;
    private FrameLayout mFrameLayout;
    private ViewPager mMovieViewPager;
    private Toolbar mToolbar;
    private FragmentManager mFragmentManager;
    private MovieDetailFragment mMovieDetailFragment;

    private MovieViewPagerAdapter mMovieViewPagerAdapter;
    private ArrayList<MoviePreviewFragment> mMoviePreviewFragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init view
        mToolbar = findViewById(R.id.toolbar);
        mDrawer = findViewById(R.id.drawer_layout); // activity_main: 전체를 감싸고 있는 레이아웃
        navigationView = findViewById(R.id.nav_view);    // activity_main: 옆에 슬라이딩 되는 뷰
        mFrameLayout = findViewById(R.id.frameContainer);
        mMovieViewPager = findViewById(R.id.viewpager);
        mFragmentManager = getSupportFragmentManager();

        setSupportActionBar(mToolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        // 초기 화면
        FragmentTransaction transaction = mFragmentManager.beginTransaction().add(R.id.frameContainer, HomeFragment.newInstance());
        transaction.addToBackStack(null);   // for BackPressed
        transaction.commit();

    }

    @Override
    public void onBackPressed() {
        if(mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        Fragment frag = null;
        String title = getString(R.string.app_name);

        switch (id) {
            case R.id.nav_movie_list:
                title = "영화 목록";
                if(mMovieDetailFragment != null) {
                    mFragmentManager.beginTransaction().remove(mMovieDetailFragment).commit();
                }
                frag = MovieListFragment.newInstance();
                break;

            case R.id.nav_movie_api:
                title = "영화 API";
                frag = TempFragment.newInstance();
                break;

            case R.id.nav_reserve:
                title = "예매하기";
                frag = TempFragment.newInstance();
                break;

            case R.id.nav_settings:
                title = "사용자 설정";
                frag = TempFragment.newInstance();
                break;
        }

        if(frag != null) {
            mFragmentManager.beginTransaction().replace(R.id.frameContainer, frag).commit();
            mToolbar.setTitle(title);
        }

        // close drawer
        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onDetailSelected(int id) {
        mMovieDetailFragment = new MovieDetailFragment();
        FragmentTransaction transaction = mFragmentManager.beginTransaction().add(R.id.frameContainer, mMovieDetailFragment);
        transaction.addToBackStack(null);   // for BackPressed
        transaction.commit();
        mToolbar.setTitle("영화 상세");
    }
}