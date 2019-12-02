package com.marksimonyi.android.cst2335finalproject;

public class ListItem {
    protected String title;
    protected String lon;
    protected String lat;
    protected String phone;

    ListItem(){
        setTitle("");
        setLon("");
        setLat("");
        setPhone("");
    }

    ListItem(String t, String lo, String la, String p){
        setTitle(t);
        setLon(lo);
        setLat(la);
        setPhone(p);
    }

    public String getTitle(){return title;}
    public String getLon(){return lon;}
    public String getLat(){return lat;}
    public String getPhone(){return phone;}

    public void setTitle(String text){title = text;}
    public void setLon(String text){lon = text;}
    public void setLat(String text){lat = text;}
    public void setPhone(String text){phone = text;}



}
