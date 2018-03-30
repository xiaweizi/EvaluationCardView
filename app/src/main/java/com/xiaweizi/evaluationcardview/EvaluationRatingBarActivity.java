package com.xiaweizi.evaluationcardview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.xiaweizi.library.EvaluationRatingBar;

public class EvaluationRatingBarActivity extends AppCompatActivity {

    private EvaluationRatingBar mRatingBar;
    private TextView mTvDes;

    private final String[] mDescriptions = new String[]{"非常不满意", "不满意，请积极改善", "一般，还需提升", "满意，服务不错", "非常满意，一百个赞"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation_rating_bar);
        mRatingBar = findViewById(R.id.rating_bar_evaluation);
        mTvDes = findViewById(R.id.tv_description);

        mRatingBar.setOnRatingChangeListener(new EvaluationRatingBar.OnRatingChangeListener() {
            @Override
            public void onChange(int electedCount) {
               if (mTvDes.getVisibility() == View.GONE) {
                   mTvDes.setVisibility(View.VISIBLE);
               }
               if (electedCount > 0 && electedCount <= mDescriptions.length) {
                   mTvDes.setText(mDescriptions[electedCount-1]);
               }
            }
        });
    }
}
