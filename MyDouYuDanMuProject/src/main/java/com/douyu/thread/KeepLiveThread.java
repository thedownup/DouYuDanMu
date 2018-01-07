package com.douyu.thread;

import java.net.Socket;
import com.douyu.handler.MessageHandler;
import com.douyu.until.MyLogic;
import com.douyu.until.RequestParse;

public class KeepLiveThread implements Runnable{

	private Socket socket = null;
	
	public KeepLiveThread(Socket socket){
		this.socket = socket;
	}
	
	@Override
	public void run() {
		try {
			while(socket.isConnected() && socket != null){
				Thread.sleep(30000);
				MessageHandler.send(socket, RequestParse.keepLive((int)System.currentTimeMillis()/1000));
				MyLogic.i("发送KeepLiveThread包");
			}
		} catch (Exception e) {
			MyLogic.e("KeepLiveThread出现错误"+e.getMessage());
			e.printStackTrace();
			return;
		}
	}

}
