package com.mysabertooth.mysabertooth;

import android.app.Application;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.util.Log;

import com.oralb.sdk.OBTBrush;
import com.oralb.sdk.OBTSDK;
import com.oralb.sdk.OBTSdkAuthorizationListener;

import java.util.List;

/**
 * Created by cassiewang on 11/25/15.
 */
public class MySabertooth extends Application {
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            //Call to initialize the OBTSDK
            OBTSDK.initialize(this);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }


}
