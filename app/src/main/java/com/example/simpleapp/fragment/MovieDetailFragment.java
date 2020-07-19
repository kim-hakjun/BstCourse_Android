package com.example.simpleapp.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.example.simpleapp.R;
import com.example.simpleapp.db.DBHelper;
import com.example.simpleapp.model.ResponseReviewInfo;
import com.example.simpleapp.model.Review;
import com.example.simpleapp.adapter.ReviewListViewAdapter;
import com.example.simpleapp.activity.AllReviewActivity;
import com.example.simpleapp.activity.ReviewWriteActivity;
import com.example.simpleapp.model.MovieDetailsInfo;
import com.example.simpleapp.model.ReviewList;
import com.example.simpleapp.util.NetworkHelper;
import com.example.simpleapp.util.RequestHelper;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MovieDetailFragment extends Fragment {

    private ScrollView mScrollView;
    private Button mThumbUpBtn, mThumbDownBtn, mReviewWriteTextBtn, mViewAllReviewBtn;
    private ImageButton mReviewWriteBtn;
    private TextView mMovieTitleText, mDateText, mGenreText, mDurationText, mThumbUpCntText, mThumbDownCntText, mReservationRateText, mReservationGradeText, mReviewerRatingText, mAudienceText, mSynopsisText, mMovieDirectorText, mMovieActorText;
    private ListView mReviewListView;
    private ImageView mPosterView;
    private RatingBar mRatingBar;
    private ReviewListViewAdapter mAdapter;
    private ArrayList<Review> mReviewList = new ArrayList<>();
    ReviewList reviewList = new ReviewList();

    private boolean mThumbUpState, mThumbDownState;

    private final int req_COMMENTING = 101;
    private final int req_viewAllReview = 102;

    MovieDetailsInfo movieDetailsInfo;

    public static MovieDetailFragment newInstance(MovieDetailsInfo info) {
        MovieDetailFragment frag = new MovieDetailFragment();
        Bundle args = new Bundle();

        args.putParcelable("movieDetails", info);

        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            movieDetailsInfo = getArguments().getParcelable("movieDetails");
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_movie_detail, container, false);

        init(rootView);
        // 영화 리뷰목록 요청
        requestReviewList();

        // thumbUpBtn Pressed
        mThumbUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thumbUpBtnClicked();
            }
        });

        // thumbDownBtn pressed
        mThumbDownBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thumbDownBtnClicked();
            }
        });

        // To solve Scroll problem between listView and ScrollView
        mReviewListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mScrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        // reviewWriteBtn Pressed
        mReviewWriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goCommenting();
            }
        });
        // reviewWriteTextBtn Pressed
        mReviewWriteTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goCommenting();
            }
        });
        // viewAllReviewBtn Pressed
        mViewAllReviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goAllReview();
            }
        });

        return rootView;
    }

    @SuppressLint("SetTextI18n")
    public void init(ViewGroup root) {
        // get layout
        mScrollView = (ScrollView) root.findViewById(R.id.mainScrollView);
        mReviewListView = (ListView) root.findViewById(R.id.reviewListView);

        // get Button
        mThumbUpBtn = (Button) root.findViewById(R.id.thumbUpBtn);
        mThumbDownBtn = (Button) root.findViewById(R.id.thumbDownBtn);
        mReviewWriteBtn = (ImageButton) root.findViewById(R.id.reviewWriteBtn);
        mReviewWriteTextBtn = (Button) root.findViewById(R.id.reviewWriteTextBtn);
        mViewAllReviewBtn = (Button) root.findViewById(R.id.viewAllReviewBtn);

        // get TextView
        mMovieTitleText = (TextView) root.findViewById(R.id.movieTitle);
        mDateText = (TextView) root.findViewById(R.id.movieReleaseDate);
        mGenreText = (TextView) root.findViewById(R.id.movieGenre);
        mDurationText = (TextView) root.findViewById(R.id.movieTime);
        mThumbUpCntText = (TextView) root.findViewById(R.id.thumbUpCntText);
        mThumbDownCntText = (TextView) root.findViewById(R.id.thumbDownCntText);
        mReservationGradeText = (TextView) root.findViewById(R.id.movieReservationGrade);
        mReservationRateText = (TextView) root.findViewById(R.id.movieReservationRate);
        mReviewerRatingText = (TextView) root.findViewById(R.id.reviewerRating);
        mAudienceText = (TextView) root.findViewById(R.id.audience);
        mSynopsisText = (TextView) root.findViewById(R.id.movieDescription);
        mMovieDirectorText = (TextView) root.findViewById(R.id.movieDirector);
        mMovieActorText = (TextView) root.findViewById(R.id.movieStars);

        // get ImageView
        mPosterView = (ImageView) root.findViewById(R.id.moviePoster);

        // get RatingBar
        mRatingBar = (RatingBar) root.findViewById(R.id.ratingBarInDetails);

        // setting View
        Glide.with(getActivity()).load(movieDetailsInfo.getImage()).into(mPosterView);
        mMovieTitleText.setText(movieDetailsInfo.getTitle());
        mDateText.setText(movieDetailsInfo.getDate());
        mGenreText.setText(movieDetailsInfo.getGenre());
        mDurationText.setText(Integer.toString(movieDetailsInfo.getDuration()));
        mThumbUpCntText.setText(Integer.toString(movieDetailsInfo.getLike()));
        mThumbDownCntText.setText(Integer.toString(movieDetailsInfo.getDislike()));
        mReservationGradeText.setText(Integer.toString(movieDetailsInfo.getReservation_grade()));
        mReservationRateText.setText(Float.toString(movieDetailsInfo.getReservation_rate()));
        mRatingBar.setRating(movieDetailsInfo.getUser_rating());
        mReviewerRatingText.setText(Float.toString(movieDetailsInfo.getUser_rating() * 2));
        mAudienceText.setText(Integer.toString(movieDetailsInfo.getAudience()));
        mSynopsisText.setText(movieDetailsInfo.getSynopsis());
        mMovieDirectorText.setText(movieDetailsInfo.getDirector());
        mMovieActorText.setText(movieDetailsInfo.getActor());
    }

    public void requestReviewList() {
        if(NetworkHelper.getConnectivityStatus(getActivity()) != NetworkHelper.TYPE_NOT_CONNECTED) {
            String url = "http://" + RequestHelper.host + ":" + RequestHelper.port + "/movie/readCommentList?id=";
            url += movieDetailsInfo.id + "&length=" + 5;

            StringRequest request = new StringRequest(
                    Request.Method.GET,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            processReviewList(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
            );

            request.setShouldCache(false);
            RequestHelper.requestQueue.add(request);
        }else {
            Toast.makeText(getActivity(), "네트워크 연결없음(DB에서 리뷰 불러옴)", Toast.LENGTH_LONG).show();
            reviewList.result.addAll(DBHelper.selectReviews(movieDetailsInfo.id));

            if(reviewList.result.size() == 0) {
                Toast.makeText(getActivity(), "저장된 데이터 없음", Toast.LENGTH_LONG).show();
            }else {
                showReviewListView();
            }
        }

    }

    public void processReviewList(String response) {
        Gson gson = new Gson();
        ResponseReviewInfo info = gson.fromJson(response, ResponseReviewInfo.class);
        if (info.code == 200) {
            reviewList = gson.fromJson(response, ReviewList.class);

            DBHelper.insertReview(reviewList.result);
            Toast.makeText(getActivity(), "DB에 리뷰 저장", Toast.LENGTH_LONG).show();
            showReviewListView();
        }
    }

    public void showReviewListView() {
        if(mReviewList.size() != 0) {
            mReviewList.clear();
        }
        mReviewList.addAll(reviewList.result);
        // reviewListView setting
        mAdapter = new ReviewListViewAdapter(mReviewList);
        mAdapter.notifyDataSetChanged();
        mReviewListView.setAdapter(mAdapter);
    }

    public void thumbUpBtnClicked() {
        if (mThumbUpState) {
            thumbUpCntFunc(true);
        } else {
            if (mThumbDownState) {
                thumbDownCntFunc(true);
            }
            thumbUpCntFunc(false);
        }
    }

    public void thumbDownBtnClicked() {
        if (mThumbDownState) {
            thumbDownCntFunc(true);
        } else {
            if (mThumbUpState) {
                thumbUpCntFunc(true);
            }
            thumbDownCntFunc(false);
        }
    }

    public void thumbUpCntFunc(boolean type) {  // true: 원래 눌려있을 경우, false: 안눌려있을 경우
        int curCnt = Integer.parseInt(mThumbUpCntText.getText().toString());

        if (type) {
            processLikeDislike(1);
            curCnt -= 1;
            mThumbUpBtn.setBackgroundResource(R.drawable.thumb_up_selector);
        } else {
            processLikeDislike(0);
            curCnt += 1;
            mThumbUpBtn.setBackgroundResource(R.drawable.ic_thumb_up_selected);
        }

        mThumbUpCntText.setText(String.valueOf(curCnt));
        mThumbUpState = !mThumbUpState;
    }

    public void thumbDownCntFunc(boolean type) {
        int curCnt = Integer.parseInt(mThumbDownCntText.getText().toString());

        if (type) {
            processLikeDislike(3);
            curCnt -= 1;
            mThumbDownBtn.setBackgroundResource(R.drawable.thumb_down_selector);
        } else {
            processLikeDislike(2);
            curCnt += 1;
            mThumbDownBtn.setBackgroundResource(R.drawable.ic_thumb_down_selected);
        }

        mThumbDownCntText.setText(String.valueOf(curCnt));
        mThumbDownState = !mThumbDownState;
    }

    public void processLikeDislike(final int type) {
        String url = "http://" + RequestHelper.host + ":" + RequestHelper.port + "/movie/increaseLikeDisLike";

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
                params.put("id", movieDetailsInfo.getId() + "");
                switch (type) {
                    case 0:     // increase like
                        params.put("likeyn", "Y");
                        break;
                    case 1:     // decrease like
                        params.put("likeyn", "N");
                        break;
                    case 2:     // increase dislike
                        params.put("dislikeyn", "Y");
                        break;
                    case 3:     // decrease dislike
                        params.put("dislikeyn", "N");
                        break;
                }
                return params;
            }
        };

        request.setShouldCache(false);
        RequestHelper.requestQueue.add(request);

    }

    // 리뷰 작성 화면
    public void goCommenting() {
        if(NetworkHelper.getConnectivityStatus(getActivity()) != NetworkHelper.TYPE_NOT_CONNECTED) {
            Intent rv_intent = new Intent(getActivity(), ReviewWriteActivity.class);
            rv_intent.putExtra("movieInfo", movieDetailsInfo);
            startActivityForResult(rv_intent, req_COMMENTING);
        }else {
            Toast.makeText(getActivity(), "작성 불가(네트워크 연결 없음)", Toast.LENGTH_LONG).show();
        }
    }

    // 리뷰 모두 보기 화면
    public void goAllReview() {
        Intent allReview_intent = new Intent(getActivity(), AllReviewActivity.class);
        allReview_intent.putExtra("movieInfo", movieDetailsInfo);
//        allReview_intent.putParcelableArrayListExtra("reviewList", mReviewList);
        startActivityForResult(allReview_intent, req_viewAllReview);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_OK) {
            requestReviewList();
        }

    }
}
