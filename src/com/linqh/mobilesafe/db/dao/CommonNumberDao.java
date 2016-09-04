package com.linqh.mobilesafe.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CommonNumberDao {
	
	public static int getGroupCount(SQLiteDatabase db){
		Cursor cursor=db.rawQuery("select count(*) from classlist", null);
		cursor.moveToNext();
		int count=cursor.getInt(0);
		cursor.close();
		return count;
	}
	public static String getGroupName(SQLiteDatabase db,int groupPosition){
		groupPosition+=1;
		Cursor cursor=db.rawQuery("select name from classlist where idx=?",
					new String[]{String.valueOf(groupPosition)});
		cursor.moveToNext();
		String GroupName=cursor.getString(0);
		cursor.close();
		return GroupName;
	}
	public static int getChildCount(SQLiteDatabase db,int groupPosition){
		groupPosition+=1;
		String table="table"+String.valueOf(groupPosition);
		Cursor cursor=db.rawQuery("select count(*) from "+table, null);
		cursor.moveToNext();
		int count=cursor.getInt(0);
		cursor.close();
		return count;
	}
	public static String getChildName(SQLiteDatabase db,int groupPosition,int childPosition){
		groupPosition+=1;
		childPosition+=1;
		String table="table"+String.valueOf(groupPosition);
		Cursor cursor=db.rawQuery("select name,number from "+table+" where _id=?",
					new String[]{String.valueOf(childPosition)});
		cursor.moveToNext();
		String ChildName=cursor.getString(0);
		String Number=cursor.getString(1);
		cursor.close();
		return ChildName+"\n"+Number;
	}
}
