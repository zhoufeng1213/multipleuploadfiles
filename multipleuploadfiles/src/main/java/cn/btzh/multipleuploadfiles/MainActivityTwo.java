package cn.btzh.multipleuploadfiles;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.previewlibrary.ThumbViewInfo;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.btzh.multipleuploadfiles.biz.Business;
import cn.btzh.multipleuploadfiles.biz.IBusiness;
import cn.btzh.multipleuploadfiles.biz.ICallBackListener;
import cn.btzh.multipleuploadfiles.global.CGHttpConfig;
import cn.btzh.multipleuploadfiles.util.CalculateScreenRate;
import cn.btzh.multipleuploadfiles.util.CheckPermession;
import cn.btzh.multipleuploadfiles.util.CompressPic;
import cn.btzh.multipleuploadfiles.util.CreateLocalFilePath;
import cn.btzh.multipleuploadfiles.util.HelperSDCard;
import cn.btzh.multipleuploadfiles.util.ImageFix;
import cn.btzh.multipleuploadfiles.util.ToastShow;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class MainActivityTwo extends AppCompatActivity implements View.OnClickListener{

    public static final int REQUEST_CODE_CAPTURE_IMAGE = 1;// 照相的requestCode

    @Bind(R.id.button_sc)
    Button button_sc;

    @Bind(R.id.button_sc_one)
    Button button_sc_one;

    @Bind(R.id.button_sc_yasuo)
    Button button_sc_yasuo;

    @Bind(R.id.button_pz)
    Button button_pz;

    @Bind(R.id.sb_attachment_layout)
    LinearLayout sb_attachment_layout;

    @Bind(R.id.myprogressbar)
    ProgressBar myprogressbar;

    @Bind(R.id.textview_xh)
    TextView textview_xh;

    @Bind(R.id.textview_multiple)
    TextView textview_multiple;

    private String strImgPath = ""; // 照片文件绝对路径
    private Context mContext;
    private String fileName = "";
    private String[] imageUrls = new String[100];
    private int imageIndex = 0;
    private List<String> localPicPath = new ArrayList<>();
    private List<String> localYasuoPicPath = new ArrayList<>();
    private ArrayList<ThumbViewInfo> mThumbViewInfoList = new ArrayList<>();

    long startTime;
    long endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_two);
        ButterKnife.bind(this);
        mContext = this;
        CheckPermession.verifyCameraPermissions(this);
        strImgPath = CreateLocalFilePath.createMkdir(mContext,"photo");
    }

    @OnClick({R.id.button_pz,R.id.button_sc,R.id.button_sc_one,R.id.button_sc_yasuo})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_sc_one:
                startTime=System.currentTimeMillis();//记录开始时间
                myprogressbar.setVisibility(View.VISIBLE);
                uploadOneFile();
                break;
            case R.id.button_sc:
                startTime=System.currentTimeMillis();//记录开始时间
                myprogressbar.setVisibility(View.VISIBLE);
                uploadFiles(localPicPath);
                break;
            case R.id.button_sc_yasuo:
                startTime=System.currentTimeMillis();//记录开始时间
                myprogressbar.setVisibility(View.VISIBLE);
                uploadFiles(localYasuoPicPath);
                break;
            case R.id.button_pz:
                fileName = new SimpleDateFormat("yyyyMMddHHmmssms")
                        .format(new Date()) + ".jpg";// 照片命名
                cameraMethod(strImgPath+fileName);
                break;
        }

    }


    // 拍照
    public void cameraMethod( String filePath) {
        if (HelperSDCard.isHadSDCard()) {
            Intent imageCaptureIntent = new Intent(
                    MediaStore.ACTION_IMAGE_CAPTURE);
            File imageFile = new File(filePath);
            Uri imageFileUri = Uri.fromFile(imageFile);
            imageCaptureIntent.putExtra(
                    MediaStore.EXTRA_OUTPUT, imageFileUri);
            startActivityForResult(imageCaptureIntent,
                    REQUEST_CODE_CAPTURE_IMAGE);
        } else {
            Intent imageCaptureIntent = new Intent(
                    MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(imageCaptureIntent,
                    REQUEST_CODE_CAPTURE_IMAGE);
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;// 一定要加，不然会报错
        }
        if (requestCode == REQUEST_CODE_CAPTURE_IMAGE) {// 拍照

            if (!HelperSDCard.isHadSDCard()) {
                if (data != null) {
                    ImageFix.saveLocalPic(data, strImgPath + fileName);
                }
            }
            localPicPath.add(strImgPath + fileName);
            String newFilePath = CompressPic.dealImageYaSuoAndShuiYin(mContext,strImgPath + fileName,strImgPath);
            localYasuoPicPath.add(newFilePath);
            mThumbViewInfoList.add(new ThumbViewInfo(newFilePath));
            dealUploadFileSuccess(newFilePath,BitmapFactory.decodeFile(newFilePath));
        }
    }

    public void dealUploadFileSuccess(String fileLocalPath, Bitmap bitmap) {
        imageUrls[imageIndex] = fileLocalPath;
        imageIndex += 1;
        ImageView mImageV = createImageBigmap(bitmap);
        sb_attachment_layout.addView(mImageV);
    }

    private ImageView createImageBigmap(Bitmap bitmap){
        ImageView imageView = new ImageView(mContext);
        LinearLayout.LayoutParams params  = new LinearLayout.LayoutParams(CalculateScreenRate.zoom(this,80),
                CalculateScreenRate.zoom(this,80) );
        params.setMargins(20, 20, 20, 20);
        imageView.setLayoutParams(params);
        imageView.setImageBitmap(bitmap);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setOnClickListener(imageOnClickListener);
        imageView.setTag(imageIndex - 1);
        return imageView;
    }

    public View.OnClickListener imageOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // 显示图片
            if (v.toString().indexOf("ImageView") > 0) {
//                ShowImageDialog.show(mContext,imageUrls[Integer.parseInt(v.getTag() + "")],true);
                  ImagePhotoActivity.startActivity(MainActivityTwo.this,mThumbViewInfoList,Integer.parseInt(v.getTag().toString()));
            }
        }
    };


    public void uploadOneFile() {
        for (int i = 0; i <localPicPath.size();i++){
            Map<String, RequestBody> params = new HashMap<>();
            params.put("path",RequestBody.create(MediaType.parse("text/plain"),
                    "multipleFileUpload/"));
            params.put("type",RequestBody.create(MediaType.parse("text/plain"),"0"));
            File imgFile = new File(localPicPath.get(i));
            params.put("filename",RequestBody.create(MediaType.parse("text/plain"),imgFile.getName()));
            RequestBody img1 = RequestBody.create(MediaType.parse("application/octet-stream"), imgFile);
            params.put("Filedata\";filename=\""+imgFile.getName(), img1);
            IBusiness business = new Business();
            business.uploadMultipleFiles(CGHttpConfig.BASE_URL,1000,params,listener_one);
        }
    }

    int uploadSuccessNum = 0;
    private ICallBackListener listener_one = new ICallBackListener() {
        @Override
        public void onSuccess(int operator, String result) {
            myprogressbar.setVisibility(View.GONE);
            if (result != null) {
                if (result.trim().contains("true")) {
                    uploadSuccessNum++;
                    // 逻辑显示
                } else {
                    ToastShow.showToast(mContext,"上传失败");
                }
            }

            if(uploadSuccessNum == localPicPath.size()){
                endTime=System.currentTimeMillis();//记录结束时间
                textview_xh.setText("循环上传耗时："+(float)(endTime-startTime)/1000);
                ToastShow.showToast(mContext,"上传成功");
            }
        }

        @Override
        public void onFaild(int operator, String result) {
            myprogressbar.setVisibility(View.GONE);
            ToastShow.showToast(mContext,"上传失败");
        }
    };


    public void uploadFiles( List<String> listPath) {
        Map<String, RequestBody> params = new HashMap<>();
        //服务器的目录
        params.put("path",RequestBody.create(MediaType.parse("text/plain"),
                "multipleFileUpload/"));
        //type：0 上传 1：删除
        params.put("type",RequestBody.create(MediaType.parse("text/plain"),"0"));
        //记录文件的数量
        params.put("fileCount",RequestBody.create(MediaType.parse("text/plain"),""+listPath.size()));
        for (int i = 0; i <listPath.size();i++){
            File imgFile = new File(listPath.get(i));
            if (imgFile.exists()) {
                //params.put("filename"+i,RequestBody.create(MediaType.parse("text/plain"),imgFile.getName()));
                //application/octet-stream:不晓得是什么文件的时候用这个
                RequestBody fileBody = RequestBody.create(
                        MediaType.parse("application/octet-stream"), imgFile);
                params.put("Filedata"+i+"\";filename=\""+imgFile.getName(), fileBody);
            }
        }
        IBusiness business = new Business();
        business.uploadMultipleFiles(CGHttpConfig.BASE_URL,1000,params,listener);
    }


    private ICallBackListener listener = new ICallBackListener() {
        @Override
        public void onSuccess(int operator, String result) {
            myprogressbar.setVisibility(View.GONE);
            if (result != null) {
                if (result.trim().contains("true")) {
                    endTime=System.currentTimeMillis();//记录结束时间
                    textview_multiple.setText("多张上传耗时："+(float)(endTime-startTime)/1000);
                    // 逻辑显示
                    ToastShow.showToast(mContext,"上传成功");
                } else {
                    ToastShow.showToast(mContext,"上传失败");
                }
            }
        }

        @Override
        public void onFaild(int operator, String result) {
            myprogressbar.setVisibility(View.GONE);
            ToastShow.showToast(mContext,"上传失败");
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }


}
