package com.ustc.gcsj.doc;

import java.text.DecimalFormat;

public class Utility {

	public static void sleep(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// change file unit:from B to KB or MB.
	public static String formatFileSize(long size) {
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (size < 1024) {
			df = new DecimalFormat();
			fileSizeString = df.format((double) size) + "B";
		} else if (size < 1048576) {
			fileSizeString = df.format((double) size / 1024) + "KB";
		} else if (size < 1073741824) {
			fileSizeString = df.format((double) size / 1048576) + "MB";
		} else {
			fileSizeString = df.format((double) size / 1073741824) + "GB";
		}
		return fileSizeString;
	}
}
