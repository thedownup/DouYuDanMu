package com.douyu.until;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.xalan.xsltc.compiler.sym;

/**
 * zjt 2018年1月7日
 */
public class MyLogic {
	private static boolean debug ;
	
	static{
		debug = Config.isDebug();
	}
	
	public static void i(String info) {
		if (!debug) 
			System.out.println(stringChange(info, "系统"));
		else 
			System.out.println(stringChangeComplex(info, "系统"));
	}
	
	public static void d(String info) {
		if (!debug) 
			System.out.println(stringChange(info, "弹幕"));
		else 
			System.out.println(stringChangeComplex(info, "弹幕"));
	}
	
	
	public static void e(String info) {
		if (!debug) 
			System.err.println(stringChange(info, "错误"));
		else 
			System.err.println(stringChangeComplex(info, "错误"));
	}
	
	public static void y(String info) {
		if (debug) 
			System.err.println(stringChangeComplex(info, "详细信息"));
	}
	
	public static String getDate() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String nowDate = sdf.format(date);
		return nowDate;
		
	}
	
	private static String stringChange(String info,String leixing) {
		String date = getDate();
		return info.format("[%s][%s]%s", leixing,date,info);
	}
	
	private static String stringChangeComplex(String info,String leixing) {
		String date = getDate();
		return info.format("[%s][%s][%s]%s", leixing,date,Thread.currentThread().getName(),info);
	}
	
	public static void main(String[] args) {
		d("aa");
	}
}
