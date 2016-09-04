package com.linqh.mobilesafe.db.dao;

import java.util.ArrayList;
import java.util.List;

import com.linqh.mobilesafe.db.BlackNumberDBopenHelper;
import com.linqh.mobilesafe.domain.BlackNumberInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 操作数据库黑名单，提供增删改查的方法
 * 
 * @author dz1
 *
 */

public class BlackNumberDao {
	private BlackNumberDBopenHelper helper;

	public BlackNumberDao(Context context) {
		helper = new BlackNumberDBopenHelper(context);
	}

	/**
	 * 添加黑名单用户
	 * 
	 * @param phone
	 *            电话号码
	 * @param mode
	 *            状态
	 * @return
	 */
	public boolean add(String phone, String mode) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("phone", phone);
		values.put("mode", mode);
		long result = db.insert("blacknumberinfo", null, values);
		db.close();
		if (result == -1) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 删除操作
	 * 
	 * @param phone
	 * @return
	 */
	public boolean delet(String phone) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("phone", phone);
		long result = db.delete("blacknumberinfo", "phone=?", new String[] { phone });
		db.close();
		if (result == 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 查找黑名单号码的拦截模式
	 * 
	 * @param phone
	 * @return
	 */
	public String find(String phone) {
		String mode = null;
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.query("blacknumberinfo", null, "phone=?", new String[] { phone }, null, null, null);
		while (cursor.moveToNext()) {
			mode = cursor.getString(cursor.getColumnIndex("mode"));
		}
		cursor.close();
		db.close();
		return mode;
	}

	/**
	 * 修改黑名单的拦截模式
	 * 
	 * @param phone
	 * @param newmode
	 * @return
	 */
	public boolean update(String phone, String newmode) {
		SQLiteDatabase db = helper.getReadableDatabase();
		ContentValues values = new ContentValues();
		values.put("phone", phone);
		values.put("mode", newmode);
		int result = db.update("blacknumberinfo", values, "phone=?", new String[] { phone });
		db.close();
		if (result == 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 返回全部的黑名单号码信息
	 * 
	 * @return
	 */
	public List<BlackNumberInfo> findall() {
		List<BlackNumberInfo> blackNumberInfos = new ArrayList<BlackNumberInfo>();
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query("blacknumberinfo", null, null, null, null, null, "_id desc");
		while (cursor.moveToNext()) {
			String phone;
			String mode;
			phone = cursor.getString(cursor.getColumnIndex("phone"));
			mode = cursor.getString(cursor.getColumnIndex("mode"));
			BlackNumberInfo info = new BlackNumberInfo();
			info.setPhone(phone);
			info.setMode(mode);
			blackNumberInfos.add(info);
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		cursor.close();
		db.close();
		return blackNumberInfos;
	}

	/**
	 * 分批加载listview
	 * @param maxCount 加载的数目
	 * @param startIndex 加载数据在list中的位置
	 * @return
	 */
	public List<BlackNumberInfo> findpart(int maxCount, int startIndex) {
		List<BlackNumberInfo> blackNumberInfos = new ArrayList<BlackNumberInfo>();
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select _id,phone,mode from blacknumberinfo order by _id desc limit ? offset ?",
				new String[] { String.valueOf(maxCount), String.valueOf(startIndex) });
		while (cursor.moveToNext()) {
			String phone;
			String mode;
			phone = cursor.getString(cursor.getColumnIndex("phone"));
			mode = cursor.getString(cursor.getColumnIndex("mode"));
			BlackNumberInfo info = new BlackNumberInfo();
			info.setPhone(phone);
			info.setMode(mode);
			blackNumberInfos.add(info);
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		cursor.close();
		db.close();
		return blackNumberInfos;
	}
	/**
	 * 分页加载listview
	 * @param maxCount 加载的数目
	 * @param startIndex 加载数据在list中的位置
	 * @return
	 */
	public List<BlackNumberInfo> findpaper(int paperNumber) {
		List<BlackNumberInfo> blackNumberInfos = new ArrayList<BlackNumberInfo>();
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select _id,phone,mode from blacknumberinfo order by _id desc limit ? offset ?",
				new String[] { String.valueOf(20), String.valueOf(20*paperNumber) });
		while (cursor.moveToNext()) {
			String phone;
			String mode;
			phone = cursor.getString(cursor.getColumnIndex("phone"));
			mode = cursor.getString(cursor.getColumnIndex("mode"));
			BlackNumberInfo info = new BlackNumberInfo();
			info.setPhone(phone);
			info.setMode(mode);
			blackNumberInfos.add(info);
			try {
				Thread.sleep(0);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		cursor.close();
		db.close();
		return blackNumberInfos;
	}
	/**
	 * 获取黑名单全部总条目
	 * @return
	 */
	public int getTotalCount() {
		List<BlackNumberInfo> blackNumberInfos = new ArrayList<BlackNumberInfo>();
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select count(*) from blacknumberinfo",null);
		cursor.moveToNext();
		int totalcount=cursor.getInt(0);
		cursor.close();
		db.close();
		return totalcount;
	}
}
