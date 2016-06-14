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
 * @Description: ��ͼƬ����SD��
 * @author pengmengyang
 * @date 2015-7-31 ����9:48:29
 */
public class SaveImage {
	/** �Ƿ񱣴�ɹ� */
	public static boolean saveImage(byte[] bytes, String filename) {
		boolean flag = false;
		/** �ж�SD���Ƿ���� */
		if (SDCardUtils.isSDCardEnable()) {
			/** �õ�SD���Ĵ洢·�� */
			String path = SDCardUtils.getSDCardPath() + "file";
//			Log.i("tag","�洢·��"+path);
			/** �õ�·���µ��ļ� */
			File file = new File(path);
			/** �ж��ļ��Ƿ���� */
			if (!file.exists()) {
				/** ��������ھʹ��� */
				file.mkdirs();// �����ļ�
			}
			/** �����ļ������ */
			FileOutputStream fileOutputStream = null;
			try {
				/** �������ʵ���� */
				fileOutputStream = new FileOutputStream(
						new File(file, filename));
				try {
					/** ����ͼƬ */
					fileOutputStream.write(bytes);
					/** ���д��ɹ� ����true */
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
					/** �ر��� */
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
	 * ��SD��ȡ�������ȥ��ͼƬ
	 * */
	public static Bitmap getImage(String filename) {
		/** ����Bitmap */
		Bitmap bitmap = null;
		/** �ж�SD���Ƿ���� */
		if (SDCardUtils.isSDCardEnable()) {
			/** �õ�SD���Ĵ洢·�� */
			String path = SDCardUtils.getSDCardPath() + "file/" + filename;
			/** ���������� */
			FileInputStream fileInputStream = null;
			try {
				/** ��������ʵ���� */
				fileInputStream = new FileInputStream(new File(path));
				/** ���������� �õ�λͼ */
				bitmap = BitmapFactory.decodeStream(fileInputStream);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return bitmap;
	}
}
