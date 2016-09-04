package com.linqh.mobilesafe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BlackNumberDBopenHelper extends SQLiteOpenHelper {

	/**
	 * 创建应用程序数据库，名称为linqh.db
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 */

	public BlackNumberDBopenHelper(Context context) {
		super(context, "linqh.db", null, 1);
		// TODO Auto-generated constructor stub
	}
	//当数据库第一次创建时调用，表结构数据库
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table blacknumberinfo (_id integer primary key autoincrement,phone varchar(20),mode varvhar(2))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
