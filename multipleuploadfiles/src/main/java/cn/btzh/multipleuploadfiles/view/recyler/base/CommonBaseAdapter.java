package cn.btzh.multipleuploadfiles.view.recyler.base;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cn.btzh.multipleuploadfiles.R;
import cn.btzh.multipleuploadfiles.view.recyler.ViewHolder;
import cn.btzh.multipleuploadfiles.view.recyler.interfaces.OnItemChildClickListener;
import cn.btzh.multipleuploadfiles.view.recyler.interfaces.OnItemClickListener;
import cn.btzh.multipleuploadfiles.view.recyler.interfaces.OnSwipeMenuClickListener;


/**
 * Author: Othershe
 * Time: 2016/9/9 15:52
 */
public abstract class CommonBaseAdapter<T> extends BaseAdapter<T> {
    protected OnItemClickListener<T> mItemClickListener;


    protected ArrayList<Integer> mViewId = new ArrayList<>();
    protected ArrayList<OnSwipeMenuClickListener<T>> mListener = new ArrayList<>();

    private ArrayList<Integer> mItemChildIds = new ArrayList<>();
    protected ArrayList<OnItemChildClickListener<T>> mItemChildListeners = new ArrayList<>();

    public CommonBaseAdapter(Context context, List<T> datas, boolean isOpenLoadMore) {
        super(context, datas, isOpenLoadMore);
    }

    protected abstract void convert(ViewHolder holder, T data, int position);

    protected abstract int getItemLayoutId();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (isCommonItemView(viewType)) {
            return ViewHolder.create(mContext, getItemLayoutId(), parent);
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = holder.getItemViewType();
        if (isCommonItemView(viewType)) {
            bindCommonItem(holder, position);
        }
    }

    private void bindCommonItem(RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        if(selectedPosition == position){
            viewHolder.getConvertView().setBackgroundColor(ContextCompat.getColor(mContext, R.color.list_background));
        }else{
            viewHolder.getConvertView().setBackgroundColor(Color.WHITE);
        }
        convert(viewHolder, mDatas.get(position),position);

        viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItemClickListener != null) {
                    selectedPosition = position;
                    notifyDataSetChanged();
                    mItemClickListener.onItemClick(viewHolder, mDatas.get(position), position);
                }
            }
        });

        for (int i = 0; i < mItemChildIds.size(); i++) {
            final int tempI = i;
            if (viewHolder.getConvertView().findViewById(mItemChildIds.get(i)) != null) {
                viewHolder.getConvertView().findViewById(mItemChildIds.get(i)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mItemChildListeners.get(tempI).onItemChildClick(viewHolder, mDatas.get(position), position);
                    }
                });
            }
        }

        if (mViewId.size() > 0 && mListener.size() > 0 && viewHolder.getSwipeView() != null) {
            ViewGroup swipeView = (ViewGroup) viewHolder.getSwipeView();

            for (int i = 0; i < mViewId.size(); i++) {
                final int tempI = i;
                swipeView.findViewById(mViewId.get(i)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.get(tempI).onSwipMenuClick(viewHolder, mDatas.get(position), position);
                    }
                });
            }
        }
    }

    @Override
    protected int getViewType(int position, T data) {
        return TYPE_COMMON_VIEW;
    }

    public void setOnItemClickListener(OnItemClickListener<T> itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public void setOnSwipMenuClickListener(int viewId, OnSwipeMenuClickListener<T> swipeMenuClickListener) {
        mViewId.add(viewId);
        mListener.add(swipeMenuClickListener);
    }

    public void setOnItemChildClickListener(int viewId, OnItemChildClickListener<T> itemChildClickListener) {
        mItemChildIds.add(viewId);
        mItemChildListeners.add(itemChildClickListener);
    }

    private int selectedPosition = -5; //默认一个参数
    public void setSelectedPosition(int position){
        this.selectedPosition = position;
    }

}
