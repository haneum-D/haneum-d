package com.example.haneum_d;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    LinearLayout situation1, situation2, situation3, situation4, situation5, situation6;
    TextView situ1_text, situ2_text, situ3_text, situ4_text, situ5_text, situ6_text;
    DBHelper dbHelper;
    SQLiteDatabase db_write;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //EdgeToEdge.enable(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {

            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/

        /* DataBase */
        dbHelper = new DBHelper(this);
        db_write = dbHelper.getWritableDatabase();


        /* 권한 요청 */ // Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,
        ActivityCompat.requestPermissions(this , new String[]{ Manifest.permission.RECORD_AUDIO},0);

        /* Toolbar */
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled((false));

        Resources resource = getResources();

        int status_bar_id = 0;
        int navigation_bar_id = 0;

//        int status_bar_h = 0;
//        int navigation_bar_h = 0;


        status_bar_id = resource.getIdentifier("status_bar_height", "dimen", "android");
        navigation_bar_id = resource.getIdentifier("navigation_bar_height", "dimen", "android");

//        if (status_bar_id != 0){
//            status_bar_h = resource.getDimensionPixelSize(status_bar_id);
//        }
//
//        if(navigation_bar_id !=0){
//            navigation_bar_h = resource.getDimensionPixelSize(navigation_bar_id);
//        }

//        LinearLayout inner = findViewById(R.id.inner);
//        inner.setPadding(0,
//                status_bar_h,
//                0,
//                navigation_bar_h);

        LinearLayout inner = findViewById(R.id.inner);
        inner.setPadding(0,
                resource.getDimensionPixelSize(status_bar_id),
                0,
                resource.getDimensionPixelSize(navigation_bar_id));

        /* 상황 선택 및 Text 설정 */
        situation1 = findViewById(R.id.situation1);
        situation1.setOnClickListener(this);
        situation1.setOnTouchListener(this);

        situ1_text = situation1.findViewById(R.id.situation_text);
        situ1_text.setText("대학생활");

        /* ----------------------------------------------------*/
        situation2 = findViewById(R.id.situation2);
        situation2.setOnClickListener(this);
        situation2.setOnTouchListener(this);

        situ2_text = situation2.findViewById(R.id.situation_text);
        situ2_text.setText("K-문화");

        /* ----------------------------------------------------*/
        situation3 = findViewById(R.id.situation3);
        situation3.setOnClickListener(this);
        situation3.setOnTouchListener(this);

        situ3_text = situation3.findViewById(R.id.situation_text);
        situ3_text.setText("K-음식");

        /* ----------------------------------------------------*/
        situation4 = findViewById(R.id.situation4);
        situation4.setOnClickListener(this);
        situation4.setOnTouchListener(this);

        situ4_text = situation4.findViewById(R.id.situation_text);
        situ4_text.setText("지역관광");

        /* ----------------------------------------------------*/
        situation5 = findViewById(R.id.situation5);
        situation5.setOnClickListener(this);
        situation5.setOnTouchListener(this);

        situ5_text = situation5.findViewById(R.id.situation_text);
        situ5_text.setText("취업면접");

        /* ----------------------------------------------------*/
        situation6 = findViewById(R.id.situation6);
        situation6.setOnClickListener(this);
        situation6.setOnTouchListener(this);

        situ6_text = situation6.findViewById(R.id.situation_text);
        situ6_text.setText("병원가기");


    }

    @Override
    public void onClick(View v){

        String situ_string = null;
        if (v == situation1){
            situ_string = "대학생활";
        }else if (v == situation2){
            situ_string = "K-문화";
        }else if (v == situation3){
            situ_string = "K-음식";
        }else if (v == situation4){
            situ_string = "지역관광";
        }else if (v == situation5){
            situ_string = "취업면접";
        }else if (v == situation6){
            situ_string = "병원가기";
        }

        if(situ_string != null) {
            Intent intent = new Intent(getApplicationContext(), ChapterActivity.class);
            intent.putExtra("situation", situ_string);
            startActivity(intent);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event){
        TextView temp_text = null;

        if(v == situation1){
            temp_text = situ1_text;
        }else if(v == situation2){
            temp_text = situ2_text;
        }else if(v == situation3){
            temp_text = situ3_text;
        }else if(v == situation4){
            temp_text = situ4_text;
        }else if(v == situation5){
            temp_text = situ5_text;
        }else if(v == situation6){
            temp_text = situ6_text;
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