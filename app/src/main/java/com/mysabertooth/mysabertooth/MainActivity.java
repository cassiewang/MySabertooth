package com.mysabertooth.mysabertooth;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.oralb.sdk.OBTBrush;
import com.oralb.sdk.OBTSDK;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public ImageView fishButton;
    public Button mainHelpDialogOk;
    public LinearLayout mainHelpDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            //Call to initialize the OBTSDK
            OBTSDK.initialize(this);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        mainHelpDialog = (LinearLayout) findViewById(R.id.help_dialog);
        mainHelpDialogOk = (Button) findViewById(R.id.btn_fish_dialog_ok);
        fishButton = (ImageView) findViewById(R.id.btn_fish);

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
}
