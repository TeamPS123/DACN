package com.psteam.lib.modeluser;

public class ChangeInfoModel {
    private boolean gender;

    private String pass;

    private boolean isBusiness;

    private String fullName;

    private String userId;

    public boolean getGender ()
    {
        return gender;
    }

    public void setGender (boolean gender)
    {
        this.gender = gender;
    }

    public String getPass ()
    {
        return pass;
    }

    public void setPass (String pass)
    {
        this.pass = pass;
    }

    public boolean getIsBusiness ()
    {
        return isBusiness;
    }

    public void setIsBusiness (boolean isBusiness)
    {
        this.isBusiness = isBusiness;
    }

    public String getFullName ()
    {
        return fullName;
    }

    public void setFullName (String fullName)
    {
        this.fullName = fullName;
    }

    public String getUserId ()
    {
        return userId;
    }

    public void setUserId (String userId)
    {
        this.userId = userId;
    }

    public ChangeInfoModel(boolean gender, String pass, boolean isBusiness, String fullName, String userId) {
        this.gender = gender;
        this.pass = pass;
        this.isBusiness = isBusiness;
        this.fullName = fullName;
        this.userId = userId;
    }
}
