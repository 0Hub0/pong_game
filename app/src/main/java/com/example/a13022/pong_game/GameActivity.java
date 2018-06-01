package com.example.a13022.pong_game;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.RectF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

import java.util.Random;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

public class GameActivity extends AppCompatActivity {
    PongView pongView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the action bar
        getSupportActionBar().hide();

        // Fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Get a Display object to access screen details
        Display display = getWindowManager().getDefaultDisplay();
        // Load the resolution into a Point object

        Point size = new Point();
        display.getSize(size);
        Intent i = new Intent(GameActivity.this, StandBy.class);
        pongView = new PongView(this, size.x, size.y, (byte) 0);
        if(getResources().getConfiguration().orientation==ORIENTATION_LANDSCAPE) {
            // Initialize pongView and set it as the view
//            pongView = new PongView(this, size.x, size.y, (byte) 0);
            setContentView(pongView);
        }
        else{
            startActivity(i);
            finish();
        }
    }
//    // This method executes when the player starts the game
    @Override
    protected void onResume() {
        super.onResume();

        // Tell the pongView resume method to execute
        pongView.resume();
    }

    // This method executes when the player quits the game
    @Override
    protected void onPause() {
        super.onPause();

        // Tell the pongView pause method to execute
        pongView.pause();
    }
}

