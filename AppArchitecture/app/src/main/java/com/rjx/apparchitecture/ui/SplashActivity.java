package com.rjx.apparchitecture.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @author Jianxiong Rao
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(this,MainActivity.class));
    }

    @Override
    protected void onStop() {
        super.onStop();
        /** onResume -> B onStop*/
        finish();
    }
}
