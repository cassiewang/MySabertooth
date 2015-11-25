package com.mysabertooth.mysabertooth;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.graphics.Typeface;
import android.widget.TextView;

import com.oralb.sdk.OBTBrush;
import com.oralb.sdk.OBTBrushListener;
import com.oralb.sdk.OBTSDK;
import com.oralb.sdk.OBTSdkAuthorizationListener;

import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements OBTBrushListener {

    public TextView fishButton;
    public Button mainHelpDialogOk;
    public LinearLayout mainHelpDialog;
    public CatView catView;
    public BrushView brushView;
    public LinearLayout catHolder;
    public Button connect;
    public ImageView shopButton;
    private MediaPlayer mediaPlayer;
    public LinearLayout heartsHolder;
    public ArrayList<ImageView> hearts;

    public OBTBrush toothbrush;

    public ToothbrushRunnable toothbrushRunnable;

    ScheduledExecutorService executor;

    ScheduledFuture future;

    int fish = 5;

    Bitmap heartFilledBitmap;
    Bitmap heartEmptyBitmap;

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

        //Initialize the Typeface
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/font.ttf");
        ((TextView) findViewById(R.id.help_dialog_text)).setTypeface(typeface);
        
        mainHelpDialog = (LinearLayout) findViewById(R.id.help_dialog);
        mainHelpDialogOk = (Button) findViewById(R.id.btn_fish_dialog_ok);
        fishButton = (TextView) findViewById(R.id.btn_fish);
        shopButton = (ImageView) findViewById(R.id.btn_shop);
        heartsHolder = (LinearLayout) findViewById(R.id.hearts_holder);

        shopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(_self, ShopActivity.class);
                intent.putExtra("fish", fish);
                startActivity(intent);
            }
        });

        fishButton.setText(fish + "");

        catHolder = (LinearLayout) findViewById(R.id.cat_holder);
        catView = new CatView(this);
        catHolder.addView(catView);

        heartFilledBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.heart_gray_8);
        heartEmptyBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.heart_red_8);

        hearts = new ArrayList<ImageView>();
        hearts.add((ImageView) findViewById(R.id.heart_1));
        hearts.add((ImageView) findViewById(R.id.heart_2));
        hearts.add((ImageView) findViewById(R.id.heart_3));
        hearts.add((ImageView) findViewById(R.id.heart_4));
        hearts.add((ImageView) findViewById(R.id.heart_5));

        catView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (catView.isScared) {
                    catView.purr();
                    catView.gotSatisfied();
                } else {
                    catView.gotScared();
                    catView.meow();
                }
            }
        });

        mainHelpDialog.setVisibility(View.GONE);

        fishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainHelpDialog.setVisibility(View.VISIBLE);
                catView.setVisibility(View.GONE);
            }
        });

        mainHelpDialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainHelpDialog.setVisibility(View.GONE);
                catView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set this activity as OBTBrushListener
        OBTSDK.setOBTBrushListener(this);
        mediaPlayer = MediaPlayer.create(this, R.raw.bgm);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        //mediaPlayer = MediaPlayer.create(this, R.raw.bgm);

        Intent intent = getIntent();
        int itemsBought = intent.getIntExtra("items", 0);

        for (int i = 0; i < itemsBought; i ++) {
            ImageView heart = hearts.get(i);
            heart.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.heart_red_8));
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        OBTSDK.stopScanning();
        // Remove the OBTBrushListener
        OBTSDK.setOBTBrushListener(null);
        mediaPlayer.release();
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
        Log.d("mysabertooth", "disconnected");
        future.cancel(false);
        executor.shutdown();

        fish = fish + 10;
        fishButton.setText(fish+"");
    }

    @Override
    public void onBrushConnected() {
        Log.d("mysabertooth", "yas");
        toothbrush = OBTSDK.getConnectedToothbrush();
        Log.d("mysabertooth", "state "+toothbrush.getCurrentBrushState());
        Log.d("mysabertooth", "pressure "+toothbrush.isHighPressure());
        OBTSDK.stopScanning();

        executor = Executors.newSingleThreadScheduledExecutor();
        future = executor.scheduleWithFixedDelay(new ToothbrushRunnable(toothbrush, catView), 0, 500, TimeUnit.MILLISECONDS);
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
       Log.d("mysabertooth", "meron state %d".format(String.valueOf(i)));
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

    @Override
    public void onDestroy() {
        mediaPlayer.stop();
        super.onDestroy();
    }

}
