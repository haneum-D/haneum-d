package com.example.haneum_d;

public class StepOneTwo_Class {
    private String stepNum;
    private String textIdx;
    private String sentenceStr;
    private String audiofileStr;
    private String recordfileStr;
    private String sentenQ;
    public StepOneTwo_Class(String stepNum, String textIdx, String sentenceStr, String audiofileStr, String recordfileStr) {
        this.stepNum = stepNum;
        this.textIdx= textIdx;
        this.sentenceStr = sentenceStr;
        this.audiofileStr = audiofileStr;
        this.recordfileStr = recordfileStr;
    }

    public StepOneTwo_Class(String stepNum, String textIdx, String sentenceStr, String audiofileStr, String recordfileStr, String sentenQ) {
        this.stepNum = stepNum;
        this.textIdx= textIdx;
        this.sentenceStr = sentenceStr;
        this.audiofileStr = audiofileStr;
        this.recordfileStr = recordfileStr;
        this.sentenQ = sentenQ;
    }

    public void setSentence(String sentence) {
        sentenceStr = sentence;
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
    public void setSenten_q(String senten_q){ sentenQ = senten_q; }
    public String getSentence() {
        return this.sentenceStr;
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
    public String getSenten_q() { return this.sentenQ; };
}
