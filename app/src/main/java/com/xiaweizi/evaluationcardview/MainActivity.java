package com.xiaweizi.evaluationcardview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private TagAdapter tagAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EvaluationRatingBar ratingBar = findViewById(R.id.ratingBar);
        ratingBar.setStarTotal(5);
        ratingBar.setSelectedCount(0);
        ratingBar.setOnRatingChangeListener(new EvaluationRatingBar.OnRatingChangeListener() {
            @Override
            public void onChange(int countSelected) {
                Log.i("xwz--->", "MainActivity:  " + "onChange: " + countSelected);
            }
        });

        NegativeReasonsLayout tagLayout = findViewById(R.id.tagLayout);
        tagAdapter = new TagAdapter(this);
        tagLayout.setAdapter(tagAdapter);
        tagLayout.setOnReasonSelectListener(new NegativeReasonsLayout.OnReasonSelectListener() {
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
        dataSource.add("android");
        dataSource.add("安卓");
        dataSource.add("安卓");
        dataSource.add("安卓11111");
        tagAdapter.onlyAddAll(dataSource);
    }

    public void click(View view) {
        EvaluationCardView cardView = new EvaluationCardView(this);
        cardView.show();
    }
}
