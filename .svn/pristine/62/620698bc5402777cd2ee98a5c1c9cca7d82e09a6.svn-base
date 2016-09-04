package com.linqh.mobilesafe.text;

import java.util.Random;

import com.linqh.mobilesafe.db.dao.BlackNumberDao;

import android.test.AndroidTestCase;

public class TestBlackNumberDao extends AndroidTestCase {
	BlackNumberDao dao;	
	@Override
	protected void setUp() throws Exception {
		dao  = new BlackNumberDao(getContext());
		super.setUp();
	}
	@Override
	protected void tearDown() throws Exception {
		dao  = null;
		super.tearDown();
	}
	//测试方法不接受返回值
	public void testAdd() throws Exception{
		Random random=new Random();
		long basenumber=12345600001l;
		for(int i=1;i<=100;i++){
			dao.add(String.valueOf(basenumber+i), String.valueOf(random.nextInt(3)+1));
		}
	}
	public void testDelet() throws Exception{
		boolean result=dao.delet("123456");
		assertEquals(true, result);
	}
	public void testUpdate() throws Exception{
		boolean result=dao.update("123456", "2");
		assertEquals(true, result);
	}
	public void testFind() throws Exception{
		String result=dao.find("123456");
		assertEquals("2", result);
	}

}
