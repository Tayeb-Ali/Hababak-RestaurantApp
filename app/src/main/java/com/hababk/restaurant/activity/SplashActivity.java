package com.hababk.restaurant.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.hababk.restaurant.R;
import com.hababk.restaurant.utils.Helper;
import com.hababk.restaurant.utils.IntentKeyConstants;
import com.hababk.restaurant.utils.SharedPreferenceUtil;

public class SplashActivity extends AppCompatActivity {
    private SharedPreferenceUtil sharedPreferenceUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView splashBg = findViewById(R.id.splashBg);
        Glide.with(this).load(R.drawable.background).into(splashBg);

        ImageView chef_logo = findViewById(R.id.chef_logo);
        Glide.with(this).load(R.drawable.chef_logo).into(chef_logo);

        sharedPreferenceUtil = new SharedPreferenceUtil(this);
        Helper.refreshSettings(sharedPreferenceUtil);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Helper.isLoggedIn(sharedPreferenceUtil)) {
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                    finish();
                } else {
                    LinearLayout authActionContainer = findViewById(R.id.authActionContainer);

                    Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
                    slide_up.setFillAfter(true);
                    authActionContainer.setVisibility(View.VISIBLE);
                    authActionContainer.startAnimation(slide_up);

                    findViewById(R.id.splash_signin_btn).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openLoginSignUpActivity(0);
                        }
                    });

                    findViewById(R.id.splash_register_btn).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openLoginSignUpActivity(1);
                        }
                    });
                }
            }
        }, 800);
    }

    private void openLoginSignUpActivity(int position) {
        Intent logiSignUpIntent = new Intent(this, LoginSignUpActivity.class);
        logiSignUpIntent.putExtra(IntentKeyConstants.TAB_POSITION, position);
        logiSignUpIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(logiSignUpIntent);
        finish();
    }
}