package com.linqh.mobilesafe.activities;

import com.linqh.mobilesafe.R;
import com.linqh.mobilesafe.utils.IntentUtils;
import com.linqh.mobilesafe.utils.Md5Utils;
import com.linqh.mobilesafe.utils.ToastUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeActivity extends Activity {
	private GridView gv_home;
	private SharedPreferences sp;
	private String[] names = { "手机防盗", "通讯卫士", "软件管理", "进程管理", "流量统计", "手机杀毒", "缓存清理", "高级工具", "设置中心" };
	private int[] icons = { R.drawable.safe, R.drawable.callmsgsafe, R.drawable.app_selector, R.drawable.taskmanager,
			R.drawable.netmanager, R.drawable.trojan, R.drawable.sysoptimize, R.drawable.atools, R.drawable.settings };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		gv_home = (GridView) findViewById(R.id.gv_home);
		gv_home.setAdapter(new HomeAddapter());
		gv_home.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {
				case 0:// 手机防盗
					String password = sp.getString("password", null);
					if (TextUtils.isEmpty(password)) {
						// 显示设置密码对话框
						showSetPasswordDialog();
					} else {
						// 显示输入密码对话框
						showPassWordDialog();
					}
					break;
				case 1:// 通讯卫士
					IntentUtils.StartActivity(HomeActivity.this, CallSmsActivity.class);
					break;
				case 2:// 软件管理
					IntentUtils.StartActivity(HomeActivity.this, AppManageActivity.class);
					break;
				case 3:// 进程管理
					IntentUtils.StartActivity(HomeActivity.this, ProcessManageActivity.class);
					break;
				case 7:// 高级工具
					IntentUtils.StartActivity(HomeActivity.this, SuperToolsActivity.class);
					break;
				case 8:// 设置中心
					IntentUtils.StartActivity(HomeActivity.this, SettingActivity.class);
					break;
				}
			}
		});
	}

	/**
	 * 输入密码对话框
	 */
	protected void showPassWordDialog() {
		builder = new Builder(this);
		view = View.inflate(HomeActivity.this, R.layout.dialog_password, null);
		builder.setView(view);
		et_password = (EditText) view.findViewById(R.id.et_password);
		bt_ok = (Button) view.findViewById(R.id.bt_ok);
		bt_cancel = (Button) view.findViewById(R.id.bt_cancel);
		bt_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		bt_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String password = et_password.getText().toString().trim();
				String savedpasswod=sp.getString("password", null);
				if(TextUtils.isEmpty(password)){
					ToastUtils.show(HomeActivity.this,"密码不能为空");
					return;
				}
				if(!savedpasswod.equals(Md5Utils.encode(password))){
					ToastUtils.show(HomeActivity.this,"密码输入错误");
					return;
				}
				ToastUtils.show(HomeActivity.this,"密码输入正确");
				dialog.dismiss();
				//判断用户是否完成	向导设置
				if(sp.getBoolean("finishsetup",false)){
					//进入防盗系统UI界面
					IntentUtils.StartActivityAndFinish(HomeActivity.this, LostFindActivity.class);
				}else {
					//进入设置向导界面
					IntentUtils.StartActivity(HomeActivity.this, Setup1Activity.class);
				}
			}
		});
		dialog = builder.show();
	}

	//享元模式
	private AlertDialog dialog;
	private AlertDialog.Builder builder;
	private View view;
	private EditText et_password;
	private Button bt_ok;
	private Button bt_cancel;

	/**
	 * 设置密码对话框
	 */
	protected void showSetPasswordDialog() {
		builder = new Builder(this);
		view = View.inflate(HomeActivity.this, R.layout.dialog_setpassword, null);
		builder.setView(view);
		et_password = (EditText) view.findViewById(R.id.et_password);
		final EditText et_confirmpassword = (EditText) view.findViewById(R.id.et_confirmpassword);
		bt_ok = (Button) view.findViewById(R.id.bt_ok);
		bt_cancel = (Button) view.findViewById(R.id.bt_cancel);
		bt_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		bt_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String password = et_password.getText().toString();
				String confirmpassword = et_confirmpassword.getText().toString();
				if (TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmpassword)) {
					ToastUtils.show(HomeActivity.this, "密码不能为空");
					return;
				}
				if (!password.equals(confirmpassword)) {
					ToastUtils.show(HomeActivity.this, "密码输入不一致");
					return;
				}
				ToastUtils.show(HomeActivity.this, "密码设置成功");
				Editor editor=sp.edit();
				editor.putString("password",Md5Utils.encode(password));
				editor.commit();
				dialog.dismiss();
				showPassWordDialog();
			}
		});

		dialog = builder.show();
	}

	private class HomeAddapter extends BaseAdapter {

		@Override
		public int getCount() {
			return names.length;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			if (convertView == null) {
				view = View.inflate(getApplicationContext(), R.layout.item_home, null);
			} else {
				view = convertView;
			}
			ImageView iv_item_icon = (ImageView) view.findViewById(R.id.iv_homeitem_icon);
			TextView tv_item_name = (TextView) view.findViewById(R.id.iv_homeitem_name);
			iv_item_icon.setImageResource(icons[position]);
			tv_item_name.setText(names[position]);
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
