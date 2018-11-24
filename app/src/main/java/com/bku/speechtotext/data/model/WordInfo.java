package com.bku.speechtotext.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WordInfo {
    @SerializedName("startTime")
    @Expose
    String startTime;
    @SerializedName("endTime")
    @Expose
    String endTime;
    @SerializedName("word")
    @Expose
    String word;

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getWord() {
        return word;
    }
}
