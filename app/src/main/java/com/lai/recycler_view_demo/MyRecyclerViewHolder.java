package com.lai.recycler_view_demo;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import static com.lai.recycler_view_demo.R.id.card_view;

/**
 * Created by Lai on 2017/6/28.
 */

public class MyRecyclerViewHolder extends RecyclerView.ViewHolder{

    private CardView mCardview;
    private TextView mTextView;

    public MyRecyclerViewHolder(View itemView) {
        super(itemView);
        mTextView = (TextView) itemView.findViewById(R.id.tv);
        mCardview = (CardView) itemView.findViewById(card_view);
    }

    public void setTextView(String text){
        if (mTextView != null){
            mTextView.setText(text);
        }
    }
}
