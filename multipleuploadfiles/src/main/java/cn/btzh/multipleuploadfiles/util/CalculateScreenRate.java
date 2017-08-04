package cn.btzh.multipleuploadfiles.util;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * 计算按照屏幕比例缩放后的尺寸
 * Created by fly(zhoufeng) on 2017/7/20.
 */

public class CalculateScreenRate {

    static float fzoom = 0f;

    /**
     * 计算按照屏幕比例缩放后的尺寸
     * @param context
     * @param size
     * @return
     */
    public static int zoom(Activity context, int size){
        if(fzoom == 0f){
            WindowManager windowManager = context.getWindowManager();
            Display display = windowManager.getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            fzoom = metrics.density;
        }
        return (int)(size * fzoom);
    }
}
