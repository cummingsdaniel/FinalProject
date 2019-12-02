package com.marksimonyi.android.cst2335finalproject;



public class ForeignExchangeList {

    //Android Studio hint: to create getter and setter, put mouse on variable and click "alt+insert"
    protected String base;
    protected String convo1;
    protected String convo2;

    /**Constructor:*/
    public ForeignExchangeList(String n, String d, String x)
    {
        base =n;
       convo1 =d;
       convo2=x;
    }

    public void update(String n,String d, String x)
    {
        base = n;
        convo1 =d;
        convo2=x;
    }




    public String getBase() {
        return base;
    }
    public String getConvo1() {
        return convo1;
    }
    public String getConvo2() {
        return convo2;
    }




}
