package com.example.pypoh.healthlywithherbadmin.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataCategory {

    @SerializedName("Nama")
    @Expose
    private String Nama;
    @SerializedName("Gambar")
    @Expose
    private String Gambar;

    public DataCategory() {
    }

    public DataCategory(String nama, String gambar) {
        Nama = nama;
        Gambar = gambar;
    }

    public String getNama() {
        return Nama;
    }

    public void setNama(String nama) {
        Nama = nama;
    }

    public String getGambar() {
        return Gambar;
    }

    public void setGambar(String gambar) {
        Gambar = gambar;
    }
}
