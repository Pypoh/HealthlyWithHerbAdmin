package com.example.pypoh.healthlywithherbadmin.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataNarasumber {

    @SerializedName("Username")
    @Expose
    private String Username;
    @SerializedName("Reputasi")
    @Expose
    private String Reputasi;
    @SerializedName("JumlahPost")
    @Expose
    private int JumlahPost;
    @SerializedName("Gambar")
    @Expose
    private String Gambar;

    public DataNarasumber() {
    }

    public DataNarasumber(String username, String reputasi, int jumlahPost, String gambar) {
        Username = username;
        Reputasi = reputasi;
        JumlahPost = jumlahPost;
        Gambar = gambar;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getReputasi() {
        return Reputasi;
    }

    public void setReputasi(String reputasi) {
        Reputasi = reputasi;
    }

    public int getJumlahPost() {
        return JumlahPost;
    }

    public void setJumlahPost(int jumlahPost) {
        JumlahPost = jumlahPost;
    }

    public String getGambar() {
        return Gambar;
    }

    public void setGambar(String gambar) {
        Gambar = gambar;
    }
}
