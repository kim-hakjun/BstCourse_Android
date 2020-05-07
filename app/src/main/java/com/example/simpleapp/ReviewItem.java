package com.example.simpleapp;

import android.os.Parcel;
import android.os.Parcelable;

public class ReviewItem implements Parcelable {

    private int mUserImage;
    private String mUserId;
    private String mWriteTime;
    private float mRating;
    private String mUserReview;
    private int mRecommend;

    public ReviewItem(int mUserImage, String mUserId, String mWriteTime, float mRating, String mUserReview, int mRecommend) {
        this.mUserImage = mUserImage;
        this.mUserId = mUserId;
        this.mWriteTime = mWriteTime;
        this.mRating = mRating;
        this.mUserReview = mUserReview;
        this.mRecommend = mRecommend;
    }

    public ReviewItem(Parcel src) {
        this.mUserImage = src.readInt();
        this.mUserId = src.readString();
        this.mWriteTime = src.readString();
        this.mRating = src.readFloat();
        this.mUserReview = src.readString();
        this.mRecommend = src.readInt();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){
        @Override
        public ReviewItem createFromParcel(Parcel src) {
            return new ReviewItem(src);
        }

        @Override
        public ReviewItem[] newArray(int size) {
            return new ReviewItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mUserImage);
        dest.writeString(mUserId);
        dest.writeString(mWriteTime);
        dest.writeFloat(mRating);
        dest.writeString(mUserReview);
        dest.writeInt(mRecommend);
    }

    public int getmUserImage() {
        return mUserImage;
    }

    public void setmUserImage(int mUserImage) {
        this.mUserImage = mUserImage;
    }

    public String getmUserId() {
        return mUserId;
    }

    public void setmUserId(String mUserId) {
        this.mUserId = mUserId;
    }

    public String getmWriteTime() {
        return mWriteTime;
    }

    public void setmWriteTime(String mWriteTime) {
        this.mWriteTime = mWriteTime;
    }

    public float getmRating() {
        return mRating;
    }

    public void setmRating(float mRating) {
        this.mRating = mRating;
    }

    public String getmUserReview() {
        return mUserReview;
    }

    public void setmUserReview(String mUserReview) {
        this.mUserReview = mUserReview;
    }

    public int getmRecommend() {
        return mRecommend;
    }

    public void setmRecommend(int mRecommend) {
        this.mRecommend = mRecommend;
    }

}
