package com.hababk.restaurant.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.JsonObject;
import com.hababk.restaurant.R;
import com.hababk.restaurant.activity.HomeActivity;
import com.hababk.restaurant.model.User;
import com.hababk.restaurant.network.ApiUtils;
import com.hababk.restaurant.network.ChefStoreService;
import com.hababk.restaurant.network.request.MobileVerifiedRequest;
import com.hababk.restaurant.utils.Helper;
import com.hababk.restaurant.utils.SharedPreferenceUtil;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Tayeb-Ali on 1/28/2018.
 */

public class VerificationCodeFragment extends BaseFragment {
    ImageView mBackIv;
    Button mVerifyBtn;
    TextView msg, timeLeft, resend;
    EditText etOtp;
    private Context mContext;
    private Activity mActivity;
    private String mobile_number;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private CountDownTimer countDownTimer;
    private String mVerificationId;
    private boolean authInProgress = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_verification_code_layout, container, false);
        mBackIv = view.findViewById(R.id.verification_code_back_iv);
        mVerifyBtn = view.findViewById(R.id.verification_code_btn);
        msg = view.findViewById(R.id.verification_code_text_2_tv);
        timeLeft = view.findViewById(R.id.timeLeft);
        resend = view.findViewById(R.id.resend);
        etOtp = view.findViewById(R.id.etOtp);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        msg.setText("being sent to: " + mobile_number);
        mBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        mVerifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(etOtp.getText()) && Helper.isNumber(etOtp.getText().toString()) && !TextUtils.isEmpty(mVerificationId)) {
                    signInWithPhoneAuthCredential(PhoneAuthProvider.getCredential(mVerificationId, etOtp.getText().toString()));
                }
            }
        });

        mContext = getContext();
        mActivity = getActivity();

        initiateAuth();
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiateAuth();
            }
        });
    }

    private void initiateAuth() {
        authInProgress = true;
        PhoneAuthProvider.getInstance().verifyPhoneNumber(mobile_number, 60, TimeUnit.SECONDS, getActivity(),
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onCodeAutoRetrievalTimeOut(String s) {
                        super.onCodeAutoRetrievalTimeOut(s);
                    }

                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        authInProgress = false;
                        if (countDownTimer != null) countDownTimer.cancel();
                        toast("Something went wrong", true);
                        if (resend != null) {
                            resend.setClickable(true);
                            resend.setAlpha(1.0f);
                        }
                    }

                    @Override
                    public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(verificationId, forceResendingToken);
                        mVerificationId = verificationId;
                        mResendToken = forceResendingToken;
                        if (resend != null) {
                            resend.setClickable(false);
                            resend.setAlpha(0.5f);
                        }
                    }
                });
        startCountdown();
    }

    private void startCountdown() {
        resend.setClickable(false);
        resend.setAlpha(0.5f);
        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long l) {
                if (timeLeft != null) {
                    timeLeft.setText(String.valueOf(l / 1000));
                }
            }

            @Override
            public void onFinish() {
                if (resend != null) {
                    resend.setClickable(true);
                    resend.setAlpha(1.0f);
                }
            }
        }.start();
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithCredential(phoneAuthCredential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                ChefStoreService service = ApiUtils.getClient().create(ChefStoreService.class);
                service.verifyMobile(new MobileVerifiedRequest(mobile_number)).enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        authInProgress = false;
                        if (response.isSuccessful()) {
                            SharedPreferenceUtil sharedPreferenceUtil = new SharedPreferenceUtil(getContext());
                            User user = Helper.getLoggedInUser(sharedPreferenceUtil);
                            user.setMobile_verified(1);
                            Helper.setLoggedInUser(sharedPreferenceUtil, user);
                            Intent intent = new Intent(mActivity, HomeActivity.class);
                            mActivity.startActivity(intent);
                            mActivity.finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        toast("Something went wrong", true);
                        authInProgress = false;
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                toast("Something went wrong", true);
                authInProgress = false;
            }
        });
    }

    public void onClickVerificationBtn() {
        Intent intent = new Intent(mActivity, HomeActivity.class);
        mActivity.startActivity(intent);
        mActivity.finish();
    }


    public static VerificationCodeFragment newInstance(String mobile_number) {
        VerificationCodeFragment fragment = new VerificationCodeFragment();
        fragment.mobile_number = mobile_number;
        return fragment;
    }

    public boolean isAuthInProgress() {
        return authInProgress;
    }
}
