package com.example.twih.front;

import android.graphics.drawable.Drawable;

/**
 * Created by twih on 2016. 11. 17..
 */

public class ListViewItem {
    private Drawable iconDrawable;  //프사
    private String nameStr; //유저 이름
    private String descStr; //상태명

    public void setIcon(Drawable icon){
        iconDrawable = icon;
    }

    public void setNameStr(String name){
        nameStr = name;
    }

    public void setDescStr(String desc){
        descStr = desc;
    }

    public Drawable getIcon(){
        return this.iconDrawable;
    }

    public String getNameStr(){
        return this.nameStr;
    }

    public String getDescStr(){
        return this.descStr;
    }

}
