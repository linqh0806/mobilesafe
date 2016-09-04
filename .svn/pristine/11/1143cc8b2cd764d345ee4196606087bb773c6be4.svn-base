package com.linqh.mobilesafe.activities;


import com.linqh.mobilesafe.R;
import com.linqh.mobilesafe.utils.ToastUtils;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public abstract class SetupBasicActivity extends Activity {
	public SharedPreferences sp;
	public GestureDetector mGestureDeetctor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		sp=getSharedPreferences("config",MODE_PRIVATE);
		mGestureDeetctor=new GestureDetector(this,new GestureDetector.SimpleOnGestureListener(){

			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
				// TODO Auto-generated method stub
				if(Math.abs(e1.getRawY()-e2.getRawY())>200){
					ToastUtils.show(SetupBasicActivity.this, "操作无效");
					return true;
				}
				if((e1.getRawX()-e2.getRawX())>150){
					show_next();
					overridePendingTransition(R.anim.translat_next_in, R.anim.translat_next_out);
				}
				if((e2.getRawX()-e1.getRawX())>150){
					show_pre();
					overridePendingTransition(R.anim.translat_pre_in, R.anim.translat_pre_out);
				}
				return super.onFling(e1, e2, velocityX, velocityY);
			}
			
		});
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		mGestureDeetctor.onTouchEvent(event);
		return super.onTouchEvent(event);
	}
	
	public abstract void show_pre();
	public abstract void show_next();
	public void pre(View view){
		show_pre();
		overridePendingTransition(R.anim.translat_pre_in, R.anim.translat_pre_out);
	}
	public void next(View view){
		show_next();
		overridePendingTransition(R.anim.translat_next_in, R.anim.translat_next_out);
	}
}
