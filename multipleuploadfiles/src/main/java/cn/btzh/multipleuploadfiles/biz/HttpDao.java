package cn.btzh.multipleuploadfiles.biz;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import cn.btzh.multipleuploadfiles.global.CGHttpConfig;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;

/**
 * Created by fly on 2017/3/13.
 */

public class HttpDao {

    private static final String TAG = HttpDao.class.getSimpleName();
    private static HttpDao httpDao;
    public final long DEFAULT_TIMEOUT = 30;

    private HttpDao(){

    }

    public static HttpDao getInstance(){
        if(httpDao == null){
            httpDao = new HttpDao();
        }
        return httpDao;
    }

    /**
     * 获取 IServiceAPI实列
     * @return IServiceApi
     */
    public IServiceAPI getIServiceAPI(){

        Gson gson = new  GsonBuilder()
                .registerTypeAdapter(String.class,new DeserializerData())
                .create();

        //retrofit底层用的okHttp,所以设置超时还需要okHttp
        //其中DEFAULT_TIMEOUT是我这边定义的一个常量
        //TimeUnit为java.util.concurrent包下的时间单位
        //TimeUnit.SECONDS这里为秒的单位
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(CGHttpConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();
        return retrofit.create(IServiceAPI.class);
    }

    public Subscriber createSubscriber(final int operatorId,final ICallBackListener iCallBackListener){

        Subscriber subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.e(TAG,"onCompleted....");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG,"faild...."+e.getMessage());
                iCallBackListener.onFaild(operatorId, CGHttpConfig.TOAST_MSG_LOAD_FAILD);
            }

            @Override
            public void onNext(String str) {
                if(str != null && !"".equals(str)){
                    iCallBackListener.onSuccess(operatorId,str);
                }else{
                    iCallBackListener.onFaild(operatorId, CGHttpConfig.TOAST_MSG_LOAD_FAILD);
                }
            }
        };

        return  subscriber;
    }

    public IServiceAPI getIServiceAPIUploadFile(String uploadUrlIP){
        Gson gson = new  GsonBuilder()
                .registerTypeAdapter(String.class,new DeserializerData())
                .create();

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(uploadUrlIP)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();
        return retrofit.create(IServiceAPI.class);
    }

}
