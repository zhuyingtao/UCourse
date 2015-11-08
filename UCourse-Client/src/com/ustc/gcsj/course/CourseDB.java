package com.ustc.gcsj.course;

import com.ustc.gcsj.main.BaseDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class CourseDB extends BaseDB {

    private String TABLE_NAME = "COURSE";
    private String COURSE_ID = "course_id";
    private String COURSE_NAME = "course_name";
    private String COURSE_ROOM = "course_room";
    private String COURSE_TEACHER = "course_teacher";
    private String COURSE_WEEK_START = "course_week_start";
    private String COURSE_WEEK_END = "course_week_end";

    public CourseDB(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( _"
                + COURSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COURSE_NAME + " text," + COURSE_ROOM + " text,"
                + COURSE_TEACHER + " text," + COURSE_WEEK_START + " INTEGER,"
                + COURSE_WEEK_END + " INTEGER);";
        Log.v("1111", "CourseDB");
        db.execSQL(sql);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);
    }

    public Cursor select() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        } catch (SQLException se) {
            this.onCreate(db);
        }

        return cursor;
    }

    public long insert(String course_name, String course_room,
                       String course_teacher, int course_week_start, int course_week_end) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COURSE_NAME, course_name);
        cv.put(COURSE_ROOM, course_room);
        cv.put(COURSE_TEACHER, course_teacher);
        cv.put(COURSE_WEEK_START, course_week_start);
        cv.put(COURSE_WEEK_END, course_week_end);
        long row = db.insert(TABLE_NAME, null, cv);
        return row;
    }

    public void delete(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = "_" + COURSE_ID + "=?";
        String[] whereValue = {Integer.toString(id)};
        db.delete(TABLE_NAME, where, whereValue);
    }

    public void update(int course_id, String course_name, String course_room,
                       String course_teacher, int course_week_start, int course_week_end) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = "_" + COURSE_ID + " = ?";
        String[] whereValue = {Integer.toString(course_id)};

        ContentValues cv = new ContentValues();
        cv.put(COURSE_NAME, course_name);
        cv.put(COURSE_ROOM, course_room);
        cv.put(COURSE_TEACHER, course_teacher);
        cv.put(COURSE_WEEK_START, course_week_start);
        cv.put(COURSE_WEEK_END, course_week_end);
        db.update(TABLE_NAME, cv, where, whereValue);
    }

    public Cursor selectByID(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " where " + COURSE_ID
                + " = " + Integer.toString(id);
        Cursor cursor = db.rawQuery(sql, null);
        return cursor;
    }

    public Cursor selectByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " where " + COURSE_NAME
                + "= ?";
        Cursor cursor = db.rawQuery(sql, new String[]{name});
        // System.out.println("xxxxxxxx"+cursor.getColumnCount()+"   "+cursor.getCount());
        return cursor;
    }
}