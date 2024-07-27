package com.example.haneum_d;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ScoreActivity extends AppCompatActivity implements View.OnClickListener {
    String getSituation;
    ArrayList<Result_Class> resultList;
    Button backMenu;
    String stepNum;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        getSituation = getIntent().getStringExtra("situation");
        stepNum = getIntent().getStringExtra("stepNum");
        resultList = new ArrayList<Result_Class>();
        resultList = (ArrayList<Result_Class>) getIntent().getSerializableExtra("resultList");

        int size = resultList.size();
        int pron_score_sum = 0;
        int accuracy_score_sum = 0;
        int completeness_score_sum = 0;
        int fluency_score_sum = 0;

        LinearLayout result_add = findViewById(R.id.result_add);

        View temp_result;
        LayoutInflater layoutInflater = LayoutInflater.from(this);


        backMenu = findViewById(R.id.backMenu);
        backMenu.setOnClickListener(this);

        for(int i = 0; i < size; i++){
            temp_result = layoutInflater.inflate(R.layout.layout_one_score, null, false);

            TextView one_senten = temp_result.findViewById(R.id.one_senten);
            TextView one_type = temp_result.findViewById(R.id.one_type);

            if(stepNum.equals("1")) {
                String sen = resultList.get(i).getWordsScore(0).getWord();
                String type = resultList.get(i).getWordsScore(0).getType();
                one_senten.setText(sen);
                one_type.setText(type);
            }else if(stepNum.equals("2")){
                String type = Integer.toString((int)Math.floor(resultList.get(i).getTotal_score().getPron_score()));
                one_senten.setText("A"+Integer.toString(i+1));
                one_type.setText(type);
            }
            result_add.addView(temp_result);

            pron_score_sum = pron_score_sum + (int) Math.floor(resultList.get(i).getTotal_score().getPron_score());
            accuracy_score_sum = accuracy_score_sum + (int) Math.floor(resultList.get(i).getTotal_score().getAccuracy_score());
            completeness_score_sum = completeness_score_sum + (int) Math.floor(resultList.get(i).getTotal_score().getCompleteness_score());
            fluency_score_sum = fluency_score_sum + (int) Math.floor(resultList.get(i).getTotal_score().getFluency_score());
        }

        TextView pron_score = findViewById(R.id.fin_score);
        TextView accu_score = findViewById(R.id.accu);
        TextView comple_score = findViewById(R.id.comple);
        TextView flu_score = findViewById(R.id.flu);

        pron_score.setText(Integer.toString((int) Math.floor(pron_score_sum/size)));
        accu_score.setText(Integer.toString((int) Math.floor(accuracy_score_sum/size)));
        comple_score.setText(Integer.toString((int) Math.floor(completeness_score_sum/size)));
        flu_score.setText(Integer.toString((int) Math.floor(fluency_score_sum/size)));


    }

    @Override
    public void onClick(View v){
        if (v == backMenu){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }

}
