package com.mysabertooth.mysabertooth;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

public class ShopActivity extends AppCompatActivity {

    Intent intent;
    int fish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        intent = getIntent();

        fish = intent.getIntExtra("fish", 0);
        Log.d("mysabertooth", String.format("this is my fish %d", fish));
    }

}
