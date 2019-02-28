package com.hababk.appstore.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hababk.appstore.R;
import com.hababk.appstore.activity.HomeActivity;
import com.hababk.appstore.network.ApiUtils;
import com.hababk.appstore.network.ChefStoreService;
import com.hababk.appstore.network.request.LoginRequest;
import com.hababk.appstore.network.response.AuthResponse;
import com.hababk.appstore.utils.Constants;
import com.hababk.appstore.utils.Helper;
import com.hababk.appstore.utils.SharedPreferenceUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by user on 1/25/2018.
 */

public class SignInFragment extends BaseFragment implements View.OnClickListener {
    TextView mForgotPassTv, errorText;
    Button mSignInBtn;
    TextView mSignUpTv;
    ImageView mPasswordVisibleIv;
    EditText mPasswordEt, mEmailEt;
    private ProgressBar progressBar;
    private ViewPager mViewPager;
    private Context mContext;
    private Activity mActivity;
    private boolean isPasswordVisible = false;

    private ChefStoreService chefStoreService;
    private SharedPreferenceUtil sharedPreferenceUtil;

    public SignInFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chefStoreService = ApiUtils.getClient().create(ChefStoreService.class);
        sharedPreferenceUtil = new SharedPreferenceUtil(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_layout, container, false);
        errorText = view.findViewById(R.id.errorText);
        mForgotPassTv = view.findViewById(R.id.signin_forgot_pass_tv);
        mSignInBtn = view.findViewById(R.id.signin_btn);
        mSignUpTv = view.findViewById(R.id.signin_signup_tv);
        mPasswordVisibleIv = view.findViewById(R.id.signin_password_visible_iv);
        mPasswordEt = view.findViewById(R.id.etPassword);
        mEmailEt = view.findViewById(R.id.etEmail);
        progressBar = view.findViewById(R.id.progressBar);

        view.findViewById(R.id.signin_forgot_pass_tv).setOnClickListener(this);
        view.findViewById(R.id.signin_password_visible_iv).setOnClickListener(this);
        view.findViewById(R.id.signin_signup_tv).setOnClickListener(this);
        view.findViewById(R.id.signin_btn).setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        mContext = getContext();
        mActivity = getActivity();
        mViewPager = (ViewPager) getActivity().findViewById(R.id.auth_viewpager);
    }

    private void init() {
        initVariables();
    }

    private void initVariables() {
        mContext = getContext();
        mViewPager = (ViewPager) getActivity().findViewById(R.id.auth_viewpager);
        mPasswordVisibleIv.setColorFilter(mContext.getResources().getColor(R.color.Gray_shade2));
    }

    public void onClickForgotPass() {
        Fragment forgotPasswordFragment = new ForgotPasswordFragment();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.splashFrame, forgotPasswordFragment, ForgotPasswordFragment.class.getName());
        fragmentTransaction.addToBackStack(ForgotPasswordFragment.class.getName());
        fragmentTransaction.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void onClickSignIn() {
        if (!Patterns.EMAIL_ADDRESS.matcher(mEmailEt.getText()).matches()) {
            toast("Enter valid email address", true);
            return;
        }
        if (TextUtils.isEmpty(mPasswordEt.getText())) {
            toast("Enter password", true);
            return;
        }

        setProgressLogin(true);
        chefStoreService.login(new LoginRequest(mEmailEt.getText().toString(), mPasswordEt.getText().toString())).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                setProgressLogin(false);
                if (response.isSuccessful()) {
                    sharedPreferenceUtil.setStringPreference(Constants.KEY_TOKEN, response.body().getToken());
                    Helper.setLoggedInUser(sharedPreferenceUtil, response.body().getUser());
                    if (response.body().getUser().getMobile_verified() == 1) {
//                        ChefProfile chefProfile = Helper.getChefDetails(sharedPreferenceUtil);
//                        Intent intent = new Intent(mActivity, (chefProfile == null || TextUtils.isEmpty(chefProfile.getName())) ? ProfileActivity.class : HomeActivity.class);
                        Intent intent = new Intent(mActivity, HomeActivity.class);
                        mActivity.startActivity(intent);
                        mActivity.finish();
                    } else {
                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.splashFrame, VerificationCodeFragment.newInstance(response.body().getUser().getMobile_number()), VerificationCodeFragment.class.getName());
                        fragmentTransaction.commit();
                    }
                } else {
                    if (errorText != null) {
                        errorText.setVisibility(View.VISIBLE);
                        errorText.setText(response.code() + ": Unable to login with provided credentials");
//                        StringBuilder stringBuilder = new StringBuilder("");
//                        stringBuilder.append(response.code());
//                        stringBuilder.append(" : ");
//                        stringBuilder.append(call.request().url().toString());
//                        stringBuilder.append("\n");
//                        stringBuilder.append("body");
//                        stringBuilder.append(" : ");
//                        stringBuilder.append(call.request().body().toString());
//                        stringBuilder.append("\n");
//                        stringBuilder.append("header");
//                        stringBuilder.append(" : ");
//                        stringBuilder.append(call.request().headers().toString());
//                        errorText.setText(stringBuilder.toString());
                    }
                    //toast("Unable to authorise with provided credentials", true);
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                setProgressLogin(false);
                if (errorText != null) {
                    errorText.setVisibility(View.VISIBLE);
                    errorText.setText("Something went wrong");
                }
                //toast("Something went wrong", true);
            }
        });
    }

    private void setProgressLogin(boolean b) {
        if (progressBar != null) {
            progressBar.setVisibility(b ? View.VISIBLE : View.INVISIBLE);
            mForgotPassTv.setClickable(!b);
            mSignInBtn.setClickable(!b);
            mSignUpTv.setClickable(!b);
            if (b) errorText.setVisibility(View.GONE);
        }
    }

    public void onClickSignUp() {
        mViewPager.setCurrentItem(1);
    }

    public void onClickVisibilityIv() {
        if (isPasswordVisible) {
            mPasswordVisibleIv.setImageResource(R.drawable.ic_hide);
            mPasswordEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
            mPasswordEt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            isPasswordVisible = false;
        } else {
            mPasswordVisibleIv.setImageResource(R.drawable.ic_show);
            mPasswordEt.setTransformationMethod(null);
            mPasswordEt.setInputType(InputType.TYPE_CLASS_TEXT);
            isPasswordVisible = true;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signin_forgot_pass_tv:
                onClickForgotPass();
                break;
            case R.id.signin_password_visible_iv:
                onClickVisibilityIv();
                break;
            case R.id.signin_signup_tv:
                onClickSignUp();
                break;
            case R.id.signin_btn:
                onClickSignIn();
                break;
        }
    }
}