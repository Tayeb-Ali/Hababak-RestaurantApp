package com.hababk.restaurant.activity;

import android.content.DialogInterface;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hababk.restaurant.R;
import com.hababk.restaurant.fragment.LoginSignUpTabFragment;
import com.hababk.restaurant.fragment.VerificationCodeFragment;
import com.hababk.restaurant.utils.IntentKeyConstants;

public class LoginSignUpActivity extends AppCompatActivity {
    private int tabPosition;
    private static final String TAG = LoginSignUpActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_sign_up);

        ImageView splashBg = findViewById(R.id.splashBg);
        Glide.with(this).load(R.drawable.background).into(splashBg);

        ImageView chef_logo = findViewById(R.id.chef_logo);
        Glide.with(this).load(R.drawable.chef_logo).into(chef_logo);

        tabPosition = getIntent().getIntExtra(IntentKeyConstants.TAB_POSITION, 0);
        openLoginSIgnUpTabFragment();
    }

    private void openLoginSIgnUpTabFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.splashFrame, LoginSignUpTabFragment.newInstance(tabPosition), TAG);
        fragmentTransaction.addToBackStack(TAG);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (isAuthInProgress()) {
            alertPhoneVerificationProgress();
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                finish();
            } else {
                super.onBackPressed();
            }
        }
    }

    private boolean isAuthInProgress() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            return false;
        } else {
            VerificationCodeFragment fragment = (VerificationCodeFragment) getSupportFragmentManager().findFragmentByTag(VerificationCodeFragment.class.getName());
            if (fragment == null) {
                return false;
            } else {
                return fragment.isAuthInProgress();
            }
        }
    }

    private void alertPhoneVerificationProgress() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Auth in progress!");
        alertDialog.setMessage("Phone number verification is in progress, are you sure you want to exit?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getSupportFragmentManager().popBackStackImmediate();
                dialog.dismiss();
                onBackPressed();
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }
}