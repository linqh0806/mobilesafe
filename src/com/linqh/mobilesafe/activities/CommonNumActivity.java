package com.linqh.mobilesafe.activities;


import com.linqh.mobilesafe.R;
import com.linqh.mobilesafe.db.dao.CommonNumberDao;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

public class CommonNumActivity extends Activity {
	private ExpandableListView elv_commonnum;
	public SQLiteDatabase db;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_commonnum);
		db=SQLiteDatabase.openDatabase("/data/data/com.linqh.mobilesafe/files/commonnum.db",
				null, SQLiteDatabase.OPEN_READONLY);
		elv_commonnum=(ExpandableListView) findViewById(R.id.elv_commonnum);
		elv_commonnum.setAdapter(new MyAdapter());
	}
	
	@Override
	protected void onDestroy() {
		db.close();
		super.onDestroy();
	}

	public class MyAdapter extends BaseExpandableListAdapter{
		@Override
		public int getGroupCount() {
			return CommonNumberDao.getGroupCount(db);
		}
		@Override
		public int getChildrenCount(int groupPosition) {
			return CommonNumberDao.getChildCount(db,groupPosition);
		}
		@Override
		public Object getGroup(int groupPosition) {
			return null;
		}
		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return null;
		}
		@Override
		public long getGroupId(int groupPosition) {
			return 0;
		}
		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return 0;
		}
		@Override
		public boolean hasStableIds() {
			return false;
		}
		@Override
		public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
			TextView tv;
			if(convertView==null){
				tv=new TextView(getApplicationContext());
			}else {
				tv=(TextView) convertView;
			}
			tv.setTextColor(Color.BLACK);
			tv.setTextSize(20);
			tv.setText("      "+CommonNumberDao.getGroupName(db,groupPosition));
			return tv;
		}
		@Override
		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
				ViewGroup parent) {
			TextView tv;
			if(convertView==null){
				tv=new TextView(getApplicationContext());
			}else{
				tv=(TextView) convertView;
			}
			tv.setTextColor(Color.GRAY);
			tv.setTextSize(18);
			tv.setText(CommonNumberDao.getChildName(db,groupPosition, childPosition));
			return tv;
		}
		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return false;
		}
		
	}
}
