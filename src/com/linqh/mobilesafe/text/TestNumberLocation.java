package com.linqh.mobilesafe.text;

import com.linqh.mobilesafe.db.dao.NumberLocationDao;

import android.test.AndroidTestCase;

public class TestNumberLocation extends AndroidTestCase {
	
	public void getNumberLocation(){		
		String location=NumberLocationDao.getNumberAddress("13808013875");
		System.out.println("该电话的归属地为："+location);
	}
}
