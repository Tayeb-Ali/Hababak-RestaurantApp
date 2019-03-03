package com.hababk.restaurant.network.request;

/**
 * Created by Tayeb-Ali on 01-03-2019.
 */

public class LoginRequest {
    private String email, password, role = "owner";

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
