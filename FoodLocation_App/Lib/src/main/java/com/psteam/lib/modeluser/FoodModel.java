package com.psteam.lib.modeluser;

import java.util.ArrayList;

public class FoodModel {
    private String price;

    private String foodId;

    private String menuId;

    private String unit;

    private String categoryName;

    private String name;

    private int count;

    private ArrayList<String> pic;

    public FoodModel() {
    }

    public String getPrice ()
    {
        return price;
    }

    public void setPrice (String price)
    {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getFoodId ()
    {
        return foodId;
    }

    public void setFoodId (String foodId)
    {
        this.foodId = foodId;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public ArrayList<String> getPic ()
    {
        return pic;
    }

    public void setPic (ArrayList<String> pic)
    {
        this.pic = pic;
    }
}
