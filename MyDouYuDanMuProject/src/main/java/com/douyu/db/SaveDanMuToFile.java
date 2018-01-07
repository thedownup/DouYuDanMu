package com.douyu.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.douyu.bean.DanMu;
import com.douyu.until.Config;
import com.douyu.until.MyLogic;

public class SaveDanMuToFile {
	private static FileWriter fileWriter = null;
	public static void saveDanMu2File(List<DanMu> danMus,int count) {
		if (!"noSave".equals(Config.isSaveTOFile())) {
			try {
				File file = new File(Config.isSaveTOFile());
				if (!file.exists()) {
					file.createNewFile();
				}
				fileWriter = new FileWriter(file,true);
				if (count == 1) {
					fileWriter.write("=====================Date = "+MyLogic.getDate()+"====================="+"\r\n");
					fileWriter.write("\r\n");
				}
				for (DanMu danMu : danMus) {
					fileWriter.write("第"+(count++)+"条消息"+danMu.toString()+"\r\n");
					fileWriter.write("\r\n");
				}
				fileWriter.flush();
			} catch (Exception e) {
				MyLogic.e(e.getMessage());
				e.printStackTrace();
			}finally {
				if (fileWriter != null) {
					try {
						fileWriter.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
}
