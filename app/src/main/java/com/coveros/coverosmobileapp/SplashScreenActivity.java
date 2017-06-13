package com.coveros.coverosmobileapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by EPainter on 6/12/2017.
 */

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Possible exit

        if(getIntent().getBooleanExtra("EXIT", false)){
            SplashScreenActivity.this.finish();
            System.exit(0);
        }
    }
}
