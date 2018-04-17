package com.example.a13022.pong_game;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import org.w3c.dom.Text;

import android.os.Handler;

/**
 * Created by 13022 on 17-04-18.
 */

public class PongView extends SurfaceView implements SurfaceHolder.Callback{
    
    private PongThread gameThread;

    public PongView(Context context, AttributeSet attributeSet){
        // Call constructor of parent class and pass 2 parameters
        super(context,attributeSet);

        /* Abstract Interfaceimport android.os.Handler; for a display surface
         * & allowing us to control the surface size/format, edit pixels, monitor changes */
        SurfaceHolder holder = getHolder();

        /* Callback notifies a Class synchronous/asynchronous of an action from another class
         is completed with success or error*/
        holder.addCallback(this);

        /* This view can receive the focus
        * Focus :A particular GUI element has been selected => Gains focus*/
        setFocusable(true);

        /* Instantiates Game Thread
        * Handler(): Allows us to send/process Message & Runnable ojbects associated
        * with a thread's MessageQueue
        * => Each Handler Instance is linked to a single thread and that thread's message queue*/
        gameThread = new PongThread(holder, context, new Handler());

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){
        gameThread.setSurfaceSize(width,height);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){
        gameThread.setRunning(true);
        gameThread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        boolean retry=true;
        gameThread.setRunning(false);
        while(retry){
            try{
                gameThread.join();
                retry=false;
            } catch (InterruptedException e) {
                //....
            }
        }


    }


}
