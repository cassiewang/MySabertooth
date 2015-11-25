package com.mysabertooth.mysabertooth;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ShopActivity extends AppCompatActivity {

    Intent intent;
    int fish;
    int items = 0;

    Button catnip;
    Button chicken;
    Button biscuit;
    Button mousey;

    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        intent = getIntent();

        fish = intent.getIntExtra("fish", 0);
        Log.d("mysabertooth", String.format("this is my fish %d", fish));

        catnip = (Button) findViewById(R.id.shop_catnip_buy);
        chicken = (Button) findViewById(R.id.shop_chicken_buy);
        biscuit = (Button) findViewById(R.id.shop_biscuit_buy);
        mousey = (Button) findViewById(R.id.shop_mousey_buy);
        back = (Button) findViewById(R.id.btn_back);


        catnip.setOnClickListener(new ShopClickListener(2));
        chicken.setOnClickListener(new ShopClickListener(3));
        biscuit.setOnClickListener(new ShopClickListener(1));
        mousey.setOnClickListener(new ShopClickListener(5));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShopActivity.this, MainActivity.class);

                intent.putExtra("fish", fish);
                intent.putExtra("items", items);
                startActivity(intent);
            }
        });
    }

    class ShopClickListener implements View.OnClickListener {
        private int price;

        public ShopClickListener(int price) {
            this.price = price;
        }

        @Override
        public void onClick(View v) {
            if (fish - price < 0) {
                Toast toast = Toast.makeText(ShopActivity.this, String.format("You can\'t afford this item. You have %d sardines left", fish), Toast.LENGTH_LONG);
                toast.show();
            } else {
                fish = fish - price;
                items++;
                Toast toast = Toast.makeText(ShopActivity.this, String.format("Saber loves you!"), Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }
}
