package com.ustc.gcsj.main;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseDB extends SQLiteOpenHelper {

    private static String DATABASE_NAME = "UCOURSE.db";
    private static int DATABASE_VERSION = 1;

    public BaseDB(Context context) {
        // TODO Auto-generated constructor stub
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

}
