package com.mysabertooth.mysabertooth;

import android.util.Log;

import com.oralb.sdk.OBTBrush;

/**
 * Created by cassiewang on 11/26/15.
 */
public class ToothbrushRunnable implements Runnable {
    public OBTBrush toothbrush;
    public CatView catView;

    int count = 1;

    public ToothbrushRunnable(OBTBrush toothbrush, CatView catView) {
        this.toothbrush = toothbrush;
    }

    @Override
    public void run() {
        Log.d("mysabertooth", "state polling"+toothbrush.getCurrentBrushState());
        Log.d("mysabertooth", "pressure polling"+toothbrush.isHighPressure());
        Log.d("mysabertooth", "count " + count);
        count++;

        if (toothbrush.isHighPressure()) {
            catView.gotScared();
            catView.meow();
        } else {
            catView.gotScared();
            catView.meow();
        }
    }

}
