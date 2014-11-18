package com.pathofthefood.flyingburger.Menu;

public class ShoppingCartEntry {

    private Products mProduct;
    private int mQuantity;

    public ShoppingCartEntry(Products product, int quantity) {
        mProduct = product;
        mQuantity = quantity;
    }

    public Products getProduct() {
        return mProduct;
    }

    public int getQuantity() {
        return mQuantity;
    }

    public void setQuantity(int quantity) {
        mQuantity = quantity;
    }

}
