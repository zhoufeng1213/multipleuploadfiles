package cn.btzh.multipleuploadfiles.util;

import android.content.Intent;
import android.graphics.Bitmap;

import java.io.FileOutputStream;

public class ImageFix {
	/**
	 * 拍照返回数据保存到指定文件
	 */
	public static boolean saveLocalPic(Intent data, String strImgPath) {
		if (data != null) {// 设置显示
			Bitmap bmp = data.getParcelableExtra("data");
			String fileName = strImgPath;
			FileOutputStream out = null;
			try {
				out = new FileOutputStream(fileName);
				bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);// 把数据写入文件
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			} finally {
				try {
					out.flush();
					out.close();
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}

			return true;
		}
		return false;
	}
}
