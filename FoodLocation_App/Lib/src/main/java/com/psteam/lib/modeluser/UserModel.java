package com.psteam.lib.modeluser;

import java.io.Serializable;

public class UserModel implements Serializable {
    private boolean gender;

    private String phone;

    private boolean isBusiness;

    private String fullName;

    private String pic;





    public UserModel(boolean gender, String phone, boolean isBusiness, String fullName, String pic) {
        this.gender = gender;
        this.phone = phone;
        this.isBusiness = isBusiness;
        this.fullName = fullName;
        this.pic = pic;
    }

    public boolean getGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean getIsBusiness() {
        return isBusiness;
    }

    public void setIsBusiness(boolean isBusiness) {
        this.isBusiness = isBusiness;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
