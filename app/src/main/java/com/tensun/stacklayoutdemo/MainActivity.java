package com.tensun.stacklayoutdemo;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fashare.stack_layout.StackLayout;
import com.fashare.stack_layout.transformer.AngleTransformer;
import com.fashare.stack_layout.transformer.StackPageTransformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    static List<Integer> sRandomColors = new ArrayList<>();                                          // 建立一組隨機顏色的儲存空間

    static {
        for (int i = 0; i < 100; i++)
            sRandomColors.add(new Random().nextInt() | 0xff000000);                                  // 往sRandomColors裡面 添加隨機的顏色
    }

    SwipeRefreshLayout mRefreshLayout;
    StackLayout mStackLayout;
    Adapter mAdapter;

    List<String> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        loadData(0);
    }

    private void initView() {
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl_refresh);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {                                                               // 觸發上拉刷新時, 想做哪些事
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {                                                             // 讓數值狀態 初始化
                        int page = 0;
                        curPage = 0;
                        mData = new ArrayList<>(Arrays.asList(                                        // 初始化內容
                                String.valueOf(page * 3),
                                String.valueOf(page * 3 + 1),
                                String.valueOf(page * 3 + 2),
                                String.valueOf(page * 3 + 3),
                                String.valueOf(page * 3 + 4))
                        );
                        mStackLayout.setAdapter(mAdapter = new Adapter(mData));
                        mRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });

        mStackLayout = (StackLayout) findViewById(R.id.stack_layout);
        mStackLayout.setAdapter(mAdapter = new Adapter(mData = new ArrayList<>()));

        mStackLayout.addPageTransformer(
                new StackPageTransformer(),                                                         // 呈現堆疊的效果
                new AngleTransformer(),                                                             // 翻頁時, 呈現傾斜的效果
                new MyAlphaTransformer()                                                            // 渐变
        );

        mStackLayout.setOnSwipeListener(new StackLayout.OnSwipeListener() {
            @Override
            public void onSwiped(                                                                   // 只要確實有翻頁, 就會觸發此方法
                                                                                                    View swipedView, int swipedItemPos, boolean isSwipeLeft, int itemLeft) {

                if (itemLeft < 5) {                                                                 // 少于5条, 加载更多
                    loadData(++curPage);                                                            // 讓mAdapter.getData().addAll() 新增的數值能夠不重複不斷增加
                }
            }
        });
    }

    int curPage = 0;                                                                                // 影響到每次觸發loadData(), 得到的數值

    private void loadData(final int page) {
        new Handler().postDelayed(new Runnable() {                                                  // 延遲一秒之後, 才執行內容
            @Override
            public void run() {
                mAdapter.getData().addAll(Arrays.asList(                                              // 增添內容
                        String.valueOf(page * 5),
                        String.valueOf(page * 5 + 1),
                        String.valueOf(page * 5 + 2),
                        String.valueOf(page * 5 + 3),
                        String.valueOf(page * 5 + 4))
                );
                mAdapter.notifyDataSetChanged();                                                    // 通知RecycelrView 进行全局刷新
            }
        }, 1000);
    }

    class Adapter extends StackLayout.Adapter<Adapter.ViewHolder> {
        List<String> mData;                                                                         // 接受數據源的儲存位置

        public List<String> getData() {                                                             // 給loadData() 使用的
            return mData;
        }

        public Adapter(List<String> data) {                                                         // 通過構造方法, 將數據源放入Adapter
            mData = data;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int position) {                      // 創建 ViewHolder
            return new ViewHolder(                                                                  // 指定 顯示每一個Item的Layout
                    LayoutInflater.from(parent.getContext()).inflate(
                            R.layout.item_card, parent, false
                    ));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {                       // 綁定 ViewHolder
            holder.mTextView.setText(mData.get(position));                                          // 把mData的每一組資料, 分別放入mTextView
            holder.itemView.findViewById(R.id.layout_content).                                         // 將Item布局 添加上每一組的隨機色彩
                    setBackgroundColor(sRandomColors.get(position % sRandomColors.size()));

            holder.itemView.setOnClickListener(new View.OnClickListener() {                         // 幫Item布局 添加點擊事件
                @Override
                public void onClick(View v) {
                                                                                                    // 當你點擊Item時, 想要做些什麼
                }
            });
        }

        @Override
        public int getItemCount() {
            return mData.size();                                                                    // 返回Item 總數量
        }

        public class ViewHolder extends StackLayout.ViewHolder {                                    // ViewHolder的原型
            TextView mTextView;

            public ViewHolder(View itemView) {                                                      // 初始化Item布局裡面的控件, 如果你有需要使用到
                super(itemView);
                mTextView = (TextView) itemView.findViewById(R.id.tv);
            }
        }
    }
}
