package com.example.a13022.pong_game;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

/**
 * Created by Gaetan on 01-06-18.
 */

public class StandBy extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standby);
        Intent i = new Intent(StandBy.this, GameActivity.class);
        if(getResources().getConfiguration().orientation==ORIENTATION_LANDSCAPE) {
            startActivity(i);
        }
        changeName();
    }
    public void changeName(){
        final Button button = findViewById(R.id.button_name);
        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent i = new Intent(StandBy.this, MainActivity.class);
                startActivity(i);
            }
        });
    }
}
