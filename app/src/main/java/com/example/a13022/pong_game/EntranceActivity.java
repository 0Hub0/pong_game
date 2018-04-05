package com.example.a13022.pong_game;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class EntranceActivity extends AppCompatActivity {
    private Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrance);

        timer = new Timer();
        /**
         * @param New Timer task
         * @param Delay
         */
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent i = new Intent(EntranceActivity.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        },2000);
    }
}
