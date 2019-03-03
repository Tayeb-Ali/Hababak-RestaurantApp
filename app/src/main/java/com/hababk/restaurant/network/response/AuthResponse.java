package com.hababk.restaurant.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hababk.restaurant.model.User;

/**
 * Created by Tayeb-Ali on 01-03-2019.
 */

public class AuthResponse {
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("user")
    @Expose
    private User user;

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }
}
