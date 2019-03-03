package com.hababk.restaurant.utils;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hababk.restaurant.model.User;
import com.hababk.restaurant.network.ApiUtils;
import com.hababk.restaurant.network.ChefStoreService;
import com.hababk.restaurant.network.response.BankDetailResponse;
import com.hababk.restaurant.network.response.BaseListModel;
import com.hababk.restaurant.network.response.ChefProfile;
import com.hababk.restaurant.network.response.MenuItemCategory;
import com.hababk.restaurant.network.response.SettingResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Tayeb-Ali on 01-03-2019.
 */

public class Helper {
    public static void setLoggedInUser(SharedPreferenceUtil sharedPreferenceUtil, User user) {
        sharedPreferenceUtil.setStringPreference(Constants.KEY_USER, new Gson().toJson(user, new TypeToken<User>() {
        }.getType()));
    }

    public static User getLoggedInUser(SharedPreferenceUtil sharedPreferenceUtil) {
        User toReturn = null;
        String savedInPrefs = sharedPreferenceUtil.getStringPreference(Constants.KEY_USER, null);
        if (savedInPrefs != null) {
            toReturn = new Gson().fromJson(savedInPrefs, new TypeToken<User>() {
            }.getType());
        }
        return toReturn;
    }

    public static boolean isNumber(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    public static boolean isLoggedIn(SharedPreferenceUtil sharedPreferenceUtil) {
        User user = getLoggedInUser(sharedPreferenceUtil);
        return user != null && user.getMobile_verified() == 1;
    }

    public static void setBankDetails(SharedPreferenceUtil sharedPreferenceUtil, BankDetailResponse bankDetailResponse) {
        sharedPreferenceUtil.setStringPreference(Constants.KEY_BANK_DETAIL, new Gson().toJson(bankDetailResponse, new TypeToken<BankDetailResponse>() {
        }.getType()));
    }

    public static BankDetailResponse getBankDetails(SharedPreferenceUtil sharedPreferenceUtil) {
        BankDetailResponse toReturn = null;
        String savedInPrefs = sharedPreferenceUtil.getStringPreference(Constants.KEY_BANK_DETAIL, null);
        if (savedInPrefs != null) {
            toReturn = new Gson().fromJson(savedInPrefs, new TypeToken<BankDetailResponse>() {
            }.getType());
        }
        return toReturn;
    }

    public static String getSpacedAccountNumber(String account_number) {
        StringBuilder accNum = new StringBuilder();
        int count = 0;
        for (char c : account_number.toCharArray()) {
            if (count == 4) {
                accNum.append("-");
                count = 0;
            }
            accNum.append(c);
            count += 1;
        }
        return accNum.toString();
    }

    public static String getApiToken(SharedPreferenceUtil sharedPreferenceUtil) {
        return "Bearer " + sharedPreferenceUtil.getStringPreference(Constants.KEY_TOKEN, null);
    }

    public static String getSpaceRemovedAccountNumber(String s) {
        return s.replaceAll("-", "");
    }

    public static void logout(SharedPreferenceUtil sharedPreferenceUtil) {
        sharedPreferenceUtil.removePreference(Constants.KEY_USER);
        sharedPreferenceUtil.removePreference(Constants.KEY_TOKEN);
        sharedPreferenceUtil.removePreference(Constants.KEY_PROFILE);
        sharedPreferenceUtil.removePreference(Constants.KEY_SETTING);
    }

    public static ArrayList<MenuItemCategory> getCategories(SharedPreferenceUtil sharedPreferenceUtil) {
        ArrayList<MenuItemCategory> toReturn = new ArrayList<>();
        String savedInPrefs = sharedPreferenceUtil.getStringPreference(Constants.KEY_CATEGORY, null);
        if (savedInPrefs != null) {
            ArrayList<MenuItemCategory> categories = new Gson().fromJson(savedInPrefs, new TypeToken<ArrayList<MenuItemCategory>>() {
            }.getType());
            toReturn.addAll(categories);
        }
        return toReturn;
    }

    public static void setCategories(SharedPreferenceUtil sharedPreferenceUtil, ArrayList<MenuItemCategory> menuItemCategories) {
        sharedPreferenceUtil.setStringPreference(Constants.KEY_CATEGORY, new Gson().toJson(menuItemCategories, new TypeToken<ArrayList<MenuItemCategory>>() {
        }.getType()));
    }

    public static void preLoadCategories(final SharedPreferenceUtil sharedPreferenceUtil) {
        ApiUtils.getClient().create(ChefStoreService.class).getMenuItemCategories(getApiToken(sharedPreferenceUtil)).enqueue(new Callback<BaseListModel<MenuItemCategory>>() {
            @Override
            public void onResponse(Call<BaseListModel<MenuItemCategory>> call, Response<BaseListModel<MenuItemCategory>> response) {
                if (response.isSuccessful()) {
                    setCategories(sharedPreferenceUtil, response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<BaseListModel<MenuItemCategory>> call, Throwable t) {

            }
        });
    }

    public static ChefProfile getChefDetails(SharedPreferenceUtil sharedPreferenceUtil) {
        ChefProfile toReturn = null;
        String savedInPrefs = sharedPreferenceUtil.getStringPreference(Constants.KEY_PROFILE, null);
        if (savedInPrefs != null) {
            toReturn = new Gson().fromJson(savedInPrefs, new TypeToken<ChefProfile>() {
            }.getType());
        }
        return toReturn;
    }

    public static void setChefDetails(SharedPreferenceUtil sharedPreferenceUtil, ChefProfile body) {
        sharedPreferenceUtil.setStringPreference(Constants.KEY_PROFILE, new Gson().toJson(body, new TypeToken<ChefProfile>() {
        }.getType()));
    }

    private static void setSettings(SharedPreferenceUtil sharedPreferenceUtil, ArrayList<SettingResponse> body) {
        sharedPreferenceUtil.setStringPreference(Constants.KEY_SETTING, new Gson().toJson(body, new TypeToken<ArrayList<SettingResponse>>() {
        }.getType()));
    }

    private static ArrayList<SettingResponse> getSettings(SharedPreferenceUtil sharedPreferenceUtil) {
        ArrayList<SettingResponse> toReturn = new ArrayList<>();
        String savedInPrefs = sharedPreferenceUtil.getStringPreference(Constants.KEY_SETTING, null);
        if (savedInPrefs != null) {
            ArrayList<SettingResponse> settings = new Gson().fromJson(savedInPrefs, new TypeToken<ArrayList<SettingResponse>>() {
            }.getType());
            toReturn.addAll(settings);
        }
        return toReturn;
    }

    public static boolean isDouble(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    public static String getReadableDateTime(String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {
            Date startDate = simpleDateFormat.parse(date);
            return new SimpleDateFormat("hh:mm aa, dd MMM yyyy", Locale.getDefault()).format(startDate);
        } catch (ParseException pe) {
            pe.printStackTrace();
            return "";
        }
    }

    public static CharSequence timeDiff(String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date startDate = new Date();
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {
            startDate = simpleDateFormat.parse(date);
        } catch (ParseException pe) {
            pe.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        return DateUtils.getRelativeTimeSpanString(startDate.getTime(), calendar.getTimeInMillis(), DateUtils.SECOND_IN_MILLIS);
    }

    public static void refreshSettings(final SharedPreferenceUtil sharedPreferenceUtil) {
        ApiUtils.getClient().create(ChefStoreService.class).getSettings().enqueue(new Callback<ArrayList<SettingResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<SettingResponse>> call, Response<ArrayList<SettingResponse>> response) {
                if (response.isSuccessful() && sharedPreferenceUtil != null)
                    setSettings(sharedPreferenceUtil, response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<SettingResponse>> call, Throwable t) {

            }
        });
    }

    public static String getSetting(SharedPreferenceUtil sharedPreferenceUtil, String settingName) {
        ArrayList<SettingResponse> settings = getSettings(sharedPreferenceUtil);
        int index = settings.indexOf(new SettingResponse(settingName));
        if (index != -1) {
            return settings.get(index).getValue();
        } else {
            return null;
        }
    }

    public static boolean isToday(String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            long today = System.currentTimeMillis();
            Date startDate = simpleDateFormat.parse(date);
            return startDate.getTime() - today < (24 * 3600000);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void closeKeyboard(Context context, View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null)
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
