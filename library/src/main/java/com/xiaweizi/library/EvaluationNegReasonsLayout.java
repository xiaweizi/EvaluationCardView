package com.xiaweizi.library;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author : xiaweizi
 *     class  : com.xiaweizi.evaluationcardview.EvaluationNegReasonsLayout
 *     e-mail : 1012126908@qq.com
 *     time   : 2018/03/05
 *     desc   : 自定义差评理由选择 view
 * </pre>
 */

public class EvaluationNegReasonsLayout extends ViewGroup {

    /** 检测数据变化的观察者 */
    private AdapterDataSetObserver mDataSetObserver;

    /** 适配器 */
    private ListAdapter mAdapter;

    /** 被选择的回调 */
    private OnReasonSelectListener mOnReasonSelectListener;

    /** 存储被选择的 view */
    private SparseBooleanArray mCheckedReasonArray = new SparseBooleanArray();

    public EvaluationNegReasonsLayout(Context context) {
        super(context);
    }

    public EvaluationNegReasonsLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EvaluationNegReasonsLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 获取Padding
        // 获得它的父容器为它设置的测量模式和大小
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        // NegativeReasonsLayout 最终的宽度和高度值
        int resultWidth = 0;
        int resultHeight = 0;

        //测量时每一行的宽度
        int lineWidth = 0;
        //测量时每一行的高度，加起来就是 NegativeReasonsLayout 的高度
        int lineHeight = 0;

        //遍历每个子元素
        for (int i = 0, childCount = getChildCount(); i < childCount; i++) {
            View childView = getChildAt(i);
            //测量每一个子view的宽和高
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);

            //获取到测量的宽和高
            int childWidth = childView.getMeasuredWidth();
            int childHeight = childView.getMeasuredHeight();

            //因为子View可能设置margin，这里要加上margin的距离
            MarginLayoutParams mlp = (MarginLayoutParams) childView.getLayoutParams();
            int realChildWidth = childWidth + mlp.leftMargin + mlp.rightMargin;
            int realChildHeight = childHeight + mlp.topMargin + mlp.bottomMargin;

            //如果当前一行的宽度加上要加入的子view的宽度大于父容器给的宽度，就换行
            if ((lineWidth + realChildWidth) > sizeWidth) {
                //换行
                resultWidth = Math.max(lineWidth, realChildWidth);
                resultHeight += realChildHeight;
                //换行了，lineWidth和lineHeight重新算
                lineWidth = realChildWidth;
                lineHeight = realChildHeight;
            } else {
                //不换行，直接相加
                lineWidth += realChildWidth;
                //每一行的高度取二者最大值
                lineHeight = Math.max(lineHeight, realChildHeight);
            }

            //遍历到最后一个的时候，肯定走的是不换行
            if (i == childCount - 1) {
                resultWidth = Math.max(lineWidth, resultWidth);
                resultHeight += lineHeight;
            }

            setMeasuredDimension(modeWidth == MeasureSpec.EXACTLY ? sizeWidth : resultWidth,
                    modeHeight == MeasureSpec.EXACTLY ? sizeHeight : resultHeight);

        }

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int flowWidth = getWidth();

        int childLeft = 0;
        int childTop = 0;

        //遍历子控件，记录每个子view的位置
        for (int i = 0, childCount = getChildCount(); i < childCount; i++) {
            View childView = getChildAt(i);

            //跳过View.GONE的子View
            if (childView.getVisibility() == View.GONE) {
                continue;
            }

            //获取到测量的宽和高
            int childWidth = childView.getMeasuredWidth();
            int childHeight = childView.getMeasuredHeight();

            //因为子View可能设置margin，这里要加上margin的距离
            MarginLayoutParams mlp = (MarginLayoutParams) childView.getLayoutParams();

            if (childLeft + mlp.leftMargin + childWidth + mlp.rightMargin > flowWidth) {
                //换行处理
                childTop += (mlp.topMargin + childHeight + mlp.bottomMargin);
                childLeft = 0;
            }
            //布局
            int left = childLeft + mlp.leftMargin;
            int top = childTop + mlp.topMargin;
            int right = childLeft + mlp.leftMargin + childWidth;
            int bottom = childTop + mlp.topMargin + childHeight;
            childView.layout(left, top, right, bottom);

            childLeft += (mlp.leftMargin + childWidth + mlp.rightMargin);
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    public ListAdapter getAdapter() {
        return mAdapter;
    }

    class AdapterDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            super.onChanged();
            reloadData();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
        }
    }


    /**
     * 重新加载刷新数据
     */
    private void reloadData() {
        removeAllViews();

        for (int i = 0; i < mAdapter.getCount(); i++) {
            final int j = i;
            mCheckedReasonArray.put(i, false);
            final View childView = mAdapter.getView(i, null, this);
            addView(childView, new MarginLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)));

            if (mAdapter instanceof OnInitSelectedPosition) {
                boolean isSelected = ((OnInitSelectedPosition) mAdapter).isSelectedPosition(i);
                if (isSelected) {
                    mCheckedReasonArray.put(i, true);
                    childView.setSelected(true);
                }
            }

            childView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCheckedReasonArray.get(j)) {
                        mCheckedReasonArray.put(j, false);
                        childView.setSelected(false);
                    } else {
                        mCheckedReasonArray.put(j, true);
                        childView.setSelected(true);
                    }
                    //回调
                    if (mOnReasonSelectListener != null) {
                        List<Integer> list = new ArrayList<>();
                        for (int k = 0; k < mAdapter.getCount(); k++) {
                            if (mCheckedReasonArray.get(k)) {
                                list.add(k);
                            }
                        }
                        mOnReasonSelectListener.onItemSelect(EvaluationNegReasonsLayout.this, list);
                    }
                }
            });
        }
    }

    /**
     * 清除所有被选择的选项
     *
     */
    public void clearAllOption() {
        for (int i = 0; i < mAdapter.getCount(); i++) {
            if (mCheckedReasonArray.get(i)) {
                getChildAt(i).setSelected(false);
            }
        }
    }

    public void setOnReasonSelectListener(OnReasonSelectListener onReasonSelectListener) {
        this.mOnReasonSelectListener = onReasonSelectListener;
    }

    /**
     * 像ListView、GridView一样使用NegativeReasonsLayout
     *
     * @param adapter 适配器
     */
    public void
    setAdapter(ListAdapter adapter) {
        if (mAdapter != null && mDataSetObserver != null) {
            mAdapter.unregisterDataSetObserver(mDataSetObserver);
        }

        //清除现有的数据
        removeAllViews();
        mAdapter = adapter;

        if (mAdapter != null) {
            mDataSetObserver = new AdapterDataSetObserver();
            mAdapter.registerDataSetObserver(mDataSetObserver);
        }
    }


    public interface OnReasonSelectListener {
        void onItemSelect(EvaluationNegReasonsLayout parent, List<Integer> selectedList);
    }

    public interface OnInitSelectedPosition {
        boolean isSelectedPosition(int position);
    }

}
