package com.hababk.appstore.network.request;

import com.hababk.appstore.network.response.MenuItem;
import com.hababk.appstore.network.response.MenuItemCategory;

import java.util.ArrayList;

/**
 * Created by a_man on 14-03-2018.
 */

public class MenuItemCreateRequest {
    private String title, detail, specification, image_url;
    private double price;
    private int is_available, is_non_veg;
    private ArrayList<Integer> categories;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getIs_available() {
        return is_available;
    }

    public void setIs_available(int is_available) {
        this.is_available = is_available;
    }

    public int getIs_non_veg() {
        return is_non_veg;
    }

    public void setIs_non_veg(int is_non_veg) {
        this.is_non_veg = is_non_veg;
    }

    public ArrayList<Integer> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<MenuItemCategory> categories) {
        this.categories = new ArrayList<>();
        for (MenuItemCategory category : categories)
            this.categories.add(category.getId());
    }

    public static MenuItemCreateRequest newInstance(MenuItem item) {
        MenuItemCreateRequest createRequest = new MenuItemCreateRequest();
        createRequest.setTitle(item.getTitle());
        createRequest.setDetail(item.getDetail());
        createRequest.setSpecification(item.getSpecification());
        createRequest.setImage_url(item.getImage_url());
        createRequest.setPrice(item.getPrice());
        createRequest.setIs_available(item.getIs_available());
        createRequest.setIs_non_veg(item.getIs_non_veg());
        createRequest.setCategories(item.getCategories());
        return createRequest;
    }
}
