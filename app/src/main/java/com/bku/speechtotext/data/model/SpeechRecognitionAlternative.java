package com.bku.speechtotext.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SpeechRecognitionAlternative {
    @SerializedName("transcript")
    @Expose
    String transcript;
    @SerializedName("confidence")
    @Expose
    double confidence;
    @SerializedName("words")
    @Expose
    List<WordInfo> words;

    public String getTranscript() {
        return transcript;
    }

    public double getConfidence() {
        return confidence;
    }

    public List<WordInfo> getWords() {
        return words;
    }
}
