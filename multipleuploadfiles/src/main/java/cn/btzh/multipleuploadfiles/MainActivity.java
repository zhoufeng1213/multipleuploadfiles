package cn.btzh.multipleuploadfiles;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.previewlibrary.ThumbViewInfo;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.btzh.multipleuploadfiles.biz.Business;
import cn.btzh.multipleuploadfiles.biz.IBusiness;
import cn.btzh.multipleuploadfiles.biz.ICallBackListener;
import cn.btzh.multipleuploadfiles.global.CGHttpConfig;
import cn.btzh.multipleuploadfiles.util.Attachment;
import cn.btzh.multipleuploadfiles.util.CheckPermession;
import cn.btzh.multipleuploadfiles.util.CompressPic;
import cn.btzh.multipleuploadfiles.util.CreateLocalFilePath;
import cn.btzh.multipleuploadfiles.util.HelperSDCard;
import cn.btzh.multipleuploadfiles.util.ImageFix;
import cn.btzh.multipleuploadfiles.util.ToastShow;
import cn.btzh.multipleuploadfiles.view.recyler.ViewHolder;
import cn.btzh.multipleuploadfiles.view.recyler.base.CommonBaseAdapter;
import cn.btzh.multipleuploadfiles.view.recyler.interfaces.OnItemChildClickListener;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import tyrantgit.explosionfield.ExplosionField;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public static final int REQUEST_CODE_CAPTURE_IMAGE = 1;// 照相的requestCode

    @Bind(R.id.button_sc)
    Button button_sc;

    @Bind(R.id.button_sc_one)
    Button button_sc_one;

    @Bind(R.id.button_sc_yasuo)
    Button button_sc_yasuo;

    @Bind(R.id.button_pz)
    Button button_pz;

    @Bind(R.id.imageRecyclerView)
    RecyclerView imageRecyclerView;

    @Bind(R.id.myprogressbar)
    ProgressBar myprogressbar;

    @Bind(R.id.textview_xh)
    TextView textview_xh;

    @Bind(R.id.textview_multiple)
    TextView textview_multiple;

    private String strImgPath = ""; // 照片文件绝对路径
    private Context mContext;
    private String fileName = "";

    private ArrayList<Attachment> attachments = new ArrayList<>();
    private ArrayList<ThumbViewInfo> thumbViewInfos = new ArrayList<>();
    private CommonBaseAdapter<Attachment> pickerAdapter;    // 图片适配器

    long startTime;
    long endTime;

    ExplosionField mExplosionField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = this;
        CheckPermession.verifyCameraPermissions(this);

        initView();
    }

    private void initView(){
        mExplosionField = ExplosionField.attach2Window(this);
        strImgPath = CreateLocalFilePath.createMkdir(mContext,"photo");

        pickerAdapter = new CommonBaseAdapter<Attachment>(mContext,attachments,
                true) {
            @Override
            protected void convert(final ViewHolder viewHolder, Attachment attachment, int position) {
                Glide.with(mContext)
                        .load(attachment.getFileYasuoPath())
                        .asBitmap()
                        .centerCrop()
                        .placeholder(com.previewlibrary.R.drawable.ic_iamge_zhanwei)
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                if (resource != null) {
                                    viewHolder.setImage(R.id.pic_pre_image,resource);
                                }
                            }
                        });
                viewHolder.setImageVisible(R.id.pic_upload_state,attachment.isFileUploadState());
                viewHolder.setImageVisible(R.id.pic_delete,!attachment.isFileUploadState());
            }

            @Override
            protected int getItemLayoutId() {
                return R.layout.picker_image_list_item;
            }

        };

        LinearLayoutManager ms= new LinearLayoutManager(this);
        ms.setOrientation(LinearLayoutManager.HORIZONTAL);
        imageRecyclerView.setLayoutManager(ms);
        imageRecyclerView.setAdapter(pickerAdapter);

        pickerAdapter.setOnItemChildClickListener(R.id.pic_pre_image, new OnItemChildClickListener<Attachment>() {
            @Override
            public void onItemChildClick(ViewHolder viewHolder, Attachment data, int position) {
                ImagePhotoActivity.startActivity(MainActivity.this,thumbViewInfos,
                        position);
            }
        });

        pickerAdapter.setOnItemChildClickListener(R.id.pic_delete, new OnItemChildClickListener<Attachment>() {
            @Override
            public void onItemChildClick(ViewHolder viewHolder, Attachment data, int position) {
                mExplosionField.explode(viewHolder.getConvertView());
                attachments.remove(position);
                thumbViewInfos.remove(position);
                pickerAdapter.notifyDataSetChanged();
            }
        });

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
                uploadYaSuoFiles(false);
                break;
            case R.id.button_sc_yasuo:
                startTime=System.currentTimeMillis();//记录开始时间
                myprogressbar.setVisibility(View.VISIBLE);
                uploadYaSuoFiles(true);
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
            String newFilePath = CompressPic.dealImageYaSuoAndShuiYin(mContext,strImgPath + fileName,strImgPath);
            Attachment attachment = new Attachment(strImgPath + fileName,newFilePath);
            if (attachment != null) {
                attachments.add(attachment);
                thumbViewInfos.add(new ThumbViewInfo(attachment.getFileYasuoPath()));
                pickerAdapter.notifyDataSetChanged();
            }
        }
    }


    public void uploadOneFile() {
        for (int i = 0; i <attachments.size();i++){
            Attachment attachment = attachments.get(i);
            if(!attachment.isFileUploadState()){
                Map<String, RequestBody> params = new HashMap<>();
                params.put("path",RequestBody.create(MediaType.parse("text/plain"),
                        "multipleFileUpload/"));
                params.put("type",RequestBody.create(MediaType.parse("text/plain"),"0"));
                File imgFile = new File(attachment.getFilePath());
                params.put("filename",RequestBody.create(MediaType.parse("text/plain"),imgFile.getName()));
                RequestBody img1 = RequestBody.create(MediaType.parse("application/octet-stream"), imgFile);
                params.put("Filedata\";filename=\""+imgFile.getName(), img1);
                IBusiness business = new Business();
                business.uploadMultipleFiles(CGHttpConfig.BASE_URL,i,params,listener_one);
            }
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

                    Attachment attachment = attachments.get(operator);
                    attachment.setFileUploadState(true);
                    pickerAdapter.notifyDataSetChanged();
                    
                } else {
                    ToastShow.showToast(mContext,"上传失败");
                }
            }

            if(uploadSuccessNum == attachments.size()){
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

    public void uploadYaSuoFiles(boolean isYasuo) {
        Map<String, RequestBody> params = new HashMap<>();
        //服务器的目录
        params.put("path",RequestBody.create(MediaType.parse("text/plain"),
                "multipleFileUpload/"));
        //type：0 上传 1：删除
        params.put("type",RequestBody.create(MediaType.parse("text/plain"),"0"));
        //记录文件的数量
        params.put("fileCount",RequestBody.create(MediaType.parse("text/plain"),""+attachments.size()));
        for (int i = 0; i <attachments.size();i++){
            Attachment attachment = attachments.get(i);
            File imgFile = new File(isYasuo?attachment.getFileYasuoPath():attachment.getFilePath());
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
                    for(Attachment attachment :attachments){
                        attachment.setFileUploadState(true);
                    }
                    pickerAdapter.notifyDataSetChanged();
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
