package com.example.simpleapp.model;

import android.os.Parcel;
import android.os.Parcelable;

/*
    id: 7077,
    writer: "ByungKwan",
    movieId: 1,
    writer_image: null,
    time: "2020-07-16 16:05:23",
    timestamp: 1594883123,
    rating: 4.9,
    contents: "안녕하세요",
    recommend: 0
 */
public class Review implements Parcelable {

    public int id;
    public String writer;
    public int movieId;
    public String writer_image;
    public String time;
    public long timestamp;
    public float rating;
    public String contents;
    public int recommend;

    public Review(int id, String writer, int movieId, String writer_image, String time, long timestamp, float rating, String contents, int recommend) {
        this.id = id;
        this.writer = writer;
        this.movieId = movieId;
        this.writer_image = writer_image;
        this.time = time;
        this.timestamp = timestamp;
        this.rating = rating;
        this.contents = contents;
        this.recommend = recommend;
    }

    public Review(Parcel src) {
        this.id = src.readInt();
        this.writer = src.readString();
        this.movieId = src.readInt();
        this.writer_image = src.readString();
        this.time = src.readString();
        this.timestamp = src.readLong();
        this.rating = src.readFloat();
        this.contents = src.readString();
        this.recommend = src.readInt();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){
        @Override
        public Review createFromParcel(Parcel src) {
            return new Review(src);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(writer);
        dest.writeInt(movieId);
        dest.writeString(writer_image);
        dest.writeString(time);
        dest.writeLong(timestamp);
        dest.writeFloat(rating);
        dest.writeString(contents);
        dest.writeInt(recommend);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getWriter_image() {
        return writer_image;
    }

    public void setWriter_image(String writer_image) {
        this.writer_image = writer_image;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public int getRecommend() {
        return recommend;
    }

    public void setRecommend(int recommend) {
        this.recommend = recommend;
    }
}
