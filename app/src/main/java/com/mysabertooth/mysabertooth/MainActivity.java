package com.mysabertooth.mysabertooth;

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
import java.util.List;



public class MainActivity extends AppCompatActivity implements OBTBrushListener {

    public ImageView fishButton;
    public Button mainHelpDialogOk;
    public LinearLayout mainHelpDialog;
    public CatView catView;
    public BrushView brushView;
    public LinearLayout catHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        OBTSDK.authorizeSdk(new OBTSdkAuthorizationListener() {
            @Override
            public void onSdkAuthorizationSuccess() {
                Log.d("mysabertooth", "successs!");
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

        catHolder = (LinearLayout) findViewById(R.id.cat_holder);
        catView = new CatView(this);
        catHolder.addView(catView);

        brushView = new BrushView(this);
        catHolder.addView(brushView);

        catView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (catView.isScared) {
                    //brushView.pause();
                    catView.gotSatisfied();
                } else {
                    //brushView.resume();
                    catView.gotScared();
                }
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
        // Remove the OBTBrushListener
        OBTSDK.setOBTBrushListener(null);
    }


    @Override
    public void onNearbyBrushesFoundOrUpdated(List<OBTBrush> list) {
        Log.d("mysabertooth", "meron");
        Log.d("mysabertooth", list.get(0).getName());
    }

    @Override
    public void onBluetoothError() {

    }

    @Override
    public void onBrushDisconnected() {

    }

    @Override
    public void onBrushConnected() {
        System.out.println("yas");
    }

    @Override
    public void onBrushConnecting() {

    }

    @Override
    public void onBrushingTimeChanged(long l) {

    }

    @Override
    public void onBrushingModeChanged(int i) {

    }

    @Override
    public void onBrushStateChanged(int i) {

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

    }
}
