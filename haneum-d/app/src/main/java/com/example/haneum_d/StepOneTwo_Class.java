package com.example.haneum_d;

public class StepOneTwo_Class {
    private String stepNum;
    private String textIdx;
    private String sentenceA;
    private String audiofileStr;
    private String recordfileStr;
    private String sentenceQ;

    private String keywordIdx;

    public StepOneTwo_Class(String stepNum, String textIdx, String sentenceA, String audiofileStr, String recordfileStr) {
        this.stepNum = stepNum;
        this.textIdx= textIdx;
        this.sentenceA = sentenceA;
        this.audiofileStr = audiofileStr;
        this.recordfileStr = recordfileStr;

    }

    public StepOneTwo_Class(String stepNum, String textIdx, String sentenceA, String audiofileStr, String recordfileStr, String sentenceQ) {
        this.stepNum = stepNum;
        this.textIdx= textIdx;
        this.sentenceA = sentenceA;
        this.audiofileStr = audiofileStr;
        this.recordfileStr = recordfileStr;
        this.sentenceQ = sentenceQ;

    }

    public StepOneTwo_Class(String stepNum, String textIdx, String sentenceA, String audiofileStr, String recordfileStr, String sentenceQ, String keywordIdx) {
        this.stepNum = stepNum;
        this.textIdx= textIdx;
        this.sentenceA = sentenceA;
        this.audiofileStr = audiofileStr;
        this.recordfileStr = recordfileStr;
        this.sentenceQ = sentenceQ;
        this.keywordIdx = keywordIdx;
    }

    public void setSentence(String sentence_a) {
        sentenceA = sentence_a;
    }
    public void setAudiofile(String audiofiletitle) {
        audiofileStr = audiofiletitle;
    }
    public void setRecordfile(String recordfiletitle){
        recordfileStr = recordfiletitle;
    }
    public void setStepNum(String step){
        stepNum = step;
    }
    public void setTextIdx(String text){
        textIdx = text;
    }
    public void setSenten_q(String sentence_q){ sentenceQ = sentence_q; }
    public void setKeywordIdx(String keyword_idx){ keywordIdx = keyword_idx; }
    public String getSentenceA() {
        return this.sentenceA;
    }
    public String getAudiofile(){
        return this.audiofileStr;
    }
    public String getRecordfile(){
        return this.recordfileStr;
    }

    public String getStepNum(){
        return this.stepNum;
    }
    public String getTextIdx(){
        return this.textIdx;
    }
    public String getSentenceQ() { return this.sentenceQ; }
    public String getKeywordIdx(){return this.keywordIdx; }
}
