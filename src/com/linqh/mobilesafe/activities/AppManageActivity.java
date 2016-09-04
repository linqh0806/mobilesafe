package com.linqh.mobilesafe.activities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import java.util.logging.LogRecord;

import org.w3c.dom.Text;

import com.linqh.mobilesafe.R;
import com.linqh.mobilesafe.activities.CallSmsActivity.Myadapter;
import com.linqh.mobilesafe.domain.AppInfo;
import com.linqh.mobilesafe.engine.AppInfosTools;
import com.linqh.mobilesafe.utils.ToastUtils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AppManageActivity extends Activity implements OnClickListener {
	private ListView lv_appinfo;
	private LinearLayout ll_loading;
	private MyAdapter adapter;
	private TextView tv_phone;
	private TextView tv_sd;
	private TextView tv_total;

	// 声明泡泡对话框
	private PopupWindow popupWindow;

	// 被点击的条目信息
	private AppInfo appInfo;

	// 定义删除程序的广播接受者
	private uninstallReceiver receiver;

	private List<AppInfo> allAppInfos;
	private List<AppInfo> userAppInfos;
	private List<AppInfo> systemAppInfos;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			ll_loading.setVisibility(View.INVISIBLE);
				adapter = new MyAdapter();
				lv_appinfo.setAdapter(adapter);
		};
	};

	@Override
	protected void onDestroy() {
		unregisterReceiver(receiver);
		super.onDestroy();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appmanage);
		// 注册广播接受者
		receiver = new uninstallReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_INSTALL_PACKAGE);
		registerReceiver(receiver, filter);
		// 查找资源ID
		lv_appinfo = (ListView) findViewById(R.id.lv_appinfo);
		ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
		tv_phone = (TextView) findViewById(R.id.tv_phone);
		tv_sd = (TextView) findViewById(R.id.tv_sd);
		tv_total = (TextView) findViewById(R.id.tv_total);
		File phonefile = Environment.getDataDirectory();
		File sdfile = Environment.getExternalStorageDirectory();
		long phonesize = phonefile.getFreeSpace();
		long sdsize = sdfile.getFreeSpace();
		tv_phone.setText("手机内存：" + Formatter.formatFileSize(AppManageActivity.this, phonesize));
		tv_sd.setText("SD存储：" + Formatter.formatFileSize(AppManageActivity.this, sdsize));
		getData();
		lv_appinfo.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if (userAppInfos != null && systemAppInfos != null) {
					if (firstVisibleItem <= userAppInfos.size()) {
						tv_total.setText("用户程序：" + userAppInfos.size() + "个");
					} else {
						tv_total.setText("系统程序：" + systemAppInfos.size() + "个");
					}
				} else {
					System.out.println("数据正在填充......");
				}
				dismissPopupWindow();
			}
		});
		lv_appinfo.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (position == 0) {
					return;
				} else if (position == userAppInfos.size() + 1) {
					return;
				} else if (position <= userAppInfos.size()) {
					int location = position - 1;
					appInfo = userAppInfos.get(location);
				} else {
					int location = position - userAppInfos.size() - 2;
					appInfo = systemAppInfos.get(location);
				}
				System.out.println("我点了：" + appInfo.getAppname());
				View contentView = View.inflate(getApplicationContext(), R.layout.item_popupwindow, null);
				popupWindow = new PopupWindow(contentView, -2, -2);
				popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
				int[] location = new int[2];
				view.getLocationOnScreen(location);
				popupWindow.showAtLocation(parent, Gravity.LEFT | Gravity.TOP, 200, location[1]);
				AlphaAnimation am = new AlphaAnimation(0.2f, 1.0f);
				am.setDuration(300);
				ScaleAnimation sm = new ScaleAnimation(0.2f, 1.0f, 0.2f, 1.0f, Animation.RELATIVE_TO_SELF, 0,
						Animation.RELATIVE_TO_SELF, 0.5f);
				sm.setDuration(300);
				AnimationSet animationSet = new AnimationSet(false);
				animationSet.addAnimation(am);
				animationSet.addAnimation(sm);
				contentView.startAnimation(animationSet);

				LinearLayout ll_uninstall = (LinearLayout) contentView.findViewById(R.id.ll_uninstall);
				ll_uninstall.setOnClickListener(AppManageActivity.this);
				LinearLayout ll_share = (LinearLayout) contentView.findViewById(R.id.ll_share);
				ll_share.setOnClickListener(AppManageActivity.this);
				LinearLayout ll_open = (LinearLayout) contentView.findViewById(R.id.ll_open);
				ll_open.setOnClickListener(AppManageActivity.this);
				LinearLayout ll_detail = (LinearLayout) contentView.findViewById(R.id.ll_detail);
				ll_detail.setOnClickListener(AppManageActivity.this);
			}
		});
	}

	protected void dismissPopupWindow() {
		if (popupWindow != null && popupWindow.isShowing()) {
			popupWindow.dismiss();
			popupWindow = null;
		}
	}

	private void getData() {
		ll_loading.setVisibility(View.VISIBLE);
		new Thread() {
			public void run() {
				allAppInfos = AppInfosTools.getAppInfos(AppManageActivity.this);
				userAppInfos = new ArrayList<AppInfo>();
				systemAppInfos = new ArrayList<AppInfo>();
				for (AppInfo appInfo : allAppInfos) {
					if (appInfo.isUserApp()) {
						userAppInfos.add(appInfo);
					} else {
						systemAppInfos.add(appInfo);
					}
				}
				handler.sendEmptyMessage(0);
			};
		}.start();
	}

	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// return allAppInfos.size();
			return userAppInfos.size() + systemAppInfos.size() + 2;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (position == 0) {
				TextView tv = new TextView(getApplicationContext());
				tv.setBackgroundColor(Color.GRAY);
				tv.setTextColor(Color.WHITE);
				tv.setText("用户程序：" + userAppInfos.size() + "个");
				return tv;
			} else if (position == userAppInfos.size() + 1) {
				TextView tv = new TextView(getApplicationContext());
				tv.setBackgroundColor(Color.GRAY);
				tv.setTextColor(Color.WHITE);
				tv.setText("系统程序：" + systemAppInfos.size() + "个");
				return tv;
			} else if (position <= userAppInfos.size()) {
				int location = position - 1;
				appInfo = userAppInfos.get(location);
			} else {
				int location = position - userAppInfos.size() - 2;
				appInfo = systemAppInfos.get(location);
			}
			View view = null;
			String appname;
			String size;
			final String packname;
			viewHold hold = null;
			if (convertView != null && convertView instanceof RelativeLayout) {
				view = convertView;
				hold = (viewHold) view.getTag();
			} else {
				hold = new viewHold();
				view = View.inflate(getApplicationContext(), R.layout.item_appinfo, null);
				hold.tv_appname = (TextView) view.findViewById(R.id.tv_appname);
				hold.tv_store = (TextView) view.findViewById(R.id.tv_store);
				hold.tv_size = (TextView) view.findViewById(R.id.tv_size);
				hold.iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
				hold.bt_uninstall = (Button) view.findViewById(R.id.bt_uninstall);
				view.setTag(hold);
			}
			appname = appInfo.getAppname();
			size = Formatter.formatFileSize(getApplicationContext(), appInfo.getApksize());
			hold.tv_appname.setText(appname);
			hold.tv_size.setText(size);
			hold.iv_icon.setImageDrawable(appInfo.getIcon());
			if (appInfo.isInRom()) {
				hold.tv_store.setText("手机内存");
			} else {
				hold.tv_store.setText("SD存储");
			}

			packname = appInfo.getPackname();
			hold.bt_uninstall.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// 卸载软件
					// <intent-filter>
					// <action android:name="android.intent.action.DELETE" />
					// <action
					// android:name="android.intent.action.UNINSTALL_PACKAGE" />
					// <category android:name="android.intent.category.DEFAULT"
					// />
					// <data android:scheme="package" />
					// </intent-filter>
					Intent intent = new Intent();
					intent.setAction(Intent.ACTION_DELETE);
					intent.addCategory("android.intent.category.DEFAULT");
					intent.setData(Uri.parse("package:" + packname));
					startActivity(intent);
				}
			});
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

	public class viewHold {
		public TextView tv_appname;
		public TextView tv_store;
		public TextView tv_size;
		public ImageView iv_icon;
		public Button bt_uninstall;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_open:
			startApplicaition();
			break;
		case R.id.ll_detail:
			detailApplication();
			System.out.println("详细：" + appInfo.getPackname());
			break;
		case R.id.ll_share:
			shareApplication();
			System.out.println("分享：" + appInfo.getAppname());
			break;
		case R.id.ll_uninstall:
			uninstallApplication();
			break;
		default:
			break;
		}
		dismissPopupWindow();
	}

	private void detailApplication() {
//		<intent-filter>
//        <action android:name="android.settings.APPLICATION_DETAILS_SETTINGS" />
//        <category android:name="android.intent.category.DEFAULT" />
//        <data android:scheme="package" />
//    </intent-filter>
		Intent intent=new Intent();
		intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setData(Uri.parse("package:"+appInfo.getPackname()));
		startActivity(intent);
	}

	private void shareApplication() {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.SEND");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT, "推荐你使用一款软件,软件的名称为:" + appInfo.getAppname() + ",我用的很爽.");
		startActivity(intent);
	}

	private void uninstallApplication() {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_DELETE);
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setData(Uri.parse("package:" + appInfo.getPackname()));
		startActivity(intent);
		System.out.println("卸载：" + appInfo.getAppname());
	}

	private void startApplicaition() {
		PackageManager pm = getPackageManager();
		Intent intent = pm.getLaunchIntentForPackage(appInfo.getPackname());
		if (intent != null) {
			startActivity(intent);
		} else {
			ToastUtils.show(this, "当前应用程序无法启动");
		}
	}

	private class uninstallReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (appInfo.isUserApp()) {
				userAppInfos.remove(appInfo);
			} else {
				systemAppInfos.remove(appInfo);
			}
			adapter.notifyDataSetChanged();
		}

	}

}
