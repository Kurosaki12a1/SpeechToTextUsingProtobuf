package com.bku.speechtotext.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SpeechRecognitionResult {
    @SerializedName("alternatives")
    @Expose
    List<SpeechRecognitionAlternative> alternatives;
    @SerializedName("languageCode")
    @Expose
    String languageCode;

    public List<SpeechRecognitionAlternative> getAlternatives() {
        return alternatives;
    }
}
