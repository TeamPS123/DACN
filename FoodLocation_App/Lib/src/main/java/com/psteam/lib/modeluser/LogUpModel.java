package com.psteam.lib.modeluser;

import java.io.Serializable;

public class LogUpModel implements Serializable {
    private boolean business;

    private boolean gender;

    private String phone;

    private String pass;

    private String fullName;


    public LogUpModel(boolean business, boolean gender, String phone, String pass, String fullName) {
        this.business = business;
        this.gender = gender;
        this.phone = phone;
        this.pass = pass;
        this.fullName = fullName;
    }

    public boolean getBusiness ()
    {
        return business;
    }

    public void setBusiness (boolean business)
    {
        this.business = business;
    }

    public boolean getGender ()
    {
        return gender;
    }

    public void setGender (boolean gender)
    {
        this.gender = gender;
    }

    public String getPhone ()
    {
        return phone;
    }

    public void setPhone (String phone)
    {
        this.phone = phone;
    }

    public String getPass ()
    {
        return pass;
    }

    public void setPass (String pass)
    {
        this.pass = pass;
    }

    public String getFullName ()
    {
        return fullName;
    }

    public void setFullName (String fullName)
    {
        this.fullName = fullName;
    }

}
