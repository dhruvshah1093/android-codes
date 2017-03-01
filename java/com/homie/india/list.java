package com.homie.india;
//model of list get and sets the parameters
public class list {

    private String user_name;
    private String imageUrl;
    private String address;
    private String rent;
    private String user_dp;
    private String id;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return user_name;
    }
    public void setName(String user_name) {
        this.user_name = user_name;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public String getRent() {
        return rent;
    }
    public void setRent(String rent) {
        this.rent =rent;
    }

    public String getUser_dp() {
        return user_dp;
    }
    public void setUser_dp(String user_dp) {
        this.user_dp = user_dp;
    }
}