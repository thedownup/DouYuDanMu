package com.douyu.Main;

import com.douyu.thread.DanMuThread;
import com.douyu.until.Config;

public class Main {
	public static void main(String[] args) {
		
		//启动主线程		
		new Thread(new DanMuThread(Config.getUrl())).start();
	}
}
