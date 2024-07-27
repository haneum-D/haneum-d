package com.example.haneum_d;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class StepThreeActivity extends AppCompatActivity implements View.OnClickListener {
    Activity activity;
    BottomSheetBehavior behavior;
    Button b_restart, b_next, l_restart, l_next;
    LinearLayout result_sheet;
    TextView index_number, sentence, sentence_q;
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

    LinearLayout result_add;
    View temp;

    int size = 0;
    int page = 0;
    int lock = 0;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stepthree);

        getSituation = getIntent().getStringExtra("situation");

        resultList = new ArrayList<>();
        result_temp = new Result_Class();

        context = this;
        activity = this;
        Log.d("hi", "1");
        index_number = findViewById(R.id.index_number);
        sentence = findViewById(R.id.sentence);
        sentence_q = findViewById(R.id.sentence_q);

        record_start = findViewById(R.id.record_start);
        record_stop = findViewById(R.id.record_stop);
        record_start.setOnClickListener(this);
        record_stop.setOnClickListener(this);

        record_play = findViewById(R.id.record_play);


        audio_start = findViewById(R.id.audio_start);
        audio_start.setOnClickListener(this);

        filepath = getCacheDir().getAbsolutePath();


        result_add = findViewById(R.id.result_add); // scroll View

        situation_content = new ArrayList<>();

        if (getSituation.equals("의학용어")) { // 병원 상황 & 진료 화제일 경우
            situation_content.add(new StepOneTwo_Class("3", "1,1,1", "접수처", "1_1_0_audio.mp3", filepath + "/1_1_1_record", "병원에 도착하면 어디로 가야 하나요?"));
            situation_content.add(new StepOneTwo_Class("3", "1,2,1", "오후 2시", "1_2_0_audio.mp3", filepath + "/1_2_1_record", "병원 예약 시간이 몇 시 인가요?"));
            situation_content.add(new StepOneTwo_Class("3", "1,3,1", "2층", "1_3_0_audio.mp3", filepath + "/1_3_1_record", "진료실 위치는 어디인가요?"));
            situation_content.add(new StepOneTwo_Class("3", "1,4,1", "접수처", "1_4_0_audio.mp3", filepath + "/1_4_1_record", "진료 접수를 하려면 어디로 가야하나요?"));
            situation_content.add(new StepOneTwo_Class("3", "1,5,1", "담당 의사, 김박사님", "1_5_0_audio.mp3", filepath + "/1_5_1_record", "담당 의사는 누구인가요?"));

        }else if (getSituation.equals("질병증상")) { // 병원 상황 & 진료 화제일 경우
            situation_content.add(new StepOneTwo_Class("3", "2,1,1", "30분", "2_1_0_audio.mp3", filepath + "/2_1_1_record", "진료 시간은 얼마나 걸리나요?"));
            situation_content.add(new StepOneTwo_Class("3", "2,2,1", "약물 치료, 물리 치료", "2_2_0_audio.mp3", filepath + "/2_2_1_record", "치료 방법에는 어떤 방법이 있나요?"));
            situation_content.add(new StepOneTwo_Class("3", "2,3,1", "주 3회", "2_3_0_audio.mp3", filepath + "/2_3_1_record", "치료 일정은 어떻게 진행되나요?"));
            situation_content.add(new StepOneTwo_Class("3", "2,4,1", "드물지만, 있을 수 있음", "2_4_0_audio.mp3", filepath + "/2_4_1_record","치료 후 부작용이 있을까요?"));
            situation_content.add(new StepOneTwo_Class("3", "2,5,1", "다음 주", "2_5_0_audio.mp3", filepath + "/2_5_1_record","치료 결과를 언제 알 수 있을까요?"));

        }else if (getSituation.equals("의료절차")) { // 병원 상황 & 진료 화제일 경우
            situation_content.add(new StepOneTwo_Class("3", "3,1,1", "접수처 안내", "3_1_0_audio.mp3", filepath + "/3_1_1_record", "입원 절차는 어떻게 진행되나요?"));
            situation_content.add(new StepOneTwo_Class("3", "3,2,1", "3층", "3_2_0_audio.mp3", filepath + "/3_2_1_record", "병실 위치가 어디인가요?"));
            situation_content.add(new StepOneTwo_Class("3", "3,3,1", "오후 2시, 4시까지", "3_3_0_audio.mp3", filepath + "/3_3_1_record", "면회 시간은 언제인가요?"));
            situation_content.add(new StepOneTwo_Class("3", "3,4,1", "다음 주, 월요일", "3_4_0_audio.mp3", filepath + "/3_4_1_record", "재활 치료 시작 시기는 언제인가요?"));
            situation_content.add(new StepOneTwo_Class("3", "3,5,1", "다음 달", "3_5_0_audio.mp3", filepath + "/3_5_1_record", "재방문 일정은 언제인가요?"));

        }else if (getSituation.equals("응급상황")) { // 병원 상황 & 진료 화제일 경우
            situation_content.add(new StepOneTwo_Class("3", "4,1,1", "기침, 콧물", "4_1_0_audio.mp3", filepath + "/4_1_1_record", "감기 증상이 있나요?"));
            situation_content.add(new StepOneTwo_Class("3", "4,2,1", "하루 세 번, 식후 30분", "4_2_0_audio.mp3", filepath + "/4_2_1_record", "감기약 복용 방법에 대해 말씀해주세요."));
            situation_content.add(new StepOneTwo_Class("3", "4,3,1", "지난 가을", "4_3_0_audio.mp3", filepath + "/4_3_1_record", "독감 예방 접종을 받았나요?"));
            situation_content.add(new StepOneTwo_Class("3", "4,4,1", "열, 몸살", "4_4_0_audio.mp3", filepath + "/4_4_1_record", "증상이 어떤 가요?"));
            situation_content.add(new StepOneTwo_Class("3", "4,5,1", "매일, 아침", "4_5_0_audio.mp3", filepath + "/4_5_1_record", "고혈압 약을 복용하고 있나요?"));

        }else if (getSituation.equals("환자지원")) { // 병원 상황 & 진료 화제일 경우
            situation_content.add(new StepOneTwo_Class("3", "5,1,1", "운동, 저염식", "5_1_0_audio.mp3", filepath + "/5_1_1_record", "고혈압 관리 방법에 대해 알려주세요."));
            situation_content.add(new StepOneTwo_Class("3", "5,2,1", "2년 전", "5_2_0_audio.mp3", filepath + "/5_2_1_record", "환자가 당뇨 진단을 받았나요?"));
            situation_content.add(new StepOneTwo_Class("3", "5,3,1", "혈당 측정, 식단 조절", "5_3_0_audio.mp3", filepath + "/5_3_1_record0", "당뇨 관리 방법에 대해 말씀해주세요."));
            situation_content.add(new StepOneTwo_Class("3", "5,4,1", "숨이 참, 기침", "5_4_0_audio.mp3", filepath + "/5_4_1_record", "환자의 증상을 설명해주시겠어요?"));
            situation_content.add(new StepOneTwo_Class("3", "5,5,1", "꽃가루, 먼지", "5_5_0_audio.mp3", filepath + "/5_5_1_record", "알레르기 증상이 있나요?"));

        }

        size = situation_content.size();

        item = situation_content.get(page);

        sentence.setText(item.getSentence());
        sentence_q.setText(item.getSenten_q());

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

                record_start.setVisibility(View.GONE);
                record_stop.setVisibility(View.VISIBLE);


                Long datetime = System.currentTimeMillis();
                String time = datetime.toString();
                recordFile = item.getRecordfile() + time + ".mp3";

                mediaRecorder = new MediaRecorder();
                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
                mediaRecorder.setOutputFile(recordFile);

                try{
                    mediaRecorder.prepare();
                    mediaRecorder.start();
                }catch (IOException e){
                    e.printStackTrace();
                }

            }
        }else if ( v == record_stop){
            if(lock == 1){

                record_start.setVisibility(View.VISIBLE);
                record_stop.setVisibility(View.GONE);

                if(mediaRecorder != null){
                    mediaRecorder.stop();
                    mediaRecorder.release();
                    mediaRecorder = null;

                    File file = new File(recordFile);

                    API_Connect api_connect = new API_Connect();

                    api_connect.connect(context, "2", file, item.getTextIdx(), new Result_Interface() {

                        @Override
                        public void success(Result_Class result) {

                            result_temp = result;
                            for(int i=0; i < result_temp.getWordsScore_size(); i++){

                                LayoutInflater layoutInflater = LayoutInflater.from(context);
                                temp = layoutInflater.inflate(R.layout.layout_three_score, null, false);

                                TextView word = temp.findViewById(R.id.word);
                                TextView score = temp.findViewById(R.id.score);
                                TextView type = temp.findViewById(R.id.type);

                                String www = result_temp.getWordsScore(i).getWord();
                                int sss = (int)Math.floor(result_temp.getWordsScore(i).getScore());
                                String ttt = result_temp.getWordsScore(i).getType();

                                word.setText(www);
                                score.setText(Integer.toString(sss));
                                type.setText(ttt);

                                result_add.addView(temp);
                            }

                            int p_score = (int) Math.floor(result.getTotal_score().getPron_score());
                            if (p_score >= 70) {
                                new ResultBottomSheet(behavior, activity);
                                sheet_text.setText("☑   훌륭해요 !");
                                result_sheet.setBackgroundColor(Color.rgb(82, 206, 214));

                                b_next.setVisibility(View.VISIBLE); // 다음 문제로 넘어가기
                                b_restart.setVisibility(View.GONE);

                                l_restart.setVisibility(View.VISIBLE); // 다시 시도하기
                                l_next.setVisibility(View.GONE);


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
            if(lock == 0) {

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

            if ( page < size-1) {

                resultList.add(result_temp);
                page++;
                item = situation_content.get(page);
                sentence.setText(item.getSentence());
                sentence_q.setText(item.getSenten_q());

                index_number.setText(Integer.toString(page+1) + "/" + situation_content.size());

                int child = result_add.getChildCount();
                for(int i = 0; i < child; i++){
                    result_add.removeView(result_add.getChildAt(0));
                }

                behavior.setState(BottomSheetBehavior.STATE_HIDDEN);




            }else if( page == size-1){


                resultList.add(result_temp);
                behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                // 결과 페이지로 넘어가기
                Log.d("error", "here");
                Intent intent = new Intent(getApplicationContext(), ScoreActivity.class);
                intent.putExtra("situation", getSituation);
                intent.putExtra("stepNum","2");
                intent.putExtra("resultList", resultList);
                startActivity(intent);
            }

        }else if (v == b_restart || v == l_restart){
            behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            record_play.setOnClickListener(null);

            int child = result_add.getChildCount();
            for(int i = 0; i < child; i++){
                result_add.removeView(result_add.getChildAt(0));
            }



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
