package com.shenhui.doubanfilm.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME_COL = "film_collect";
    public static final String TABLE_NAME_TOP = "film_top_250";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_FILM = "film_id";
    public static final String COLUMN_TOP = "top";
    public static final String COLUMN_CONTENT = "content";

    public static final String DATABASE_NAME = "film.db";
    public static final int DATABASE_VERSION = 1;

    private static DBHelper dbHelper;

    /**
     * 建立collect的表
     */
    private static final String DATABASE_CREATE_COL
            = "CREATE TABLE " + TABLE_NAME_COL + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_FILM + " CHAR(8) UNIQUE, " +
            COLUMN_CONTENT + " NOT NULL);";

    /**
     * 建立top250的表
     */
    private static final String DATABASE_CREATE_TOP
            = "CREATE TABLE " + TABLE_NAME_TOP + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_TOP + " CHAR(8) UNIQUE, " +
            COLUMN_CONTENT + " NOT NULL);";

    public static DBHelper getInstance(Context context) {
        if (dbHelper == null) {
            synchronized (DBHelper.class) {
                if (dbHelper == null) {
                    dbHelper = new DBHelper(context);
                }
            }
        }
        return dbHelper;
    }

    private DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE_COL);
        sqLiteDatabase.execSQL(DATABASE_CREATE_TOP);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_COL);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_TOP);
        onCreate(sqLiteDatabase);
    }
}
