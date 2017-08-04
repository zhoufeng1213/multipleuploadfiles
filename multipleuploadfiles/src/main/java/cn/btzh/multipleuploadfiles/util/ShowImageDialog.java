package cn.btzh.multipleuploadfiles.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.File;

import cn.btzh.multipleuploadfiles.R;


/**
 * 模块名称:
 * Created by fly(zhoufeng) on 2017/7/12.
 */

public class ShowImageDialog {


    public static void show(Context mContext,String filePath,boolean isLocalPic){
        final Dialog dialog = new Dialog(mContext, R.style.ImageDialog);
        dialog.show();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View viewDialog = inflater.inflate(R.layout.showmageiew, null);


        final ImageView image = (ImageView) viewDialog.findViewById(R.id.imageView1);

        if(isLocalPic){
            Glide.with(mContext.getApplicationContext()).
                    load(new File(filePath)).
                    asBitmap().
                    into(image);
        }else{
            SimpleTarget target = new SimpleTarget<Bitmap>(300,600) {

                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    if(resource != null){
                        image.setImageBitmap(resource);
                    }
                }
            };
            Glide.with(mContext.getApplicationContext()).load(filePath).asBitmap().into(target);


        }

//        image.setImageBitmap(BitmapFactory.decodeFile(filePath));
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.hide();
            }
        });

        Display display = ((Activity)mContext).getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        //设置dialog的宽高为屏幕的宽高
        ViewGroup.LayoutParams layoutParams = new  ViewGroup.LayoutParams(width, height);
        dialog.setContentView(viewDialog, layoutParams);
    }

}
