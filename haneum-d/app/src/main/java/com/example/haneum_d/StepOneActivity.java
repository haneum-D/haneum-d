package com.example.haneum_d;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class StepOneActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int[] SAMPLE_RATE_CANDIDATES = new int[]{16000, 11025, 22050, 44100};
    private static final int CHANNEL = AudioFormat.CHANNEL_IN_MONO;
    private static final int ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    private Handler handler = new Handler();
    byte[] buffer, data;
    Activity activity;
    BottomSheetBehavior behavior;
    Button b_restart, b_next, l_restart, l_next;
    LinearLayout result_sheet;
    TextView index_number, sentence;
    ArrayList<StepOneTwo_Class> situation_content;
    ArrayList<Result_Class> resultList;
    Result_Class result_temp;
    String filepath;
    String getSituation, getChapter;
    ImageView record_start, record_stop, audio_start, record_play;
    StepOneTwo_Class item;
    String recordFile, tempFilepath;
    Context context;
    MediaRecorder mediaRecorder;
    TextView sheet_text;
    ImageView word_img;

    AudioRecord audioRecord;
    boolean IsRecording = false;
    BufferedOutputStream outputStream;

    BufferedInputStream inputStream;
    File waveFile, tempFile;


    private final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    private final int RECORDER_CHANNELS = AudioFormat.CHANNEL_CONFIGURATION_MONO;  //안드로이드 녹음시 채널 상수값
    private final int WAVE_CHANNEL_MONO = 1;  //wav 파일 헤더 생성시 채널 상수값
    private final int HEADER_SIZE = 0x2c;
    private final int RECORDER_BPP = 16;
    private final int RECORDER_SAMPLERATE = 0xac44;
    private int BUFFER_SIZE = 0;
    private int mAudioLen = 0;

    int size;
    int page = 0;
    int lock = 0;

    int read = 0;
    int len = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stepone);

        getSituation = getIntent().getStringExtra("situation");
        getChapter = getIntent().getStringExtra("chapter");

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
        tempFilepath = getCacheDir().getAbsolutePath() + "/tempFile.bak";

        situation_content = new ArrayList<>();

        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        //ID , TOPIC , CHAPTER , STEP_NUM, TEXT_IDX , SENTENCE_A, AUDIOFILE_STR, RECORDFILE_STR, SENTENCE_Q, SUB_TOPIC, KEYWWORD_IDX

        Cursor cursor = db.rawQuery("SELECT * FROM contents WHERE topic = ? AND chapter = ? AND step_num = ?",new String [] {getSituation,getChapter, "1"});
        while (cursor.moveToNext()){
            situation_content.add(new StepOneTwo_Class(cursor.getString(3), cursor.getString(4), cursor.getString(5),
                    cursor.getString(6), filepath + cursor.getString(7)));
            Log.d("오류위치", "herererere");
        }

        // 대학생활, K-문화, K-음식, 지역관광, 취업면접, 병원가기 : chapter1 ~ 4

        size = situation_content.size();
        item = situation_content.get(page);
        sentence.setText(item.getSentenceA());
        index_number.setText(Integer.toString(page + 1) + "/" + situation_content.size());
        word_img = findViewById(R.id.word_img);

        /* 단어별 이미지
        if(item.getTextIdx().equals("4,0,0")) {
            word_img.setImageResource(R.drawable.a0);
        }
        */
        Random rand = new Random();
        int ran = rand.nextInt(5);

        if (ran == 0) {
            word_img.setImageResource(R.drawable.cha2);
        } else if (ran == 1) {
            word_img.setImageResource(R.drawable.cha3);
        } else if (ran == 2) {
            word_img.setImageResource(R.drawable.cha4);
        } else if (ran == 3) {
            word_img.setImageResource(R.drawable.cha5);
        } else if (ran == 4) {
            word_img.setImageResource(R.drawable.cha5);
        }

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
    public void onClick(View v) {

        if (v == audio_start) {
            if (lock == 0) {
                playAssetSound(context, item.getAudiofile());
            }
        } else if (v == record_start) {
            if (lock == 0) {
                lock = 1;

                record_play.setOnClickListener(null);

                record_start.setVisibility(View.GONE);
                record_stop.setVisibility(View.VISIBLE);


                Long datetime = System.currentTimeMillis();
                String time = datetime.toString();
                recordFile = item.getRecordfile() + time + ".mp3";
                //recordFile = item.getRecordfile() + time + ".wav";


                mediaRecorder = new MediaRecorder();
                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                mediaRecorder.setAudioSamplingRate(16000);
                mediaRecorder.setOutputFile(recordFile);


                try {
                    mediaRecorder.prepare();
                    mediaRecorder.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }



                /* .wav 로 녹음하기
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        BUFFER_SIZE = AudioRecord.getMinBufferSize(RECORDER_SAMPLERATE, RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING);
                        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                            audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, RECORDER_SAMPLERATE, RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING, BUFFER_SIZE);
                            //audioRecord = new AudioRecord(MediaRecorder.AudioSource.VOICE_RECOGNITION, RECORDER_SAMPLERATE, RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING, BUFFER_SIZE);
                            audioRecord.startRecording();
                            IsRecording = true;
                            Log.d("작동하나요", "제발");
                            writeAudioDataToFile();


                        }

                    }
                };

                Thread th = new Thread(runnable);
                th.start();
                */

            }
        } else if (v == record_stop) {

            if (lock == 1) {

                /* .wav로 녹음하기
                IsRecording = false;

                audioRecord.stop();
                audioRecord.release();
                audioRecord = null;
                */

                record_start.setVisibility(View.VISIBLE);
                record_stop.setVisibility(View.GONE);



                if (mediaRecorder != null) {
                    mediaRecorder.stop();
                    mediaRecorder.release();
                    mediaRecorder = null;

                    File file = new File(recordFile);

                    API_Connect api_connect = new API_Connect();
                    Log.d("file_name", recordFile);
                    Log.d("item_idx", item.getTextIdx());
                    api_connect.connect(context, "1", file, item.getTextIdx(), new Result_Interface() {

                        @Override
                        public void success(Result_Class result) {

                            record_start.setOnClickListener(null);
                            result_temp = result;
                            Log.d("word_score_ss", result.getScore());
                            Double p_score = Double.parseDouble(result.getScore());

                            //int p_score = (int)Math.floor(Double.parseDouble(result.getScore()));

                            Log.d("word_score", Double.toString(p_score));
                            if (p_score >= 55) {
                                new ResultBottomSheet(behavior, activity);
                                sheet_text.setText("☑   훌륭해요 !");
                                result_sheet.setBackgroundColor(Color.rgb(82, 206, 214));

                                b_next.setVisibility(View.VISIBLE); // 다음 문제로 넘어가기
                                b_restart.setVisibility(View.GONE);

                                l_restart.setVisibility(View.VISIBLE); // 다시 시도하기
                                l_next.setVisibility(View.GONE);

                            } else if (p_score < 55 && p_score >= 40) {
                                new ResultBottomSheet(behavior, activity);
                                sheet_text.setText("☑   다시 시도해볼까요?");
                                result_sheet.setBackgroundColor(Color.rgb(255, 187, 40));
                                b_next.setVisibility(View.GONE); // 다음 문제로 넘어가기
                                b_restart.setVisibility(View.VISIBLE);
                                l_restart.setVisibility(View.GONE); // 다시 시도하기
                                l_next.setVisibility(View.VISIBLE);

                            } else if (p_score < 40) {
                                new ResultBottomSheet(behavior, activity);
                                sheet_text.setText("☑   아쉬워요!");
                                result_sheet.setBackgroundColor(Color.rgb(245, 95, 95));
                                b_next.setVisibility(View.GONE); // 다음 문제로 넘어가기
                                b_restart.setVisibility(View.VISIBLE);
                                l_restart.setVisibility(View.GONE); // 다시 시도하기
                                l_next.setVisibility(View.VISIBLE);

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
        } else if (v == record_play) {

            if (lock == 0) {

                //playAssetSound(context, recordFile);

                MediaPlayer mediaPlayer;
                mediaPlayer = new MediaPlayer();
                Log.d("play_file_name", recordFile);
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
        } else if (v == b_next || v == l_next) {

            record_start.setOnClickListener(this);

            if (page < size - 1) {

                resultList.add(result_temp);
                page++;
                item = situation_content.get(page);
                sentence.setText(item.getSentenceA());
                index_number.setText(Integer.toString(page + 1) + "/" + situation_content.size());

                /*
                if(item.getTextIdx().equals("4,0,0")) {
                    word_img.setImageResource(R.drawable.a0); //0
                }
                */

                Random rand = new Random();
                int ran = rand.nextInt(5);

                if (ran == 0) {
                    word_img.setImageResource(R.drawable.cha2);
                } else if (ran == 1) {
                    word_img.setImageResource(R.drawable.cha3);
                } else if (ran == 2) {
                    word_img.setImageResource(R.drawable.cha4);
                } else if (ran == 3) {
                    word_img.setImageResource(R.drawable.cha5);
                } else if (ran == 4) {
                    word_img.setImageResource(R.drawable.cha5);
                }

                behavior.setState(BottomSheetBehavior.STATE_HIDDEN);

            } else if (page == size - 1) {


                resultList.add(result_temp);
                behavior.setState(BottomSheetBehavior.STATE_HIDDEN);

                // 결과 페이지로 넘어가기

                Intent intent = new Intent(getApplicationContext(), ScoreActivity.class);
                intent.putExtra("stepNum", "1");
                intent.putExtra("situation", getSituation);
                intent.putExtra("chapter", getChapter);
                intent.putExtra("resultList", resultList);
                startActivity(intent);
            }

        } else if (v == b_restart || v == l_restart) {
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


    /* .wav로 녹음하기
    private void writeAudioDataToFile(){
        buffer = new byte[BUFFER_SIZE];
        data = new byte[BUFFER_SIZE];
        waveFile = new File(recordFile);
        tempFile = new File(tempFilepath);

        try {
            outputStream = new BufferedOutputStream(new FileOutputStream(tempFile));
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }

        if (null != outputStream) {
            try {
                Log.d("여기가 무한반복?", "111");
                while (IsRecording) {
                    read = audioRecord.read(data, 0, BUFFER_SIZE);
                    if (AudioRecord.ERROR_INVALID_OPERATION != read) {
                        outputStream.write(data);
                    }
                }
                Log.d("여기가 무한반복?", "222");
                outputStream.flush();
                mAudioLen = (int) tempFile.length();
                inputStream = new BufferedInputStream(new FileInputStream(tempFile));
                outputStream.close();
                outputStream = new BufferedOutputStream(new FileOutputStream(waveFile));
                outputStream.write(getFileHeader());
                len = HEADER_SIZE;
                while ((read = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer);
                }
                outputStream.flush();
                inputStream.close();
                outputStream.close();
            } catch (IOException e1) {

                e1.printStackTrace();
            }
        }
    }

    private byte[] getFileHeader() {
        byte[] header = new byte[HEADER_SIZE];
        int totalDataLen = mAudioLen + 40;
        long byteRate = RECORDER_BPP * RECORDER_SAMPLERATE * WAVE_CHANNEL_MONO / 8;
        header[0] = 'R';  // RIFF/WAVE header
        header[1] = 'I';
        header[2] = 'F';
        header[3] = 'F';
        header[4] = (byte) (totalDataLen & 0xff);
        header[5] = (byte) ((totalDataLen >> 8) & 0xff);
        header[6] = (byte) ((totalDataLen >> 16) & 0xff);
        header[7] = (byte) ((totalDataLen >> 24) & 0xff);
        header[8] = 'W';
        header[9] = 'A';
        header[10] = 'V';
        header[11] = 'E';
        header[12] = 'f';  // 'fmt ' chunk
        header[13] = 'm';
        header[14] = 't';
        header[15] = ' ';
        header[16] = 16;  // 4 bytes: size of 'fmt ' chunk
        header[17] = 0;
        header[18] = 0;
        header[19] = 0;
        header[20] = (byte) 1;  // format = 1 (PCM방식)
        header[21] = 0;
        header[22] = WAVE_CHANNEL_MONO;
        header[23] = 0;
        header[24] = (byte) (RECORDER_SAMPLERATE & 0xff);
        header[25] = (byte) ((RECORDER_SAMPLERATE >> 8) & 0xff);
        header[26] = (byte) ((RECORDER_SAMPLERATE >> 16) & 0xff);
        header[27] = (byte) ((RECORDER_SAMPLERATE >> 24) & 0xff);
        header[28] = (byte) (byteRate & 0xff);
        header[29] = (byte) ((byteRate >> 8) & 0xff);
        header[30] = (byte) ((byteRate >> 16) & 0xff);
        header[31] = (byte) ((byteRate >> 24) & 0xff);
        header[32] = (byte) RECORDER_BPP * WAVE_CHANNEL_MONO / 8;  // block align
        header[33] = 0;
        header[34] = RECORDER_BPP;  // bits per sample
        header[35] = 0;
        header[36] = 'd';
        header[37] = 'a';
        header[38] = 't';
        header[39] = 'a';
        header[40] = (byte) (mAudioLen & 0xff);
        header[41] = (byte) ((mAudioLen >> 8) & 0xff);
        header[42] = (byte) ((mAudioLen >> 16) & 0xff);
        header[43] = (byte) ((mAudioLen >> 24) & 0xff);

        return header;
    }

    */

}
