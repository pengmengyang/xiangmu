package com.example.expressscanningsystem.tools;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.graphics.Bitmap;
/**图片字节处理*/
public class ImageByrry {

	// bitmap转换字节
		public static byte[] Bitmap2Bytes(Bitmap bm) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
			try {
				return baos.toByteArray();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					baos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return null;
		}

		/** md5加密 */
		public String hashKeyForDisk(String key) {
			String cacheKey;
			try {
				final MessageDigest mDigest = MessageDigest.getInstance("MD5");
				mDigest.update(key.getBytes());
				cacheKey = bytesToHexString(mDigest.digest());
			} catch (NoSuchAlgorithmException e) {
				cacheKey = String.valueOf(key.hashCode());
			}
			return cacheKey;
		}

		private String bytesToHexString(byte[] bytes) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				String hex = Integer.toHexString(0xFF & bytes[i]);
				if (hex.length() == 1) {
					sb.append('0');
				}
				sb.append(hex);
			}
			return sb.toString();
		}
	
}
