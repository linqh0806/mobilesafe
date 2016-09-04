package com.linqh.mobilesafe.service;

import com.linqh.mobilesafe.R;
import com.linqh.mobilesafe.db.dao.NumberLocationDao;
import com.linqh.mobilesafe.utils.ToastUtils;

import android.R.color;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.ContentObserver;
import android.graphics.PixelFormat;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.LayoutInflater.Filter;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class NumberLocationService extends Service {
	private TelephonyManager tm;
	private MyPhoneListener listener;
	private NumberOutLocationReceiver receiver;

	private WindowManager wm;
	private View view;
	
	private ImageView iv_toastbackground;
	private WindowManager.LayoutParams params;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		listener = new MyPhoneListener();
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);

		receiver = new NumberOutLocationReceiver();
		IntentFilter filter = new IntentFilter("android.intent.action.NEW_OUTGOING_CALL");
		registerReceiver(receiver, filter);
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		unregisterReceiver(receiver);
		receiver = null;
		super.onDestroy();
	}

	private class NumberOutLocationReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String outNumber = getResultData();
			String location = NumberLocationDao.getNumberAddress(outNumber);
			MyToast(location);
			view.setOnTouchListener(new OnTouchListener() {
				int startX;
				int startY;
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch(event.getAction()){
					case MotionEvent.ACTION_DOWN:
						startX=(int) event.getRawX();	
						startY=(int) event.getRawY();	
						break;
					case MotionEvent.ACTION_UP:
						SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
						Editor editor = sp.edit();
						editor.putInt("lastx", params.x);
						editor.putInt("lasty", params.y);
						editor.commit();
						break;
					case MotionEvent.ACTION_MOVE:
						int lastX=(int)event.getRawX();
						int lastY=(int)event.getRawY();
						int dx=lastX-startX;
						int dy=lastY-startY;
						params.x+=dx;
						params.y+=dy;
						if(params.x<0) params.x = 0;
						if(params.y<0) params.y = 0;
						if(params.y>wm.getDefaultDisplay().getHeight()-view.getHeight()){
							params.y=wm.getDefaultDisplay().getHeight()-view.getHeight();
						}
						if(params.x>wm.getDefaultDisplay().getWidth()-view.getWidth()){
							params.x=wm.getDefaultDisplay().getWidth()-view.getWidth();
						}
						wm.updateViewLayout(view, params);
						startX=(int)event.getRawX();
						startY=(int)event.getRawY();
						break;
					}
					return true;
				}
			});
		}

	}

	private void MyToast(String location) {
		int which = getSharedPreferences("config", MODE_PRIVATE).getInt("which", 0);
		int[] bgs=new int[]{R.drawable.call_locate_blue,R.drawable.call_locate_gray,R.drawable.call_locate_green,
				R.drawable.call_locate_orange,R.drawable.call_locate_white};
		wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		view = View.inflate(getApplicationContext(), R.layout.item_toast_location, null);
		view.setBackgroundResource(bgs[which]);
		TextView tv_toast_location=(TextView) view.findViewById(R.id.tv_toast_location);
		tv_toast_location.setText(location);
		params = new WindowManager.LayoutParams();
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.gravity=Gravity.LEFT+Gravity.TOP;
		SharedPreferences sp=getSharedPreferences("config", MODE_PRIVATE);
		params.x=sp.getInt("lastx", 0);
		params.y=sp.getInt("lasty", 0);
		params.format = PixelFormat.TRANSLUCENT;
		params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		wm.addView(view, params);
	}

	private class MyPhoneListener extends PhoneStateListener {
		@Override
		public void onCallStateChanged(int state, final String incomingNumber) {
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE:// 空闲
				if (view != null) {
					wm.removeView(view);
					view = null;
				}
				break;
			case TelephonyManager.CALL_STATE_RINGING:// 响铃
				String location = NumberLocationDao.getNumberAddress(incomingNumber);
				System.out.println("显示来电提醒");
				MyToast(location);
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:// 接通电话

				break;
			}
			super.onCallStateChanged(state, incomingNumber);
		}
	}

}
