package com.linqh.mobilesafe.activities;

import java.util.List;

import com.linqh.mobilesafe.R;
import com.linqh.mobilesafe.db.dao.BlackNumberDao;
import com.linqh.mobilesafe.domain.BlackNumberInfo;
import com.linqh.mobilesafe.utils.ToastUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class CallSmsActivity extends Activity {
	private ListView lv_callsms;
	private BlackNumberDao dao;
	private List<BlackNumberInfo> blacknumberinfos;
	private Myadapter adapter;
	private LinearLayout ll_loading;
	private int startIndex = 0;
	private int	maxCount = 20;
	private int totalCount=0;
	private int paperNumber=0;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			ll_loading.setVisibility(View.INVISIBLE);
			if (adapter == null) {
				adapter = new Myadapter();
				lv_callsms.setAdapter(adapter);
			} else {
				adapter.notifyDataSetChanged();
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_callsms);
		lv_callsms = (ListView) findViewById(R.id.lv_callsms);
		ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
		dao = new BlackNumberDao(this);
		getdata();

		lv_callsms.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
				case OnScrollListener.SCROLL_STATE_IDLE://空闲不动
					int position=lv_callsms.getLastVisiblePosition();
					int size=blacknumberinfos.size();
					if(position==(size-1)){
						if(paperNumber>=dao.getTotalCount()/20){
							//ToastUtils.show(CallSmsActivity.this, "没有更多内容了");
						}else {
							System.out.println("开始向下翻页");
							//startIndex+=maxCount;
							paperNumber+=1;
							getdata();	
						}
					}
					if(paperNumber!=0&&lv_callsms.getFirstVisiblePosition()==0){
						System.out.println("开始向上翻页");
						//startIndex+=maxCount;
						paperNumber-=1;
						getdata();	
					}
					break;
				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL://滑动
					
					break;
				case OnScrollListener.SCROLL_STATE_FLING://自由滑动
					
					break;

				default:
					break;
				}
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				
			}
		});
	}

	private void getdata() {
		ll_loading.setVisibility(View.VISIBLE);
		new Thread() {
			public void run() {
//				if(blacknumberinfos==null){
//					blacknumberinfos = dao.findpart(maxCount, startIndex);
//				}else{
//					blacknumberinfos.addAll(dao.findpart(maxCount,startIndex));
//				}			
				blacknumberinfos=dao.findpaper(paperNumber);
				handler.sendEmptyMessage(0);
			};
		}.start();
	}

	public class Myadapter extends BaseAdapter {
		@Override
		public int getCount() {
			return blacknumberinfos.size();
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view = null;
			Viewholder viewholder;
			if (convertView == null) {
				viewholder = new Viewholder();
				view = View.inflate(getApplicationContext(), R.layout.item_callsms, null);
				viewholder.tv_phone = (TextView) view.findViewById(R.id.tv_phone);
				viewholder.tv_status = (TextView) view.findViewById(R.id.tv_status);
				viewholder.iv_delete_blacknumber = (ImageView) view.findViewById(R.id.iv_delete_blacknumber);
				view.setTag(viewholder);
			} else {
				view = convertView;
				viewholder = (Viewholder) view.getTag();
			}
			final String phone = blacknumberinfos.get(position).phone;
			final String mode = blacknumberinfos.get(position).mode;
			viewholder.tv_phone.setText(phone);
			if ("1".equals(mode)) {
				viewholder.tv_status.setText("电话拦截");
			} else if ("2".equals(mode)) {
				viewholder.tv_status.setText("短信拦截");
			} else {
				viewholder.tv_status.setText("全部拦截");
			}
			viewholder.iv_delete_blacknumber.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					AlertDialog.Builder builder = new Builder(CallSmsActivity.this);
					builder.setTitle("警告");
					builder.setMessage("确定删除这个黑名单号码么?");
					builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							boolean result = dao.delet(phone);
							if (result) {
								ToastUtils.show(CallSmsActivity.this, "删除成功");
								blacknumberinfos.remove(position);
								adapter.notifyDataSetChanged();
							} else {
								ToastUtils.show(CallSmsActivity.this, "删除失败");
							}
						}
					});
					builder.setNegativeButton("取消", null);
					builder.show();
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

	/**
	 * view的孩子的容器
	 * 
	 * @author dz1
	 *
	 */
	class Viewholder {
		TextView tv_phone;
		TextView tv_status;
		ImageView iv_delete_blacknumber;
	}

	/**
	 * 弹出添加黑名单对话框
	 * 
	 * @param view
	 */
	public void addBlacknumber(View view) {
		AlertDialog.Builder builder = new Builder(this);
		final View dialogview = View.inflate(getApplicationContext(), R.layout.dialog_addblacknumber, null);
		final AlertDialog dialog = builder.create();
		Button bt_ok = (Button) dialogview.findViewById(R.id.bt_ok);
		Button bt_cancel = (Button) dialogview.findViewById(R.id.bt_cancel);
		dialog.setView(dialogview, 0, 0, 0, 0);
		dialog.show();

		bt_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		bt_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String mode = "3";
				String phone;
				EditText et_phone = (EditText) dialogview.findViewById(R.id.et_phone);
				RadioGroup radioGroup = (RadioGroup) dialogview.findViewById(R.id.rg);
				RadioButton radioButton = (RadioButton) dialog.findViewById(radioGroup.getCheckedRadioButtonId());
				int rb_id = radioButton.getId();
				phone = et_phone.getText().toString();
				if (TextUtils.isEmpty(phone)) {
					ToastUtils.show(CallSmsActivity.this, "黑名单号码不能为空");
					return;
				}
				switch (rb_id) {
				case R.id.rb_phone:
					mode = "1";
					break;
				case R.id.rb_msg:
					mode = "2";
					break;
				case R.id.rb_all:
					mode = "3";
					break;
				default:
					break;
				}
				boolean result = dao.add(phone, mode);
				if (result) {
					ToastUtils.show(CallSmsActivity.this, "添加成功");
					BlackNumberInfo object = new BlackNumberInfo();
					object.setPhone(phone);
					object.setMode(mode);
					blacknumberinfos.add(0, object);
					adapter.notifyDataSetChanged();
				} else {
					ToastUtils.show(CallSmsActivity.this, "添加失败");
				}
				dialog.dismiss();
			}
		});
	}
}
