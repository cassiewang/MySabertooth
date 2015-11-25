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
public class BrushView extends SurfaceView implements Runnable {

    volatile boolean playing;

    Thread gameThread = null;
    SurfaceHolder ourHolder;
    Canvas canvas;
    Paint paint;
    long fps;
    private long timeThisFrame;
    Bitmap bitmapBrush;
    boolean isMoving = false;
    float speed = 100;
    float brushXPosition = 0;
    private int frameWidth = 1000;
    private int frameHeight = 1000;

    // How many frames are there on the sprite sheet?
    private int frameCount = 3;

    private boolean forward = true;

    private Rect frameToDraw = new Rect(
            0,
            0,
            frameWidth,
            frameHeight);

    RectF whereToDraw = new RectF(
            0, 0,
            frameWidth,
            frameHeight);

    public BrushView(Context context) {
        super(context);
        setZOrderOnTop(true);
        ourHolder = getHolder();
        ourHolder.setFormat(PixelFormat.TRANSPARENT);

        final BrushView _self = this;

        ourHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                //stop render thread here
                pause();
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                draw();
            }
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

        });

        bitmapBrush = BitmapFactory.decodeResource(this.getResources(), R.drawable.brush_24);

    }


    @Override
    public void run() {
        while (playing) {
            update();
            draw();

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {
        if (forward) {
            brushXPosition = brushXPosition + speed;
        } else {
            brushXPosition = brushXPosition - speed;
        }

        if (brushXPosition > (frameCount - 1) * speed) {
            forward = false;
        } else if (brushXPosition <= 0) {
            forward = true;
        }


    }

    public void draw() {
        System.out.println("DRAW "+ brushXPosition+""+ourHolder.getSurface().isValid());

        if (ourHolder.getSurface().isValid()) {
            canvas = ourHolder.lockCanvas();
            whereToDraw.set((int) brushXPosition,
                    0,
                    (int) brushXPosition + frameWidth,
                    frameHeight);

            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

            canvas.drawBitmap(bitmapBrush,
                    frameToDraw,
                    whereToDraw, null);

            ourHolder.unlockCanvasAndPost(canvas);
        }
    }


    public void pause() {
        playing = false;
        if (gameThread != null) {
            try {
                gameThread.join();
            } catch (InterruptedException e) {
                Log.e("Error:", "joining thread");
            }
        }

    }

    // If SimpleGameEngine Activity is started theb
    // start our thread.
    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }
}