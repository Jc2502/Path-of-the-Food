package com.pathofthefood.flyingburger.Address;

import java.io.Serializable;

/**
 * Created by Juan Acosta on 11/23/2014.
 */
public class Recomend implements Serializable {
    private String distance;
    private String id;
    private String restaurant_id;

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }
}
