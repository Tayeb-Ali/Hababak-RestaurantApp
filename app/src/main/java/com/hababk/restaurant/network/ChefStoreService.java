package com.hababk.restaurant.network;

import com.google.gson.JsonObject;
import com.hababk.restaurant.model.User;
import com.hababk.restaurant.network.request.BankDetailRequest;
import com.hababk.restaurant.network.request.ChefProfileUpdateRequest;
import com.hababk.restaurant.network.request.FcmTokenUpdateRequest;
import com.hababk.restaurant.network.request.LoginRequest;
import com.hababk.restaurant.network.request.MenuItemCreateRequest;
import com.hababk.restaurant.network.request.MobileVerifiedRequest;
import com.hababk.restaurant.network.request.RegisterRequest;
import com.hababk.restaurant.network.request.SupportRequest;
import com.hababk.restaurant.network.response.AuthResponse;
import com.hababk.restaurant.network.response.BankDetailResponse;
import com.hababk.restaurant.network.response.BaseListModel;
import com.hababk.restaurant.network.response.ChefProfile;
import com.hababk.restaurant.network.response.EarningResponse;
import com.hababk.restaurant.network.response.MenuItem;
import com.hababk.restaurant.network.response.MenuItemCategory;
import com.hababk.restaurant.network.response.Order;
import com.hababk.restaurant.network.response.Review;
import com.hababk.restaurant.network.response.SettingResponse;
import com.hababk.restaurant.network.response.SupportResponse;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by a_man on 05-12-2017.
 */

public interface ChefStoreService {
    @Headers("Accept: application/json")
    @POST("api/login")
    Call<AuthResponse> login(@Body LoginRequest loginRequest);

    @Headers("Accept: application/json")
    @POST("api/register")
    Call<AuthResponse> register(@Body RegisterRequest registerRequest);

    @Headers("Accept: application/json")
    @POST("api/verify-mobile")
    Call<JsonObject> verifyMobile(@Body MobileVerifiedRequest mobileVerifiedRequest);

    @Headers("Accept: application/json")
    @POST("api/support")
    Call<SupportResponse> support(@Body SupportRequest supportRequest);

    @Headers("Accept: application/json")
    @PUT("api/user")
    Call<User> updateFcmToken(@Header("Authorization") String token, @Body FcmTokenUpdateRequest fcmTokenUpdateRequest);

    @Headers("Accept: application/json")
    @GET("api/bank-detail")
    Call<BankDetailResponse> getBankDetails(@Header("Authorization") String token);

    @Headers("Accept: application/json")
    @POST("api/bank-detail")
    Call<BankDetailResponse> setBankDetails(@Header("Authorization") String token, @Body BankDetailRequest bankDetailRequest);

    @Headers("Accept: application/json")
    @GET("api/menuitem")
    Call<BaseListModel<MenuItem>> getMenuItems(@Header("Authorization") String token, @Query("status") String status, @Query("page") Integer page);

    @Headers("Accept: application/json")
    @GET("api/order")
    Call<BaseListModel<Order>> getOrders(@Header("Authorization") String token, @Query("status") String status, @Query("active_orders") int active_orders, @Query("deliveries") int deliveries, @Query("page") Integer page);

    @Headers("Accept: application/json")
    @GET("api/order/{id}")
    Call<Order> getOrderById(@Header("Authorization") String token, @Path("id") String orderId);

    @Headers("Accept: application/json")
    @PUT("api/order/{id}")
    Call<Order> updateOrderStatus(@Header("Authorization") String token, @Body HashMap<String, String> orderStatusUpdateRequest, @Path("id") Integer orderId);

    @Headers("Accept: application/json")
    @POST("api/menuitem")
    Call<MenuItem> createMenuItem(@Header("Authorization") String token, @Body MenuItemCreateRequest menuItemCreateRequest);

    @Headers("Accept: application/json")
    @POST("api/menuitem/{id}")
    Call<MenuItem> updateMenuItem(@Header("Authorization") String token, @Body MenuItemCreateRequest menuItemUpdateRequest, @Path("id") Integer menuItemId);

    @Headers("Accept: application/json")
    @GET("api/category")
    Call<BaseListModel<MenuItemCategory>> getMenuItemCategories(@Header("Authorization") String token);

    @Headers("Accept: application/json")
    @PUT("api/store/update")
    Call<ChefProfile> updateProfile(@Header("Authorization") String token, @Body ChefProfileUpdateRequest chefProfileUpdateRequest);

    @Headers("Accept: application/json")
    @GET("api/store")
    Call<ChefProfile> getProfile(@Header("Authorization") String token);

    @Headers("Accept: application/json")
    @GET("api/settings")
    Call<ArrayList<SettingResponse>> getSettings();

    @Headers("Accept: application/json")
    @GET("api/rating/")
    Call<BaseListModel<Review>> getReviewsMine(@Header("Authorization") String token, @Query("page") Integer page);

    @Headers("Accept: application/json")
    @GET("api/earnings")
    Call<EarningResponse> getEarnings(@Header("Authorization") String token, @Query("page") int page);
}
