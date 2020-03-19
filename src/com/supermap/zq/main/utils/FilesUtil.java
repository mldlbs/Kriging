package com.supermap.zq.main.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Calendar;

public class FilesUtil {
	private static Logger _log = LoggerFactory.getLogger(FilesUtil.class);
	public static void resetFiles() {
		_log.info( "重置文件开始- \t" + DateUtil.getGeneralString());
		Calendar calendar = Calendar.getInstance();
		File needBkFile = new File("D:\\03差值分析\\插值.udbx");
		File bkFile = new File("D:\\03差值分析\\old\\插值"+calendar.getTimeInMillis()+".udbx");
		try {
			//Boolean isBKed = needBkFile.renameTo(bkFile);
			//System.out.println("备份成功？" + isBKed);
			File newFile = new File("D:\\03差值分析\\备份\\插值.udbx");
			File oldFile = new File("D:\\03差值分析\\插值.udbx").getAbsoluteFile();
			Path filepath = Files.copy(newFile.toPath(),oldFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			System.out.println("复制成功？" + filepath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		_log.info( "重置文件成功 - \t" + DateUtil.getGeneralString());
	}
}
