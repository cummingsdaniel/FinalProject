package com.marksimonyi.android.cst2335finalproject;



public class ForeignExchangeList {

    //Android Studio hint: to create getter and setter, put mouse on variable and click "alt+insert"
    protected String currency;
    protected long id;

    /**Constructor:*/
    public ForeignExchangeList(String n,  long i)
    {
        currency =n;
       ;
        id = i;
    }

    public void update(String n)
    {
        currency = n;

    }

    /**Chaining constructor: */
    public ForeignExchangeList(String n, String e) { this(n, 0);}




    public String getcurrency() {
        return currency;
    }

    public long getId() {
        return id;
    }


}
