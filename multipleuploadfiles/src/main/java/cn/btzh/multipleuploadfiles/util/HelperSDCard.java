package cn.btzh.multipleuploadfiles.util;

import android.os.Environment;

/**
 * Created by fly on 2017/3/16.
 */

public class HelperSDCard {

    /**
     * 判断是否有sd卡
     * @return
     */
    public static boolean isHadSDCard(){
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }
}
