package com.douyu.until;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.douyu.bean.ServerInfo;


public class ParseUntil {
	private static final String REGEX_SERVER = "%5B%7B%22ip%22%3A%22(.*?)%22%2C%22port%22%3A%22(.*?)%22%7D%5D";
	private static final String REGEX_ROOMID = "\"room_id\":(\\d*),";
	private static final String REGEX_ROOM_STATUS = "\"show_status\":(\\d*),";
	@SuppressWarnings("deprecation")
	//解析url为 host 加上 ip
	public static  ArrayList<ServerInfo> parseUrl(String url) {
		JSONParser parser = new JSONParser();
		Object obj = null;
		ArrayList<ServerInfo> ports = new ArrayList<ServerInfo>();
		try {
			Document document = Jsoup.connect(url).get();
			Matcher matcher = getMatcher(document.toString(), REGEX_SERVER);
			String result = "";
			//要先执行find
			while(matcher.find()){
				result = URLDecoder.decode(matcher.group());
				obj = parser.parse(result);
				StringBuffer stringBuffer = new StringBuffer();
				JSONArray jsonArray = (JSONArray) obj;
				for (int i = 0; i < jsonArray.size(); i++) {
					JSONObject jsonObject = (JSONObject)jsonArray.get(i);
					String host = jsonObject.get("ip").toString();
					String port = jsonObject.get("port").toString();
					ServerInfo serverInfo = new ServerInfo(host, Integer.valueOf(port));
					ports.add(serverInfo);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ports;
	}
	
	//返回一组ip和host
	public static  ServerInfo parseUrlOnlyOne(String url) {
		List<ServerInfo> serverInfos = parseUrl(url);
		ServerInfo serverInfo = serverInfos.get((int)(Math.random() * serverInfos.size()));
		return serverInfo;
	}
	
	public static boolean parseIsLive(String url) {
		int rid = -1;
		Document document = null;
		try {
			document = Jsoup.connect(url).get();
			if (document == null) {
				return false;
			}
			Matcher matcher = getMatcher(document.toString(), REGEX_ROOM_STATUS);
			return matcher.find() && "1".equals(matcher.group(1));
		} catch (Exception e) {
			e.printStackTrace();
			MyLogic.e("获取主播是否在直播异常");
			return false;
		}
		
	}
	
	public static int parseRoomId(String url) {
		int rid = -1;
		Document document = null;
		try {
			document = Jsoup.connect(url).get();
			Matcher matcher = getMatcher(document.toString(), REGEX_ROOMID);
			while(matcher.find()){
				rid = Integer.valueOf(matcher.group(1));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rid;
		
	}
	
	
	public static Matcher getMatcher(String content, String regex) {
		Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
		return pattern.matcher(content);
	}
	public static void main(String[] args) {
		System.out.println(parseIsLive("https://www.douyu.com/lpl"));
	}
}
