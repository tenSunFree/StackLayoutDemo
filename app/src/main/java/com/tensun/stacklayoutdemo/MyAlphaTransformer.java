package com.tensun.stacklayoutdemo;

import android.view.View;

import com.fashare.stack_layout.StackLayout;

/**
 * User: fashare(153614131@qq.com)
 * Date: 2017-07-19
 * Time: 00:33
 * <br/>
 *
 * 自定义透明度渐变, 包含收藏和删除的图片变化
 */
public final class MyAlphaTransformer extends StackLayout.PageTransformer {

    private float mMinAlpha = 0f;
    private float mMaxAlpha = 1f;

    public MyAlphaTransformer(float minAlpha, float maxAlpha) {
        mMinAlpha = minAlpha;
        mMaxAlpha = maxAlpha;
    }

    public MyAlphaTransformer() {                                                                   // 把值 賦予二個參數的構造方法
        this(0f, 1f);
    }

    @Override
    public void transformPage(View view, float position, boolean isSwipeLeft) {

        View ivLike = view.findViewById(R.id.iv_like),                                                // 用view 取得想要操作的控件 然後實體化, ivDelete contentView的類型 也都是View
                ivDelete = view.findViewById(R.id.iv_delete),
                contentView = view.findViewById(R.id.layout_content),
                tv = view.findViewById(R.id.tv);

        ivLike.setAlpha(mMinAlpha);                                                                 // 讓ivLike 在初始畫面不要顯示
        ivDelete.setAlpha(mMinAlpha);                                                               // 讓ivDelete 在初始畫面不要顯示

        if (position > -1 && position <= 0) { // [-1,0]

            float diffAlpha = (mMaxAlpha-mMinAlpha) * Math.abs(position);                           // position取絕對值 賦予diffAlpha, diffAlpha會在越接近左右螢幕邊緣 數值越大 因為取絕對值

            contentView.setAlpha(mMaxAlpha - diffAlpha);                                            // 讓contentView 漸漸消失的效果, (mMaxAlpha-diffAlpha) 得到的數值, 會因為越靠近螢幕左右邊緣 而越小
            if(isSwipeLeft) {                                                                       // 向左滑: 显示"爱心"; 向右滑: 显示"叉叉"
                ivLike.setAlpha(diffAlpha);
                tv.setAlpha(1 - diffAlpha * 6);                                                     // 讓tv 在滑動的過程, 快速消失
            } else {
                ivDelete.setAlpha(diffAlpha);
                tv.setAlpha(1 - diffAlpha * 6);                                                     // 讓tv 在滑動的過程, 快速消失
            }
        }
    }
}
