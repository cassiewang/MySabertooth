package com.mysabertooth.mysabertooth;

import android.graphics.BitmapFactory;
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
        this.catView = catView;
    }

    @Override
    public void run() {
        boolean pressured = toothbrush.isHighPressure();
        Log.d("mysabertooth", "pressure polling"+toothbrush.isHighPressure());

        if (pressured) {
            if (!catView.isScared) {
                catView.gotScared();
                catView.meow();
            }
        } else {
            if (catView.isScared) {
                catView.gotSatisfied();
                catView.purr();
            }
        }
    }

}
