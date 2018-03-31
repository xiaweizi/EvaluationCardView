package com.xiaweizi.evaluationcardview;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaweizi.library.EvaluationNegReasonsLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import es.dmoral.toasty.Toasty;

public class EvaluationNegReasonLayoutActivity extends AppCompatActivity {

    private EvaluationNegReasonsLayout mReasonLayout;
    private MyAdapter mAdapter;
    private Button mBtnAdd;
    private Button mBtnRemove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation_neg_reason_layout);
        mReasonLayout = findViewById(R.id.reason_layout);
        mBtnAdd = findViewById(R.id.btn_add);
        mBtnRemove = findViewById(R.id.btn_remove);
        mAdapter = new MyAdapter(this);
        mReasonLayout.setAdapter(mAdapter);
        List<String> reasonsData = new ArrayList<>();
        reasonsData.add("swift");
        reasonsData.add("Android");
        reasonsData.add("Java");
        reasonsData.add("Python");
        mAdapter.setData(reasonsData);
        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.add(createData());
            }
        });
        mBtnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.remove();
            }
        });
        mReasonLayout.setOnReasonSelectListener(new EvaluationNegReasonsLayout.OnReasonSelectListener() {
            @Override
            public void onItemSelect(EvaluationNegReasonsLayout parent, List<Integer> selectedList) {
                StringBuilder sb = new StringBuilder();
                for (Integer integer : selectedList) {
                    sb.append((String) parent.getAdapter().getItem(integer)).append(",");
                }
                Toasty.info(EvaluationNegReasonLayoutActivity.this, "starCount:\t" + sb.toString(), Toast.LENGTH_SHORT, false).show();
            }
        });
    }

    private String createData() {
        String[] reasons = new String[]{"Android", "C", "PASCAL", "PythonPython", "PHP", "Ruby", "Lua", "JavaScript", "Java", "Virtual Basic", "C++", "C#", "HTML5"};
        Random random = new Random();
        return reasons[random.nextInt(reasons.length-1)];
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

        void remove() {
            if (mData != null) {
                if (mData.size() > 1) {
                    Random random = new Random();
                    int index = random.nextInt(mData.size() - 1);
                    mData.remove(index);
                } else if (mData.size() > 0){
                    mData.remove(0);
                }
            }
            notifyDataSetChanged();
        }

        void add(String reason) {
            if (mData != null) {
                if (mData.size() > 1) {
                    Random random = new Random();
                    int index = random.nextInt(mData.size() - 1);
                    mData.add(index, reason);
                } else {
                    mData.add(reason);
                }
            }
            notifyDataSetChanged();
        }
    }
}
