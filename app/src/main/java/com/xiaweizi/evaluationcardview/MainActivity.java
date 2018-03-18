package com.xiaweizi.evaluationcardview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.xiaweizi.library.EvaluationCardView;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void click(View view) {
        EvaluationCardView cardView = new EvaluationCardView(this);
        ArrayList<String> reasonsData = new ArrayList<>();
        reasonsData.add("回复太慢");
        reasonsData.add("对业务不了解");
        reasonsData.add("服务态度差");
        reasonsData.add("问题没有得到解决");
        cardView.setReasonsData(reasonsData);
        cardView.show();
        cardView.setOnEvaluationCallback(new EvaluationCardView.OnEvaluationCallback() {
            @Override
            public void onEvaluationCommitClick(int starCount, Set<String> reasons) {
                Toast.makeText(MainActivity.this, "starCount:" + starCount + "\n reasons:" + reasons.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void ratingBar(View view) {
        startActivity(new Intent(this, EvaluationRatingBarActivity.class));
    }

    public void reasonLayout(View view) {
        startActivity(new Intent(this, EvaluationNegReasonLayoutActivity.class));
    }

    public void cardView(View view) {
        startActivity(new Intent(this, EvaluationCardViewActivity.class));
    }
}
