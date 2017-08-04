package cn.btzh.multipleuploadfiles;

import android.app.Activity;
import android.content.Intent;

import com.previewlibrary.PhotoActivity;
import com.previewlibrary.ThumbViewInfo;

import java.util.ArrayList;

/**
 * 模块名称: 图片显示界面（继承PhotoActivity是为了设置theme，去掉跳转的时候出现白屏）
 * Created by fly(zhoufeng) on 2017/8/3.
 */

public class ImagePhotoActivity extends PhotoActivity{

    /***
     * 启动预览
     *
     * @param activity     活动对象
     * @param tempData     图片集合
     * @param currentIndex 当前索引坐标
     ***/
    public static void startActivity(Activity activity, ArrayList<ThumbViewInfo> tempData, int currentIndex) {
        // 图片的地址
        //获取图片的bitmap
        Intent intent = new Intent(activity, ImagePhotoActivity.class);
        intent.putParcelableArrayListExtra("imagePaths", tempData);
        intent.putExtra("position", currentIndex);
        activity.startActivity(intent);
    }


}
