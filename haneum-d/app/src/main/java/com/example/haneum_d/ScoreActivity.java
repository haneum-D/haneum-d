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
    String getSituation, getChapter;
    ArrayList<Result_Class> resultList;
    Button backMenu;
    String stepNum;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        getSituation = getIntent().getStringExtra("situation");
        getChapter = getIntent().getStringExtra("chapter");

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

            if(stepNum.equals("1")) {

                temp_result = layoutInflater.inflate(R.layout.layout_one_score, null, false);

                TextView one_num = temp_result.findViewById(R.id.one_num);
                TextView one_senten = temp_result.findViewById(R.id.one_senten);
                TextView one_score = temp_result.findViewById(R.id.one_score);


                one_num.setText("W_"+ Integer.toString(i+1));

                String sen = resultList.get(i).getRecognized();

                Log.d("여기서 오류나요", sen);
                one_senten.setText(sen);

                double d_score = Double.parseDouble(resultList.get(i).getScore());

                int i_score = (int) Math.floor(d_score);

                one_score.setText(Integer.toString(i_score));

                result_add.addView(temp_result);


                pron_score_sum = pron_score_sum + i_score;
                /*
                if(type.equals("None")){
                    type = "잘했어요! 문제가 없어요";
                }else if(type.equals("Omission")){
                    type = "소리를 빼먹었어요";
                }else if(type.equals("Insertion")){
                    type = "소리를 더 넣었어요";
                }else if(type.equals("Mispronunciation")){
                    type = "소리를 잘 못 말했어요.";
                }else if(type.equals("UnexpectiedBraak") || type.equals("Unexpectied Braak") ){
                    type = "중간에 말을 멈췄어요";

                }else if(type.equals("MissingBreak") || type.equals("Missing Break") ){
                    type = "쉬지 않고 계속 말했어요.";

                }else if(type.equals("Monotone")){
                    type = "악센트를 더 줘야 해요.";

                } */
                Log.d("여기서 오류나요", "오류위치8");

            }else if(stepNum.equals("2")){
                temp_result = layoutInflater.inflate(R.layout.layout_two_score, null, false);

                TextView two_num = temp_result.findViewById(R.id.two_num);
                TextView two_senten = temp_result.findViewById(R.id.two_senten);
                TextView two_score = temp_result.findViewById(R.id.two_score);

                two_num.setText("A_"+Integer.toString(i+1));

                String temp_senten = "";
                int word_size = resultList.get(i).getWordsScore_size();

                for(int j = 0; j < word_size; j++){
                    temp_senten = temp_senten + resultList.get(i).getWordsScore(j).getWord();
                }
                two_senten.setText(temp_senten);

                String score = Integer.toString((int)Math.floor(resultList.get(i).getTotal_score().getPron_score()));

                two_score.setText(score);
                result_add.addView(temp_result);

                pron_score_sum = pron_score_sum + (int) Math.floor(resultList.get(i).getTotal_score().getPron_score());
                accuracy_score_sum = accuracy_score_sum + (int) Math.floor(resultList.get(i).getTotal_score().getAccuracy_score());
                completeness_score_sum = completeness_score_sum + (int) Math.floor(resultList.get(i).getTotal_score().getCompleteness_score());
                fluency_score_sum = fluency_score_sum + (int) Math.floor(resultList.get(i).getTotal_score().getFluency_score());

            }else if(stepNum.equals("3")){
                temp_result = layoutInflater.inflate(R.layout.layout_two_score, null, false);

                TextView two_num = temp_result.findViewById(R.id.two_num);
                TextView two_senten = temp_result.findViewById(R.id.two_senten);
                TextView two_score = temp_result.findViewById(R.id.two_score);

                two_num.setText("A_"+Integer.toString(i+1));

                String temp_senten = "";
                int word_size = resultList.get(i).getWordsScore_size();

                for(int j = 0; j < word_size; j++){
                    temp_senten = temp_senten + resultList.get(i).getWordsScore(j).getWord();
                }
                two_senten.setText(temp_senten);

                String score = Integer.toString((int)Math.floor(resultList.get(i).getTotal_score().getPron_score()));

                two_score.setText(score);
                result_add.addView(temp_result);

                pron_score_sum = pron_score_sum + (int) Math.floor(resultList.get(i).getTotal_score().getPron_score());
                accuracy_score_sum = accuracy_score_sum + (int) Math.floor(resultList.get(i).getTotal_score().getAccuracy_score());
                completeness_score_sum = completeness_score_sum + (int) Math.floor(resultList.get(i).getTotal_score().getCompleteness_score());
                fluency_score_sum = fluency_score_sum + (int) Math.floor(resultList.get(i).getTotal_score().getFluency_score());
            }


        }

        TextView pron_score = findViewById(R.id.fin_score);
        pron_score.setText(Integer.toString((int) Math.floor(pron_score_sum/size)));

        if(stepNum.equals("2") || stepNum.equals("3")) {
            LinearLayout score_layout = findViewById(R.id.score_layout);
            score_layout.setVisibility(View.VISIBLE);

            TextView accu_score = findViewById(R.id.accu);
            TextView comple_score = findViewById(R.id.comple);
            TextView flu_score = findViewById(R.id.flu);

            accu_score.setText(Integer.toString((int) Math.floor(accuracy_score_sum/size)));
            comple_score.setText(Integer.toString((int) Math.floor(completeness_score_sum/size)));
            flu_score.setText(Integer.toString((int) Math.floor(fluency_score_sum/size)));
        }
        
    }

    @Override
    public void onClick(View v){
        if (v == backMenu){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }

}
