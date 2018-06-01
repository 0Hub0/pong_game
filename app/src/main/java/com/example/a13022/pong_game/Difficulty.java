package com.example.a13022.pong_game;

import java.util.ArrayList;

/**
 * Created by Gaetan on 05-05-18.
 */

public class Difficulty {
    // difficulty = 0
    public static ArrayList easy(){
        // TODO : use less memory for digits, short instead of float ?
        float reactionTime = 800;
        byte ballSpeed = 2;

        ArrayList array = new ArrayList();

        array.add(reactionTime);
        array.add(ballSpeed);

        return array;
    }
    public static ArrayList normal(){
        // TODO : use less memory for digits, short instead of float ?
        float reactionTime = 800;
        byte ballSpeed = 3;

        ArrayList array = new ArrayList();

        array.add(reactionTime);
        array.add(ballSpeed);

        return array;
    }
    public static ArrayList heroic(){
        // TODO : use less memory for digits, short instead of float ?
        float reactionTime = 500;
        byte ballSpeed = 5;

        ArrayList array = new ArrayList();

        array.add(reactionTime);
        array.add(ballSpeed);

        return array;
    }
}
