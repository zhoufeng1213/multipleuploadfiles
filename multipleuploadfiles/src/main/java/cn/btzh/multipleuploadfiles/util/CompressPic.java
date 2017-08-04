package cn.btzh.multipleuploadfiles.util;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import id.zelory.compressor.Compressor;

/**
 * 模块名称:
 * Created by fly(zhoufeng) on 2017/7/12.
 */

public class CompressPic {

    public static String dealImageYaSuoAndShuiYin(Context mContext,
                                                  String filePath,String photoLocalPath){
        try {
            //1、先得到照片的原图
            File actualImage = new File(filePath);

            //2、需要先压缩一下然后打水印，否则可能会因为图片过大内存溢出
            File yasuoFile = new Compressor(mContext)
                    .setMaxWidth(720)
                    .setMaxHeight(960)
                    .setQuality(80)
                    .setCompressFormat(Bitmap.CompressFormat.WEBP)
                    //                        .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                    //                                Environment.DIRECTORY_PICTURES).getAbsolutePath())
                    .setDestinationDirectoryPath(photoLocalPath)
                    .compressToFile(actualImage,"yasuo_"+new SimpleDateFormat("yyyyMMddHHmmssms", Locale.CHINA)
                            .format(new Date())+".jpg");




            //5、返回最后打水印压缩图片的路径
            return yasuoFile.getAbsolutePath();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
