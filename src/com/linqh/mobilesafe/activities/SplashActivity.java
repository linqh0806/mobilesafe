package com.linqh.mobilesafe.activities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.linqh.mobilesafe.R;
import com.linqh.mobilesafe.utils.AppInfoUtils;
import com.linqh.mobilesafe.utils.IntentUtils;
import com.linqh.mobilesafe.utils.StreamUtils;
import com.linqh.mobilesafe.utils.ToastUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SplashActivity extends Activity {
	private static final int NEED_UPDATE = 1;
	private TextView tv_splash_version;
	private RelativeLayout rl_splash_root;
	private TextView tv_splash_download;

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case NEED_UPDATE:
				showUpdateDialog(msg);
				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		tv_splash_version = (TextView) findViewById(R.id.tv_splash_version);
		tv_splash_version.setText(AppInfoUtils.getAppVersionName(this));
		rl_splash_root = (RelativeLayout) findViewById(R.id.rl_splash_root);
		tv_splash_download = (TextView) findViewById(R.id.tv_splash_download);
		AlphaAnimation aa = new AlphaAnimation(0.2f, 1.0f);
		aa.setDuration(2000);
		rl_splash_root.startAnimation(aa);
		// 读取配置文件
		SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
		// 检测更新
		if (sp.getBoolean("update", false)) {
			checkVersion();
		} else {
			IntentUtils.StartActivityForDelayAndFinish(SplashActivity.this, HomeActivity.class, 2000);
		}
		// 拷贝资产文件到手机中
		copyAssets("address.db");
		copyAssets("commonnum.db");
	}

	private void copyAssets(String databaseName) {
		File file = new File(getFilesDir(), databaseName);
		AssetManager am = getAssets();
		if (file.exists() && file.length() > 0) {
			System.out.println("数据已存在，不需要拷贝");
		} else {
			System.out.println("正在拷贝数据中");
			try {
				InputStream is = am.open(databaseName);
				FileOutputStream fos = new FileOutputStream(file);
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = is.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
				}
				fos.close();
				is.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 连接服务器检查版本进行更新
	 */
	private void checkVersion() {
		new Thread() {
			public void run() {
				try {
					URL url = new URL(getString(R.string.serverurl));
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(5000);
					if (conn.getResponseCode() == 200) {
						InputStream is = conn.getInputStream();
						String json = StreamUtils.readStream(is);
						JSONObject jsonObj = new JSONObject(json);
						UpdateInfo info = new UpdateInfo();
						int severVersion = jsonObj.getInt("version");
						info.serverVersion = severVersion;
						String downloadurl = jsonObj.getString("downloadurl");
						info.downloadurl = downloadurl;
						String desc = jsonObj.getString("desc");
						info.desc = desc;

						if (severVersion > AppInfoUtils.getAppVersionCode(getApplicationContext())) {
							System.out.println("有新的版本号，需要升级");
							Message msg = Message.obtain();
							msg.obj = info;
							msg.what = NEED_UPDATE;
							handler.sendMessageDelayed(msg, 2000);
						} else {
							System.out.println("版本号一致，进入应用程序主界面");
							IntentUtils.StartActivityForDelayAndFinish(SplashActivity.this, HomeActivity.class, 2000);
						}
						// System.out.println("version:" + sererVersion);
						// System.out.println("downloadurl:" + downloadurl);
						// System.out.println("desc:" + desc);
					}
				} catch (MalformedURLException e) {// url路径不合法
					// TODO Auto-generated catch block
					e.printStackTrace();
					ToastUtils.show(SplashActivity.this, "URL路径错误");
					IntentUtils.StartActivityForDelayAndFinish(SplashActivity.this, HomeActivity.class, 2000);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					ToastUtils.show(SplashActivity.this, "网络异常");
					IntentUtils.StartActivityForDelayAndFinish(SplashActivity.this, HomeActivity.class, 2000);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					ToastUtils.show(SplashActivity.this, "服务器端配置文件错误");
					IntentUtils.StartActivityForDelayAndFinish(SplashActivity.this, HomeActivity.class, 2000);
				}
			};
		}.start();
	}

	/**
	 * 显示提醒升级对话框
	 * 
	 * @param msg
	 */
	private void showUpdateDialog(android.os.Message msg) {
		final UpdateInfo info = (UpdateInfo) msg.obj;
		String desc = info.desc;
		AlertDialog.Builder builder = new Builder(SplashActivity.this);
		builder.setTitle("升级提醒");
		builder.setMessage(desc);
		builder.setNegativeButton("稍后再说", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				IntentUtils.StartActivityAndFinish(SplashActivity.this, HomeActivity.class);
			}
		});
		builder.setPositiveButton("立刻升级", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				HttpUtils httpUtils = new HttpUtils();
				final File file = new File(Environment.getExternalStorageDirectory(), "xx.apk");
				httpUtils.download(info.downloadurl, file.getAbsolutePath(), new RequestCallBack<File>() {

					@Override
					public void onLoading(long total, long current, boolean isUploading) {
						// TODO Auto-generated method stub
						super.onLoading(total, current, isUploading);
						tv_splash_download.setText(current + "/" + total);
					}

					@Override
					public void onSuccess(ResponseInfo<File> arg0) {
						// TODO Auto-generated method stub
						ToastUtils.show(SplashActivity.this, "下载成功");
						// <action android:name="android.intent.action.VIEW" />
						// <category
						// android:name="android.intent.category.DEFAULT" />
						// <data android:scheme="content" />
						// <data android:scheme="file" />
						// <data
						// android:mimeType="application/vnd.android.package-archive"
						// />
						Intent intent = new Intent();
						intent.setAction("android.intent.action.VIEW");
						intent.addCategory("android.intent.category.DEFAULT");
						intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
						startActivity(intent);
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						ToastUtils.show(SplashActivity.this, "下载失败");
					}
				});
			}
		});
		builder.show();
	}

	class UpdateInfo {
		int serverVersion;
		String downloadurl;
		String desc;
	}
}
