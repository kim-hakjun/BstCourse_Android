package com.example.simpleapp.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.simpleapp.model.MovieDetailsInfo;
import com.example.simpleapp.model.MovieSummaryInfo;
import com.example.simpleapp.model.Review;

import java.util.ArrayList;


public class DBHelper {

    private static SQLiteDatabase db;
    public final static String DB_NAME = "movie";

    public static class DatabaseOpenHelper extends SQLiteOpenHelper {
        public DatabaseOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {   // DB 새로 생성
            // 영화목록, 영화 상세정보, 한줄평목록 저장할 db 생성
            String tableName_1 = "movie_summary";
            String tableName_2 = "movie_details";
            String tableName_3 = "review";

            String sql_1 = "CREATE table if not exists " + tableName_1 + "(" +
                    "id integer primary key, " +
                    "title text, " +
                    "title_eng text, " +
                    "date text, " +
                    "user_rating float, " +
                    "audience_rating float, " +
                    "reviewer_rating float, " +
                    "reservation_rate float, " +
                    "reservation_grade integer, " +
                    "grade integer, " +
                    "thumb text, " +
                    "image text" +
                    ")";

            String sql_2 = "CREATE table if not exists " + tableName_2 + "(" +
                    "title text, " +
                    "id integer primary key, " +
                    "date text, " +
                    "user_rating float, " +
                    "audience_rating float, " +
                    "reviewer_rating float, " +
                    "reservation_rate float, " +
                    "reservation_grade integer, " +
                    "grade integer, " +
                    "thumb text, " +
                    "image text, " +
                    "photos text, " +
                    "videos text, " +
                    "outlinks text, " +
                    "genre text, " +
                    "duration integer, " +
                    "audience integer, " +
                    "synopsis text, " +
                    "director text, " +
                    "actor text, " +
                    "like_num integer, " +
                    "dislike_num integer" +
                    ")";

            String sql_3 = "CREATE table if not exists " + tableName_3 + "(" +
                    "id  integer primary key, " +
                    "writer  text, " +
                    "movie_id  integer, " +
                    "writer_image  text, " +
                    "time  text, " +
                    "timestamp  long, " +
                    "rating  float, " +
                    "contents  text, " +
                    "recommend  integer" +
                    ")";

            try {
                db.execSQL(sql_1);
                db.execSQL(sql_2);
                db.execSQL(sql_3);
            } catch (Exception e) {
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {  // 기존 DB 업그레이드
        }

    }

    public static void openDB(Context context) {
        try {
            DatabaseOpenHelper helper = new DatabaseOpenHelper(context, DB_NAME, null, 1);
            db = helper.getWritableDatabase();   // 쓸 수 있는 권한
        } catch (Exception e) {
        }
    }

    public static void insertMovieSummary(ArrayList<MovieSummaryInfo> summaryInfos) {
        String sql = "INSERT OR REPLACE INTO movie_summary" +
                "(id, title, title_eng, date, user_rating, audience_rating, reviewer_rating, reservation_rate, reservation_grade, grade, thumb, image) " +
                "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            for (int i = 0; i < summaryInfos.size(); i++) {
                MovieSummaryInfo cur = summaryInfos.get(i);

                Object[] params = { cur.getId(),
                                    cur.getTitle(),
                                    cur.getTitle_eng(),
                                    cur.getDate(),
                                    cur.getUser_rating(),
                                    cur.getAudience_rating(),
                                    cur.getReviewer_rating(),
                                    cur.getReservation_rate(),
                                    cur.getReservation_grade(),
                                    cur.getGrade(),
                                    cur.getThumb(),
                                    cur.getImage()
                };

                db.execSQL(sql, params);
                Log.d("DB 추가 -> ", cur.getGrade()+"");
            }
            Log.d("DB 추가 -> ", "영화 목록 데이터");
        } catch (Exception e) {
            Log.d("DB 추가안됨 -> ", "영화 목록 데이터");
        }
    }

    public static void insertMovieDetails(MovieDetailsInfo detailsInfo) {
        String sql = "INSERT OR REPLACE INTO movie_details" +
                "(title, id, date, user_rating, audience_rating, reviewer_rating, reservation_rate, reservation_grade, " +
                "grade, thumb, image, photos, videos, outlinks, genre, duration, audience, synopsis, director, actor, like_num, dislike_num) " +
                "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try{
            Object[] params = { detailsInfo.getTitle(), detailsInfo.getId(), detailsInfo.getDate(), detailsInfo.getUser_rating(), detailsInfo.getAudience_rating(),
                                detailsInfo.getReviewer_rating(), detailsInfo.getReservation_rate(), detailsInfo.getReservation_grade(),
                                detailsInfo.getGrade(), detailsInfo.getThumb(), detailsInfo.getImage(), detailsInfo.getPhotos(), detailsInfo.getVideos(), detailsInfo.getOutlinks(),
                                detailsInfo.getGenre(), detailsInfo.getDuration(), detailsInfo.getAudience(), detailsInfo.getSynopsis(), detailsInfo.getDirector(),
                                detailsInfo.getActor(), detailsInfo.getLike(), detailsInfo.getDislike()};

            db.execSQL(sql, params);
            Log.d("DB 추가 -> ", "영화 상세 정보: " + detailsInfo.getTitle());
        }catch (Exception e) {
            Log.d("DB 추가안됨 -> ", "영화 상세 정보: " + detailsInfo.getTitle());
        }
    }

    public static void insertReview(ArrayList<Review> reviews) {
        String sql = "INSERT OR REPLACE INTO review" +
                "(id, writer, movie_id, writer_image, time, timestamp, rating, contents, recommend) " +
                "values (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            for(int i = 0; i < reviews.size(); i++) {
                Review cur = reviews.get(i);

                Object[] params = { cur.getId(), cur.getWriter(), cur.getMovieId(), cur.getWriter_image(),
                        cur.getTime(), cur.getTimestamp(),cur.getRating(), cur.getContents(), cur.getRecommend()};

                db.execSQL(sql, params);
                Log.d("DB 추가 -> ", "#" + cur.getMovieId() + " 리뷰 목록");
            }
        }catch (Exception e) {}
    }

    public static ArrayList<MovieSummaryInfo> selectMovieSummaryInfos(int order) {
        String sql = "SELECT * FROM movie_summary ";
        if(order == 1) {
            sql += "ORDER BY reservation_rate DESC";
        }else if(order == 2) {  // 기준을 모르겠음
            sql += "ORDER BY user_rating";
        }else if(order == 3) {
            sql += "ORDER BY date DESC";
        }

        ArrayList<MovieSummaryInfo> ret = new ArrayList<MovieSummaryInfo>();

        try{
            Cursor cursor = db.rawQuery(sql, null);

            while(cursor.moveToNext()) {
                MovieSummaryInfo cur = new MovieSummaryInfo(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getFloat(4),
                        cursor.getFloat(5),
                        cursor.getFloat(6),
                        cursor.getFloat(7),
                        cursor.getInt(8),
                        cursor.getInt(9),
                        cursor.getString(10),
                        cursor.getString(11)
                );
                Log.d("grade 확인 -> ", cursor.getInt(9)+"");
                ret.add(cur);
            }
            cursor.close();
        }catch (Exception e) {
            Log.d("영화 요약정보 불러오기 에러->" , " sql 실행안됨");
        }

        return ret;
    }

    public static ArrayList<MovieDetailsInfo> selectMovieDetailsInfos(int id) {
        String sql = "SELECT * FROM movie_details WHERE id = " + id;
        ArrayList<MovieDetailsInfo> ret = new ArrayList<MovieDetailsInfo>();

        try {
            Cursor cursor = db.rawQuery(sql, null);

            while(cursor.moveToNext()) {
                MovieDetailsInfo cur = new MovieDetailsInfo(
                        cursor.getString(0),
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getFloat(3),
                        cursor.getFloat(4),
                        cursor.getFloat(5),
                        cursor.getFloat(6),
                        cursor.getInt(7),
                        cursor.getInt(8),
                        cursor.getString(9),
                        cursor.getString(10),
                        cursor.getString(11),
                        cursor.getString(12),
                        cursor.getString(13),
                        cursor.getString(14),
                        cursor.getInt(15),
                        cursor.getInt(16),
                        cursor.getString(17),
                        cursor.getString(18),
                        cursor.getString(19),
                        cursor.getInt(20),
                        cursor.getInt(21)
                );
                ret.add(cur);
            }
            cursor.close();
        }catch (Exception E) {
            Log.d("영화 상세정보 불러오기 에러->" , " sql 실행안됨");
        }


        return ret;
    }

    public static ArrayList<Review> selectReviews(int id) {
        String sql = "SELECT * FROM review WHERE movie_id = " + Integer.toString(id) + " ORDER BY timestamp DESC";
        ArrayList<Review> ret = new ArrayList<Review>();

        try {
            Cursor cursor = db.rawQuery(sql, null);

            while(cursor.moveToNext()) {
                Review cur = new Review(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getInt(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getLong(5),
                        cursor.getFloat(6),
                        cursor.getString(7),
                        cursor.getInt(8)
                );
                ret.add(cur);
            }
            cursor.close();
        }catch (Exception e) {
            Log.d("리뷰 불러오기 에러->" , " sql 실행안됨");
        }

        return ret;
    }

}
