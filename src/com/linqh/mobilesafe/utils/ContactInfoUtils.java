package com.linqh.mobilesafe.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class ContactInfoUtils {
	/**
	 * 
	 * @return
	 */
	public static List<ContactInfo> getContactInfos(Context context) {
		ContentResolver resolver = context.getContentResolver();
		List<ContactInfo> contactInfos = new ArrayList<ContactInfoUtils.ContactInfo>();
		Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
		Uri datauri = Uri.parse("content://com.android.contacts/data");
		Cursor cursor = resolver.query(uri, new String[] { "contact_id" }, null, null, null);
		while (cursor.moveToNext()) {
			String id = cursor.getString(0);
			if (id != null) {
				ContactInfo Info = new ContactInfoUtils().new ContactInfo();
				Cursor datacursor = resolver.query(datauri, new String[] { "data1", "mimetype" }, "raw_contact_id=?",
						new String[] { id }, null);
				while (datacursor.moveToNext()) {
					String data1 = datacursor.getString(0);
					String mimetype = datacursor.getString(1);
					System.out.println("data1:"+data1+",mimetype:"+mimetype);
					if (mimetype.equals("vnd.android.cursor.item/email_v2")) {
						Info.email = data1;
					} else if (mimetype.equals("vnd.android.cursor.item/phone_v2")) {
						Info.phone = data1;
					} else if (mimetype.equals("vnd.android.cursor.item/name")) {
						Info.name = data1;
					}
				}
				contactInfos.add(Info);
			}
		}
		return contactInfos;
	}

	public class ContactInfo {
		public String name;
		public String email;
		public String phone;
	}
}
