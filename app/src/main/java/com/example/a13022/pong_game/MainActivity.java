package com.example.a13022.pong_game;


import android.content.Context;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    public String opponentName = "";
    public String username="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        popUpMenu();
        settingsButton();
        goButton();
        Toast.makeText(MainActivity.this, getUser2(), Toast.LENGTH_SHORT).show();
        this.setTheme(R.style.AppTheme);
    }
    // Creates the popup_menu popup_menu in order to select whether we want to play with another person or solo.
    public void popUpMenu(){
        final Button button = findViewById(R.id.button_id);
        final EditText opponent_name = findViewById(R.id.username_opponent);
        final EditText username = findViewById(R.id.username);
        final Button button_go = findViewById(R.id.button_selection);
        opponent_name.setVisibility(View.INVISIBLE);
        button_go.setVisibility(View.INVISIBLE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(MainActivity.this, button);
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
                    public boolean onMenuItemClick(MenuItem item) {
//                        Toast.makeText(MainActivity.this, "You clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        if(item.getTitle().equals("Multiplayer")){
                            opponent_name.setVisibility(View.VISIBLE);
                            button_go.setVisibility(View.VISIBLE);
                        }
                        else{
                            opponent_name.setVisibility(View.INVISIBLE);
                            button_go.setVisibility(View.INVISIBLE);
                            setUsername(username.getText().toString());
                            Toast.makeText(MainActivity.this, "You clicked : " + getUsername(), Toast.LENGTH_SHORT).show();
                            //TODO : If the player plays against the computer, the next page should be the game
                            Timer timer = new Timer();
                            /**
                             * @param New Timer task
                             * @param Delay
                             */
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    Intent i = new Intent(MainActivity.this, GameActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            },1000);
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });
    }
    public void goButton(){
       final Button button = findViewById(R.id.button_selection);
       final EditText username = findViewById(R.id.username);
       final EditText opponent_name = findViewById(R.id.username_opponent);
       button.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View view){
                //TODO : when button "Go" is pressed the next page should either the game or a loading page to connect both the players.
                setUsername(username.getText().toString());
                setUser2(opponent_name.getText().toString());
                Toast.makeText(MainActivity.this, "You clicked : " + getUsername() + " , " + getUser2(), Toast.LENGTH_SHORT).show();
           }
       });
    }

    public void settingsButton(){
        final ImageButton button = findViewById(R.id.button_settings);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Context context = MainActivity.this;
                Class destinationClass = SettingsActivity.class;
                Intent i = new Intent(context,destinationClass);

                //TODO : direction towards the page settings has to be put here
                Toast.makeText(MainActivity.this, "You clicked the robot DAMN YOU", Toast.LENGTH_SHORT).show();
                startActivity(i);
            }
        });
    }

    public void setUser2(String opponentName){
        this.opponentName = opponentName;
    }
    public String getUser2(){
        return this.opponentName;
    }
    public void setUsername(String username){
        if(username.isEmpty() || username.equals("")) {
            username = "Default User";
        }
        this.username = username;
    }
    public String getUsername(){
        return this.username;
    }
}
