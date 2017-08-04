package cn.btzh.multipleuploadfiles.global;

import android.app.Application;
import android.os.Build;
import android.os.StrictMode;

/**
 * 模块名称:
 * Created by fly(zhoufeng) on 2017/8/2.
 */

public class GlobalApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
    }
}
