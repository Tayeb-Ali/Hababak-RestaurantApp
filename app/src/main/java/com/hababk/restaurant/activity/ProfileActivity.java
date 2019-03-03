package com.hababk.restaurant.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.kbeanie.multipicker.api.CameraImagePicker;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.hababk.restaurant.R;
import com.hababk.restaurant.network.ApiUtils;
import com.hababk.restaurant.network.ChefStoreService;
import com.hababk.restaurant.network.request.ChefProfileUpdateRequest;
import com.hababk.restaurant.network.response.ChefProfile;
import com.hababk.restaurant.utils.FirebaseUploader;
import com.hababk.restaurant.utils.Helper;
import com.hababk.restaurant.utils.SharedPreferenceUtil;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity implements ImagePickerCallback {
    private ProgressBar restImageProgress, progressBar;
    private SwipeRefreshLayout restDetailsLoadingProgress;
    private Button itemSave;
    private EditText restAddress, restDetails;
    private TextInputEditText restDeliveryTime, restDeliveryFee, restMinOrder, restName, restTagLine, restArea, restHourOpen, restHourClose, priceForTwo;
    private View restImagePicker;
    private ImageView itemImage;
    private SwitchCompat preOrderSwitch, vegOnlySwitch;

    private String pickerPath;
    private ImagePicker imagePicker;
    private CameraImagePicker cameraPicker;
    private File mediaFile;
    private String itemImageUrl;
    private boolean imageUploadInProgress;

    private ChefStoreService storeService;
    private SharedPreferenceUtil sharedPreferenceUtil;

    private final int REQUEST_CODE_PERMISSION = 55, PLACE_PICKER_REQUEST = 66, MY_PERMISSIONS_REQUEST_LOCATION = 44;
    private Double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_left_white);
            actionBar.setTitle("Profile");
        }

        sharedPreferenceUtil = new SharedPreferenceUtil(this);
        storeService = ApiUtils.getClient().create(ChefStoreService.class);

        restImageProgress = findViewById(R.id.restImageProgress);
        progressBar = findViewById(R.id.progressBar);
        restDetailsLoadingProgress = findViewById(R.id.restDetailsLoadingProgress);

        itemSave = findViewById(R.id.itemSave);
        restName = findViewById(R.id.restName);
        restHourOpen = findViewById(R.id.restHourOpen);
        restHourClose = findViewById(R.id.restHourClose);
        restTagLine = findViewById(R.id.restTagLine);
        restDeliveryTime = findViewById(R.id.restDeliveryTime);
        restDeliveryFee = findViewById(R.id.restDeliveryFee);
        restMinOrder = findViewById(R.id.restMinOrder);
        priceForTwo = findViewById(R.id.priceForTwo);
        restArea = findViewById(R.id.restArea);
        restAddress = findViewById(R.id.restAddress);
        restDetails = findViewById(R.id.restDetails);
        restImagePicker = findViewById(R.id.restImagePicker);
        itemImage = findViewById(R.id.restImage);
        vegOnlySwitch = findViewById(R.id.vegOnlySwitch);
        preOrderSwitch = findViewById(R.id.preOrderSwitch);

        findViewById(R.id.getLocation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivityForResult(new PlacePicker.IntentBuilder().build(ProfileActivity.this), PLACE_PICKER_REQUEST);
                    latitude = 15.501501;
                    longitude = 32.7126829;
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
        restImagePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picImage();
            }
        });
        itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picImage();
            }
        });
        restHourOpen.setKeyListener(null);
        restHourClose.setKeyListener(null);
        restHourOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickTime(1);
            }
        });
        restHourClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickTime(2);
            }
        });
        restDetailsLoadingProgress.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initDetails();
            }
        });
        itemSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(restName.getText())) {
                    Toast.makeText(ProfileActivity.this, "Provide restaurant name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(restTagLine.getText())) {
                    Toast.makeText(ProfileActivity.this, "Provide a tag line", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(restDeliveryTime.getText()) || !Helper.isNumber(restDeliveryTime.getText().toString())) {
                    Toast.makeText(ProfileActivity.this, "Provide average delivery time", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(restDeliveryFee.getText()) || !Helper.isDouble(restDeliveryFee.getText().toString())) {
                    Toast.makeText(ProfileActivity.this, "Provide delivery fee", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(restMinOrder.getText()) || !Helper.isDouble(restMinOrder.getText().toString())) {
                    Toast.makeText(ProfileActivity.this, "Provide min order", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(priceForTwo.getText()) || !Helper.isDouble(priceForTwo.getText().toString())) {
                    Toast.makeText(ProfileActivity.this, "Provide average price for two", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(restArea.getText())) {
                    Toast.makeText(ProfileActivity.this, "Provide restaurant area", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(restAddress.getText())) {
                    Toast.makeText(ProfileActivity.this, "Provide restaurant address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(restDetails.getText())) {
                    Toast.makeText(ProfileActivity.this, "Provide some restaurant details", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(restHourOpen.getText())) {
                    Toast.makeText(ProfileActivity.this, "Provide opening hours", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(restHourClose.getText())) {
                    Toast.makeText(ProfileActivity.this, "Provide closing hours", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (latitude == null || latitude == 0.0d || longitude == null || longitude == 0.0d) {
                    Toast.makeText(ProfileActivity.this, "Fetch location coordinates", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (imageUploadInProgress) {
                    Toast.makeText(ProfileActivity.this, "Image upload is in progress", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(itemImageUrl)) {
                    Toast.makeText(ProfileActivity.this, "Choose and upload an image", Toast.LENGTH_SHORT).show();
                    return;
                }
                Helper.closeKeyboard(ProfileActivity.this, itemSave);
                updateProfile();
            }
        });

        initDetails();
    }

    private void pickTime(final int i) {
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String time = (hourOfDay < 10 ? "0" + hourOfDay : hourOfDay) + ":" + (minute < 10 ? "0" + minute : minute) + ":00";
                        if (i == 1)
                            restHourOpen.setText(time);
                        else if (i == 2)
                            restHourClose.setText(time);
                    }
                }, mHour, mMinute, true);
        timePickerDialog.show();
    }

    private void picImage() {
        if (checkStoragePermissions()) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setMessage("Get image from");
            alertDialog.setPositiveButton(getString(R.string.camera), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();

                    cameraPicker = new CameraImagePicker(ProfileActivity.this);
                    cameraPicker.shouldGenerateMetadata(true);
                    cameraPicker.shouldGenerateThumbnails(true);
                    cameraPicker.setImagePickerCallback(ProfileActivity.this);
                    pickerPath = cameraPicker.pickImage();
                }
            });
            alertDialog.setNegativeButton(getString(R.string.gallery), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();

                    imagePicker = new ImagePicker(ProfileActivity.this);
                    imagePicker.shouldGenerateMetadata(true);
                    imagePicker.shouldGenerateThumbnails(true);
                    imagePicker.setImagePickerCallback(ProfileActivity.this);
                    imagePicker.pickImage();
                }
            });
            alertDialog.create().show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, REQUEST_CODE_PERMISSION);
        }
    }

    private void updateProfile() {
        ChefProfileUpdateRequest chefProfileUpdateRequest = new ChefProfileUpdateRequest();
        chefProfileUpdateRequest.setAddress(restAddress.getText().toString());
        chefProfileUpdateRequest.setArea(restArea.getText().toString());
        chefProfileUpdateRequest.setDelivery_fee(Double.valueOf(restDeliveryFee.getText().toString()));
        chefProfileUpdateRequest.setDelivery_time(restDeliveryTime.getText().toString());
        chefProfileUpdateRequest.setDetails(restDetails.getText().toString());
        chefProfileUpdateRequest.setImage_url(itemImageUrl);
        chefProfileUpdateRequest.setLatitude(latitude);
        chefProfileUpdateRequest.setLongitude(longitude);
        chefProfileUpdateRequest.setMinimum_order(Double.valueOf(restMinOrder.getText().toString()));
        chefProfileUpdateRequest.setCost_for_two(Double.valueOf(priceForTwo.getText().toString()));
        chefProfileUpdateRequest.setName(restName.getText().toString());
        chefProfileUpdateRequest.setOpens_at(restHourOpen.getText().toString());
        chefProfileUpdateRequest.setCloses_at(restHourClose.getText().toString());
        chefProfileUpdateRequest.setTagline(restTagLine.getText().toString());
        chefProfileUpdateRequest.setPreorder(preOrderSwitch.isChecked());
        chefProfileUpdateRequest.setServes_non_veg(!vegOnlySwitch.isChecked());

        setUpdateDetailsProgress(true);
        storeService.updateProfile(Helper.getApiToken(sharedPreferenceUtil), chefProfileUpdateRequest).enqueue(new Callback<ChefProfile>() {
            @Override
            public void onResponse(Call<ChefProfile> call, Response<ChefProfile> response) {
                setUpdateDetailsProgress(false);
                if (response.isSuccessful()) {
                    Toast.makeText(ProfileActivity.this, "Restaurant's profile set!", Toast.LENGTH_LONG).show();
                    Helper.setChefDetails(sharedPreferenceUtil, response.body());
                    finish();
                } else {
                    Toast.makeText(ProfileActivity.this, "Something went wrong updating details #101", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ChefProfile> call, Throwable t) {
                setUpdateDetailsProgress(false);
                Toast.makeText(ProfileActivity.this, "Something went wrong updating details #102", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initDetails() {
        setFetchDetailsProgress(true);
        ChefProfile savedDetails = Helper.getChefDetails(sharedPreferenceUtil);
        if (savedDetails != null) {
            setDetails(savedDetails);
            setFetchDetailsProgress(false);
        } else {
            storeService.getProfile(Helper.getApiToken(sharedPreferenceUtil)).enqueue(new Callback<ChefProfile>() {
                @Override
                public void onResponse(Call<ChefProfile> call, Response<ChefProfile> response) {
                    setFetchDetailsProgress(false);
                    if (response.isSuccessful()) {
                        Helper.setChefDetails(sharedPreferenceUtil, response.body());
                        if (TextUtils.isEmpty(response.body().getName())) {
                            Toast.makeText(ProfileActivity.this, "Restaurant's profile needs to be set", Toast.LENGTH_LONG).show();
                        }
                        setDetails(response.body());
                    }
                }

                @Override
                public void onFailure(Call<ChefProfile> call, Throwable t) {
                    setFetchDetailsProgress(false);
                    Toast.makeText(ProfileActivity.this, "Something went wrong fetching details", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void setDetails(ChefProfile savedDetails) {
        this.itemImageUrl = savedDetails.getImage_url();
        if (!TextUtils.isEmpty(this.itemImageUrl)) {
            restImagePicker.setVisibility(View.GONE);
            Glide.with(this)
                    .load(savedDetails.getImage_url())
                    .apply(new RequestOptions().centerCrop().placeholder(R.drawable.placeholder_store_profile).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).dontAnimate())
                    .into(itemImage);
        }
        restName.setText(savedDetails.getName());
        restTagLine.setText(savedDetails.getTagline());
        restDeliveryTime.setText(savedDetails.getDelivery_time());
        if (savedDetails.getDelivery_fee() != null)
            restDeliveryFee.setText(new DecimalFormat("###.##").format(savedDetails.getDelivery_fee()));
        if (savedDetails.getMinimum_order() != null)
            restMinOrder.setText(new DecimalFormat("###.##").format(savedDetails.getMinimum_order()));
        if (savedDetails.getCost_for_two() != null)
            priceForTwo.setText(new DecimalFormat("###.##").format(savedDetails.getCost_for_two()));
        restArea.setText(savedDetails.getArea());
        restAddress.setText(savedDetails.getAddress());
        restDetails.setText(savedDetails.getDetails());
        restHourOpen.setText(savedDetails.getOpens_at());
        restHourClose.setText(savedDetails.getCloses_at());
        vegOnlySwitch.setChecked(savedDetails.getServes_non_veg() != 1);
        preOrderSwitch.setChecked(savedDetails.getPreorder() == 1);
        latitude = savedDetails.getLatitude();
        longitude = savedDetails.getLongitude();
    }

    private void setImageUpdateProgress(boolean b) {
        restImageProgress.setVisibility(b ? View.VISIBLE : View.INVISIBLE);
        imageUploadInProgress = b;
        restImagePicker.setClickable(!b);
        itemImage.setClickable(!b);
        itemSave.setClickable(!b);
    }

    private void setFetchDetailsProgress(boolean b) {
        restDetailsLoadingProgress.setRefreshing(b);
        itemSave.setClickable(!b);
    }

    private void setUpdateDetailsProgress(boolean b) {
        progressBar.setVisibility(b ? View.VISIBLE : View.INVISIBLE);
        itemSave.setClickable(!b);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        try {
                            startActivityForResult(new PlacePicker.IntentBuilder().build(this), PLACE_PICKER_REQUEST);
                        } catch (GooglePlayServicesRepairableException e) {
                            e.printStackTrace();
                        } catch (GooglePlayServicesNotAvailableException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == AppCompatActivity.RESULT_OK) {
            switch (requestCode) {
                case Picker.PICK_IMAGE_DEVICE:
                    if (imagePicker == null) {
                        imagePicker = new ImagePicker(this);
                        imagePicker.setImagePickerCallback(this);
                    }
                    imagePicker.submit(data);
                    break;
                case Picker.PICK_IMAGE_CAMERA:
                    if (cameraPicker == null) {
                        cameraPicker = new CameraImagePicker(this);
                        cameraPicker.setImagePickerCallback(this);
                        cameraPicker.reinitialize(pickerPath);
                    }
                    cameraPicker.submit(data);
                    break;
                case PLACE_PICKER_REQUEST:
                    Place place = PlacePicker.getPlace(this, data);
//                    latitude = place.getLatLng().latitude;
//                    longitude = place.getLatLng().longitude;

                    latitude = 15.501501;
                    longitude = 32.7126829;
                    Toast.makeText(ProfileActivity.this, "Location coordinates captured", Toast.LENGTH_SHORT).show();
                    restAddress.setText(place.getAddress());
                    break;
            }
        }
    }

    @Override
    public void onImagesChosen(List<ChosenImage> images) {
        setImageUpdateProgress(true);
        restImagePicker.setVisibility(View.GONE);
        mediaFile = new File(Uri.parse(images.get(0).getOriginalPath()).getPath());
        Glide.with(this)
                .load(mediaFile)
                .apply(new RequestOptions().centerCrop().placeholder(R.drawable.placeholder_store_profile).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).dontAnimate())
                .into(itemImage);

        FirebaseUploader firebaseUploader = new FirebaseUploader(new FirebaseUploader.UploadListener() {
            @Override
            public void onUploadFail(String message) {
                setImageUpdateProgress(false);
                Toast.makeText(ProfileActivity.this, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUploadSuccess(String downloadUrl) {
                setImageUpdateProgress(false);
                ProfileActivity.this.itemImageUrl = downloadUrl;
            }

            @Override
            public void onUploadProgress(int progress) {

            }

            @Override
            public void onUploadCancelled() {
                setImageUpdateProgress(false);
            }
        }, "rest_profile_" + System.currentTimeMillis());
        firebaseUploader.setReplace(true);
        firebaseUploader.uploadImage(this, mediaFile);
    }

    @Override
    public void onError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // You have to save path in case your activity is killed.
        // In such a scenario, you will need to re-initialize the CameraImagePicker
        outState.putString("picker_path", pickerPath);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // After Activity recreate, you need to re-intialize these
        // two values to be able to re-intialize CameraImagePicker
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("picker_path")) {
                pickerPath = savedInstanceState.getString("picker_path");
            }
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean checkStoragePermissions() {
        return
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        &&
                        ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        &&
                        ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

}