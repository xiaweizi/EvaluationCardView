package com.xiaweizi.evaluationcardview;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xiaweizi.library.EvaluationNegReasonsLayout;

import java.util.ArrayList;
import java.util.List;

public class EvaluationNegReasonLayoutActivity extends AppCompatActivity {

    private EvaluationNegReasonsLayout mReasonLayout;
    private MyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation_neg_reason_layout);
        mReasonLayout = findViewById(R.id.reason_layout);
        mAdapter = new MyAdapter(this);
        mReasonLayout.setAdapter(mAdapter);
        ArrayList<String> reasonsData = new ArrayList<>();
        reasonsData.add("回复太慢");
        reasonsData.add("对业务不了解");
        reasonsData.add("服务态度差");
        reasonsData.add("问题没有得到解决");
        mAdapter.setData(reasonsData);

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
            View view = View.inflate(mContext, com.xiaweizi.library.R.layout.item_negative_reason, null);
            TextView textView = view.findViewById(com.xiaweizi.library.R.id.tv_negative_reason);
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
