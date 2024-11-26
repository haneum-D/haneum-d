package com.example.haneum_d;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Dimension;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class StepTwoActivity extends AppCompatActivity implements View.OnClickListener {

    Button b_restart, b_next, l_restart, l_next;
    LinearLayout result_sheet;
    TextView index_number, sentence, sentence_q;
    TextView sheet_text;
    ImageView record_start, record_stop, audio_start, record_play;

    ArrayList<StepOneTwo_Class> situation_content;
    ArrayList<Result_Class> resultList;
    Result_Class result_temp;
    StepOneTwo_Class item;
    String filepath;
    String getSituation, getChapter;
    String recordFile;


    Activity activity;
    BottomSheetBehavior behavior;
    Context context;
    MediaRecorder mediaRecorder;

    int size;
    int page = 0;
    int lock = 0;

    TextView t_loading;
    FrameLayout f_loading;
    @Override
    public void onCreate(Bundle savedInstanceState){
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steptwo);

        getSituation = getIntent().getStringExtra("situation");
        getChapter = getIntent().getStringExtra("chapter");

        resultList = new ArrayList<>();
        result_temp = new Result_Class();

        context = this;
        activity = this;

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

        situation_content = new ArrayList<>();

        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        //ID , TOPIC , CHAPTER , STEP_NUM, TEXT_IDX , SENTENCE_A, AUDIOFILE_STR, RECORDFILE_STR, SENTENCE_Q, SUB_TOPIC, KEYWWORD_IDX

        Cursor cursor = db.rawQuery("SELECT * FROM contents WHERE topic = ? AND chapter = ? AND step_num = ?",new String [] {getSituation,getChapter,"2"});
        while (cursor.moveToNext()){
            situation_content.add(new StepOneTwo_Class(cursor.getString(3), cursor.getString(4), cursor.getString(5),
                    cursor.getString(6), filepath + cursor.getString(7), cursor.getString(8)));
        }

        size = situation_content.size();

        item = situation_content.get(page);

        sentence.setText(item.getSentenceA());
        sentence_q.setText(item.getSentenceQ());

        index_number.setText(Integer.toString(page+1) + "/" + situation_content.size());

        /* Bottom Sheet */
        View bottomSheet = findViewById(R.id.result_sheet);
        behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);


        result_sheet = bottomSheet.findViewById(R.id.result_sheet);

        sheet_text = bottomSheet.findViewById(R.id.sheet_text);

        t_loading = findViewById(R.id.t_loading);
        f_loading = findViewById(R.id.f_loading);

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
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                mediaRecorder.setAudioSamplingRate(16000);
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

                new ResultBottomSheet(behavior, activity);
                sheet_text.setText("☑   녹음 된 문장을 확인 중입니다.");
                sheet_text.setTextSize(Dimension.SP, 19);
                result_sheet.setBackgroundColor(Color.parseColor("#D9D9D9"));

                f_loading.setVisibility(View.GONE);
                b_next.setVisibility(View.GONE);
                b_restart.setVisibility(View.GONE);
                t_loading.setVisibility(View.VISIBLE);
                t_loading.setText("잠시만 기다려주세요.");

                record_start.setVisibility(View.VISIBLE);
                record_stop.setVisibility(View.GONE);

                if(mediaRecorder != null){
                    mediaRecorder.stop();
                    mediaRecorder.release();
                    mediaRecorder = null;

                    File file = new File(recordFile);

                    API_Connect api_connect = new API_Connect();

                    api_connect.connect(context, "1", file, item.getTextIdx(), new Result_Interface() {

                        @Override
                        public void success(Result_Class result) {
                            String status = result.getStatus();
                            Log.d("e_status", status);

                            behavior.setState(BottomSheetBehavior.STATE_HIDDEN);

                            record_start.setOnClickListener(null);
                            result_temp = result;

                            if(status.equals("ok")) {


                                int p_score = (int) Math.floor(result.getTotal_score().getPron_score());
                                if (p_score >= 70) {
                                    new ResultBottomSheet(behavior, activity);
                                    sheet_text.setText("☑   훌륭해요 !");
                                    result_sheet.setBackgroundColor(Color.rgb(82, 206, 214));

                                    f_loading.setVisibility(View.VISIBLE);

                                    b_next.setVisibility(View.VISIBLE); // 다음 문제로 넘어가기
                                    b_restart.setVisibility(View.GONE);

                                    l_restart.setVisibility(View.VISIBLE); // 다시 시도하기
                                    l_next.setVisibility(View.GONE);


                                } else if (p_score < 70 && p_score >= 40) {
                                    new ResultBottomSheet(behavior, activity);
                                    sheet_text.setText("☑   다시 시도해볼까요?");
                                    result_sheet.setBackgroundColor(Color.rgb(255, 187, 40));

                                    f_loading.setVisibility(View.VISIBLE);
                                    b_next.setVisibility(View.GONE); // 다음 문제로 넘어가기
                                    b_restart.setVisibility(View.VISIBLE);
                                    l_restart.setVisibility(View.GONE); // 다시 시도하기
                                    l_next.setVisibility(View.VISIBLE);


                                } else if (p_score < 40) {
                                    new ResultBottomSheet(behavior, activity);
                                    sheet_text.setText("☑   아쉬워요!");
                                    result_sheet.setBackgroundColor(Color.rgb(245, 95, 95));

                                    f_loading.setVisibility(View.VISIBLE);
                                    b_next.setVisibility(View.GONE); // 다음 문제로 넘어가기
                                    b_restart.setVisibility(View.VISIBLE);
                                    l_restart.setVisibility(View.GONE); // 다시 시도하기
                                    l_next.setVisibility(View.VISIBLE);

                                }
                            }else if(status.equals("error")){
                                Toast.makeText(context, "다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                            }

                        }
                        @Override
                        public void failure(Throwable t) {
                            // Display error
                            behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                            Toast.makeText(context, "다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                            Log.d("error", "api connect error");
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
            record_start.setOnClickListener(this);
            if ( page < size-1) {

                resultList.add(result_temp);
                page++;
                item = situation_content.get(page);
                sentence.setText(item.getSentenceA());
                sentence_q.setText(item.getSentenceQ());

                index_number.setText(Integer.toString(page+1) + "/" + situation_content.size());
                behavior.setState(BottomSheetBehavior.STATE_HIDDEN);

            }else if( page == size-1){

                resultList.add(result_temp);
                behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                // 결과 페이지로 넘어가기
                Log.d("error", "here");
                Intent intent = new Intent(getApplicationContext(), ScoreActivity.class);
                intent.putExtra("situation", getSituation);
                intent.putExtra("chapter",getChapter);
                intent.putExtra("stepNum","2");
                intent.putExtra("resultList", resultList);
                startActivity(intent);

            }

        }else if (v == b_restart || v == l_restart){

            record_start.setOnClickListener(this);
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
