package com.douyu.until;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import org.apache.xalan.xsltc.compiler.sym;

/**
 * zjt 2018年1月7日
 */
public class Config {	
	public static final String PROPERTIES_NAME = "conf.properties";
	public static final String URL_NAME = "URL";
	public static final String DEBUG = "debug";
	public static final String DB_FILEPATH = "db.filePath";
	public static final String DB_FILE = "db.file";
	public static final String BIANMA = "UTF-8";
	private static Properties properties ;
	static{
		try {
			properties = new Properties();
			InputStream inputStream = new FileInputStream(PROPERTIES_NAME);
			BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream,BIANMA));
			properties.load(bf);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean isDebug(){
		boolean isDebug = Boolean.parseBoolean(properties.getProperty(DEBUG).toLowerCase());
		return isDebug;
	}
	
	public static String getUrl() {
		return (String) properties.get(URL_NAME);
	}
	
	public static String isSaveTOFile() {
		boolean isSave = Boolean.parseBoolean(properties.getProperty(DB_FILE).toLowerCase());
		if (isSave) {
			try {
				return new String(properties.getProperty(DB_FILEPATH).getBytes(),BIANMA);
			} catch (UnsupportedEncodingException e) {
				MyLogic.e("获取保存地址出错");
				e.printStackTrace();
				return "noSave";
			}
		}else{
			return "noSave";
		}
	}
	
}
