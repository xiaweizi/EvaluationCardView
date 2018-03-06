package com.xiaweizi.library;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author : xiaweizi
 *     class  : com.xiaweizi.evaluationcardview.EvaluationCardView
 *     e-mail : 1012126908@qq.com
 *     time   : 2018/03/05
 *     desc   :
 * </pre>
 */

public class EvaluationCardView {

    private static final String TAG = "EvaluationCardView";

    private final AlertDialog mAlertDialog;
    private Context mContext;
    private EvaluationNegReasonsLayout mReasonsLayout;
    private EvaluationRatingBar mRatingBar;
    private TextView mTvDescription;
    private EditText mEtOtherReason;

    public EvaluationCardView(Context context) {
        mContext = context;
        mAlertDialog = new AlertDialog.Builder(context).create();
        if (mAlertDialog.getWindow() != null) {
            mAlertDialog.getWindow().setDimAmount(0);
        }
        View view = View.inflate(mContext, R.layout.layout_evaluation_card, null);
        mReasonsLayout = view.findViewById(R.id.negative_layout);
        mRatingBar = view.findViewById(R.id.rating_bar_evaluation);
        mTvDescription = view.findViewById(R.id.tv_evaluation_description);
        mEtOtherReason = view.findViewById(R.id.et_other_reason);
        initReasonsLayout();
        initRatingBar();
        mAlertDialog.setView(view);
    }

    /** 初始化 RatingBar */
    private void initRatingBar() {
        final String[] descriptions = new String[]{"非常不满意", "不满意，请积极改善", "一般，还需提升", "满意，服务不错", "非常满意，一百个赞"};
        mRatingBar.setOnRatingChangeListener(new EvaluationRatingBar.OnRatingChangeListener() {
            @Override
            public void onChange(int countSelected) {
                mTvDescription.setVisibility(View.VISIBLE);
                if (countSelected > 0 && countSelected <= descriptions.length) {
                    mTvDescription.setText(descriptions[countSelected - 1]);
                    mReasonsLayout.setVisibility(countSelected <= 3 ? View.VISIBLE : View.GONE);
                    mEtOtherReason.setVisibility(countSelected <= 3 ? View.VISIBLE : View.GONE);
                }
            }
        });
    }

    /** 初始化理由布局 */
    private void initReasonsLayout() {
        MyAdapter adapter = new MyAdapter(mContext);
        mReasonsLayout.setAdapter(adapter);
        List<String> dataSource = new ArrayList<>();
        dataSource.add("回复太慢");
        dataSource.add("对业务不太了解");
        dataSource.add("服务态度差");
        dataSource.add("问题没有得到解决");
        adapter.setData(dataSource);
        mReasonsLayout.setOnReasonSelectListener(new EvaluationNegReasonsLayout.OnReasonSelectListener() {
            @Override
            public void onItemSelect(EvaluationNegReasonsLayout parent, List<Integer> selectedList) {
                if (selectedList != null && selectedList.size() > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (int i : selectedList) {
                        sb.append(parent.getAdapter().getItem(i));
                        sb.append(":");
                    }
                    Log.i(TAG, "移动研发:" + sb.toString());
                }else{
                    Log.i(TAG, "没有选择标签");
                }
            }
        });
    }

    public void show() {
        if (mAlertDialog != null && !mAlertDialog.isShowing()) {
            mAlertDialog.show();
        }
    }

    public void dismiss() {
        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
        }
    }

    static class MyAdapter extends BaseAdapter {

        private Context mContext;
        private List<String> mData;

        MyAdapter(Context context) {
            mContext = context;
            mData = new ArrayList<>();
        }

        @Override
        public int getCount() {return mData.size();}
        @Override
        public String getItem(int position) {return mData.get(position);}
        @Override
        public long getItemId(int position) {return position;}

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(mContext, R.layout.item_negative_reason, null);
            TextView textView = view.findViewById(R.id.tv_negative_reason);
            textView.setText(mData.get(position));
            return view;
        }

        void setData(List<String> data) {
            if (data == null) {
                return;
            }
            mData = data;
            notifyDataSetChanged();
        }
    }
}
