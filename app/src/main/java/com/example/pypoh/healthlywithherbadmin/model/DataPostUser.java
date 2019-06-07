package com.example.pypoh.healthlywithherbadmin.model;

public class DataPostUser {

    private String image;
    private String title;
    private String deskripsi;
    private String date;
    private String status;

    public DataPostUser(String image, String title, String deskripsi, String date, String status) {
        this.image = image;
        this.title = title;
        this.deskripsi = deskripsi;
        this.date = date;
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
