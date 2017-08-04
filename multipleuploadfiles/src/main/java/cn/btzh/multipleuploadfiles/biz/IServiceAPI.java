package cn.btzh.multipleuploadfiles.biz;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by fly on 2017/3/13.
 */

public interface IServiceAPI {

    @GET("{modulename}/control.php")
    Observable<String> getHttpGetResult(@Path("modulename") String modulename, @Query("f") int operator, @Query(value = "p", encoded = true) String params);


    //post提交使用field一定要加上 @FormUrlEncoded，不然解析json会出错
    @FormUrlEncoded
    @POST("{modulename}/control.php")
    Observable<String> getHttpPostResult(@Path("modulename") String modulename, @Field("f") int operator, @Field("p") String params);

    //上传图片的是否@Multipart和@FormUrlEncoded不能一起用，
    // 否则会出现Only one encoding annotation is allowed.
    @Multipart
    @POST("{uploadUrlName}")
    Observable<String> uploadFile(@Path("uploadUrlName") String uploadUrlName, @PartMap Map<String, RequestBody> params);

    //上传图片的是否@Multipart和@FormUrlEncoded不能一起用，
    // 否则会出现Only one encoding annotation is allowed.
    @Multipart
    @POST("{uploadUrlName}")
    Observable<String> uploadMultipleFiles(@Path("uploadUrlName") String uploadUrlName, @PartMap Map<String, RequestBody> params);

}
