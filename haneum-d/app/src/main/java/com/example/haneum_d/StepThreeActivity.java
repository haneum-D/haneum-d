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
    String getSituation, getChapter;
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
        getChapter = getIntent().getStringExtra("chapter");
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
        //situation_content.add(new StepOneTwo_Class("3", "1,1,1", "접수처", "1_1_0_audio.mp3", filepath + "/1_1_1_record", "병원에 도착하면 어디로 가야 하나요?"));
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        //ID , TOPIC , CHAPTER , STEP_NUM, TEXT_IDX , SENTENCE_A, AUDIOFILE_STR, RECORDFILE_STR, SENTENCE_Q, SUB_TOPIC, KEYWWORD_IDX

        Cursor cursor = db.rawQuery("SELECT * FROM contents WHERE topic = ? AND chapter = ? AND step_num = ?",new String [] {getSituation,getChapter,"3"});
        while (cursor.moveToNext()){
            Log.d("keword_idx", cursor.getString(10));
            situation_content.add(new StepOneTwo_Class(cursor.getString(3), cursor.getString(4), cursor.getString(5),
                    cursor.getString(6), filepath + cursor.getString(7), cursor.getString(8), cursor.getString(10)));
            Log.d("오류위치", "herererere");
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

                record_start.setVisibility(View.VISIBLE);
                record_stop.setVisibility(View.GONE);

                if(mediaRecorder != null){
                    mediaRecorder.stop();
                    mediaRecorder.release();
                    mediaRecorder = null;

                    File file = new File(recordFile);

                    API_Connect api_connect = new API_Connect();
                    Log.d("여기에서 오류가 날까요..", item.getKeywordIdx());
                    api_connect.connect(context, "2", file, item.getKeywordIdx(), new Result_Interface() {

                        @Override
                        public void success(Result_Class result) {

                            record_start.setOnClickListener(null);

                            result_temp = result;

                            if (result_temp.getKeyword().equals("in")) {
                                for (int i = 0; i < result_temp.getWordsScore_size(); i++) {

                                    LayoutInflater layoutInflater = LayoutInflater.from(context);
                                    temp = layoutInflater.inflate(R.layout.layout_three_score, null, false);


                                    TextView score = temp.findViewById(R.id.score);
                                    TextView type = temp.findViewById(R.id.type);

                                    int sss = (int) Math.floor(result_temp.getWordsScore(i).getScore());
                                    String ttt = result_temp.getWordsScore(i).getType();

                                    score.setText(Integer.toString(sss));
                                    type.setText(ttt);

                                    TextView word = temp.findViewById(R.id.word);
                                    String www = result_temp.getWordsScore(i).getWord();
                                    word.setText(www);


                                    result_add.addView(temp);
                                }
                            }

                            String keyword = result.getKeyword();

                            int p_score = (int) Math.floor(result.getTotal_score().getPron_score());
                            if (p_score >= 70 && keyword.equals("in")) {
                                new ResultBottomSheet(behavior, activity);
                                sheet_text.setText("☑   훌륭해요 !");
                                result_sheet.setBackgroundColor(Color.rgb(82, 206, 214));

                                b_next.setVisibility(View.VISIBLE); // 다음 문제로 넘어가기
                                b_restart.setVisibility(View.GONE);

                                l_restart.setVisibility(View.VISIBLE); // 다시 시도하기
                                l_next.setVisibility(View.GONE);


                            }else if ( p_score <70 && p_score >=40 && keyword.equals("in")){
                                sheet_text.setText("☑   다시 시도해볼까요?");
                                result_sheet.setBackgroundColor(Color.rgb(255, 187, 40));
                                b_next.setVisibility(View.GONE); // 다음 문제로 넘어가기
                                b_restart.setVisibility(View.VISIBLE);
                                l_restart.setVisibility(View.GONE); // 다시 시도하기
                                l_next.setVisibility(View.VISIBLE);
                                new ResultBottomSheet(behavior, activity);

                            }else if ( p_score < 40 || keyword.equals("notin")){
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
            record_start.setOnClickListener(this);
            if ( page < size-1) {

                resultList.add(result_temp);
                page++;
                item = situation_content.get(page);
                sentence.setText(item.getSentenceA());
                sentence_q.setText(item.getSentenceQ());

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
                intent.putExtra("chapter",getChapter);
                intent.putExtra("stepNum","3");
                intent.putExtra("resultList", resultList);
                startActivity(intent);
            }

        }else if (v == b_restart || v == l_restart){
            record_start.setOnClickListener(this);
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
