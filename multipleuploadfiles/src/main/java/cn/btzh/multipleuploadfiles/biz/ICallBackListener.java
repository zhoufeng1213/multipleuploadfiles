package cn.btzh.multipleuploadfiles.biz;

/**
 * Created by fly on 2017/3/13.
 */

public interface ICallBackListener {

    public void onSuccess(int operator, String result);

    public void onFaild(int operator, String result);

}
