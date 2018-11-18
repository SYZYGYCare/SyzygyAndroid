package com.dollop.syzygy.data;

/**
 * Created by awidiyadew on 12/09/16.
 */
public class BaseItem {
    private int mIcon;
    private String mName;

    public BaseItem(int icon,String name)
    {
        mIcon=icon;
        mName = name;
    }

    public String getName() {
        return mName;
    }

    public int getIcon() {
        return mIcon;
    }}
