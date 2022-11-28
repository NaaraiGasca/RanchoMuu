package com.example.ranchomuu;

import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class video extends AppCompatActivity {

    private VideoView v1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("Video tutorial");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_video);
        v1 = findViewById(R.id.vvTutorialVideo);

        v1.setVideoURI(Uri.parse("android.resource://"+ getPackageName() + "/" + R.raw.tutorial));
        v1.start();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
}