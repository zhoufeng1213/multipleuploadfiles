package cn.btzh.multipleuploadfiles.biz;

import java.util.Map;

import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by fly on 2017/3/13.
 */

public class Business implements IBusiness {

    @Override
    public void getHttpGetResult(String moduleName, int operatorId, String params, ICallBackListener iCallBackListener) {
        //先获取serviceapi示例
        IServiceAPI iServiceAPI = HttpDao.getInstance().getIServiceAPI();

        iServiceAPI.getHttpGetResult(moduleName,operatorId,params)
                // Subscriber前面执行的代码都是在I/O线程中运行
                .subscribeOn(Schedulers.io())
                // 操作observeOn之后操作主线程中运行.
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(HttpDao.getInstance().createSubscriber(operatorId,iCallBackListener));
    }

    @Override
    public void getHttpPostResult(String moduleName, int operatorId, String params, ICallBackListener iCallBackListener) {
        IServiceAPI iServiceAPI = HttpDao.getInstance().getIServiceAPI();

        iServiceAPI.getHttpPostResult(moduleName,operatorId,params)
                // Subscriber前面执行的代码都是在I/O线程中运行
                .subscribeOn(Schedulers.io())
                // 操作observeOn之后操作主线程中运行.
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(HttpDao.getInstance().createSubscriber(operatorId,iCallBackListener));
    }

    @Override
    public void uploadFile(String uploadUrl,int operatorId, Map<String, RequestBody> params, ICallBackListener iCallBackListener) {

        //uploadUrlIP必须是"/"结尾，否则会报错
        String uploadUrlIP = uploadUrl.substring(0, uploadUrl.lastIndexOf("/")+1);
        String uploadUrlName = uploadUrl.substring(uploadUrl.lastIndexOf("/")+1);

        IServiceAPI iServiceAPI = HttpDao.getInstance().getIServiceAPIUploadFile(uploadUrlIP);
        iServiceAPI.uploadFile(uploadUrlName,params)
                // Subscriber前面执行的代码都是在I/O线程中运行
                .subscribeOn(Schedulers.io())
                // 操作observeOn之后操作主线程中运行.
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(HttpDao.getInstance().createSubscriber(operatorId,iCallBackListener));
    }

    @Override
    public void uploadMultipleFiles(String uploadUrl, int operatorId, Map<String, RequestBody> params, ICallBackListener iCallBackListener) {
        //uploadUrlIP必须是"/"结尾，否则会报错
        String uploadUrlIP = uploadUrl.substring(0, uploadUrl.lastIndexOf("/")+1);
        String uploadUrlName = uploadUrl.substring(uploadUrl.lastIndexOf("/")+1);

        IServiceAPI iServiceAPI = HttpDao.getInstance().getIServiceAPIUploadFile(uploadUrlIP);
        iServiceAPI.uploadMultipleFiles(uploadUrlName,params)
                // Subscriber前面执行的代码都是在I/O线程中运行
                .subscribeOn(Schedulers.io())
                // 操作observeOn之后操作主线程中运行.
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(HttpDao.getInstance().createSubscriber(operatorId,iCallBackListener));
    }

}
