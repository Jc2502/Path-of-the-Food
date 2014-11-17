package com.pathofthefood.flyingburger;

/**
 * Created by Juan Acosta on 11/17/2014.
 */
public class DrawerItem {
    private String title;
    private int icon;
    public DrawerItem(){}

    public DrawerItem(String title, int icon){
        this.title = title;
        this.icon = icon;
    }
    public String getTitle(){
        return this.title;
    }

    public int getIcon(){
        return this.icon;
    }
    public void setTitle(String title){
        this.title = title;
    }

    public void setIcon(int icon){
        this.icon = icon;
    }
}
