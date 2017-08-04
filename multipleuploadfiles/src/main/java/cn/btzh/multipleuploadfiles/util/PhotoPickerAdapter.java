package cn.btzh.multipleuploadfiles.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.List;

import cn.btzh.multipleuploadfiles.R;

/**
 *
 */
public class PhotoPickerAdapter extends BaseAdapter {

    private List<Attachment> items;

    private LayoutInflater layoutInflater;

    private Context context;

    public PhotoPickerAdapter(Context context,List<Attachment> items) {
        super();
        this.context = context;
        this.items = items;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = this.layoutInflater.inflate(R.layout.picker_image_list_item, null);

            ImageView preUploadImg = (ImageView) convertView.findViewById(R.id.pic_pre_image);
            ImageView stateImg = (ImageView) convertView.findViewById(R.id.pic_upload_state);
            viewHolder = new ViewHolder(preUploadImg, stateImg);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

//        viewHolder.stateImg.setVisibility(View.INVISIBLE);

        if (items != null) {
            Attachment attachment = items.get(position);
            Glide.with(context)
                    .load(attachment.getFilePath())
                    .asBitmap()
                    .centerCrop()
                    .placeholder(com.previewlibrary.R.drawable.ic_iamge_zhanwei)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            if (resource != null) {
                                viewHolder.preUploadImg.setImageBitmap(resource);
                            }
                        }
                    });

        }
        return convertView;
    }

    class ViewHolder {
        ImageView preUploadImg;
        ImageView stateImg;

        public ViewHolder(ImageView preUploadImg, ImageView stateImg) {
            this.preUploadImg = preUploadImg;
            this.stateImg = stateImg;
        }
    }

}

