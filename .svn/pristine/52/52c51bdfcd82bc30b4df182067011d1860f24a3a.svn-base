package com.linqh.mobilesafe.activities;

import java.util.List;

import com.linqh.mobilesafe.R;
import com.linqh.mobilesafe.utils.ContactInfoUtils;
import com.linqh.mobilesafe.utils.ContactInfoUtils.ContactInfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ContactActivity extends Activity {
	private ListView lv_contact;
	private List<ContactInfo> infos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_contact);
		lv_contact = (ListView) findViewById(R.id.lv_contact);
		infos = ContactInfoUtils.getContactInfos(this);

		lv_contact.setAdapter(new ContactAdapter());
		lv_contact.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				String phone = infos.get(position).phone;
				Intent data = new Intent();
				data.putExtra("phone", phone);
				setResult(0,data);
				finish();
			}
		});
	}

	private class ContactAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return infos.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(getApplicationContext(), R.layout.item_contact, null);
			TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
			TextView tv_phone = (TextView) view.findViewById(R.id.tv_phone);
			TextView tv_email = (TextView) view.findViewById(R.id.tv_email);
			tv_name.setText(infos.get(position).name);
			tv_phone.setText(infos.get(position).phone);
			tv_email.setText(infos.get(position).email);
			return view;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

	}
}
