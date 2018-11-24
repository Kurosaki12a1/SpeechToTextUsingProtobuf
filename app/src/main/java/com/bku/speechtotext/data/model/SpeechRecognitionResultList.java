package com.bku.speechtotext.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SpeechRecognitionResultList {
    @SerializedName("results")
    @Expose
    List<SpeechRecognitionResult> result;

    public List<SpeechRecognitionResult> getResult() {
        return result;
    }
}
