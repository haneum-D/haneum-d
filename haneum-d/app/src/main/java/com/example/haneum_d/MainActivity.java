package com.example.haneum_d;

import android.Manifest;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    LinearLayout situation1, situation2, situation3, situation4, situation5;
    TextView situ1_text, situ2_text, situ3_text, situ4_text, situ5_text;

    LinearLayout l_temp;
    TextView t_temp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        /* 권한 요청 */
        ActivityCompat.requestPermissions(this , new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO},0);

        /* Toolbar */
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled((false));

        /* 부분 Bold 처리 */
        TextView textview1 = findViewById(R.id.textview1);
        String text1 = "병원에서의 상황을 선택하여\n한국어를 공부해봅시다.";
        String part1 = "병원에서의 상황을 선택";

        int start = text1.indexOf(part1);
        int end = start + part1.length();

        SpannableString spanString = new SpannableString(text1);
        spanString.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanString.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanString.setSpan(new RelativeSizeSpan(1.0f), start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);

        textview1.setText(spanString);

        /* 상황 선택 및 Text 설정 */
        situation1 = findViewById(R.id.situation1);
        situation1.setOnClickListener(this);
        situation1.setOnTouchListener(this);

        situ1_text = situation1.findViewById(R.id.situation_text);
        situ1_text.setText("의학\n용어");


        /* ----------------------------------------------------*/
        situation2 = findViewById(R.id.situation2);
        situation2.setOnClickListener(this);
        situation2.setOnTouchListener(this);

        situ2_text = situation2.findViewById(R.id.situation_text);
        situ2_text.setText("질병\n증상");

        /* ----------------------------------------------------*/
        situation3 = findViewById(R.id.situation3);
        situation3.setOnClickListener(this);
        situation3.setOnTouchListener(this);

        situ3_text = situation3.findViewById(R.id.situation_text);
        situ3_text.setText("의료\n절차");

        /* ----------------------------------------------------*/
        situation4 = findViewById(R.id.situation4);
        situation4.setOnClickListener(this);
        situation4.setOnTouchListener(this);

        situ4_text = situation4.findViewById(R.id.situation_text);
        situ4_text.setText("응급\n상황");

        /* ----------------------------------------------------*/
        situation5 = findViewById(R.id.situation5);
        situation5.setOnClickListener(this);
        situation5.setOnTouchListener(this);

        situ5_text = situation5.findViewById(R.id.situation_text);
        situ5_text.setText("환자\n지원");

        /* ----------------------------------------------------*/
        l_temp = findViewById(R.id.temp);

        t_temp = l_temp.findViewById(R.id.situation_text);
        t_temp.setText("추후 추가 예정");
        t_temp.setTextColor(Color.rgb(128,128,128));


    }

    @Override
    public void onClick(View v){

        String situ_string = null;
        if (v == situation1){
            situ_string = "의학용어";
        }else if (v == situation2){
            situ_string = "질병증상";
        }else if (v == situation3){
            situ_string = "의료절차";
        }else if (v == situation4){
            situ_string = "응급상황";
        }else if (v == situation5){
            situ_string = "환자지원";
        }

        if(situ_string != null) {
            Intent intent = new Intent(getApplicationContext(), StepActivity.class);
            intent.putExtra("situation", situ_string);
            startActivity(intent);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event){
        TextView temp_text = null;

        if(v == situation1){
            temp_text = situ1_text;
            Log.d("situ1", "냐셔111");
        }else if(v == situation2){

            temp_text = situ2_text;
        }else if(v == situation3){

            temp_text = situ3_text;
        }else if(v == situation4){
            temp_text = situ4_text;
        }else if(v == situation5){
            temp_text = situ5_text;
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