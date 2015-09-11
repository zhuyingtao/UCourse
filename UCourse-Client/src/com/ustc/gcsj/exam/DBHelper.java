package com.ustc.gcsj.exam;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

public class DBHelper {
	public static SQLiteDatabase db = null;
	String root = Environment.getExternalStorageDirectory().getPath();

	public DBHelper() {
		if (db == null) {
			db = SQLiteDatabase.openOrCreateDatabase(root + "/exam.db", null);
			createTable();
		}
	}

	public void createTable() {
		String sql = "create table if not exists exam_info(id integer primary key ,"
				+ "examName varchar(255),examPlace varchar(255),examTime varchar(255),"
				+ "examAlert varchar(255))";
		try {
			db.execSQL(sql);
		} catch (SQLException e) {
			System.out.println("出错了--> " + e.toString());
		}
	}

	public void insertData(String examName, String examPlace, String examTime,
			String examAlert) {
		createTable();
		Cursor cursor = null;
		String sql1 = "select * from exam_info where examName = ? and examTime = ?";
		cursor = db.rawQuery(sql1, new String[] { examName, examTime });
		if (cursor.getCount() != 0)
			return;

		String sql = "insert into exam_info values(null,?,?,?,?)";
		try {
			db.execSQL(sql, new String[] { examName, examPlace, examTime,
					examAlert });
		} catch (SQLException se) {
			se.printStackTrace();
		}
	}

	public void deleteData(String examName, String examTime) {
		createTable();
		String sql = "delete from exam_info where examName = ? and examTime = ? ";
		try {
			db.execSQL(sql, new String[] { examName, examTime });
		} catch (SQLException se) {
			se.printStackTrace();
		}
	}

	public Cursor getData() {
		createTable();
		Cursor cursor = null;
		String sql = null;
		try {
			sql = "select examName,examPlace,examTime,examAlert from exam_info";
			cursor = db.rawQuery(sql, null);
		} catch (SQLException se) {
			this.createTable();
			this.getData();
		}
		return cursor;
	}
}
