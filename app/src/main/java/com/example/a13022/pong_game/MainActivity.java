package com.example.a13022.pong_game;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        popUpMenu();

    }
    // Creates the popup menu in order to select whether we want to play with another person or solo.
    public void popUpMenu(){
        final Button button = findViewById(R.id.button_id);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(MainActivity.this, button);
                //TODO : revoir signification des "Inflater"
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(MainActivity.this, "You clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
                popup.show();
            }
        });
    }

}
