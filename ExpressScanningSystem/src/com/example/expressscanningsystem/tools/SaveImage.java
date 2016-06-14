package com.example.expressscanningsystem.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * 
 * @ClassName: SaveImage
 * @Description: 把图片存入SD卡
 * @author pengmengyang
 * @date 2015-7-31 上午9:48:29
 */
public class SaveImage {
	/** 是否保村成功 */
	public static boolean saveImage(byte[] bytes, String filename) {
		boolean flag = false;
		/** 判断SD卡是否可用 */
		if (SDCardUtils.isSDCardEnable()) {
			/** 得到SD卡的存储路径 */
			String path = SDCardUtils.getSDCardPath() + "file";
//			Log.i("tag","存储路径"+path);
			/** 得到路径下的文件 */
			File file = new File(path);
			/** 判断文件是否存在 */
			if (!file.exists()) {
				/** 如果不存在就创建 */
				file.mkdirs();// 创建文件
			}
			/** 声明文件输出流 */
			FileOutputStream fileOutputStream = null;
			try {
				/** 给输出流实例化 */
				fileOutputStream = new FileOutputStream(
						new File(file, filename));
				try {
					/** 存入图片 */
					fileOutputStream.write(bytes);
					/** 如果写入成功 返回true */
					flag = true;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (fileOutputStream != null) {
					/** 关闭流 */
					try {
						fileOutputStream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		return flag;
	}

	/**
	 * 从SD卡取出保存进去的图片
	 * */
	public static Bitmap getImage(String filename) {
		/** 声明Bitmap */
		Bitmap bitmap = null;
		/** 判断SD卡是否可用 */
		if (SDCardUtils.isSDCardEnable()) {
			/** 得到SD卡的存储路径 */
			String path = SDCardUtils.getSDCardPath() + "file/" + filename;
			/** 声明输入流 */
			FileInputStream fileInputStream = null;
			try {
				/** 给输入流实例化 */
				fileInputStream = new FileInputStream(new File(path));
				/** 解析输入流 得到位图 */
				bitmap = BitmapFactory.decodeStream(fileInputStream);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return bitmap;
	}
}
