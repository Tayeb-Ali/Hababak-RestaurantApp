package com.hababk.appstore.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hababk.appstore.R;
import com.hababk.appstore.network.ApiUtils;
import com.hababk.appstore.network.ChefStoreService;
import com.hababk.appstore.network.request.BankDetailRequest;
import com.hababk.appstore.network.response.BankDetailResponse;
import com.hababk.appstore.utils.Helper;
import com.hababk.appstore.utils.SharedPreferenceUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BankDetailActivity extends AppCompatActivity {
    private Button mUpdateBtn;
    private EditText bankHolderName, bankName, bankBranchCode, bankAccountNumber;
    private ProgressBar bankDetailsLoadingProgress, progressBar;

    private ChefStoreService storeService;
    private SharedPreferenceUtil sharedPreferenceUtil;
    //private ArrayAdapter<String> mBankNameAdapter;
    //private String[] bankNames;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_left_white);
            actionBar.setTitle("Bank detail");
        }

        sharedPreferenceUtil = new SharedPreferenceUtil(this);
        storeService = ApiUtils.getClient().create(ChefStoreService.class);

        bankHolderName = findViewById(R.id.bankHolderName);
        bankName = findViewById(R.id.bankName);
        bankBranchCode = findViewById(R.id.bankBranchCode);
        bankAccountNumber = findViewById(R.id.bankAccountNumber);
        mUpdateBtn = findViewById(R.id.bank_detail_update_btn);
        bankDetailsLoadingProgress = findViewById(R.id.bankDetailsLoadingProgress);
        progressBar = findViewById(R.id.progressBar);
        mUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(bankHolderName.getText())) {
                    Toast.makeText(BankDetailActivity.this, "Provide name with bank details", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(bankName.getText())) {
                    Toast.makeText(BankDetailActivity.this, "Provide your bank name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(bankBranchCode.getText())) {
                    Toast.makeText(BankDetailActivity.this, "Provide branch code", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(bankAccountNumber.getText())) {
                    Toast.makeText(BankDetailActivity.this, "Provide valid 16 digit account number", Toast.LENGTH_SHORT).show();
                    return;
                }
                updateBankDetails(new BankDetailRequest(bankHolderName.getText().toString(), bankName.getText().toString(), bankBranchCode.getText().toString(), Helper.getSpaceRemovedAccountNumber(bankAccountNumber.getText().toString())));
            }
        });
        bankAccountNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 16 && !s.toString().contains("-")) {
                    bankAccountNumber.setText(Helper.getSpacedAccountNumber(s.toString()));
                    bankAccountNumber.clearFocus();
                    mUpdateBtn.requestFocus();
                } else if (s.length() > 19) {
                    bankAccountNumber.setText("");
                    Toast.makeText(BankDetailActivity.this, "Enter valid account number", Toast.LENGTH_SHORT).show();
                }
            }
        });

        initDetails();

    }

    private void updateBankDetails(BankDetailRequest bankDetailRequest) {
        setUpdateDetailsProgress(true);
        storeService.setBankDetails(Helper.getApiToken(sharedPreferenceUtil), bankDetailRequest).enqueue(new Callback<BankDetailResponse>() {
            @Override
            public void onResponse(Call<BankDetailResponse> call, Response<BankDetailResponse> response) {
                setUpdateDetailsProgress(false);
                if (response.isSuccessful()) {
                    Helper.setBankDetails(sharedPreferenceUtil, response.body());
                    Toast.makeText(BankDetailActivity.this, "Bank details updated", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(BankDetailActivity.this, "Something went wrong updating details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BankDetailResponse> call, Throwable t) {
                setUpdateDetailsProgress(false);
                Toast.makeText(BankDetailActivity.this, "Something went wrong updating details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initDetails() {
        setFetchDetailsProgress(true);
        BankDetailResponse savedDetails = Helper.getBankDetails(sharedPreferenceUtil);
        if (savedDetails != null) {
            setDetails(savedDetails);
            setFetchDetailsProgress(false);
        } else {
            storeService.getBankDetails(Helper.getApiToken(sharedPreferenceUtil)).enqueue(new Callback<BankDetailResponse>() {
                @Override
                public void onResponse(Call<BankDetailResponse> call, Response<BankDetailResponse> response) {
                    setFetchDetailsProgress(false);
                    if (response.isSuccessful()) {
                        Helper.setBankDetails(sharedPreferenceUtil, response.body());
                        setDetails(response.body());
                    }
                }

                @Override
                public void onFailure(Call<BankDetailResponse> call, Throwable t) {
                    setFetchDetailsProgress(false);
                    Toast.makeText(BankDetailActivity.this, "Something went wrong fetching details", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void setDetails(BankDetailResponse savedDetails) {
        bankHolderName.setText(savedDetails.getName());
        bankName.setText(savedDetails.getBank_name());
        bankBranchCode.setText(savedDetails.getIfsc());
        bankAccountNumber.setText(Helper.getSpacedAccountNumber(savedDetails.getAccount_number()));
    }

    private void setFetchDetailsProgress(boolean b) {
        bankDetailsLoadingProgress.setVisibility(b ? View.VISIBLE : View.INVISIBLE);
        mUpdateBtn.setClickable(!b);
    }

    private void setUpdateDetailsProgress(boolean b) {
        progressBar.setVisibility(b ? View.VISIBLE : View.INVISIBLE);
        mUpdateBtn.setClickable(!b);
    }

//    private void initBankNamesSpinner() {
//        initVariables();
//        setSpinnerAdapter();
//    }

//    private void initVariables() {
//        bankNames = getResources().getStringArray(R.array.bank_names);
//    }

//    private void setSpinnerAdapter() {
//        mBankNameAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item) {
//            @Override
//            public boolean isEnabled(int position) {
//                return position != 0 && super.isEnabled(position);
//            }
//        };
//        mBankNameAdapter.clear();
//        mBankNameAdapter.addAll(bankNames);
//        mBankSpinner.setAdapter(mBankNameAdapter);
//    }
}
