package com.ustc.gcsj.doc;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

public class DBHelper {

    public static SQLiteDatabase db = null;
    String docDir = Environment.getExternalStorageDirectory().getPath();

    public DBHelper() {
        db = SQLiteDatabase.openOrCreateDatabase(docDir + "/filelist.db", null);
    }

    public SQLiteDatabase getOrCreateTable() {
        try {
            String sql = "create table file_info(id integer primary key ,"
                    + "docName varchar(255),docCourse varchar(255),"
                    + "docURL varchar(255))";
            db.execSQL(sql);
        } catch (SQLException se) {
            System.out.println("===table already exist!");
        }
        return db;
    }

    public void insertData(String docName, String docCourse, String docURL) {
        Cursor cursor;
        String sql1 = "select * from file_info where docURL = ?";
        cursor = db.rawQuery(sql1, new String[]{docURL});
        if (cursor.getCount() != 0)
            return;

        String sql = "insert into file_info values(null,?,?,?)";
        try {
            db.execSQL(sql, new String[]{docName, docCourse, docURL});
        } catch (SQLException se) {
            this.getOrCreateTable();
            this.insertData(docName, docCourse, docURL);
        }
        cursor.close();
    }

    public void deleteData(String docURL) {
        String sql = "delete from file_info where docURL = ?";
        try {
            db.execSQL(sql, new String[]{docURL});
        } catch (SQLException se) {
            this.getOrCreateTable();
            this.deleteData(docURL);
        }
    }

    public Cursor getData(String docCourse) {
        Cursor cursor = null;
        String sql;
        try {
            if (docCourse == null) {
                sql = "select docURL,docCourse from file_info";
                cursor = db.rawQuery(sql, null);
            } else {
                sql = "select docURL,docCourse from file_info where docCourse = ?";
                cursor = db.rawQuery(sql, new String[]{docCourse});
            }
        } catch (SQLException se) {
            this.getOrCreateTable();
            this.getData(docCourse);
        }
        return cursor;
    }
}
