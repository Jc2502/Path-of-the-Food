package com.pathofthefood.flyingburger.Restaurant;

import java.io.Serializable;

/**
 * Created by Juan Acosta on 11/16/2014.
 */
public class Restaurants implements Serializable {
    private String id;
    private String name;
    private String textaddress;
    private String onlycash;
    private String type;
    private String description;
    private String created_at;
    private String updated_at;
    private String latitude;
    private String longitude;
    private String icon_type;
    private String image_url;

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTextaddress() {
        return textaddress;
    }

    public void setTextaddress(String textaddress) {
        this.textaddress = textaddress;
    }

    public String getOnlycash() {
        return onlycash;
    }

    public void setOnlycash(String onlycash) {
        this.onlycash = onlycash;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getIcon_type() {
        return icon_type;
    }

    public void setIcon_type(String icon_type) {
        this.icon_type = icon_type;
    }
}
