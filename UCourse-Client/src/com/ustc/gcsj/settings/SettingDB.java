package com.ustc.gcsj.settings;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ustc.gcsj.main.BaseDB;

public class SettingDB extends BaseDB {

    private String TABLE_NAME = "setting";
    private String SETTING_ID = "setting_id";
    private String SETTING_TOTAL_WEEK = "setting_total_week";
    private String SETTING_EXAM_SWITCH = "setting_exam_switch";

    public SettingDB(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public void onCreate(SQLiteDatabase db) {
        // String sql =
        // "CREATE TABLE IF NOT EXISTS setting(settring_id INTEGER PRIMARY KEY, setting_total_week INTEGER, setting_exam_switch BOOLEAN)";
        String sql = "CREATE TABLE IF NOT EXISTS" + TABLE_NAME + " ("
                + SETTING_ID + " INTEGER PRIMARY KEY" + SETTING_TOTAL_WEEK
                + " INTEGER" + SETTING_EXAM_SWITCH + " BOOLEAN);";
        db.execSQL(sql);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        String sql = "DROP TABLE IF EXISTS" + TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);
    }

    public Cursor select() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db
                .query(TABLE_NAME, null, null, null, null, null, null);
        return cursor;
    }

    public long insert(int totalweek, boolean examswitch) {
        Cursor cursor = select();
        if (cursor.moveToFirst()) {
            update(totalweek, examswitch);
            return 1;
        } else {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(SETTING_ID, 1);
            cv.put(SETTING_TOTAL_WEEK, totalweek);
            cv.put(SETTING_EXAM_SWITCH, examswitch);
            long row = db.insert(TABLE_NAME, null, cv);
            return row;
        }

    }

    public void update(int totalweek, boolean examswitch) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = SETTING_ID + " = 1";
        ContentValues cv = new ContentValues();
        cv.put(SETTING_TOTAL_WEEK, totalweek);
        cv.put(SETTING_EXAM_SWITCH, examswitch);
        db.update(TABLE_NAME, cv, where, null);
    }

}
