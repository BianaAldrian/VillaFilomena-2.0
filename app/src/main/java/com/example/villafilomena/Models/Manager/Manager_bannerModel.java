package com.example.villafilomena.Models.Manager;

public class Manager_bannerModel {
    private String bannerId, bannerName, bannerUrl, bannerStat;

    public Manager_bannerModel(String bannerId, String bannerName, String bannerUrl) {
        this.bannerId = bannerId;
        this.bannerName = bannerName;
        this.bannerUrl = bannerUrl;
    }

    public String getBannerId() {
        return bannerId;
    }

    public void setBannerId(String bannerId) {
        this.bannerId = bannerId;
    }

    public String getBannerName() {
        return bannerName;
    }

    public void setBannerName(String bannerName) {
        this.bannerName = bannerName;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }
}
