package cn.btzh.multipleuploadfiles.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * 在本地新建保存文件的文件夹
 * Created by fly on 2017/3/16.
 */

public class CreateLocalFilePath {

    public static String createMkdir(Context mContext, String moduleName){
        String strImgPath = "";
        if(HelperSDCard.isHadSDCard()){
            strImgPath = Environment.getExternalStorageDirectory().toString()
                    + "/multipleuploadFILES/pic/"+moduleName+"/";// 存放照片的文件夹
        }else{
            File filesDir = mContext.getFilesDir();
            strImgPath = filesDir.getPath() + "/pic/"+ moduleName +"/";
        }
        File out = new File(strImgPath);
        if (!out.exists()) {
            out.mkdirs();
        } else {
        }
        return strImgPath;
    }

}
