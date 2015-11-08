package com.ustc.gcsj.course;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ustc.gcsj.main.BaseDB;

public class CourseInfoDB extends BaseDB {

    private String TABLE_NAME = "COURSEINFO";
    private String COURSEINFO_ID = "courseinfo_id";
    private String COURSE_NAME = "course_name";
    private String COURSE_TIME_HOUR = "course_time_hour";
    private String COURSE_TIME_MIN = "course_time_min";
    private String COURSE_DAY = "course_day";
    private String COURSE_CLASS_START = "course_class_start";
    private String COURSE_CLASS_END = "course_class_end";

    public CourseInfoDB(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (_"
                + COURSEINFO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COURSE_NAME + " text," + COURSE_TIME_HOUR + " INTEGER,"
                + COURSE_TIME_MIN + " INTEGER," + COURSE_DAY + " INTEGER,"
                + COURSE_CLASS_START + " INTEGER," + COURSE_CLASS_END
                + " INTEGER);";
        Log.v("1111", "CourseInfoDB");
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

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            // System.out.println("          ");
            // System.out.println(cursor.getString(1));
        }

        return cursor;
    }

    public long insert(String course_name, int course_time_hour,
                       int course_time_min, int course_day, int course_class_start,
                       int course_class_end) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COURSE_NAME, course_name);
        cv.put(COURSE_TIME_HOUR, course_time_hour);
        cv.put(COURSE_TIME_MIN, course_time_min);
        cv.put(COURSE_DAY, course_day);
        cv.put(COURSE_CLASS_START, course_class_start);
        cv.put(COURSE_CLASS_END, course_class_end);
        long row = db.insert(TABLE_NAME, null, cv);
        return row;
    }

    public void delete(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = "_" + COURSEINFO_ID + "=?";
        String[] whereValue = {Integer.toString(id)};
        db.delete(TABLE_NAME, where, whereValue);
    }

    public void update(int courseinfo_id, String course_name,
                       int course_time_hour, int course_time_min, int course_day,
                       int course_class_start, int course_class_end) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = "_" + COURSEINFO_ID + " = ?";
        String[] whereValue = {Integer.toString(courseinfo_id)};

        ContentValues cv = new ContentValues();
        cv.put(COURSE_NAME, course_name);
        cv.put(COURSE_TIME_HOUR, course_time_hour);
        cv.put(COURSE_TIME_MIN, course_time_min);
        cv.put(COURSE_DAY, course_day);
        cv.put(COURSE_CLASS_START, course_class_start);
        cv.put(COURSE_CLASS_END, course_class_end);
        db.update(TABLE_NAME, cv, where, whereValue);
    }

    public Cursor selectByID(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " where " + COURSEINFO_ID
                + " = " + Integer.toString(id);
        Cursor cursor = db.rawQuery(sql, null);
        return cursor;
    }

    public Cursor selectByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " where " + COURSE_NAME
                + " = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{name});
        return cursor;
    }

    public Cursor selectByDay(int day) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " where " + COURSE_DAY
                + " = " + Integer.toString(day) + " order by "
                + COURSE_CLASS_START;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(sql, null);
        } catch (SQLException se) {
            this.onCreate(db);
        }
        cursor = db.rawQuery(sql, null);
        /*
         * for(int i=0;i<cursor.getCount();i++) { cursor.moveToPosition(i);
		 * System.out.println("           ");
		 * System.out.println(cursor.getInt(0));
		 * System.out.println(cursor.getString(1));
		 * System.out.println(cursor.getInt(2));
		 * System.out.println(cursor.getInt(3));
		 * System.out.println(cursor.getInt(4));
		 * System.out.println(cursor.getInt(5));
		 * System.out.println(cursor.getInt(6)); }
		 */
        return cursor;
    }

}
