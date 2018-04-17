package com.example.a13022.pong_game;

import android.graphics.Paint;

/**
 * Created by Gaetan on 13-04-18.
 */

public class Ball {
    float centerx,centery,diamx,diamy;
    int radius;

    /* For style& color of geometries, text and bitmaps*/
    Paint paint;

    Ball(int radius, Paint paint){
        this.radius=radius;
        this.paint=paint;
    }
}
