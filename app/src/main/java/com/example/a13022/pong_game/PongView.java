package com.example.a13022.pong_game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Gaetan on 16-04-18.
 */

public class PongView extends SurfaceView implements Runnable{
    // This is our thread
    Thread mGameThread = null;

    // We need a SurfaceHolder object
    // We will see it in action in the draw method soon.
    SurfaceHolder mOurHolder;

    // A boolean which we will set and unset
    // when the game is running- or not
    // It is volatile because it is accessed from inside and outside the thread
    volatile boolean mPlaying;

    // Game is mPaused at the start
    boolean mPaused = true;

    // A Canvas and a Paint object
    Canvas mCanvas;
    Paint mPaint;

    // This variable tracks the game frame rate
    long mFPS;

    // The size of the screen in pixels
    int mScreenX;
    int mScreenY;

    // The players mBat
    Bat mBat;
    Bat mOpponentBat;

    // A mBall
    Ball mBall;

    // The mScore
    int mScore = 0;

    // Lives
    int mLives = 3;
    public PongView(Context context, int x, int y){
        // Call SurfaceView constructor
        super(context);

        this.mScreenX =x;
        this.mScreenY=y;

        this.mOurHolder=getHolder();
        this.mPaint=new Paint();

        this.mBat = new Bat(this.mScreenX, this.mScreenY );
        this.mOpponentBat = new Bat(this.mScreenX, 20);
        // Create a mBall
        mBall = new Ball(mScreenX, mScreenY);
        setupAndRestart();
    }

    public void run(){
        while (mPlaying) {

            // Capture the current time in milliseconds in startFrameTime
            long startFrameTime = System.currentTimeMillis();

            // Update the frame
            if(!mPaused){
                update();
            }

            // Draw the frame
            draw();

        /*
            Calculate the FPS this frame
            We can then use the result to
            time animations in the update methods.
        */
            long timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame >= 1) {
                mFPS = 1000 / timeThisFrame;
            }

        }
    }
    public void setupAndRestart(){
        mBall.reset(this.mScreenY, this.mScreenX);

        // if game over reset scores and mLives
        if(this.mLives == 0) {
            this.mScore = 0;
            this.mLives = 3;
        }
    }
    // Everything that needs to be updated goes in here
    // Movement, collision detection etc.
    public void update(){
        // Move the mBat if required
        mBat.update(mFPS);
        mOpponentBat.update(mFPS);

        mBall.update(mFPS);
        // Check for mBall colliding with mBat
        if(RectF.intersects(mBat.getRect(), mBall.getRect())) {
            mBall.setRandomXVelocity();
            mBall.reverseYVelocity();
            mBall.clearObstacleY(mBat.getRect().top - 2);

            mScore++;
//            mBall.increaseVelocity();
            // play a sound
//            sp.play(beep1ID, 1, 1, 0, 0, 1);
        }
        // Bounce the mBall back when it hits the bottom of screen
        if(mBall.getRect().bottom > mScreenY){
            mBall.reverseYVelocity();
            mBall.clearObstacleY(mScreenY - 2);

            // Lose a life
            mLives--;
            // play a sound
//            sp.play(loseLifeID, 1, 1, 0, 0, 1);

            if(mLives == 0){
                mPaused = true;
                setupAndRestart();
            }
        }
        // Bounce the mBall back when it hits the top of screen
        if(mBall.getRect().top < 0){
            mBall.reverseYVelocity();
            mBall.clearObstacleY(12);
            // play a sound
//            sp.play(beep2ID, 1, 1, 0, 0, 1);
        }
        // If the mBall hits left wall bounce
        if(mBall.getRect().left < 0){
            mBall.reverseXVelocity();
            mBall.clearObstacleX(2);
            // play a sound
//            sp.play(beep3ID, 1, 1, 0, 0, 1);
        }
        if(mBall.getRect().right > mScreenX-1){
            mBall.reverseXVelocity();
            mBall.clearObstacleX(mScreenX - 22);
            // play a sound
//            sp.play(beep3ID, 1, 1, 0, 0, 1);
        }
    }
    // Draw the newly updated scene
    public void draw() {

        // Make sure our drawing surface is valid or we crash
        if (mOurHolder.getSurface().isValid()) {

            // Draw everything here

            // Lock the mCanvas ready to draw
            mCanvas = mOurHolder.lockCanvas();

            // Clear the screen with my favorite color
            mCanvas.drawColor(Color.argb(255, 120, 197, 87));

            // Choose the brush color for drawing
            mPaint.setColor(Color.argb(255, 255, 255, 255));

            // Draw the mBat
            mCanvas.drawRect(mBat.getRect(), mPaint);
            mCanvas.drawRect(mOpponentBat.getRect(), mPaint);
            // Draw the mBall
            mCanvas.drawRect(mBall.getRect(), mPaint);


            // Change the drawing color to white
            mPaint.setColor(Color.argb(255, 255, 255, 255));

            // Draw the mScore
            mPaint.setTextSize(40);
            mCanvas.drawText("Score: " + mScore + "   Lives: " + mLives, 10, 50, mPaint);

            // Draw everything to the screen
            mOurHolder.unlockCanvasAndPost(mCanvas);
        }

    }
    // The SurfaceView class implements onTouchListener
    // So we can override this method and detect screen touches.
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {

        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {

            // Player has touched the screen
            case MotionEvent.ACTION_DOWN:

                mPaused = false;

                // Is the touch on the right or left?
                if(motionEvent.getX() > mScreenX / 2){
                    mBat.setMovementState(mBat.RIGHT);
                }
                else{
                    mBat.setMovementState(mBat.LEFT);
                }

                break;

            // Player has removed finger from screen
            case MotionEvent.ACTION_UP:

                mBat.setMovementState(mBat.STOPPED);
                break;
        }
        return true;
    }
    // If the Activity is paused/stopped
    // shutdown our thread.
    public void pause() {
        mPlaying = false;
        try {
            mGameThread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }

    }

    // If the Activity starts/restarts
    // start our thread.
    public void resume() {
        mPlaying = true;
        mGameThread = new Thread(this);
        mGameThread.start();
    }
}
