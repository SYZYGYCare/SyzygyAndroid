package com.dollop.syzygy.data;

/**
 * Created by awidiyadew on 12/09/16.
 */
public class GroupItem extends BaseItem {
    private int mLevel;

    public GroupItem(int icon,String name) {
        super(icon,name);
        mLevel = 0;
    }

    public void setLevel(int level){
        mLevel = level;
    }

    public int getLevel(){
        return mLevel;
    }
}
