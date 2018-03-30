package com.xiaweizi.evaluationcardview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
