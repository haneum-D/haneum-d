package com.example.haneum_d;

public class ContentsInfo { //ID , TOPIC , CHAPTER , STEP_NUM, TEXT_IDX , SENTENCE_A, AUDIOFILE_STR, RECORDFILE_STR, SENTENCE_Q, SUB_TOPIC, KEYWWORD_IDX
    public static final String TABLE_NAME = "contents";
    public static final String COLUMN_ID = "id"; // primary_key
    public static final String COLUMN_TOPIC = "topic";
    public static final String COLUMN_CHAPTER = "chapter";
    public static final String COLUMN_STEP_NUM = "step_num";
    public static final String COLUMN_TEXT_IDX = "text_idx";
    public static final String COLUMN_SENTENCE_A = "sentenceA"; // 추후 변수명 변경하기 : sentenA
    public static final String COLUMN_AUDIOFILE_STR = "audiofile_str";
    public static final String COLUMN_RECORDFILE_STR = "recordfile_str";
    public static final String COLUMN_SENTENCE_Q = "sentenceQ"; // step2 & step3
    public static final String COLUMN_SUB_TOPIC = "sub_topic"; // step2 & step3
    public static final String COLUMN_KEYWORD_IDX = "keyword_idx"; // step3
    public static final String COLUMN_HCHECK = "hcheck";


    // STEP1
    // stepNum, textIdx, sentenceStr, audiofileStr, recordfildStr
    //"1", "1,0,0", "학교", "1_0_0_audio.wav", filepath + "/1_0_0_record"

    // STEP2
    // stepNum, textIdx, sentenceStr, audiofileStr, recordfileStr, sentenQ
    // "2", "1,1,1", "캠퍼스가 정말 좋아.", "1_1_1_audio.mp3", filepath + "/1_1_1_record", "우리 학교 어때?"

    // STEP3
    // stepNum, textIdx, sentenceStr, audiofileStr, recordfileStr, sentenQ
    // "3", "1,1,1", "캠퍼스,좋아.", "1_1_1_audio.mp3", filepath + "/1_1_1_record", "우리 학교 어때?"

}
