package com.example.haneum_d;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class StepActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    Button step1, step2, step3;
    String getSituation;
    String text1, part1;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        getSituation = getIntent().getStringExtra("situation");

        /* 부분 Bold 처리 */
        TextView textview1 = findViewById(R.id.textview1);
        Log.d("situation", getSituation);

        if (getSituation.equals("의학용어") || getSituation.equals("의료절차")) {

            text1 = getSituation + "를 선택하셨군요.\n그럼 학습해볼까요?";
            part1 = "학습해볼까요?";
        }else {
            text1 = getSituation + "을 선택하셨군요.\n그럼 학습해볼까요?";
            part1 = "학습해볼까요?";
        }
        int start = text1.indexOf(part1);
        int end = start + part1.length();

        SpannableString spanString = new SpannableString(text1);
        spanString.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanString.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanString.setSpan(new RelativeSizeSpan(1.0f), start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);

        textview1.setText(spanString);

        step1 = findViewById(R.id.step1);
        step1.setOnClickListener(this);
        step1.setOnTouchListener(this);

        step2 = findViewById(R.id.step2);
        step2.setOnClickListener(this);
        step2.setOnTouchListener(this);

        step3 = findViewById(R.id.step3);
        step3.setOnClickListener(this);
        step3.setOnTouchListener(this);

    }

    @Override
    public void onClick(View v){

        if(v == step1){
            Intent intent = new Intent(getApplicationContext(), StepOneActivity.class);
            intent.putExtra("situation", getSituation);
            startActivity(intent);
        }else if(v == step2){
            Intent intent = new Intent(getApplicationContext(), StepTwoActivity.class);
            intent.putExtra("situation", getSituation);
            startActivity(intent);
        }else if(v == step3){
            Intent intent = new Intent(getApplicationContext(), StepThreeActivity.class);
            intent.putExtra("situation", getSituation);
            startActivity(intent);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event){
        Button temp = null;

        if(v == step1){
            temp = step1;
        }else if(v == step2){
            temp = step2;
        }else if(v == step3){
            temp = step3;
        }

        if(temp != null) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                temp.setTextColor(Color.rgb(255, 255, 255));
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                temp.setTextColor(Color.rgb(0, 0, 0));
            } else if (event.getAction() == MotionEvent.ACTION_MOVE ) {
                if(v.isPressed()){
                    temp.setTextColor(Color.rgb(255, 255, 255));
                }else if(!v.isPressed()){
                    temp.setTextColor(Color.rgb(0, 0, 0));
                }
            }
        }

        return false;
    }
}
