package com.example.haneum_d;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ChapterActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    LinearLayout chapter1, chapter2, chapter3, chapter4;
    TextView chap1_text, chap2_text, chap3_text, chap4_text;

    String getSituation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);

        getSituation = getIntent().getStringExtra("situation");

        /* 부분 Bold 처리 */
        TextView textview1 = findViewById(R.id.textview1);
        String text1 = "[ " + getSituation + " ] 주제를 통해\n한국어를 공부해봅시다.";
        String part1 = "[ " + getSituation + " ] 주제를 통해";

        int start = text1.indexOf(part1);
        int end = start + part1.length();

        SpannableString spanString = new SpannableString(text1);
        spanString.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanString.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanString.setSpan(new RelativeSizeSpan(1.0f), start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);

        textview1.setText(spanString);

        /* 상황 선택 및 Text 설정 */
        chapter1 = findViewById(R.id.chapter1);
        chapter1.setOnClickListener(this);
        chapter1.setOnTouchListener(this);

        chap1_text = chapter1.findViewById(R.id.situation_text);
        chap1_text.setText("Chapter1");


        /* ----------------------------------------------------*/
        chapter2 = findViewById(R.id.chapter2);
        chapter2.setOnClickListener(this);
        chapter2.setOnTouchListener(this);

        chap2_text = chapter2.findViewById(R.id.situation_text);
        chap2_text.setText("Chapter2");

        /* ----------------------------------------------------*/
        chapter3 = findViewById(R.id.chapter3);
        chapter3.setOnClickListener(this);
        chapter3.setOnTouchListener(this);

        chap3_text = chapter3.findViewById(R.id.situation_text);
        chap3_text.setText("Chapter3");

        /* ----------------------------------------------------*/
        chapter4 = findViewById(R.id.chapter4);
        chapter4.setOnClickListener(this);
        chapter4.setOnTouchListener(this);

        chap4_text = chapter4.findViewById(R.id.situation_text);
        chap4_text.setText("Chapter4");

    }

    @Override
    public void onClick(View v){

        String chap_string = null;
        if (v == chapter1){
            chap_string = "chapter1";
        }else if (v == chapter2){
            chap_string = "chapter2";
        }else if (v == chapter3){
            chap_string = "chapter3";
        }else if (v == chapter4){
            chap_string = "chapter4";
        }
        if(getSituation != null) {
            Intent intent = new Intent(getApplicationContext(), StepActivity.class);
            intent.putExtra("situation", getSituation);
            intent.putExtra("chapter", chap_string);
            startActivity(intent);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event){
        TextView temp_text = null;

        if(v == chapter1){
            temp_text = chap1_text;
        }else if(v == chapter2){
            temp_text = chap2_text;
        }else if(v == chapter3){
            temp_text = chap3_text;
        }else if(v == chapter4){
            temp_text = chap4_text;
        }

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            temp_text.setTextColor(Color.rgb(255, 255, 255));
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            temp_text.setTextColor(Color.rgb(0, 0, 0));
        } else if (event.getAction() == MotionEvent.ACTION_MOVE ) {
            if(!v.isPressed()){
                temp_text.setTextColor(Color.rgb(0, 0, 0));
            }else if(v.isPressed()){
                temp_text.setTextColor(Color.rgb(255, 255, 255));
            }
        }

        return false;
    }
}