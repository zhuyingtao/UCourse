package com.ustc.gcsj.note;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.ustc.gcsj.main.BaseDB;

public class NoteDB extends BaseDB {
    private String TABLE_NAME = "note";
    private String NOTE_ID = "note_id";
    private String NOTE_CLASSNAME = "note_classname";
    private String NOTE_NOTEDETAIL = "note_notedetail";
    private String NOTE_DATE = "note_date";

    public NoteDB(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + NOTE_ID + " INTEGER PRIMARY KEY autoincrement,"
                + NOTE_CLASSNAME + " text," + NOTE_NOTEDETAIL + " text,"
                + NOTE_DATE + " text);";
        try {

            db.execSQL(sql);
        } catch (SQLException se) {
            db.execSQL(sql);
        }

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        String sql = "DROP TABLE IF EXISTS" + TABLE_NAME;
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
            cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        }
        return cursor;

    }

    public long insert(String nclassname, String notedetail, String notedate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NOTE_CLASSNAME, nclassname);
        cv.put(NOTE_NOTEDETAIL, notedetail);
        cv.put(NOTE_DATE, notedate);
        long row = db.insert(TABLE_NAME, null, cv);
        return row;

        // }
    }

    public void delete(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = NOTE_ID + "=?";
        String[] whereValue = {Integer.toString(id)};
        db.delete(TABLE_NAME, where, whereValue);
        db.close();
    }

    public void update(int id, String nclassname, String notedetail,
                       String notedate) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = NOTE_ID + " = ?";
        String[] whereValue = {Integer.toString(id)};
        ContentValues cv = new ContentValues();
        cv.put(NOTE_CLASSNAME, nclassname);
        cv.put(NOTE_NOTEDETAIL, notedetail);
        cv.put(NOTE_DATE, notedate);
        db.update(TABLE_NAME, cv, where, whereValue);

    }

    public Cursor selectbyClassName(String className) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, "note_classname=?",
                new String[]{className}, null, null, null);

        return cursor;
    }

    public Cursor selectclasslist() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{"note_classname"},
                null, null, "note_classname", null, null);
        /*
         * while(cursor.moveToNext()){ String note_classname =
		 * cursor.getString(cursor.getColumnIndex("note_classname")); //String
		 * note_notedetail =
		 * cursor.getString(cursor.getColumnIndex("note_notedetail")); //String
		 * note_date = cursor.getString(cursor.getColumnIndex("note_date"));
		 * System.out.println("query------->" + "课程名："+note_classname); }
		 */
        return cursor;

    }

}
