package com.xiaweizi.evaluationcardview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.xiaweizi.library.EvaluationCardView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import es.dmoral.toasty.Toasty;

public class EvaluationCardViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation_card_view);
        click();
    }

    public void click() {
        EvaluationCardView cardView = new EvaluationCardView(this);
        List<String> reasonsData = new ArrayList<>();
        reasonsData.add("回复太慢");
        reasonsData.add("对业务不了解");
        reasonsData.add("服务态度差");
        reasonsData.add("问题没有得到解决");
        cardView.setReasonsData(reasonsData);
        cardView.show();
        cardView.setOnEvaluationCallback(new EvaluationCardView.OnEvaluationCallback() {
            @Override
            public void onEvaluationCommitClick(int starCount, Set<String> reasons) {
                StringBuilder sb = new StringBuilder();
                for (String reason : reasons) {
                    sb.append(reason + "\n");
                }
                Toasty.info(EvaluationCardViewActivity.this, "星星数量：" + starCount + "\n差评理由：" + sb.toString(), Toast.LENGTH_LONG, false).show();
            }
        });
    }
}
