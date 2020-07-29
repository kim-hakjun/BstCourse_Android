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

import android.app.usage.NetworkStats;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.simpleapp.FragmentCallback;
import com.example.simpleapp.db.DBHelper;
import com.example.simpleapp.fragment.HomeFragment;
import com.example.simpleapp.fragment.MovieDetailFragment;
import com.example.simpleapp.fragment.MovieListFragment;
import com.example.simpleapp.R;
import com.example.simpleapp.fragment.TempFragment;
import com.example.simpleapp.model.MovieDetailsList;
import com.example.simpleapp.model.MovieSummaryList;
import com.example.simpleapp.model.ResponseMovieInfo;
import com.example.simpleapp.util.NetworkHelper;
import com.example.simpleapp.util.RequestHelper;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, FragmentCallback, View.OnClickListener {

    private DrawerLayout mDrawer;
    private NavigationView navigationView;
    private FrameLayout mFrameLayout;
    private ViewPager mMovieViewPager;
    private Toolbar mToolbar;
    private FragmentManager mFragmentManager;
    private MovieDetailFragment mMovieDetailFragment;

    private LinearLayout mSortLayout;
    private ImageView mSortBase;
    private ImageView mSortReservationView;
    private ImageView mSortCurationView;
    private ImageView mSortReleaseView;

    Boolean isShown = false;

    Animation translateUp;
    Animation translateDown;

    MovieSummaryList movieSummaryList = new MovieSummaryList();
    MovieDetailsList movieDetailsList = new MovieDetailsList();

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

        mSortLayout = findViewById(R.id.sort_anim);
        mSortBase = findViewById(R.id.sort_base);
        mSortBase.setOnClickListener(this);
        mSortReservationView = findViewById(R.id.sort_reservation);
        mSortReservationView.setOnClickListener(this);
        mSortCurationView = findViewById(R.id.sort_curation);
        mSortCurationView.setOnClickListener(this);
        mSortReleaseView = findViewById(R.id.sort_release);
        mSortReleaseView.setOnClickListener(this);

        translateUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_up);
        translateUp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isShown = false;
                mSortLayout.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        translateDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_down);

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

        // open DB
        DBHelper.openDB(getApplicationContext());

        // 영화 목록 데이터 받기 및 view 초기화
        getMovieList(1);

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

                // 영화목록 프레그먼트로 ArrayList<movieBriefInfo> 넘겨줌
                frag = MovieListFragment.newInstance(movieSummaryList.result);
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
    public void onClick(View v) {
        int id = v.getId();

        mSortLayout.bringToFront();

        switch (id) {
            case R.id.sort_base :
                if(isShown) {
                    mSortLayout.startAnimation(translateUp);
                }else {
                    mSortLayout.setVisibility(View.VISIBLE);
                    mSortLayout.startAnimation(translateDown);
                }
                isShown = !isShown;
                break;

            case R.id.sort_reservation :
                mSortBase.setImageResource(R.drawable.order11);
                mSortLayout.startAnimation(translateUp);
                getMovieList(1);
                break;

            case R.id.sort_curation :
                mSortBase.setImageResource(R.drawable.order22);
                mSortLayout.startAnimation(translateUp);
                getMovieList(2);
                break;

            case R.id.sort_release :
                mSortBase.setImageResource(R.drawable.order33);
                mSortLayout.startAnimation(translateUp);
                getMovieList(3);
                break;
        }
    }

    public void getMovieList(int param) {
        if(NetworkHelper.getConnectivityStatus(getApplicationContext()) != NetworkHelper.TYPE_NOT_CONNECTED) {
            if(RequestHelper.requestQueue == null) {
                RequestHelper.requestQueue = Volley.newRequestQueue(getApplicationContext());
            }
            requestMovieList(param);
        }else {
            Toast.makeText(getApplicationContext(), "네트워크 연결 없음(DB에서 불러옴)", Toast.LENGTH_LONG).show();
            // DB에서 불러오기
            movieSummaryList.result = DBHelper.selectMovieSummaryInfos(param);

            if(movieSummaryList.result.size() == 0) {
                Toast.makeText(getApplicationContext(), "저장된 데이터 없음", Toast.LENGTH_LONG).show();
            }else {
                mToolbar.setTitle("영화 목록");
                FragmentTransaction transaction = mFragmentManager.beginTransaction().replace(R.id.frameContainer, MovieListFragment.newInstance(movieSummaryList.result));
                transaction.addToBackStack(null);   // for BackPressed
                transaction.commit();
            }
        }
    }

    public void requestMovieList(int param) {
        String url = "http://" + RequestHelper.host + ":" + Integer.toString(RequestHelper.port) + "/movie/readMovieList?";
        url += "type=" + Integer.toString(param);

        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        processMovieList(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                    }
                }
        );

        request.setShouldCache(false);
        RequestHelper.requestQueue.add(request);

    }

    // 받은 영화 데이터 처리 (JSON -> Object)
    public void processMovieList(String response) {
        Gson gson = new Gson();

        ResponseMovieInfo info = gson.fromJson(response, ResponseMovieInfo.class);

        if(info.code == 200) {
            movieSummaryList = gson.fromJson(response, MovieSummaryList.class);
            DBHelper.insertMovieSummary(movieSummaryList.result);

            mToolbar.setTitle("영화 목록");
            FragmentTransaction transaction = mFragmentManager.beginTransaction().replace(R.id.frameContainer, MovieListFragment.newInstance(movieSummaryList.result));
            transaction.addToBackStack(null);   // for BackPressed
            transaction.commit();
        }
    }

    public void requestMovieDetailsInfo(int id) {
        String url = "http://" + RequestHelper.host + ":" + RequestHelper.port + "/movie/readMovie?id=";
        url += id;

        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        ResponseMovieInfo info = gson.fromJson(response, ResponseMovieInfo.class);

                        if(info.code == 200) {
                            movieDetailsList = gson.fromJson(response, MovieDetailsList.class);
                            // insert MovieDetails
                            DBHelper.insertMovieDetails(movieDetailsList.result.get(0));
                            showDetailsPage();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                    }
                }
        );

        request.setShouldCache(false);
        RequestHelper.requestQueue.add(request);
    }

    public void showDetailsPage() {
        mMovieDetailFragment = MovieDetailFragment.newInstance(movieDetailsList.result.get(0));
        FragmentTransaction transaction = mFragmentManager.beginTransaction().add(R.id.frameContainer, mMovieDetailFragment);
        transaction.addToBackStack(null);   // for BackPressed
        transaction.commit();
        mToolbar.setTitle("영화 상세");
    }

    @Override
    public void onDetailSelected(int id) {
        if(NetworkHelper.getConnectivityStatus(getApplicationContext()) != NetworkHelper.TYPE_NOT_CONNECTED) {
            requestMovieDetailsInfo(id);
        }else {
            Toast.makeText(getApplicationContext(), "네트워크 연결 없음(DB에서 상세 정보 불러옴)", Toast.LENGTH_LONG).show();
            // DB에서 불러오기
            movieDetailsList.result = DBHelper.selectMovieDetailsInfos(id);
            if(movieDetailsList.result.size() != 0) {
                showDetailsPage();
            }else {
                Toast.makeText(getApplicationContext(), "저장된 데이터 없음", Toast.LENGTH_LONG).show();
            }
        }

    }

}