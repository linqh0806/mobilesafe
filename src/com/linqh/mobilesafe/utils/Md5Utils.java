package com.linqh.mobilesafe.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Utils {

	/**
	 * 加密字符串 采用md5算法
	 * @param text
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String encode(String text){
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("md5");
			String password="123456";
			byte[] result=digest.digest(password.getBytes());
			StringBuffer sb=new StringBuffer();
			for(byte b:result){
				String hex=Integer.toHexString(b&0xff)+0; //加盐操作
				if(hex.length()==1){
					sb.append("0");
				}
				sb.append(hex);
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";//can't reach
		}
		
	}

}
