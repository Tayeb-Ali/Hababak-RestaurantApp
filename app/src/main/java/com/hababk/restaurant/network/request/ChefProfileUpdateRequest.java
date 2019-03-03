package com.hababk.restaurant.network.request;

/**
 * Created by Tayeb-Ali on 27-03-2018.
 */

public class ChefProfileUpdateRequest {
    private String name, tagline, image_url, delivery_time, details, area, address, opens_at, closes_at, status = "open";
    private Double minimum_order, delivery_fee, cost_for_two, longitude = 0.0d, latitude = 0.0d;
    private boolean preorder, serves_non_veg;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getDelivery_time() {
        return delivery_time;
    }

    public void setDelivery_time(String delivery_time) {
        this.delivery_time = delivery_time;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getMinimum_order() {
        return minimum_order;
    }

    public void setMinimum_order(Double minimum_order) {
        this.minimum_order = minimum_order;
    }

    public Double getDelivery_fee() {
        return delivery_fee;
    }

    public void setDelivery_fee(Double delivery_fee) {
        this.delivery_fee = delivery_fee;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public boolean isPreorder() {
        return preorder;
    }

    public void setPreorder(boolean preorder) {
        this.preorder = preorder;
    }

    public String getOpens_at() {
        return opens_at;
    }

    public void setOpens_at(String opens_at) {
        this.opens_at = opens_at;
    }

    public String getCloses_at() {
        return closes_at;
    }

    public void setCloses_at(String closes_at) {
        this.closes_at = closes_at;
    }

    public boolean isServes_non_veg() {
        return serves_non_veg;
    }

    public void setServes_non_veg(boolean serves_non_veg) {
        this.serves_non_veg = serves_non_veg;
    }

    public Double getCost_for_two() {
        return cost_for_two;
    }

    public void setCost_for_two(Double cost_for_two) {
        this.cost_for_two = cost_for_two;
    }
}
