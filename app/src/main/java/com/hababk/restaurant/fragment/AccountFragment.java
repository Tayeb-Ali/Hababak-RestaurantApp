package com.hababk.restaurant.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hababk.restaurant.R;
import com.hababk.restaurant.activity.BankDetailActivity;
import com.hababk.restaurant.activity.EarningsActivity;
import com.hababk.restaurant.activity.ProfileActivity;
import com.hababk.restaurant.activity.ReviewActivity;
import com.hababk.restaurant.activity.SplashActivity;
import com.hababk.restaurant.activity.SupportActivity;
import com.hababk.restaurant.utils.Helper;
import com.hababk.restaurant.utils.SharedPreferenceUtil;

/**
 * Created by Tayeb-Ali on 1/28/2019.
 */

public class AccountFragment extends Fragment implements View.OnClickListener {

    public AccountFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        view.findViewById(R.id.profile).setOnClickListener(this);
        view.findViewById(R.id.review).setOnClickListener(this);
        view.findViewById(R.id.documents).setOnClickListener(this);
        view.findViewById(R.id.bankDetails).setOnClickListener(this);
        view.findViewById(R.id.earnings).setOnClickListener(this);
        view.findViewById(R.id.support).setOnClickListener(this);
        //view.findViewById(R.id.setting).setOnClickListener(this);
        view.findViewById(R.id.logout).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.profile:
                startActivity(new Intent(getContext(), ProfileActivity.class));
                break;
            case R.id.review:
                startActivity(new Intent(getContext(), ReviewActivity.class));
                break;
            case R.id.documents:
                //startActivity(new Intent(getContext(), ProfileActivity.class));
                break;
            case R.id.bankDetails:
                startActivity(new Intent(getContext(), BankDetailActivity.class));
                break;
            case R.id.earnings:
                startActivity(new Intent(getContext(), EarningsActivity.class));
                break;
            case R.id.support:
                startActivity(new Intent(getContext(), SupportActivity.class));
                break;
//            case R.id.setting:
//                startActivity(new Intent(getContext(), ProfileActivity.class));
//                break;
            case R.id.logout:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle("Logout");
                alertDialog.setMessage(R.string.you_want_logout);
                alertDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Helper.logout(new SharedPreferenceUtil(getContext()));
                        dialog.dismiss();
                        Intent logiSignUpIntent = new Intent(getContext(), SplashActivity.class);
                        logiSignUpIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(logiSignUpIntent);
                        getActivity().finish();
                    }
                });
                alertDialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.show();
                break;
        }
    }
}
