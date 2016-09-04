package com.linqh.mobilesafe.activities;

import java.util.ArrayList;
import java.util.List;

import com.linqh.mobilesafe.R;
import com.linqh.mobilesafe.domain.ProcessInfo;
import com.linqh.mobilesafe.engine.ProcessInfosTools;
import com.linqh.mobilesafe.engine.TaskInfoProvider;
import com.linqh.mobilesafe.utils.IntentUtils;
import com.linqh.mobilesafe.utils.ToastUtils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ProcessManageActivity extends Activity {
	private TextView tv_count;
	private TextView tv_using;
	private ListView lv_proinfo;
	private LinearLayout ll_loading;
	private TextView tv_total;

	//
	private long availmem;
	private long totalmem;
	private MyAdapter adapter;

	// 进程信息集合
	private List<ProcessInfo> processInfos;
	private List<ProcessInfo> userproInfos;
	private List<ProcessInfo> sysproInfos;
	private List<ProcessInfo> cloproInfos;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			ll_loading.setVisibility(View.INVISIBLE);
			adapter = new MyAdapter();
			lv_proinfo.setAdapter(adapter);
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_processmanage);
		tv_count = (TextView) findViewById(R.id.tv_count);
		tv_using = (TextView) findViewById(R.id.tv_using);
		lv_proinfo = (ListView) findViewById(R.id.lv_proinfo);
		ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
		tv_total = (TextView) findViewById(R.id.tv_total);
		// 创建userInfo与sysInfo对象
		userproInfos = new ArrayList<ProcessInfo>();
		sysproInfos = new ArrayList<ProcessInfo>();
		// 显示当前进程数，当前可以用内存和总内存
		int count = ProcessInfosTools.getProcessCount(ProcessManageActivity.this);
		tv_count.setText("当前进程：" + count + "个");
		availmem = ProcessInfosTools.getAvailRom(ProcessManageActivity.this);
		totalmem = ProcessInfosTools.getTotalRom(ProcessManageActivity.this);
		tv_using.setText("可用内存：" + Formatter.formatFileSize(ProcessManageActivity.this, availmem) + "/"
				+ Formatter.formatFileSize(ProcessManageActivity.this, totalmem));
		// UI显示，设置listview
		fillData();
		lv_proinfo.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if (firstVisibleItem <= userproInfos.size()) {
					tv_total.setText("用户进程：" + userproInfos.size() + "个");
				} else {
					tv_total.setText("系统进程：" + sysproInfos.size() + "个");
				}
			}
		});

		lv_proinfo.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (position == 0) {
					return;
				} else if (position == userproInfos.size() + 1) {
					return;
				} else if (position <= userproInfos.size()) {
					int location = position - 1;
					if (userproInfos.get(location).isChecked()) {
						userproInfos.get(location).setChecked(false);
						;
					} else {
						userproInfos.get(location).setChecked(true);
						;
					}
				} else {
					int location = position - userproInfos.size() - 2;
					if (sysproInfos.get(location).isChecked()) {
						sysproInfos.get(location).setChecked(false);
						;
					} else {
						sysproInfos.get(location).setChecked(true);
						;
					}
				}
				// 通知界面进行更新
				adapter.notifyDataSetChanged();
			}
		});
	}

	private void fillData() {
		ll_loading.setVisibility(View.VISIBLE);
		new Thread() {
			@Override
			public void run() {
				processInfos = TaskInfoProvider.getTaskInfoProvider(ProcessManageActivity.this);
				for (ProcessInfo processInfo : processInfos) {
					if (processInfo.isUsertask()) {
						userproInfos.add(processInfo);
					} else {
						sysproInfos.add(processInfo);
					}
				}
				handler.sendEmptyMessage(0);
			}
		}.start();
	}

	public class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			if (getSharedPreferences("config", MODE_PRIVATE).getBoolean("show_system", false)) {
				return userproInfos.size() + sysproInfos.size() + 2;
			} else {
				return userproInfos.size() + 1;
			}
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			ProcessInfo processInfo;
			if (position == 0) {
				TextView tv = new TextView(ProcessManageActivity.this);
				tv.setBackgroundColor(Color.GRAY);
				tv.setTextColor(Color.WHITE);
				tv.setText("用户进程：" + userproInfos.size() + "个");
				return tv;
			} else if (position == userproInfos.size() + 1) {
				TextView tv = new TextView(ProcessManageActivity.this);
				tv.setBackgroundColor(Color.GRAY);
				tv.setTextColor(Color.WHITE);
				tv.setText("系统进程：" + sysproInfos.size() + "个");
				return tv;
			} else if (position <= userproInfos.size()) {
				int location = position - 1;
				processInfo = userproInfos.get(location);
			} else {
				int location = position - userproInfos.size() - 2;
				processInfo = sysproInfos.get(location);
			}
			viewHold hold = null;
			if (convertView != null && convertView instanceof RelativeLayout) {
				view = convertView;
				hold = (viewHold) view.getTag();
			} else {
				hold = new viewHold();
				view = View.inflate(ProcessManageActivity.this, R.layout.item_proinfo, null);
				hold.iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
				hold.tv_packname = (TextView) view.findViewById(R.id.tv_packname);
				hold.tv_memsize = (TextView) view.findViewById(R.id.tv_memsize);
				hold.tv_store = (TextView) view.findViewById(R.id.tv_store);
				hold.cb = (CheckBox) view.findViewById(R.id.cb_uninstall);
				view.setTag(hold);
			}
			// 如果现实的自己的软件，将checkbox隐藏
			if (processInfo.getPackname().equals(getPackageName())) {
				hold.cb.setVisibility(View.INVISIBLE);
				hold.cb.setClickable(false);
			}
			hold.iv_icon.setImageDrawable(processInfo.getIcon());
			hold.tv_memsize.setText(Formatter.formatFileSize(ProcessManageActivity.this, processInfo.getMemsize()));
			hold.tv_packname.setText(processInfo.getPackname());
			if (processInfo.isUsertask()) {
				hold.tv_store.setText("用户进程");
			} else {
				hold.tv_store.setText("系统进程");
			}
			if (processInfo.isChecked()) {
				hold.cb.setChecked(true);
				;
			} else {
				hold.cb.setChecked(false);
			}
			return view;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}
	}

	class viewHold {
		public ImageView iv_icon;
		public TextView tv_packname;
		public TextView tv_memsize;
		public TextView tv_store;
		public CheckBox cb;
	}

	public void all(View view) {
		for (ProcessInfo processInfo : userproInfos) {
			if (processInfo.getPackname().equals(getPackageName())) {
				continue;
			}
			processInfo.setChecked(true);
		}
		for (ProcessInfo processInfo : sysproInfos) {
			processInfo.setChecked(true);
		}
		adapter.notifyDataSetChanged();
	}

	public void reverse(View view) {
		for (ProcessInfo processInfo : userproInfos) {
			if (processInfo.getPackname().equals(getPackageName())) {
				continue;
			}
			if (processInfo.isChecked()) {
				processInfo.setChecked(false);
			} else {
				processInfo.setChecked(true);
			}
		}
		for (ProcessInfo processInfo : sysproInfos) {
			if (processInfo.isChecked()) {
				processInfo.setChecked(false);
			} else {
				processInfo.setChecked(true);
			}
		}
		adapter.notifyDataSetChanged();
	}

	public void clean(View view) {
		int count = 0;
		long size = 0;
		cloproInfos = new ArrayList<ProcessInfo>();
		ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		for (ProcessInfo processInfo : userproInfos) {
			if (processInfo.isChecked()) {
				am.killBackgroundProcesses(processInfo.getPackname());
				size += processInfo.getMemsize();
				count++;
				cloproInfos.add(processInfo);
			}
		}
		for (ProcessInfo processInfo : sysproInfos) {
			if (processInfo.isChecked()) {
				am.killBackgroundProcesses(processInfo.getPackname());
				size += processInfo.getMemsize();
				count++;
				cloproInfos.add(processInfo);
			}
		}
		for (ProcessInfo processInfo : cloproInfos) {
			if (processInfo.isUsertask()) {
				userproInfos.remove(processInfo);
			} else {
				sysproInfos.remove(processInfo);
			}
		}
		tv_count.setText("当前进程：" + (userproInfos.size() + sysproInfos.size()) + "个");
		tv_using.setText("可用内存：" + Formatter.formatFileSize(ProcessManageActivity.this, availmem + size) + "/"
				+ Formatter.formatFileSize(ProcessManageActivity.this, totalmem));
		adapter.notifyDataSetChanged();
		ToastUtils.show(ProcessManageActivity.this,
				"关闭" + count + "个进程,释放" + Formatter.formatFileSize(ProcessManageActivity.this, size) + "内存");
	}

	public void setting(View view) {
		IntentUtils.StartActivity(this, ProcessSettingActivity.class);
	}

	@Override
	protected void onStart() {
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
		super.onStart();
	}
}
