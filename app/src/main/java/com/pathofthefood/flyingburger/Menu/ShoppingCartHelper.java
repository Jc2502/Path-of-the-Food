package com.pathofthefood.flyingburger.Menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShoppingCartHelper {

    public static final String PRODUCT_INDEX = "PRODUCT_INDEX";

    private static Map<Products, ShoppingCartEntry> cartMap = new HashMap<Products, ShoppingCartEntry>();

    public static void setQuantity(Products product, int quantity) {
        // Get the current cart entry
        ShoppingCartEntry curEntry = cartMap.get(product);

        // If the quantity is zero or less, remove the products
        if (quantity <= 0) {
            if (curEntry != null)
                removeProduct(product);
            return;
        }

        // If a current cart entry doesn't exist, create one
        if (curEntry == null) {
            curEntry = new ShoppingCartEntry(product, quantity);
            cartMap.put(product, curEntry);
            return;
        }

        // Update the quantity
        curEntry.setQuantity(quantity);
    }

    public static int getProductQuantity(Products product) {
        // Get the current cart entry
        ShoppingCartEntry curEntry = cartMap.get(product);

        if (curEntry != null)
            return curEntry.getQuantity();

        return 0;
    }

    public static void removeProduct(Products product) {
        cartMap.remove(product);
    }

    public static ArrayList<Products> getCartList() {
        ArrayList<Products> cartList = new ArrayList<Products>(cartMap.keySet().size());
        for (Products p : cartMap.keySet()) {
            cartList.add(p);
        }
        return cartList;
    }


}
