package com.example.a13022.pong_game;

import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by Gaetan on 13-04-18.
 */

public class Bat {
    int batWidth;
    int batHeight;
    Paint paint;

    RectF bounds;
    int collision;

    Bat(int batWidth, int batHeight, Paint paint){
        this.batHeight=batHeight;
        this.batWidth=batWidth;
        this.paint=paint;

        /* Create new Rectangle with specified ciirdubates
        * param: bottom,left,right,top*/
        this.bounds=new RectF(0,0,batWidth,batHeight);
        this.collision=0;
    }
}
