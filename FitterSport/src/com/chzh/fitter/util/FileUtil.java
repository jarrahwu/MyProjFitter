package com.chzh.fitter.util;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Environment;

public class FileUtil {

	public static final String SD_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath();

	public static final String RES_ROOT = SD_PATH + "/" + "fitter";

	public static final String IMAGE_DIR = RES_ROOT + "/" + "img";
	
	public static final String VIDEO_DIR = RES_ROOT + "/" + "video";

	private static String mPhotoFilePath;

	@SuppressLint("SimpleDateFormat")
	public static File getPortraitFile() {
		makeRootDir();
		String fname = new SimpleDateFormat("yyMMddHHmmss").format(new Date() ) + ".jpg";
		File photoFile = new File(IMAGE_DIR + "/" + fname);
		mPhotoFilePath = photoFile.getAbsolutePath();
		return photoFile;

	}


	public static String getPhotoFilePath() {
		return mPhotoFilePath;
	}

	public static void makeRootDir() {
		File resRoot = new File(RES_ROOT);
		if (!resRoot.exists()) {
			resRoot.mkdir();
			L.red("res_root not exist mkdir");
		}

		File imageDir = new File(IMAGE_DIR);
		if (!imageDir.exists()) {
			imageDir.mkdir();
			L.red("publish_dir not exist mkdir");
		}
	}
	
	/**
	 * 保存图片
	 * @param bm 需要保存的bitmap
	 * @param path 保存的路径
	 * @param fileName 保存的文件名
	 * @return 保存bitmap的完整路径
	 */
	public static String saveFile(Bitmap bm, String path, String fileName ,boolean isHighQuality) {
		int quality = 70;  //质量默认 70%
		
		File dirFile = new File(path);
		if (!dirFile.exists()) {
			dirFile.mkdir();
		}
		//判断是否为高质量
		if (isHighQuality) {
			quality = 100;
		}
		
		File myCaptureFile = new File(path + fileName);
		BufferedOutputStream bos;

		try {
			bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
			bm.compress(Bitmap.CompressFormat.JPEG, quality, bos);
			bos.flush();
			bos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return myCaptureFile.getPath();
	}


	public static File getVideoDir() {
		File videoDir = new File(VIDEO_DIR);
		if (!videoDir.exists()) {
			videoDir.mkdirs();
		}
		return videoDir;
	}

}
