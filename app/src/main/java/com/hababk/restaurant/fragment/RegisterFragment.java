package com.hababk.appstore.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hababk.appstore.R;
import com.hababk.appstore.model.Country;
import com.hababk.appstore.network.ApiUtils;
import com.hababk.appstore.network.ChefStoreService;
import com.hababk.appstore.network.request.RegisterRequest;
import com.hababk.appstore.network.response.AuthResponse;
import com.hababk.appstore.utils.Constants;
import com.hababk.appstore.utils.Helper;
import com.hababk.appstore.utils.SharedPreferenceUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by user on 1/25/2018.
 */

public class RegisterFragment extends BaseFragment {
    private EditText name, email, phone, password1, password2;
    private AppCompatSpinner countryCode;

    private Button mRegisterBtn;
    private TextView mSignInTv, errorText;
    private ViewPager mViewPager;
    private ProgressBar progressBar;

    public RegisterFragment() {
    }

    private ChefStoreService chefStoreService;
    private SharedPreferenceUtil sharedPreferenceUtil;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chefStoreService = ApiUtils.getClient().create(ChefStoreService.class);
        sharedPreferenceUtil = new SharedPreferenceUtil(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup_layout, container, false);
        mRegisterBtn = view.findViewById(R.id.register_btn);
        mSignInTv = view.findViewById(R.id.register_signin_tv);
        name = view.findViewById(R.id.etName);
        email = view.findViewById(R.id.etEmail);
        countryCode = view.findViewById(R.id.countryCode);
        phone = view.findViewById(R.id.etPhone);
        password1 = view.findViewById(R.id.etPassword1);
        password2 = view.findViewById(R.id.etPassword2);
        progressBar = view.findViewById(R.id.progressBar);
        errorText = view.findViewById(R.id.errorText);
        setupCountryCodes();
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(name.getText())) {
                    toast("Please provide your name", true);
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches()) {
                    toast("Enter valid email address", true);
                    return;
                }
                if (phone.getText().toString().length() != 10 && !Patterns.PHONE.matcher(phone.getText()).matches()) {
                    toast("Enter valid phone number", true);
                    return;
                }
                if (password1.getText().toString().length() < 6) {
                    toast("Provide at least 6 character password", true);
                    return;
                }
                if (TextUtils.isEmpty(password1.getText()) || TextUtils.isEmpty(password2.getText()) || !password1.getText().toString().equals(password2.getText().toString())) {
                    toast("Please enter valid password, Twice.", true);
                    return;
                }
                onClickRegister(new RegisterRequest(name.getText().toString(), email.getText().toString(), password1.getText().toString(), (((Country) countryCode.getSelectedItem()).getDialCode() + phone.getText().toString()).replaceAll("\\s+", "")));
            }
        });
        mSignInTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.post(new Runnable() {
                    @Override
                    public void run() {
                        mViewPager.setCurrentItem(0);
                    }
                });
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewPager = (ViewPager) getActivity().findViewById(R.id.auth_viewpager);
    }

    private void setupCountryCodes() {
        ArrayList<Country> countries = getCountries();
        if (countries != null) {
            ArrayAdapter<Country> myAdapter = new ArrayAdapter<Country>(getContext(), R.layout.item_country_spinner, countries);
            countryCode.setAdapter(myAdapter);
        }
    }

    private ArrayList<Country> getCountries() {
        ArrayList<Country> toReturn = new ArrayList<>();
//        toReturn.add(new Country("RU", "Russia", "+7"));
//        toReturn.add(new Country("TJ", "Tajikistan", "+992"));
//        toReturn.add(new Country("US", "United States", "+1"));
//        return toReturn;

        try {
            JSONArray countrArray = new JSONArray(readEncodedJsonString(getContext()));
            toReturn = new ArrayList<>();
            for (int i = 0; i < countrArray.length(); i++) {
                JSONObject jsonObject = countrArray.getJSONObject(i);
                String countryName = jsonObject.getString("name");
                String countryDialCode = jsonObject.getString("dial_code");
                String countryCode = jsonObject.getString("code");
                Country country = new Country(countryCode, countryName, countryDialCode);
                toReturn.add(country);
            }
            Collections.sort(toReturn, new Comparator<Country>() {
                @Override
                public int compare(Country lhs, Country rhs) {
                    return lhs.getName().compareTo(rhs.getName());
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    private String readEncodedJsonString(Context context) throws java.io.IOException {
        String base64 = context.getResources().getString(R.string.countries_code);
        byte[] data = Base64.decode(base64, Base64.DEFAULT);
        return new String(data, "UTF-8");
    }

    public void onClickRegister(final RegisterRequest registerRequest) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle(registerRequest.getMobile_number());
        alertDialog.setMessage("Are your sure to use this number? As this will be used for phone number verification.\nKindly verify!");
        alertDialog.setPositiveButton("Yes, m sure", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                register(registerRequest);
            }
        });
        alertDialog.setNegativeButton("No, need edits", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                phone.requestFocus();
            }
        });
        alertDialog.show();
    }

    private void register(RegisterRequest registerRequest) {
        setProgressRegister(true);
        chefStoreService.register(registerRequest).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                setProgressRegister(false);
                if (response.isSuccessful()) {
                    sharedPreferenceUtil.setStringPreference(Constants.KEY_TOKEN, response.body().getToken());
                    Helper.setLoggedInUser(sharedPreferenceUtil, response.body().getUser());
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.splashFrame, VerificationCodeFragment.newInstance(response.body().getUser().getMobile_number()), VerificationCodeFragment.class.getName());
                    fragmentTransaction.commit();
                } else {
                    if (errorText != null) {
                        errorText.setVisibility(View.VISIBLE);
                        try {
                            JSONObject errorObject = new JSONObject(response.errorBody().string());
                            if (errorObject.has("errors")) {
                                JSONObject errors = errorObject.getJSONObject("errors");
                                notifyError(errors, "email");
                                notifyError(errors, "password");
                                notifyError(errors, "mobile_number");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            errorText.setText("Unable to register with provided credentials");
                        } catch (IOException e) {
                            errorText.setText("Unable to register with provided credentials");
                            e.printStackTrace();
                        }
                    }
                    //toast("Unable to register with provided credentials", true);
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                setProgressRegister(false);
                if (errorText != null) {
                    errorText.setVisibility(View.VISIBLE);
                    errorText.setText("Something went wrong");
                }
                //toast("Something went wrong", true);
            }
        });
    }

    private void notifyError(JSONObject errors, String field) throws JSONException {
        if (errors != null && errors.has(field)) {
            JSONArray fieldArr = errors.getJSONArray(field);
            if (fieldArr.length() > 0) {
                errorText.setText(fieldArr.get(0).toString());
                //toast(fieldArr.get(0).toString(), true);
            }
        }
    }

    private void setProgressRegister(boolean b) {
        if (progressBar != null) {
            progressBar.setVisibility(b ? View.VISIBLE : View.INVISIBLE);
            mRegisterBtn.setClickable(!b);
            mSignInTv.setClickable(!b);
            if (b) errorText.setVisibility(View.GONE);
        }
    }

}
