package com.mysabertooth.mysabertooth;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.oralb.sdk.OBTBrush;
import com.oralb.sdk.OBTBrushListener;
import com.oralb.sdk.OBTSDK;
import com.oralb.sdk.OBTSdkAuthorizationListener;

import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements OBTBrushListener {

    public ImageView fishButton;
    public Button mainHelpDialogOk;
    public LinearLayout mainHelpDialog;
    public CatView catView;
    public BrushView brushView;
    public LinearLayout catHolder;
    public Button connect;
    public ImageView shopButton;

    public OBTBrush toothbrush;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Log.d("mysabertooth", "authroize!");

        final Context _self = this;

        OBTSDK.authorizeSdk(new OBTSdkAuthorizationListener() {
                @Override
                public void onSdkAuthorizationSuccess() {
                    Log.d("mysabertooth", "successs!");

                    if (OBTSDK.isBluetoothAvailableAndEnabled()) {
                        Log.d("checking Bluetooth", "found true");
                        OBTSDK.startScanning();
                    } else Log.d("checking Bluetooth", "found false");
                }

                @Override
                public void onSdkAuthorizationFailed(int i) {
                    Log.d("mysabertooth", "failuree!");
                }
            }
        );

        mainHelpDialog = (LinearLayout) findViewById(R.id.help_dialog);
        mainHelpDialogOk = (Button) findViewById(R.id.btn_fish_dialog_ok);
        fishButton = (ImageView) findViewById(R.id.btn_fish);
        connect = (Button) findViewById(R.id.btn_connect);
        shopButton = (ImageView) findViewById(R.id.btn_shop);

        shopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(_self, ShopActivity.class);
                startActivity(intent);
            }
        });

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OBTSDK.startScanning();
            }
        });

        catHolder = (LinearLayout) findViewById(R.id.cat_holder);
        catView = new CatView(this);
        catHolder.addView(catView);

        brushView = new BrushView(this);
        catHolder.addView(brushView);

        catView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (catView.isScared) {
                    //brushView.pause();
                    catView.gotSatisfied();
                } else {
                    //brushView.resume();
                    catView.gotScared();
                }*/
            }
        });

        mainHelpDialog.setVisibility(View.GONE);

        fishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainHelpDialog.setVisibility(View.VISIBLE);

            }
        });

        mainHelpDialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainHelpDialog.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set this activity as OBTBrushListener
        OBTSDK.setOBTBrushListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        OBTSDK.stopScanning();
        // Remove the OBTBrushListener
        OBTSDK.setOBTBrushListener(null);
    }


    @Override
    public void onNearbyBrushesFoundOrUpdated(List<OBTBrush> list) {
        int found = list.size();
        Log.d("mysabertooth", "meron" + found);
        if (found > 0) {
            Log.d("mysabertooth", "meron "+found);
            OBTSDK.connectToothbrush(list.get(0), true);
        }
    }

    @Override
    public void onBluetoothError() {

    }

    @Override
    public void onBrushDisconnected() {

    }

    @Override
    public void onBrushConnected() {
        Log.d("mysabertooth", "yas");

    }

    @Override
    public void onBrushConnecting() {
        Log.d("mysabertooth", "connectinga");

    }

    @Override
    public void onBrushingTimeChanged(long l) {

    }

    @Override
    public void onBrushingModeChanged(int i) {

    }

    @Override
    public void onBrushStateChanged(int i) {
       // Log.d("mysabertooth", "meron state %d".format(String.valueOf(i)));
    }

    @Override
    public void onRSSIChanged(int i) {

    }

    @Override
    public void onBatteryLevelChanged(float v) {

    }

    @Override
    public void onSectorChanged(int i) {

    }

    @Override
    public void onHighPressureChanged(boolean b) {
        Log.d("mysabertooth", "pressure"+b);
    }

}
