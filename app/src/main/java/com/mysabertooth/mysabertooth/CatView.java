package com.mysabertooth.mysabertooth;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by cassiewang on 11/25/15.
 */
public class CatView extends SurfaceView implements Runnable {

    volatile boolean playing;

    Thread gameThread = null;
    SurfaceHolder ourHolder;
    Canvas canvas;
    Paint paint;
    long fps;
    private long timeThisFrame;
    Bitmap bitmapCat;
    boolean isMoving = false;
    float walkSpeedPerSecond = 250;
    float catXPosition = 0;
    private int frameWidth = 3000;
    private int frameHeight = 3000;

    // How many frames are there on the sprite sheet?
    private int frameCount = 4;

    // Start at the first frame - where else?
    private int currentFrame = 0;

    // What time was it when we last changed frames
    private long lastFrameChangeTime = 0;

    // How long should each frame last
    private int frameLengthInMilliseconds = 100;

    private Rect frameToDraw = new Rect(
            0,
            0,
            frameWidth,
            frameHeight);

    RectF whereToDraw = new RectF(
            catXPosition,                0,
            catXPosition + frameWidth,
            frameHeight);

    public CatView(Context context) {
        super(context);
        setZOrderOnTop(true);
        ourHolder = getHolder();
        ourHolder.setFormat(PixelFormat.TRANSPARENT);

        ourHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                //stop render thread here
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                //start render thread here
                draw();
            }
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

        });


        bitmapCat = BitmapFactory.decodeResource(this.getResources(), R.drawable.cat_petting_8);

        playing = true;
        isMoving = true;
        System.out.println("Dat");
        //update();
        //draw();
        //gameThread = new Thread(this);
        //gameThread.start();
    }


    @Override
    public void run() {
        while (playing) {
            System.out.println("Cate"+playing);
            long startFrameTime = System.currentTimeMillis();
            update();
            draw();

            timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame;
            }
        }
    }

    public void update() {
        if(isMoving){
            catXPosition = catXPosition + (walkSpeedPerSecond / fps);
           Log.d("cat", "moving" + catXPosition);
        }
    }

    public void getCurrentFrame(){

        long time = System.currentTimeMillis();
        if(isMoving) {
            if ( time > lastFrameChangeTime + frameLengthInMilliseconds) {
                lastFrameChangeTime = time;
                currentFrame++;
                if (currentFrame >= frameCount) {

                    currentFrame = 0;
                }
            }
        }
        frameToDraw.left = currentFrame * frameWidth;
        frameToDraw.right = frameToDraw.left + frameWidth;
    }

    public void draw() {
        System.out.println("DRAW "+ourHolder.getSurface().isValid());
        if (ourHolder.getSurface().isValid()) {
            canvas = ourHolder.lockCanvas();

            /*whereToDraw.set((int)catXPosition,
                    0,
                    (int)catXPosition + frameWidth,
                    frameHeight);*/

            whereToDraw.set((int)catXPosition,
                    0,
                    (int)catXPosition + frameWidth,
                    frameHeight);

            //getCurrentFrame();
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

            canvas.drawBitmap(bitmapCat,
                    frameToDraw,
                    whereToDraw, null);

            Log.d("draw", "drew"+bitmapCat);
            ourHolder.unlockCanvasAndPost(canvas);
        }
    }

    /*public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }
    }

    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }*/
}
