package com.marksimonyi.android.cst2335finalproject;


import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class RecipeDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "RecipeResultsDatabase";
    public static final int VERSION = 1;
    public static final String COL_ID = "_id";
    public static final String COL_TITLE = "Title";
    public static final String COL_IMG = "ImageUrl";
    public static final String COL_URL = "PageUrl";
    public static final String REC_FAV_TABLE_NAME = "Favourites";
    public static final String REC_SAV_TABLE_NAME = "Saved";

    public RecipeDBHelper(Activity ctx){
        //The factory parameter should be null, unless you know a lot about Database Memory management
        super(ctx, DATABASE_NAME, null, VERSION);
    }

    public void onCreate(SQLiteDatabase db)
    {
        //Make sure you put spaces between SQL statements and Java strings:
        db.execSQL("CREATE TABLE " + REC_FAV_TABLE_NAME + "( "
                + COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_TITLE + " TEXT, " + COL_IMG + " TEXT, " + COL_URL + " TEXT)");
        db.execSQL("CREATE TABLE " + REC_SAV_TABLE_NAME + "( "
                + COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_TITLE + " TEXT, " + COL_IMG + " TEXT, " + COL_URL + " TEXT)");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.i("Database upgrade", "Old version:" + oldVersion + " newVersion:"+newVersion);

        //Delete the old table:
        db.execSQL("DROP TABLE IF EXISTS " + REC_FAV_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + REC_SAV_TABLE_NAME);

        //Create a new table:
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.i("Database downgrade", "Old version:" + oldVersion + " newVersion:"+newVersion);

        //Delete the old table:
        db.execSQL("DROP TABLE IF EXISTS " + REC_FAV_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + REC_SAV_TABLE_NAME);

        //Create a new table:
        onCreate(db);
    }
    public static final void printCursor(Cursor c) {
        Log.i("DBHelper.printCursor", "Database Version: " + VERSION);
//•	The database version number
        Log.i("DBHelper.printCursor", "Columns in Cursor: " + c.getColumnCount());
//•	The number of columns in the cursor.
        Log.i("DBHelper.printCursor", "Column Names in Cursor: " + c.getColumnNames());
//•	The name of the columns in the cursor.
        Log.i("DBHelper.printCursor", "Number of results in Cursor: " + c.getCount() );
//•	The number of results in the cursor
        Log.i("DBHelper.printCursor", "Cursor Results: ");
        c.moveToFirst();
        int row = 0;
        while(!c.isAfterLast()) {
            Log.i("printCursor:Results", "Cursor Row: "+row);
            for (String name : c.getColumnNames() ) {
                Log.i("printCursor:Results", name + ": " + c.getString(c.getColumnIndex(name)));
            }
            row++;
            c.moveToNext();
        }
//•	Each row of results in the cursor.

    }
}