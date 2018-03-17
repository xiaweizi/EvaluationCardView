package com.xiaweizi.evaluationcardview;

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
        reasonsData.add("1");
        reasonsData.add("2");
        reasonsData.add("3");
        reasonsData.add("4");
        cardView.setReasonsData(reasonsData);
        cardView.show();
        cardView.setOnEvaluationCallback(new EvaluationCardView.OnEvaluationCallback() {
            @Override
            public void onEvaluationCommitClick(int starCount, Set<String> reasons) {
                Toast.makeText(MainActivity.this, "starCount:" + starCount + "\n reasons:" + reasons.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
