package com.example.haneum_d;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Result_Class implements Serializable {

    @SerializedName("status")
    private String status;

    /* ------------------------------- */
    @Expose
    @SerializedName("recognized")
    private String recognized;

    @Expose
    @SerializedName("score")
    private String score;

    /* ------------------------------- */
    @Expose
    @SerializedName("total_score")
    private TotalScore total_score;

    @Expose
    @SerializedName("words_score")
    private ArrayList<WordsScore> words_score;

    @Expose
    @SerializedName("stt_result")
    private String stt_result;

    @Expose
    @SerializedName("keyword")
    private String keyword;

    public String getStatus(){
        return status;
    }

    /* ------------------------------- */

    public String getRecognized(){
        return recognized;
    }

    public String getScore(){
        return score;
    }

    /* ------------------------------- */

    public TotalScore getTotal_score(){
        return total_score;
    }

    public int getWordsScore_size(){
        return words_score.size();
    }
    public WordsScore getWordsScore(int id){
        return words_score.get(id);
    }

    public String getStt_result(){
        return stt_result;
    }

    public String getKeyword(){return keyword; }

}


class TotalScore implements Serializable{
    @SerializedName("pron_score")
    private Double pron_score;

    @SerializedName("accuracy_score")
    private Double accuracy_score;

    @SerializedName("completeness_score")
    private Double completeness_score;

    @SerializedName("fluency_score")
    private Double fluency_score;

    public Double getPron_score(){
        return pron_score;
    }
    public Double getAccuracy_score(){
        return accuracy_score;
    }
    public Double getCompleteness_score(){
        return completeness_score;
    }
    public Double getFluency_score(){
        return fluency_score;
    }
}

class WordsScore implements Serializable{
    @SerializedName("word")
    private String word;

    @SerializedName("score")
    private Double score;

    @SerializedName("type")
    private String type;

    public String getWord(){
        return word;
    }

    public Double getScore(){
        return score;
    }

    public String getType(){
        return type;
    }
}
