package com.example.haneum_d;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class StepOneActivity extends AppCompatActivity implements View.OnClickListener {
    Activity activity;
    BottomSheetBehavior behavior;
    Button b_restart, b_next, l_restart, l_next;
    LinearLayout result_sheet;
    TextView index_number, sentence;
    ArrayList<StepOneTwo_Class> situation_content;
    ArrayList<Result_Class> resultList;
    Result_Class result_temp;
    String filepath;
    String getSituation;
    ImageView record_start, record_stop, audio_start, record_play;
    StepOneTwo_Class item;
    String recordFile;
    Context context;
    MediaRecorder mediaRecorder;
    TextView sheet_text;
    int size;
    int page = 0;
    int lock = 0;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stepone);

        getSituation = getIntent().getStringExtra("situation");

        resultList = new ArrayList<>();
        result_temp = new Result_Class();

        context = this;
        activity = this;

        index_number = findViewById(R.id.index_number);
        sentence = findViewById(R.id.sentence);

        record_start = findViewById(R.id.record_start);
        record_stop = findViewById(R.id.record_stop);
        record_start.setOnClickListener(this);
        record_stop.setOnClickListener(this);

        record_play = findViewById(R.id.record_play);



        audio_start = findViewById(R.id.audio_start);
        audio_start.setOnClickListener(this);

        filepath = getCacheDir().getAbsolutePath();




        situation_content = new ArrayList<>();

        if (getSituation.equals("의학용어")) { // 병원 상황 & 진료 화제일 경우
            situation_content.add(new StepOneTwo_Class("1", "1,0,0", "병원", "1_0_0_audio.mp3", filepath + "/1_0_0_record"));
            situation_content.add(new StepOneTwo_Class("1", "1,0,1", "도착", "1_0_1_audio.mp3", filepath + "/1_0_1_record"));
            situation_content.add(new StepOneTwo_Class("1", "1,0,2", "환자", "1_0_2_audio.mp3", filepath + "/1_0_2_record"));
            situation_content.add(new StepOneTwo_Class("1", "1,0,3", "예약", "1_0_3_audio.mp3", filepath + "/1_0_3_record"));
            situation_content.add(new StepOneTwo_Class("1", "1,0,4", "접수처", "1_0_4_audio.mp3", filepath + "/1_0_4_record"));
            situation_content.add(new StepOneTwo_Class("1", "1,0,5", "진료실", "1_0_5_audio.mp3", filepath + "/1_0_5_record"));
            situation_content.add(new StepOneTwo_Class("1", "1,0,6", "담당", "1_0_6_audio.mp3", filepath + "/1_0_6_record"));
            situation_content.add(new StepOneTwo_Class("1", "1,0,7", "접수", "1_0_7_audio.mp3", filepath + "/1_0_7_record"));
            situation_content.add(new StepOneTwo_Class("1", "1,0,8", "의사", "1_0_8_audio.mp3", filepath + "/1_0_8_record"));
        }else if (getSituation.equals("질병증상")) { // 병원 상황 & 진료 화제일 경우
            situation_content.add(new StepOneTwo_Class("1", "2,0,0", "시간", "2_0_0_audio.mp3", filepath + "/2_0_0_record"));
            situation_content.add(new StepOneTwo_Class("1", "2,0,1", "치료", "2_0_1_audio.mp3", filepath + "/2_0_1_record"));
            situation_content.add(new StepOneTwo_Class("1", "2,0,2", "방법", "2_0_2_audio.mp3", filepath + "/2_0_2_record"));
            situation_content.add(new StepOneTwo_Class("1", "2,0,3", "약물 치료", "2_0_3_audio.mp3", filepath + "/2_0_3_record"));
            situation_content.add(new StepOneTwo_Class("1", "2,0,4", "물리 치료", "2_0_4_audio.mp3", filepath + "/2_0_4_record"));
            situation_content.add(new StepOneTwo_Class("1", "2,0,5", "일정", "2_0_5_audio.mp3", filepath + "/2_0_5_record"));
            situation_content.add(new StepOneTwo_Class("1", "2,0,6", "회", "2_0_6_audio.mp3", filepath + "/2_0_6_record"));
            situation_content.add(new StepOneTwo_Class("1", "2,0,7", "부작용", "2_0_7_audio.mp3", filepath + "/2_0_7_record"));
            situation_content.add(new StepOneTwo_Class("1", "2,0,8", "드물다", "2_0_8_audio.mp3", filepath + "/2_0_8_record"));
            situation_content.add(new StepOneTwo_Class("1", "2,0,9", "결과", "2_0_9_audio.mp3", filepath + "/2_0_9_record"));
        }else if (getSituation.equals("의료절차")) { // 병원 상황 & 진료 화제일 경우
            situation_content.add(new StepOneTwo_Class("1", "3,0,0", "입원", "3_0_0_audio.mp3", filepath + "/3_0_0_record"));
            situation_content.add(new StepOneTwo_Class("1", "3,0,1", "절차", "3_0_1_audio.mp3", filepath + "/3_0_1_record"));
            situation_content.add(new StepOneTwo_Class("1", "3,0,2", "접수", "3_0_2_audio.mp3", filepath + "/3_0_2_record"));
            situation_content.add(new StepOneTwo_Class("1", "3,0,3", "병실", "3_0_3_audio.mp3", filepath + "/3_0_3_record"));
            situation_content.add(new StepOneTwo_Class("1", "3,0,4", "층", "3_0_4_audio.mp3", filepath + "/3_0_4_record"));
            situation_content.add(new StepOneTwo_Class("1", "3,0,5", "면회", "3_0_5_audio.mp3", filepath + "/3_0_5_record"));
            situation_content.add(new StepOneTwo_Class("1", "3,0,6", "재활", "3_0_6_audio.mp3", filepath + "/3_0_6_record"));
            situation_content.add(new StepOneTwo_Class("1", "3,0,7", "추후", "3_0_7_audio.mp3", filepath + "/3_0_7_record"));
            situation_content.add(new StepOneTwo_Class("1", "3,0,8", "예약", "3_0_8_audio.mp3", filepath + "/3_0_8_record"));
            situation_content.add(new StepOneTwo_Class("1", "3,0,9", "재방문", "3_0_9_audio.mp3", filepath + "/3_0_9_record"));
        }else if (getSituation.equals("응급상황")) { // 병원 상황 & 진료 화제일 경우
            situation_content.add(new StepOneTwo_Class("1", "4,0,0", "감기", "4_0_0_audio.mp3", filepath + "/4_0_0_record"));
            situation_content.add(new StepOneTwo_Class("1", "4,0,1", "증상", "4_0_1_audio.mp3", filepath + "/4_0_1_record"));
            situation_content.add(new StepOneTwo_Class("1", "4,0,2", "기침", "4_0_2_audio.mp3", filepath + "/4_0_2_record"));
            situation_content.add(new StepOneTwo_Class("1", "4,0,3", "콧물", "4_0_3_audio.mp3", filepath + "/4_0_3_record"));
            situation_content.add(new StepOneTwo_Class("1", "4,0,4", "독감", "4_0_4_audio.mp3", filepath + "/4_0_4_record"));
            situation_content.add(new StepOneTwo_Class("1", "4,0,5", "예방", "4_0_5_audio.mp3", filepath + "/4_0_5_record"));
            situation_content.add(new StepOneTwo_Class("1", "4,0,6", "접종", "4_0_6_audio.mp3", filepath + "/4_0_6_record"));
            situation_content.add(new StepOneTwo_Class("1", "4,0,7", "열", "4_0_7_audio.mp3", filepath + "/4_0_7_record"));
            situation_content.add(new StepOneTwo_Class("1", "4,0,8", "몸살", "4_0_8_audio.mp3", filepath + "/4_0_8_record"));
            situation_content.add(new StepOneTwo_Class("1", "4,0,9", "고혈압", "4_0_9_audio.mp3", filepath + "/4_0_9_record"));
            situation_content.add(new StepOneTwo_Class("1", "4,0,10", "복용", "4_0_10_audio.mp3", filepath + "/4_0_10_record"));
        }else if (getSituation.equals("환자지원")) { // 병원 상황 & 진료 화제일 경우
            situation_content.add(new StepOneTwo_Class("1", "5,0,0", "관리", "5_0_0_audio.mp3", filepath + "/5_0_0_record"));
            situation_content.add(new StepOneTwo_Class("1", "5,0,1", "규칙적인", "5_0_1_audio.mp3", filepath + "/5_0_1_record"));
            situation_content.add(new StepOneTwo_Class("1", "5,0,2", "운동", "5_0_2_audio.mp3", filepath + "/5_0_2_record"));
            situation_content.add(new StepOneTwo_Class("1", "5,0,3", "저염식", "5_0_3_audio.mp3", filepath + "/5_0_3_record"));
            situation_content.add(new StepOneTwo_Class("1", "5,0,4", "당뇨", "5_0_4_audio.mp3", filepath + "/5_0_4_record"));
            situation_content.add(new StepOneTwo_Class("1", "5,0,5", "천식", "5_0_5_audio.mp3", filepath + "/5_0_5_record"));
            situation_content.add(new StepOneTwo_Class("1", "5,0,6", "숨", "5_0_6_audio.mp3", filepath + "/5_0_6_record"));
            situation_content.add(new StepOneTwo_Class("1", "5,0,7", "알레르기", "5_0_7_audio.mp3", filepath + "/5_0_7_record"));
            situation_content.add(new StepOneTwo_Class("1", "5,0,8", "먼지", "5_0_8_audio.mp3", filepath + "/5_0_8_record"));
            situation_content.add(new StepOneTwo_Class("1", "5,0,9", "꽃가루", "5_0_9_audio.mp3", filepath + "/5_0_9_record"));
        }

        size = situation_content.size();
        Log.d("error", "위치 1-1");
        item = situation_content.get(page);
        Log.d("error", "위치 1-2");
        sentence.setText(item.getSentence());
        index_number.setText(Integer.toString(page+1) + "/" + situation_content.size());

        /* Bottom Sheet */
        View bottomSheet = findViewById(R.id.result_sheet);
        behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        result_sheet = bottomSheet.findViewById(R.id.result_sheet);

        sheet_text = bottomSheet.findViewById(R.id.sheet_text);

        b_restart = bottomSheet.findViewById(R.id.b_restart);
        b_restart.setOnClickListener(this);
        b_next = bottomSheet.findViewById(R.id.b_next);
        b_next.setOnClickListener(this);

        l_restart = bottomSheet.findViewById(R.id.l_restart);
        l_restart.setOnClickListener(this);
        l_next = bottomSheet.findViewById(R.id.l_next);
        l_next.setOnClickListener(this);




    }

    @Override
    public void onClick(View v){

        if(v == audio_start){
            if(lock == 0) {
                playAssetSound(context, item.getAudiofile());
            }
        }else if(v == record_start){
            if(lock == 0){
                lock = 1;

                record_play.setOnClickListener(null);
                Log.d("error", "2-1");
                record_start.setVisibility(View.GONE);
                record_stop.setVisibility(View.VISIBLE);
                Log.d("error", "2-2");

                Long datetime = System.currentTimeMillis();
                String time = datetime.toString();
                recordFile = item.getRecordfile() + time + ".mp3";

                mediaRecorder = new MediaRecorder();
                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
                mediaRecorder.setOutputFile(recordFile);

                Log.d("record", recordFile);

                try{
                    mediaRecorder.prepare();
                    mediaRecorder.start();
                }catch (IOException e){
                    e.printStackTrace();
                }

            }
        }else if ( v == record_stop){
            if(lock == 1){
                Log.d("error", "3-1");
                record_start.setVisibility(View.VISIBLE);
                record_stop.setVisibility(View.GONE);
                Log.d("error", "3-2");
                if(mediaRecorder != null){
                    mediaRecorder.stop();
                    mediaRecorder.release();
                    mediaRecorder = null;

                    File file = new File(recordFile);

                    API_Connect api_connect = new API_Connect();

                    api_connect.connect(context, "1", file, item.getTextIdx(), new Result_Interface() {

                        @Override
                        public void success(Result_Class result) {

                            result_temp = result;

                            int p_score = (int)Math.floor(result.getTotal_score().getPron_score());
                            if (p_score >= 70) {
                                new ResultBottomSheet(behavior, activity);
                                sheet_text.setText("☑   훌륭해요 !");
                                result_sheet.setBackgroundColor(Color.rgb(82, 206, 214));
                                Log.d("error", "4-1");
                                b_next.setVisibility(View.VISIBLE); // 다음 문제로 넘어가기
                                b_restart.setVisibility(View.GONE);
                                Log.d("error", "4-2");
                                l_restart.setVisibility(View.VISIBLE); // 다시 시도하기
                                l_next.setVisibility(View.GONE);
                                Log.d("error", "4-3");

                            }else if ( p_score <70 && p_score >=40){
                                sheet_text.setText("☑   다시 시도해볼까요?");
                                result_sheet.setBackgroundColor(Color.rgb(255, 187, 40));
                                b_next.setVisibility(View.GONE); // 다음 문제로 넘어가기
                                b_restart.setVisibility(View.VISIBLE);
                                l_restart.setVisibility(View.GONE); // 다시 시도하기
                                l_next.setVisibility(View.VISIBLE);
                                new ResultBottomSheet(behavior, activity);
                            }else if ( p_score < 40){
                                sheet_text.setText("☑   아쉬워요!");
                                result_sheet.setBackgroundColor(Color.rgb(245, 95, 95));
                                b_next.setVisibility(View.GONE); // 다음 문제로 넘어가기
                                b_restart.setVisibility(View.VISIBLE);
                                l_restart.setVisibility(View.GONE); // 다시 시도하기
                                l_next.setVisibility(View.VISIBLE);
                                new ResultBottomSheet(behavior, activity);
                            }

                        }
                        @Override
                        public void failure(Throwable t) {
                            // Display error
                            Log.d("error", "result_interface error");
                        }

                    });

                }
                lock = 0;
                record_play.setOnClickListener(this);

            }
        }else if (v == record_play){
            Log.d("error", "6-1");
            if(lock == 0) {
                Log.d("error", "6-2");
                //playAssetSound(context, recordFile);

                MediaPlayer mediaPlayer;
                mediaPlayer = new MediaPlayer();
                try {

                    mediaPlayer
                            .setDataSource(recordFile);
                    mediaPlayer.prepare();
                } catch (Exception e) {
                }

                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                    public void onPrepared(MediaPlayer mp) {
                        mediaPlayer.start();
                    }
                });


            }
        } else if (v == b_next || v == l_next){
            Log.d("error", "5-1");

            Log.d("size", Integer.toString(size));
            if ( page < size-1) {
                Log.d("error", "5-2");
                resultList.add(result_temp);
                page++;
                item = situation_content.get(page);
                sentence.setText(item.getSentence());
                index_number.setText(Integer.toString(page+1) + "/" + situation_content.size());
                behavior.setState(BottomSheetBehavior.STATE_HIDDEN);

            }else if( page == size-1){


                resultList.add(result_temp);
                behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                // 결과 페이지로 넘어가기
                Log.d("error", "here");
                Intent intent = new Intent(getApplicationContext(), ScoreActivity.class);
                intent.putExtra("stepNum", "1");
                intent.putExtra("situation", getSituation);
                intent.putExtra("resultList", resultList);
                startActivity(intent);
            }

        }else if (v == b_restart || v == l_restart){
            behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            record_play.setOnClickListener(null);


        }

    }

    public static void playAssetSound(Context context, String soundFileName) {
        try {
            MediaPlayer mediaPlayer = new MediaPlayer();

            AssetFileDescriptor descriptor = context.getAssets().openFd(soundFileName);
            mediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();

            mediaPlayer.prepare();
            mediaPlayer.setVolume(1f, 1f);
            mediaPlayer.setLooping(false);
            mediaPlayer.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
