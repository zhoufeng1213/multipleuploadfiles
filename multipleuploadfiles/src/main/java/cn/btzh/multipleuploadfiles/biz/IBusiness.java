package cn.btzh.multipleuploadfiles.biz;

import java.util.Map;

import okhttp3.RequestBody;

/**
 * Created by fly on 2017/3/13.
 */

public interface IBusiness {

    /**
     * 获取get结果
     * @param operatorId
     * @param params
     * @param iCallBackListener
     */
    void getHttpGetResult(String moduleName, int operatorId, String params, ICallBackListener iCallBackListener);

    /**
     * 获取post结果
     * @param operatorId
     * @param params
     * @param iCallBackListener
     */
    void getHttpPostResult(String moduleName, int operatorId, String params, ICallBackListener iCallBackListener);


    /**
     * 上传图片
     * @param uploadUrl
     * @param params
     */
    void uploadFile(String uploadUrl, int operatorId, Map<String, RequestBody> params, ICallBackListener iCallBackListener);
    void uploadMultipleFiles(String uploadUrl, int operatorId, Map<String, RequestBody> params, ICallBackListener iCallBackListener);
}
