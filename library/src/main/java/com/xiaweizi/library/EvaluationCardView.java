package com.xiaweizi.library;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private TextView mTvCommit;
    private MyAdapter mAdapter;

    private int mStarCount = 0;
    private Set<String> mReasons = new HashSet<>();

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
        mTvCommit = view.findViewById(R.id.tv_commit);
        mTvCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallback != null) {
                    mCallback.onEvaluationCommitClick(mStarCount, mReasons);
                }
            }
        });
        initReasonsLayout();
        initRatingBar();
        mAlertDialog.setView(view);
        mAlertDialog.setCanceledOnTouchOutside(false);
    }

    /** 初始化 RatingBar */
    private void initRatingBar() {
        final String[] descriptions = new String[]{"非常不满意", "不满意，请积极改善", "一般，还需提升", "满意，服务不错", "非常满意，一百个赞"};
        mRatingBar.setOnRatingChangeListener(new EvaluationRatingBar.OnRatingChangeListener() {
            @Override
            public void onChange(int countSelected) {
                mStarCount = countSelected;
                mTvDescription.setVisibility(View.VISIBLE);
                if (countSelected > 0 && countSelected <= descriptions.length) {
                    mTvDescription.setText(descriptions[countSelected - 1]);
                    mReasonsLayout.setVisibility(countSelected <= 3 ? View.VISIBLE : View.GONE);
                }
            }
        });
    }

    /** 初始化理由布局 */
    private void initReasonsLayout() {
        mAdapter = new MyAdapter(mContext);
        mReasonsLayout.setAdapter(mAdapter);
        mReasonsLayout.setOnReasonSelectListener(new EvaluationNegReasonsLayout.OnReasonSelectListener() {
            @Override
            public void onItemSelect(EvaluationNegReasonsLayout parent, List<Integer> selectedList) {
                if (selectedList != null && selectedList.size() > 0) {
                    for (int i : selectedList) {
                        mReasons.add(mAdapter.getItem(i));
                    }
                }else{
                    mReasons.clear();
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

    public void setReasonsData(List<String> reasonsData) {
        if (reasonsData != null && mAdapter != null) {
            mAdapter.setData(reasonsData);
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

    private OnEvaluationCallback mCallback;

    public void setOnEvaluationCallback(OnEvaluationCallback callback) {
        this.mCallback = callback;
    }

    public interface OnEvaluationCallback {
        void onEvaluationCommitClick(int starCount, Set<String> reasons);
    }
}
