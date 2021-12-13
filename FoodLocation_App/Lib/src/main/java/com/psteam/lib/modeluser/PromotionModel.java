package com.psteam.lib.modeluser;

import java.io.Serializable;

public class PromotionModel implements Serializable {
    private String name;

    private String id;

    private String value;

    private String info;

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getValue ()
    {
        return value;
    }

    public void setValue (String value)
    {
        this.value = value;
    }

    public String getInfo ()
    {
        return info;
    }

    public void setInfo (String info)
    {
        this.info = info;
    }

}
