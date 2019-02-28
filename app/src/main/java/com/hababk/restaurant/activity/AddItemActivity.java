package com.hababk.restaurant.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.kbeanie.multipicker.api.CameraImagePicker;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.hababk.restaurant.R;
import com.hababk.restaurant.fragment.CategorySelectionDialog;
import com.hababk.restaurant.network.ApiUtils;
import com.hababk.restaurant.network.ChefStoreService;
import com.hababk.restaurant.network.request.MenuItemCreateRequest;
import com.hababk.restaurant.network.response.MenuItemCategory;
import com.hababk.restaurant.utils.Constants;
import com.hababk.restaurant.utils.FirebaseUploader;
import com.hababk.restaurant.utils.Helper;
import com.hababk.restaurant.utils.SharedPreferenceUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddItemActivity extends AppCompatActivity implements ImagePickerCallback {
    private static String DATA_MENU_ITEM = "MenuItem";
    private View restImagePicker;
    private ImageView itemImage;
    private Button itemSave;
    private EditText itemName, itemDescription, itemPrice, itemSpecification;
    private SwitchCompat itemAvailable;
    private Spinner itemVegNon;
    private TextView itemCategory;
    private ProgressBar itemImageProgress, progressBar;
    private CategorySelectionDialog dialogSelectCategories;

    private String pickerPath;
    private ImagePicker imagePicker;
    private CameraImagePicker cameraPicker;
    private File mediaFile;
    private String itemImageUrl;
    private boolean imageUploadInProgress;

    private SharedPreferenceUtil sharedPreferenceUtil;

    private final int REQUEST_CODE_PERMISSION = 55;

    private com.hababk.restaurant.network.response.MenuItem menuItemToEdit;
    private ArrayList<MenuItemCategory> categories, selectedCategories = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        sharedPreferenceUtil = new SharedPreferenceUtil(this);
        initUi();
        menuItemToEdit = getIntent().getParcelableExtra(DATA_MENU_ITEM);
        if (menuItemToEdit != null) {
            itemImageUrl = menuItemToEdit.getImage_url();
            restImagePicker.setVisibility(View.GONE);
            Glide.with(this)
                    .load(menuItemToEdit.getImage_url())
                    .apply(new RequestOptions().centerCrop().placeholder(R.drawable.placeholder_food).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).dontAnimate())
                    .into(itemImage);
            itemName.setText(menuItemToEdit.getTitle());
            itemAvailable.setChecked(menuItemToEdit.getIs_available() == 1);
            itemPrice.setText(String.valueOf(menuItemToEdit.getPrice()));
            itemDescription.setText(menuItemToEdit.getDetail());
            itemSpecification.setText(menuItemToEdit.getSpecification());
            itemVegNon.setSelection(menuItemToEdit.getIs_non_veg());

            selectedCategories.addAll(menuItemToEdit.getCategories());
            setCategorySelectionText();

            itemSave.setText("Update Item");
            if (getSupportActionBar() != null)
                getSupportActionBar().setTitle("Update Item");
        }

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
        itemSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(itemName.getText())) {
                    Toast.makeText(AddItemActivity.this, "Give item name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (selectedCategories.isEmpty()) {
                    Toast.makeText(AddItemActivity.this, "Select item category", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(itemDescription.getText())) {
                    Toast.makeText(AddItemActivity.this, "Give item description", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(itemPrice.getText())) {
                    Toast.makeText(AddItemActivity.this, "Give item price", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(itemSpecification.getText())) {
                    Toast.makeText(AddItemActivity.this, "Give item specification", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (itemImageUrl == null || imageUploadInProgress) {
                    Toast.makeText(AddItemActivity.this, "Image upload is in progress", Toast.LENGTH_SHORT).show();
                    return;
                }

                MenuItemCreateRequest itemCreateRequest = new MenuItemCreateRequest();
                itemCreateRequest.setCategories(selectedCategories);
                itemCreateRequest.setDetail(itemDescription.getText().toString());
                itemCreateRequest.setImage_url(itemImageUrl);
                itemCreateRequest.setIs_available(itemAvailable.isChecked() ? 1 : 0);
                itemCreateRequest.setIs_non_veg(itemVegNon.getSelectedItemPosition());
                itemCreateRequest.setSpecification(itemSpecification.getText().toString());
                itemCreateRequest.setPrice(Double.parseDouble(itemPrice.getText().toString()));
                itemCreateRequest.setTitle(itemName.getText().toString());

                createUpdateItem(itemCreateRequest);
            }
        });

        setupCategories();
    }

    private void initUi() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_left_white);
            actionBar.setTitle("Add item");
        }

        itemSave = findViewById(R.id.itemSave);
        itemName = findViewById(R.id.itemName);
        itemDescription = findViewById(R.id.itemDescription);
        itemPrice = findViewById(R.id.itemPrice);
        itemAvailable = findViewById(R.id.itemAvailable);
        itemCategory = findViewById(R.id.itemCategory);
        itemCategory.setSelected(true);
        itemSpecification = findViewById(R.id.itemSpecification);
        itemVegNon = findViewById(R.id.itemVegNon);
        itemImageProgress = findViewById(R.id.itemImageProgress);
        progressBar = findViewById(R.id.progressBar);
        restImagePicker = findViewById(R.id.restImagePicker);
        itemImage = findViewById(R.id.restImage);

        String currency = Helper.getSetting(sharedPreferenceUtil, "currency");
        if (!TextUtils.isEmpty(currency)) {
            itemPrice.setHint("Price in " + currency);
        }
    }

    private void createUpdateItem(MenuItemCreateRequest itemCreateRequest) {
        setItemCreateProgress(true);
        if (menuItemToEdit != null) {
            ApiUtils.getClient().create(ChefStoreService.class).updateMenuItem(Helper.getApiToken(sharedPreferenceUtil), itemCreateRequest, menuItemToEdit.getId()).enqueue(new Callback<com.hababk.restaurant.network.response.MenuItem>() {
                @Override
                public void onResponse(Call<com.hababk.restaurant.network.response.MenuItem> call, Response<com.hababk.restaurant.network.response.MenuItem> response) {
                    setItemCreateProgress(false);
                    if (response.isSuccessful()) {
                        sharedPreferenceUtil.setBooleanPreference(Constants.KEY_REFRESH_ITEMS, true);
                        Toast.makeText(AddItemActivity.this, "Item updated", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(AddItemActivity.this, "Item update failed", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<com.hababk.restaurant.network.response.MenuItem> call, Throwable t) {
                    setItemCreateProgress(false);
                    Toast.makeText(AddItemActivity.this, "Item update failed", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            ApiUtils.getClient().create(ChefStoreService.class).createMenuItem(Helper.getApiToken(sharedPreferenceUtil), itemCreateRequest).enqueue(new Callback<com.hababk.restaurant.network.response.MenuItem>() {
                @Override
                public void onResponse(Call<com.hababk.restaurant.network.response.MenuItem> call, Response<com.hababk.restaurant.network.response.MenuItem> response) {
                    setItemCreateProgress(false);
                    if (response.isSuccessful()) {
                        sharedPreferenceUtil.setBooleanPreference(Constants.KEY_REFRESH_ITEMS, true);
                        Toast.makeText(AddItemActivity.this, "Item Created", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(AddItemActivity.this, "Item creation failed", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<com.hababk.restaurant.network.response.MenuItem> call, Throwable t) {
                    setItemCreateProgress(false);
                    Toast.makeText(AddItemActivity.this, "Item creation failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void setupCategories() {
        categories = Helper.getCategories(sharedPreferenceUtil);
        dialogSelectCategories = new CategorySelectionDialog(this, R.style.DialogBox, categories, selectedCategories, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedCategories.clear();
                selectedCategories.addAll(dialogSelectCategories.getSelection());
                setCategorySelectionText();
            }
        });
        itemCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSelectCategories.show();
            }
        });
    }

    private void setCategorySelectionText() {
        StringBuilder selectionText = new StringBuilder();
        for (MenuItemCategory category : selectedCategories) {
            selectionText.append(category.getTitle());
            selectionText.append(" ");
        }
        itemCategory.setText(selectionText);
    }

    private void picImage() {
        if (checkStoragePermissions()) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setMessage("Get image from");
            alertDialog.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();

                    cameraPicker = new CameraImagePicker(AddItemActivity.this);
                    cameraPicker.shouldGenerateMetadata(true);
                    cameraPicker.shouldGenerateThumbnails(true);
                    cameraPicker.setImagePickerCallback(AddItemActivity.this);
                    pickerPath = cameraPicker.pickImage();
                }
            });
            alertDialog.setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();

                    imagePicker = new ImagePicker(AddItemActivity.this);
                    imagePicker.shouldGenerateMetadata(true);
                    imagePicker.shouldGenerateThumbnails(true);
                    imagePicker.setImagePickerCallback(AddItemActivity.this);
                    imagePicker.pickImage();
                }
            });
            alertDialog.create().show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, REQUEST_CODE_PERMISSION);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == AppCompatActivity.RESULT_OK) {
            if (requestCode == Picker.PICK_IMAGE_DEVICE) {
                if (imagePicker == null) {
                    imagePicker = new ImagePicker(this);
                    imagePicker.setImagePickerCallback(this);
                }
                imagePicker.submit(data);
            } else if (requestCode == Picker.PICK_IMAGE_CAMERA) {
                if (cameraPicker == null) {
                    cameraPicker = new CameraImagePicker(this);
                    cameraPicker.setImagePickerCallback(this);
                    cameraPicker.reinitialize(pickerPath);
                }
                cameraPicker.submit(data);
            }
        }
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

    @Override
    public void onImagesChosen(List<ChosenImage> images) {
        setImageUpdateProgress(true);
        restImagePicker.setVisibility(View.GONE);
        mediaFile = new File(Uri.parse(images.get(0).getOriginalPath()).getPath());
        Glide.with(this)
                .load(mediaFile)
                .apply(new RequestOptions().centerCrop().placeholder(R.drawable.placeholder_food).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).dontAnimate())
                .into(itemImage);

        FirebaseUploader firebaseUploader = new FirebaseUploader(new FirebaseUploader.UploadListener() {
            @Override
            public void onUploadFail(String message) {
                setImageUpdateProgress(false);
                Toast.makeText(AddItemActivity.this, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUploadSuccess(String downloadUrl) {
                setImageUpdateProgress(false);
                AddItemActivity.this.itemImageUrl = downloadUrl;
            }

            @Override
            public void onUploadProgress(int progress) {

            }

            @Override
            public void onUploadCancelled() {
                setImageUpdateProgress(false);
            }
        }, "rest_detail" + System.currentTimeMillis());
        firebaseUploader.setReplace(true);
        firebaseUploader.uploadImage(this, mediaFile);
    }

    private void setImageUpdateProgress(boolean b) {
        itemImageProgress.setVisibility(b ? View.VISIBLE : View.INVISIBLE);
        imageUploadInProgress = b;
        restImagePicker.setClickable(!b);
        itemImage.setClickable(!b);
        itemSave.setClickable(!b);
    }

    private void setItemCreateProgress(boolean b) {
        progressBar.setVisibility(b ? View.VISIBLE : View.INVISIBLE);
        restImagePicker.setClickable(!b);
        itemImage.setClickable(!b);
        itemSave.setClickable(!b);
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

    private boolean checkStoragePermissions() {
        return
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        &&
                        ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        &&
                        ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    public static Intent newIntent(Context context, com.hababk.restaurant.network.response.MenuItem menuItem) {
        Intent intent = new Intent(context, AddItemActivity.class);
        intent.putExtra(DATA_MENU_ITEM, menuItem);
        return intent;
    }
}
