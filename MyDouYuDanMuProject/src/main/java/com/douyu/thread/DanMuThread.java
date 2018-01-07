package com.douyu.thread;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.douyu.bean.DanMu;
import com.douyu.bean.DanMuServer;
import com.douyu.bean.Message;
import com.douyu.bean.ServerInfo;
import com.douyu.db.SaveDanMuToFile;
import com.douyu.handler.MessageHandler;
import com.douyu.until.Config;
import com.douyu.until.MD5Util;
import com.douyu.until.MyLogic;
import com.douyu.until.ParseUntil;
import com.douyu.until.RequestParse;
import com.douyu.until.ReviceParse;
/**
 * zjt 2018年1月7日
 */
public class DanMuThread implements Runnable{
	private static  String URL_NAME = "";
	private List<DanMuServer> DanMuServers = new ArrayList<DanMuServer>();
	private List<DanMu> danMus = new ArrayList<DanMu>();
	private int rid = -1;
	private int gid = -1;
	private int much = 10;//多少条写入文本
	private int count = 0;

	public DanMuThread(String url){
		this.URL_NAME = url;
	}

	//获取弹幕服务器
	private MessageHandler.OnReceiveListener listener = new MessageHandler.OnReceiveListener(){

		boolean f1,f2,finish;

		@Override
		public void onReceive(List<String> responses) {
			for (String response : responses) {
				if (response.contains("msgrepeaterlist")) {
					MyLogic.i("获取到弹幕服务器组正在解析");
					Map<String, List<String>> map =  ReviceParse.decoding(response,"port");
					Set<String> set = map.keySet();
					Iterator<String> iterator = set.iterator();
					while(iterator.hasNext()){
						Object k = iterator.next(); 
						Object v = map.get(k);
						ArrayList<String> arrayList = (ArrayList<String>) v;
						for (int i = 0;i<arrayList.size();i++) {
							DanMuServers.add(new DanMuServer(k.toString(), arrayList.get(i))); 
						}

						for (DanMuServer danMuServer : DanMuServers) {
							MyLogic.y(danMuServer.toString());
						}
					}
					f1 = true;
				}

				if (response.contains("setmsggroup")) {
					MyLogic.i("获取弹幕群组编号");
					Map<String, List<String>> map =  ReviceParse.decoding(response,"gid");
					gid = Integer.valueOf(map.get("gid").get(0));
					MyLogic.i("弹幕群组编号为"+gid);
					f2 = true;
				}
			}

			finish = f1 && f2;
		}

		@Override
		public boolean isFinished() {
			return finish;
		}

	};

	//读取弹幕
	private MessageHandler.OnReceiveListener danMuListener = new MessageHandler.OnReceiveListener(){

		boolean f1,f2,finish;

		@Override
		public void onReceive(List<String> responses) {
			for (String response : responses) {
				if (response.contains("chatmsg")) {
					//MyLogic.i("获取到弹幕正在解析");
					count++;
					DanMu danMu = ReviceParse.parseDanmaku(response, rid);
					danMus.add(danMu);
					MyLogic.d(danMu.getSnick()+":"+danMu.getContent());
				}
				
				if (danMus.size() > much) {
					SaveDanMuToFile.saveDanMu2File(danMus,count-much);
					danMus.clear();
					MyLogic.i("写入数据到"+Config.isSaveTOFile()+">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
				}
			}

			finish = f1 && f2;
		}

		@Override
		public boolean isFinished() {
			return finish;
		}

	};


	private void loginRequest(ServerInfo server) {
		Socket socket = null;
		try {
			socket = new Socket(server.getHost(), server.getPort());
			MyLogic.i("服务器为:"+server.getHost() + "端口号为:"+server.getPort());
			String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
			String uuid = UUID.randomUUID().toString().replace("-", "").toUpperCase();
			String vk = MD5Util.MD5(timestamp + "7oE9nPEG9xXV69phU31FYCLUagKeYtsF" + uuid);
			//发送登陆请求
			MessageHandler.send(socket, RequestParse.gid(rid, uuid, timestamp, vk));
			//等待接收  (一定要接收 ！！！！！！！！！！！！！！！！！！！)
			MessageHandler.receive(socket, listener);

		} catch (IOException e) {
			MyLogic.e("登陆到服务器失败！"+e.getMessage());
		} finally {
			if (socket != null)
				try {
					socket.close();
				} catch (IOException e) {
					MyLogic.e("连接关闭异常");
				}
		}
	}


	public void startGetDanMu(){
		if (DanMuServers == null || DanMuServers.size() == 0) {
			MyLogic.e("没有可以获取的弹幕服务器");
			return;
		}
		DanMuServer danMuServer = DanMuServers.get((int)Math.random() * DanMuServers.size());
		MyLogic.i("获取的弹幕服务器ip:"+danMuServer.getIp()+"  port:"+danMuServer.getPort());
		try {
			Socket socket = new Socket(danMuServer.getIp(),Integer.valueOf(danMuServer.getPort()));
			MyLogic.y("弹幕申请登陆请求: "+RequestParse.danMuLogin(rid));
			MyLogic.y("弹幕申请加入请求 : "+RequestParse.joinGroup(rid, gid));
			MessageHandler.send(socket, RequestParse.danMuLogin(rid));
			MessageHandler.send(socket, RequestParse.joinGroup(rid, gid));

			//开启心跳包
			new Thread(new KeepLiveThread(socket)).start();
			
			MessageHandler.receive(socket, danMuListener);
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
			MyLogic.e("连接弹幕服务器出现错误"+e.getMessage());
			return;
		}
	}

	@Override
	public void run() {
		MyLogic.i("启动解析器网址为:"+URL_NAME);
		rid = ParseUntil.parseRoomId(URL_NAME);
		if (rid == -1) {
			MyLogic.e("网址解析错误");
			return;
		}

		if (!ParseUntil.parseIsLive(URL_NAME)) {
			MyLogic.e("该房间还没有直播!");
			MyLogic.i("退出解析器");
			return;
		}
		MyLogic.i("获取房间号成功:" + rid);
		ServerInfo serverInfo = ParseUntil.parseUrlOnlyOne(URL_NAME);
		if (serverInfo == null) {
			MyLogic.e("获取服务器失败");
			MyLogic.i("退出解析器");
			return;
		}else {
			MyLogic.i("获取服务器成功");
		}

		MyLogic.i("获取弹幕服务器");
		//获取弹幕服务器
		loginRequest(serverInfo);
		//获取弹幕
		MyLogic.i("开始获取弹幕");
		startGetDanMu();
	}


}
