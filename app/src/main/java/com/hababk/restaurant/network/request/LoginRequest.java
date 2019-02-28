package com.hababk.restaurant.network.request;

/**
 * Created by a_man on 12-03-2018.
 */

public class LoginRequest {
    private String email, password, role = "owner";

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
