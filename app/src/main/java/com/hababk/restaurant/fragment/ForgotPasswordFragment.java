package com.hababk.appstore.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hababk.appstore.R;

/**
 * Created by user on 1/28/2018.
 */

public class ForgotPasswordFragment extends Fragment {
    EditText mEmailTv;
    Button mSendMailBtn;
    TextView mLoginTv;
    ImageView mBackIv;
    private Context mContext;

    public ForgotPasswordFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgot_pass_layout, container, false);
        mEmailTv = view.findViewById(R.id.forgot_pass_email_tv);
        mSendMailBtn = view.findViewById(R.id.forgot_pass_send_mail_btn);
        mLoginTv = view.findViewById(R.id.forgot_pass_login_tv);
        mBackIv = view.findViewById(R.id.forgot_pass_back_iv);

        mLoginTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        mBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        mContext = getContext();
    }

    private void init() {

    }

}
