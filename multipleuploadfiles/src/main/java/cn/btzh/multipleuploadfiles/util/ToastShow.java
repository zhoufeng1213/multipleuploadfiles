package cn.btzh.multipleuploadfiles.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by fly on 2017/3/13.
 */

public class ToastShow {

    private static Toast mToast;

    public static void showToast(Context mContext,String msg){
        if(mToast == null){
            mToast = Toast.makeText(mContext,msg,Toast.LENGTH_SHORT);
        }else{
            mToast.setText(msg);
        }
        mToast.show();
    }
}
