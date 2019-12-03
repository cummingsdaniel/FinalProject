package com.marksimonyi.android.cst2335finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * InternetStuff.java
 * Section 020
 * Daniel Cummings
 * 2019-12-02
 */

/*This class holds the sqlite database*/
public class NewsAppDatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "news.db";
    public static final String TABLE_NAME = "Fav_table";
    public static final String COL_ID = "ID";
    public static final String COL_URL = "URL";
    public static final String COL_TITLE = "TITlE";
    public static final String COL_DESCRIPTION = "DESCRIPTION";
    public static final String COL_IMAGE_URL = "IMAGE";
    public NewsAppDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "( " + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_URL + " TEXT, " + COL_TITLE +" TEXT, " + COL_DESCRIPTION +" TEXT, " + COL_IMAGE_URL + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String title, String url, String description, String imageUrl) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_URL, url);
        contentValues.put(COL_TITLE, title);
        contentValues.put(COL_DESCRIPTION, description);
        contentValues.put(COL_IMAGE_URL, imageUrl);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }
    public boolean removeData(String url) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, COL_URL+"='"+url+"'", null);
        if(result == 0)
            return false;
        else
            return true;
    }
}
