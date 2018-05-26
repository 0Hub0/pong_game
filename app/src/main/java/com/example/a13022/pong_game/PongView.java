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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

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

    // For reaction time
    long time = 0;
    boolean flagMove = true;
    boolean flagIntersection = false;
    float reactionTimeOpponentBat=0f;
    public PongView(Context context, int x, int y, byte difficulty){
        // Call SurfaceView constructor
        super(context);

        this.mScreenX =x;
        this.mScreenY=y;

        this.mOurHolder=getHolder();
        this.mPaint=new Paint();

        this.mBat = new Bat(this.mScreenX, this.mScreenY );
        this.mOpponentBat = new Bat(this.mScreenX, 40);
        // Create a mBall
        mBall = new Ball(mScreenX, mScreenY);
        mBall.reset( this.mScreenX, mScreenY);
        // TODO : add difficulties here
        if(difficulty == 0) {
            ArrayList array = Difficulty.easy();
            this.reactionTimeOpponentBat = (float) array.get(0);
            mBall.increaseVelocity((byte) array.get(1));
        }
        setupAndRestart();
    }

    public void run(){
        while (mPlaying) {

            // Capture the current time in milliseconds in startFrameTime
            long startFrameTime = System.currentTimeMillis();

            // Update the frame
            if(!mPaused){
                try {
                    update();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
    public void update() throws InterruptedException {
        // Move the mBat if required
        mBat.update(mFPS);
        mBall.update(20);
        float xBat = mOpponentBat.getXCoordonate();
        float xBall = mBall.getXCoordonate();
        long currentTimeMillis = System.currentTimeMillis();
        // The opponents bat moves according to position of the ball
        moveBat(xBall, xBat);
        mOpponentBat.update(mFPS);
        // Check for mBall colliding with our mBat
        if(RectF.intersects(mBat.getRect(), mBall.getRect())) {
            mBall.reverseYVelocity();
            mBall.setRandomXVelocity();
            mBall.clearObstacleY(mBat.getRect().top - 2);
            mScore++;
        }
        // Check for mBall colliding with the mOpponentBat
        if(RectF.intersects(mOpponentBat.getRect(), mBall.getRect())) {
            mBall.setRandomXVelocity();
            mBall.reverseYVelocity();
            mBall.clearObstacleY(mOpponentBat.getRect().bottom +22 );
        }
        // Bounce the mBall back when it hits the bottom of screen
        if(mBall.getRect().bottom > mScreenY){
            this.flagIntersection = true;
            mBall.reverseYVelocity();
            mBall.clearObstacleY(mScreenY - 2);
        }
        // Bounce the mBall back when it hits the top of screen
        if(mBall.getRect().top < 0){
            this.flagIntersection = true;
            mBall.reverseYVelocity();
            mBall.clearObstacleY(12);
        }
        // If the mBall hits left wall bounce
        if(mBall.getRect().left < 0){
            this.flagIntersection = true;
            mBall.reverseXVelocity();
            mBall.clearObstacleX(2);
        }
        if(mBall.getRect().right > mScreenX-1){
            this.flagIntersection = true;
            mBall.reverseXVelocity();
            mBall.clearObstacleX(mScreenX - 22);
        }
        if(this.flagIntersection == true){
            if(currentTimeMillis - this.time >= this.reactionTimeOpponentBat){
                this.flagMove = true;
                this.time = currentTimeMillis;
                this.flagIntersection = false;
            }
            else{
                this.flagMove = false;
            }
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
            // Background color game
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
            // TODO : text inserted on game over here
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
    public void moveBat(float xBall, float xBat){
        if(xBall - xBat > 0 && this.flagMove == true){
            mOpponentBat.setMovementState(mOpponentBat.RIGHT);
        }
        else if(xBall - xBat < 0 &&  this.flagMove == true){
            mOpponentBat.setMovementState(mOpponentBat.LEFT);
        }
        else{
            mOpponentBat.setMovementState(mOpponentBat.STOPPED);
        }
    }

// import android.util.AttributeSet;
// import android.view.KeyEvent;
// import android.view.Surface;
// import android.view.SurfaceHolder;
// import android.view.SurfaceView;
// import android.widget.TextView;

// import org.w3c.dom.Text;

// import android.os.Handler;

// /**
//  * Created by 13022 on 17-04-18.
//  */

// public class PongView extends SurfaceView implements SurfaceHolder.Callback{

//     private PongThread gameThread;

//     public PongView(Context context, AttributeSet attributeSet){
//         // Call constructor of parent class and pass 2 parameters
//         super(context,attributeSet);

//         /* Abstract Interfaceimport android.os.Handler; for a display surface
//          * & allowing us to control the surface size/format, edit pixels, monitor changes */
//         SurfaceHolder holder = getHolder();

//         /* Callback notifies a Class synchronous/asynchronous of an action from another class
//          is completed with success or error*/
//         holder.addCallback(this);

//         /* This view can receive the focus
//         * Focus :A particular GUI element has been selected => Gains focus*/
//         setFocusable(true);

//         /* Instantiates Game Thread
//         * Handler(): Allows us to send/process Message & Runnable ojbects associated
//         * with a thread's MessageQueue
//         * => Each Handler Instance is linked to a single thread and that thread's message queue*/
//         gameThread = new PongThread(holder, context, new Handler());

//     }

//     @Override
//     public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){
//         gameThread.setSurfaceSize(width,height);
//     }

//     @Override
//     public void surfaceCreated(SurfaceHolder holder){
//         gameThread.setRunning(true);
//         gameThread.start();
//     }

//     @Override
//     public void surfaceDestroyed(SurfaceHolder holder){
//         boolean retry=true;
//         gameThread.setRunning(false);
//         while(retry){
//             try{
//                 gameThread.join();
//                 retry=false;
//             } catch (InterruptedException e) {
//                 // To finish?
//             }
//         }


//     }



}
