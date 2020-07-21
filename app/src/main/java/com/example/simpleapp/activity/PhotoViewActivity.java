package com.example.simpleapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.bumptech.glide.Glide;
import com.example.simpleapp.R;

import com.github.chrisbanes.photoview.PhotoView;

public class PhotoViewActivity extends AppCompatActivity {

    PhotoView photoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);

        photoView = findViewById(R.id.photoView);

        Intent intent = getIntent();
        String url = intent.getStringExtra("imageUrl");
        Glide.with(getApplicationContext()).load(url).into(photoView);
    }

    @Override
    public void onBackPressed() {
        finish();
        return;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
