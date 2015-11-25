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
import android.media.AudioManager;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.media.SoundPool;
import android.media.AudioAttributes;

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
    Bitmap scaredCat;
    boolean isMoving = false;
    float walkSpeedPerSecond = 1024;
    float catXPosition = 0;
    private int frameWidth = 1000;
    private int frameHeight = 1000;

    //To play cat sounds
    SoundPool sp;
    int catMeow;
    int catPurr;


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

    public boolean isScared = false;

    public CatView(Context context) {
        super(context);
        setZOrderOnTop(true);
        ourHolder = getHolder();
        ourHolder.setFormat(PixelFormat.TRANSPARENT);

        final CatView _self = this;
        initializeSoundPool(context);
        ourHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                //stop render thread here
                pause();
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
               resume();
                //draw();
            }
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

        });

        bitmapCat = BitmapFactory.decodeResource(this.getResources(), R.drawable.cat_petting_24);
        scaredCat = BitmapFactory.decodeResource(this.getResources(), R.drawable.cat_scared_24);


    }


    @Override
    public void run() {
        while (playing) {
            System.out.println("Cate" + playing);
            long startFrameTime = System.currentTimeMillis();
            update();
            draw();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private void initializeSoundPool(Context context) {
        if((android.os.Build.VERSION.SDK_INT) == 21){
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .build();
            sp = new SoundPool.Builder()
                    .setMaxStreams(2)
                    .setAudioAttributes(audioAttributes)
                    .build();
            catMeow = sp.load(context, R.raw.cat_meow, 1);
            catPurr = sp.load(context, R.raw.cat_purr, 1);
        }
        else{
            sp = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
            catMeow = sp.load(context, R.raw.cat_meow, 1);
            catPurr = sp.load(context, R.raw.cat_purr, 1);
        }
    }

    public void meow() {
        sp.play (catMeow, 0.9f, 0.9f, 1, 0, 1);
    }

    public void purr() {
        sp.play (catPurr, 0.9f, 0.9f, 1, 0, 1);
    }

    public void update() {
        if (forward) {
            catXPosition = catXPosition + walkSpeedPerSecond;
        } else {
            catXPosition = catXPosition - walkSpeedPerSecond;
        }

        if (catXPosition > (frameCount - 1) * walkSpeedPerSecond) {
            forward = false;
        } else if (catXPosition <= 0) {
            forward = true;
        }

    }

    public void draw() {
        if (ourHolder.getSurface().isValid()) {
            canvas = ourHolder.lockCanvas();

            frameToDraw.set((int) catXPosition,
                    0,
                    (int) catXPosition + frameWidth,
                    frameHeight);

            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

            Bitmap toDraw;

            if (isScared) {
                toDraw = scaredCat;
                //meow();
            } else {
                toDraw = bitmapCat;
                //purr();
            }

            canvas.drawBitmap(toDraw,
                    frameToDraw,
                    whereToDraw, null);

            ourHolder.unlockCanvasAndPost(canvas);
        }

    }


    public void pause() {
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
    }

    public void gotScared() {
        isScared = true;
    }

    public void gotSatisfied() {
        isScared = false;
    }
}
