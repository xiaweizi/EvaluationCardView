package com.xiaweizi.evaluationcardview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;

/**
 * <pre>
 *     author : xiaweizi
 *     class  : com.xiaweizi.evaluationcardview.XLHRatingBar
 *     e-mail : 1012126908@qq.com
 *     time   : 2018/03/05
 *     desc   : 自定义客服评价 RatingBar
 * </pre>
 */

public class EvaluationRatingBar extends LinearLayout {

    /** 星星总数 */
    private int mStarTotal;
    /** 星星被选中的数量 */
    private int mSelectedCount;
    /** 星星对应的资源 id */
    private int mStarResId;
    /** 高度 */
    private float mHeight;
    /** 间隔宽度 */
    private float mIntervalWidth;
    /** 是否可被编辑 */
    private boolean mEditable;

    public EvaluationRatingBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.EvaluationRatingBar);
        mStarTotal = typedArray.getInt(R.styleable.EvaluationRatingBar_starTotal, 5);
        mSelectedCount = typedArray.getInt(R.styleable.EvaluationRatingBar_selectedCount, 0);
        mEditable = typedArray.getBoolean(R.styleable.EvaluationRatingBar_editable, false);
        mHeight = typedArray.getDimension(R.styleable.EvaluationRatingBar_height, dp2Px(context, 0));
        mIntervalWidth = typedArray.getDimension(R.styleable.EvaluationRatingBar_intervalWidth, dp2Px(context, 0));
        mStarResId = typedArray.getResourceId(R.styleable.EvaluationRatingBar_starResId, -1);
        typedArray.recycle();
        initView();
    }

    private float dp2Px(Context context, float value) {
        if (context == null) {
            return 0;
        }
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, metrics);
    }

    public int getStarTotal() {
        return mStarTotal;
    }

    /**
     * 设置展示星星的总数
     * @param starTotal 展示星星的总数
     */
    public void setStarTotal(int starTotal) {
        this.mStarTotal = starTotal;
        initView();
    }

    public int getSelectedCount() {
        return mSelectedCount;
    }

    /**
     * 设置星星被选择的个数
     * @param selectedCount 星星被选择的个数
     */
    public void setSelectedCount(int selectedCount) {
        if (selectedCount > mStarTotal) {
            return;
        }
        this.mSelectedCount = selectedCount;
        initView();
    }


    private void initView() {
        removeAllViews();
        for (int i = 0; i < mStarTotal; i++) {
            CheckBox cb = new CheckBox(getContext());
            LayoutParams layoutParams;
            if (mHeight == 0) {
                layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            } else {
                layoutParams = new LayoutParams((int) mHeight, (int) mHeight);
            }

            layoutParams.gravity = Gravity.CENTER_VERTICAL;
            if (i != 0 && i != mStarTotal - 1) {
                layoutParams.leftMargin = (int) mIntervalWidth;
                layoutParams.rightMargin = (int) mIntervalWidth;
            } else if (i == 0) {
                layoutParams.rightMargin = (int) mIntervalWidth;
            } else if (i == mStarTotal - 1) {
                layoutParams.leftMargin = (int) mIntervalWidth;
            }
            addView(cb, layoutParams);
            cb.setButtonDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
            if (mStarResId == -1) {
                mStarResId = R.drawable.comment_ratingbar_selector;
            }
            cb.setBackgroundResource(mStarResId);
            if (i + 1 <= mSelectedCount) {
                cb.setChecked(true);
            }
            cb.setEnabled(mEditable);
            cb.setOnClickListener(new MyClickListener(i));
        }

    }

    private class MyClickListener implements OnClickListener {
        int position;

        MyClickListener(int position) {
            super();
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            mSelectedCount = position + 1;
            for (int i = 0; i < mStarTotal; i++) {
                CheckBox cb = (CheckBox) getChildAt(i);
                if (i <= position) {
                    cb.setChecked(true);
                } else if (i > position) {
                    cb.setChecked(false);
                }
            }
            if (mOnRatingChangeListener != null) {
                mOnRatingChangeListener.onChange(mSelectedCount);
            }
        }

    }

    private OnRatingChangeListener mOnRatingChangeListener;
    public void setOnRatingChangeListener(OnRatingChangeListener onRatingChangeListener) {mOnRatingChangeListener = onRatingChangeListener;}

    public interface OnRatingChangeListener {
        /**
         * @param electedCount 星星选中的个数
         */
        void onChange(int electedCount);
    }
}
