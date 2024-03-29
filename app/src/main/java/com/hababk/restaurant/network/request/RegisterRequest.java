package com.hababk.restaurant.network.request;

/**
 * Created by Tayeb-Ali on 01-03-2019.
 */

public class RegisterRequest {
    private String name, email, password, mobile_number, role = "owner";

    public RegisterRequest(String name, String email, String password, String mobile_number) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.mobile_number = mobile_number;
    }

    public String getMobile_number() {
        return mobile_number;
    }
}
