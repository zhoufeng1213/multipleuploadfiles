package cn.btzh.multipleuploadfiles.view.recyler.interfaces;


import cn.btzh.multipleuploadfiles.view.recyler.ViewHolder;
/**
 * Author: Othershe
 * Time: 2016/8/29 10:48
 */
public interface OnItemClickListener<T> {
    void onItemClick(ViewHolder viewHolder, T data, int position);
}
