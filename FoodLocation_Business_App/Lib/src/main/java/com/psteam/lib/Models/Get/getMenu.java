package com.psteam.lib.Models.Get;

import java.util.List;

public class getMenu {
    public getMenu(String menuId, String name, List<getFood> foodList) {
        this.menuId = menuId;
        this.name = name;
        this.foodList = foodList;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<getFood> getFoodList() {
        return foodList;
    }

    public void setFoodList(List<getFood> foodList) {
        this.foodList = foodList;
    }

    private String menuId ;
    private String name ;
    private List<getFood> foodList ;
}
