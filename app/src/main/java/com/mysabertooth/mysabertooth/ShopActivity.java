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
import android.widget.TextView;
import android.graphics.Typeface;

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

        //Initialize the Typeface
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/font.ttf");
        ((TextView) findViewById(R.id.catnip_textView)).setTypeface(typeface);
        ((TextView) findViewById(R.id.catnip_cost_textView)).setTypeface(typeface);
        ((TextView) findViewById(R.id.chicken_textView)).setTypeface(typeface);
        ((TextView) findViewById(R.id.chicken_cost_textView)).setTypeface(typeface);
        ((TextView) findViewById(R.id.biscuit_textView)).setTypeface(typeface);
        ((TextView) findViewById(R.id.biscuit_cost_textView)).setTypeface(typeface);
        ((TextView) findViewById(R.id.mousey_textView)).setTypeface(typeface);
        ((TextView) findViewById(R.id.mousey_cost_textView)).setTypeface(typeface);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShopActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);


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
            if (Data.fish - price < 0) {
                Toast toast = Toast.makeText(ShopActivity.this, String.format("You can\'t afford this item. You have %d sardines left", Data.fish), Toast.LENGTH_LONG);
                toast.show();
            } else {
                Data.fish = Data.fish - price;
                Data.items++;
                Toast toast = Toast.makeText(ShopActivity.this, String.format("Saber loves you!"), Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }
}
