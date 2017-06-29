package com.lai.recycler_view_demo;

import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class MainActivity extends AppCompatActivity implements OnRecyclerViewItemOnClickListener {

    private RecyclerView mRv;
    private List<String> mDatas;
    private Toolbar mToolbar;
    private MyRecyclerViewAdapter mAdapter;
    private ItemTouchHelper mItemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //获取屏幕宽度
        WindowManager wm = this.getWindowManager();
        final int width = wm.getDefaultDisplay().getWidth();

       mItemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                //拖拽和滑动的标志
                int dragFlags = 0, swipeFlags = 0;
                //判断是否是网格布局还是瀑布流
                if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager || recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                    //网格布局有四个方向
                    dragFlags = ItemTouchHelper.UP |
                            ItemTouchHelper.DOWN |
                            ItemTouchHelper.LEFT |
                            ItemTouchHelper.RIGHT;
                } else if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                    //线性布局有两个方向
                    dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                    swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                }
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                //长按会回调这个方法
                int from=viewHolder.getAdapterPosition();
                int to=target.getAdapterPosition();

                String moveItem = mDatas.get(from);
                mDatas.remove(from);
                mDatas.add(to,moveItem);//交换数据链表中数据的位置

                mAdapter.notifyItemMoved(from,to);//更新适配器中item的位置
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                TextView tv = (TextView) viewHolder.itemView.findViewById(R.id.tv);
                String data = tv.getText().toString();
                mDatas.remove(data);
                //这里处理滑动删除
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public boolean isLongPressDragEnabled() {
                //返回true则为所有item都设置可以拖拽
                return true;
            }

            //当item拖拽开始时调用
            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                super.onSelectedChanged(viewHolder, actionState);
                if(actionState==ItemTouchHelper.ACTION_STATE_DRAG){
                    viewHolder.itemView.setBackgroundColor(Color.TRANSPARENT);//拖拽时设置背景色为透明
                }
            }

            //当item拖拽完成时调用
            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                viewHolder.itemView.setBackgroundColor(Color.WHITE);//拖拽停止时设置背景色为白色
            }

            //当item视图变化时调用
            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                //根据item滑动偏移的值修改item透明度。screenwidth是我提前获得的屏幕宽度
                viewHolder.itemView.setAlpha(1-Math.abs(dX)/width);
            }
        });

        initView();
    }

    public void initData() {
        mDatas = new ArrayList<>();
        for (char i = 'A'; i <= 'Z'; i++) {
            mDatas.add(String.valueOf(i));
        }
    }

    private void initView() {
        mRv = (RecyclerView) findViewById(R.id.rv);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        //ItemTouchHelper是一个处理RecyclerView的滑动删除和拖拽的辅助类，RecyclerView 的item拖拽移动和滑动删除就靠它来实现。
        mItemTouchHelper.attachToRecyclerView(mRv);

        //设置动画效果
        SlideInUpAnimator animator = new SlideInUpAnimator(new OvershootInterpolator(1f));
        mRv.setItemAnimator(animator);

        initToolBar();
        initData();
        initRecyclerView();
    }

    private void initToolBar() {
        mToolbar.inflateMenu(R.menu.toolbar_menu);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.linear:
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
                        mAdapter.setDatas(mDatas);
                        setManager(linearLayoutManager);
                        break;
                    case R.id.grid:
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 3);
                        mAdapter.setDatas(mDatas);
                        setManager(gridLayoutManager);
                        break;
                    case R.id.waterfall:
                        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

                        List<String> datas = new ArrayList<>();
                        StringBuffer sb = new StringBuffer();
                        for (char i = 'A'; i <= 'Z'; i++) {
                            sb.append(i);
                            datas.add(sb.toString());
                        }
                        mAdapter.setDatas(datas);
                        setManager(staggeredGridLayoutManager);
                        break;
                    case R.id.add:
                        mDatas.add(2, "test");
                        mAdapter.setDatas(mDatas);
                        mAdapter.notifyItemInserted(2);
                        break;
                    case R.id.delete:
                        mDatas.remove(2);
                        mAdapter.setDatas(mDatas);
                        mAdapter.notifyItemRemoved(2);
                        break;
                }
                return false;
            }
        });
    }

    private void initRecyclerView() {
        mAdapter = new MyRecyclerViewAdapter();
        mAdapter.setDatas(mDatas);

        //设置监听
        mAdapter.setListener(this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        setManager(gridLayoutManager);
        mRv.setAdapter(mAdapter);
    }

    public void setManager(RecyclerView.LayoutManager manager) {
        mRv.setLayoutManager(manager);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(this, "我是" + mDatas.get(position), Toast.LENGTH_SHORT).show();
    }
}
