package com.linqh.mobilesafe.engine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import android.R.xml;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore.Images.Thumbnails;
import android.text.TextDirectionHeuristic;
import android.util.Xml;
import android.view.inputmethod.InputBinding;

public class SmsTools {

	/**
	 * 声明一个接口 ， 包含一些回调函数
	 * 
	 * @author 小b
	 *
	 */
	public interface BackupSmsCallBack {
		/**
		 * 短信备份之前调用的方法
		 * 
		 * @param max
		 *            短信的总条目个数
		 */
		public void beforeSmsBackup(int max);

		/**
		 * 当短信备份过程中调用的方法
		 * 
		 * @param process
		 *            当前备份的进度
		 */
		public void onSmsBackup(int process);

	}

	public static boolean smsReduction(Context context, BackupSmsCallBack callBack, String filename) {
		File file = new File(context.getFilesDir(), filename);
		String road = file.getAbsolutePath().toString();
		System.out.println("----------------------road:" + road);
		ContentResolver resolver = context.getContentResolver();
		Uri uri = Uri.parse("content://sms");
		ContentValues values = null;
		XmlPullParser xp = Xml.newPullParser();
		try {
			InputStream is = new FileInputStream(file);
			xp.setInput(is, "utf-8");
			int type = xp.getEventType();
			int process=0;
			while (type != XmlPullParser.END_DOCUMENT) {
				switch (type) {
				case XmlPullParser.START_TAG:// 获取当前节点
					if ("infos".equals(xp.getName())) {
						int max=Integer.parseInt(xp.getAttributeValue(null, "total"));
						callBack.beforeSmsBackup(max);
						System.out.println("---------------------------------1");
					} else if ("sms".equals(xp.getName())) {
						values = new ContentValues();
						System.out.println("---------------------------------2");
					} else if ("address".equals(xp.getName())) {
						values.put("address", xp.nextText());
						System.out.println("---------------------------------3");
					} else if ("date".equals(xp.getName())) {
						values.put("date", xp.nextText());
						System.out.println("---------------------------------4");
					} else if ("body".equals(xp.getName())) {
						values.put("body", xp.nextText());
						System.out.println("---------------------------------5");
					} else if ("type".equals(xp.getName())) {
						values.put("type", xp.nextText());
						System.out.println("---------------------------------6");
					}
					break;
				case XmlPullParser.END_TAG:
					if ("sms".equals(xp.getName())) {
						Thread.sleep(900);
						process++;
						callBack.onSmsBackup(process);
						resolver.insert(uri, values);
						System.out.println("---------------------------------7");
					}
					break;
				}
				// System.out.println("address:" + address + ",date:" + date +
				// ",body:" + body);
				type = xp.next();
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	public static boolean smsBackup(Context context, BackupSmsCallBack callBack, String filename) {
		try {
			ContentResolver resolver = context.getContentResolver();
			Uri uri = Uri.parse("content://sms");
			XmlSerializer serializer = Xml.newSerializer();
			File file = new File(context.getFilesDir(), filename);
			FileOutputStream fos = new FileOutputStream(file);
			serializer.setOutput(fos, "utf-8");
			serializer.startDocument("utf-8", true);
			serializer.startTag(null, "infos");
			Cursor cursor = resolver.query(uri, new String[] { "address", "date", "body", "type" }, null, null, null);
			int max = cursor.getCount();
			serializer.attribute(null, "total", String.valueOf(max));
			callBack.beforeSmsBackup(max);
			int process = 0;
			while (cursor.moveToNext()) {
				serializer.startTag(null, "sms");

				serializer.startTag(null, "address");
				String address = cursor.getString(0);
				serializer.text(address);
				serializer.endTag(null, "address");

				serializer.startTag(null, "date");
				String date = cursor.getString(1);
				serializer.text(date);
				serializer.endTag(null, "date");

				serializer.startTag(null, "body");
				String body = cursor.getString(2);
				serializer.text(body);
				serializer.endTag(null, "body");

				serializer.startTag(null, "type");
				String type = cursor.getString(3);
				serializer.text(type);
				serializer.endTag(null, "type");

				serializer.endTag(null, "sms");
				Thread.sleep(900);
				process++;
				callBack.onSmsBackup(process);
				System.out.println(address + "  " + body + "  " + type);
			}
			cursor.close();
			serializer.endTag(null, "infos");
			serializer.endDocument();
			fos.close();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}
}
