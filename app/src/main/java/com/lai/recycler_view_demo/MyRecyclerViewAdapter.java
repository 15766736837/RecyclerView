package com.lai.recycler_view_demo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Lai on 2017/6/28.
 */

public class MyRecyclerViewAdapter extends RecyclerView.Adapter {
    private List<String> mDatas;
    private OnRecyclerViewItemOnClickListener mListener;

    public void setDatas(List<String> datas) {
        mDatas = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        MyRecyclerViewHolder myViewHolder = new MyRecyclerViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ((MyRecyclerViewHolder) holder).setTextView(mDatas.get(position));

        //设置点击事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    public void setListener(OnRecyclerViewItemOnClickListener listener) {
        mListener = listener;
    }
}
