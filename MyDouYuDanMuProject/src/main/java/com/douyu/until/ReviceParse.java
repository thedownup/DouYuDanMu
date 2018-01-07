package com.douyu.until;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import com.douyu.bean.DanMu;

public class ReviceParse {
	private static String REGEX_SPLIT = "(.*)=";
	private static String REGEX_DANMU = "uid@=(.*)/nn@=(.*)/txt@=(.*)/cid@=(.*)/";
	
	public static Map<String, List<String>> decoding(String content,String key) {
		content = deFilterStr(content);
		String[] strs = content.split("/");
		Map<String, List<String>> map = new HashMap<>();
		List<String> arrayList = new ArrayList<String>();
 		for (String str : strs) {
 			if (str.contains(key)) {
 				Matcher matcher = ParseUntil.getMatcher(str, key+REGEX_SPLIT);
 	 			while(matcher.find()){
 	 				str = str.replaceFirst(key+"(.*)=", "");
 	 				arrayList.add(str);
 	 				MyLogic.y(key+"="+str);
 	 			}
			}
		}
 		
 		if ("port".equals(key)) {
			map.put("danmu.douyu.com", arrayList);
		}else {
			map.put(key, arrayList);
		}
 		
		return map;
	}
	
	
	//解析弹幕
	public static DanMu parseDanmaku(String content,int rid) {
		DanMu danMu = null;
		Matcher matcher = ParseUntil.getMatcher(content, REGEX_DANMU);
		while(matcher.find()){
			try {
				int uid = Integer.valueOf(matcher.group(1));
				String snick = new String(matcher.group(2).getBytes(),"utf-8");
				String txt = new String(matcher.group(3).getBytes(),"utf-8");
				danMu = new DanMu(uid, snick, txt, rid);
				MyLogic.y(danMu.toString());
			} catch (UnsupportedEncodingException e) {
				MyLogic.e("编码解析错误");
				e.printStackTrace();
			}
		}
		return danMu;
	}

	
	public static String deFilterStr(String str) {
		if (str == null) return null;
		return str.trim().replace("@A", "@").replace("@S", "/").replace("@A", "");
	}
}
