package com.xiaweizi.library;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
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
    private NegativeReasonsLayout mReasonsLayout;
    private EvaluationRatingBar mRatingBar;
    private TextView mTvDescription;
    private EditText mEtOtherReason;
    private TagAdapter tagAdapter;
    private View mView;
    private String[] mDescriptions = new String[]{"非常不满意", "不满意，请积极改善", "一般，还需提升", "满意，服务不错", "非常满意，一百个赞"};

    public EvaluationCardView(Context context) {
        mContext = context;
        mAlertDialog = new AlertDialog.Builder(context).create();
        if (mAlertDialog.getWindow() != null) {
            mAlertDialog.getWindow().setDimAmount(0);
        }
        mView = View.inflate(mContext, R.layout.layout_evaluation_card, null);
        mReasonsLayout = mView.findViewById(R.id.negative_layout);
        mRatingBar = mView.findViewById(R.id.rating_bar_evaluation);
        mTvDescription = mView.findViewById(R.id.tv_evaluation_description);
        mEtOtherReason = mView.findViewById(R.id.et_other_reason);

        initReasonsLayout(context);
        mRatingBar.setOnRatingChangeListener(new EvaluationRatingBar.OnRatingChangeListener() {
            @Override
            public void onChange(int countSelected) {
                mTvDescription.setVisibility(View.VISIBLE);
                if (countSelected > 0 && countSelected <= mDescriptions.length) {
                    mTvDescription.setText(mDescriptions[countSelected - 1]);
                    mReasonsLayout.setVisibility(countSelected <= 3 ? View.VISIBLE : View.GONE);
                    mEtOtherReason.setVisibility(countSelected <= 3 ? View.VISIBLE : View.GONE);
                }
            }
        });

        mAlertDialog.setView(mView);
    }

    private void initReasonsLayout(Context context) {
        tagAdapter = new TagAdapter(context);
        mReasonsLayout.setAdapter(tagAdapter);
        mReasonsLayout.setOnReasonSelectListener(new NegativeReasonsLayout.OnReasonSelectListener() {
            @Override
            public void onItemSelect(NegativeReasonsLayout parent, List<Integer> selectedList) {
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
        initMobileData();
    }

    private void initMobileData() {
        List<String> dataSource = new ArrayList<>();
        dataSource.add("回复太慢");
        dataSource.add("对业务不太了解");
        dataSource.add("服务态度差");
        dataSource.add("问题没有得到解决");
        tagAdapter.onlyAddAll(dataSource);
    }

    public void show() {
        if (mAlertDialog != null && !mAlertDialog.isShowing()) {
            mAlertDialog.show();
        }
    }
}
