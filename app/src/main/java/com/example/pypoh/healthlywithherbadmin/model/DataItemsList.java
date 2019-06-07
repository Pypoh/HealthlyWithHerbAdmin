package com.example.pypoh.healthlywithherbadmin.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataItemsList {

    @SerializedName("itemListId")
    @Expose
    private Integer itemListId;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("itemBgUrl")
    @Expose
    private String itemBgUrl;
    @SerializedName("itemFgColor")
    @Expose
    private String itemFgColor;
    @SerializedName("itemTitle")
    @Expose
    private String itemTitle;
    @SerializedName("items")
    @Expose
    private List<DataItem> items = null;

    public DataItemsList(Integer itemListId, String type, String itemBgUrl, String itemFgColor, String itemTitle, List<DataItem> items) {
        this.itemListId = itemListId;
        this.type = type;
        this.itemBgUrl = itemBgUrl;
        this.itemFgColor = itemFgColor;
        this.itemTitle = itemTitle;
        this.items = items;
    }

    public Integer getItemListId() {
        return itemListId;
    }

    public void setItemListId(Integer itemListId) {
        this.itemListId = itemListId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getItemBgUrl() {
        return itemBgUrl;
    }

    public void setItemBgUrl(String itemBgUrl) {
        this.itemBgUrl = itemBgUrl;
    }

    public String getItemFgColor() {
        return itemFgColor;
    }

    public void setItemFgColor(String itemFgColor) {
        this.itemFgColor = itemFgColor;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public List<DataItem> getItems() {
        return items;
    }

    public void setItems(List<DataItem> items) {
        this.items = items;
    }


}
