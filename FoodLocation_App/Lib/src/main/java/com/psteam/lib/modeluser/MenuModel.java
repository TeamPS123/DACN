package com.psteam.lib.modeluser;

import java.util.ArrayList;

public class MenuModel {
    private ArrayList<FoodModel> foodList;

    private String name;

    private String menuId;

    public MenuModel(ArrayList<FoodModel> foodList, String name, String menuId) {
        this.foodList = foodList;
        this.name = name;
        this.menuId = menuId;
    }

    public ArrayList<FoodModel> getFoodList ()
    {
        return foodList;
    }

    public void setFoodList (ArrayList<FoodModel> foodList)
    {
        this.foodList = foodList;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getMenuId ()
    {
        return menuId;
    }

    public void setMenuId (String menuId)
    {
        this.menuId = menuId;
    }
}
