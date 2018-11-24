package com.bku.speechtotext.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecognitionBody {
    @SerializedName("audio")
    @Expose
    RecognitionAudio audio;
    @SerializedName("config")
    @Expose
    RecognitionConfig config;

    public RecognitionAudio getAudio() {
        return audio;
    }

    public void setAudio(RecognitionAudio audio) {
        this.audio = audio;
    }

    public RecognitionConfig getConfig() {
        return config;
    }

    public void setConfig(RecognitionConfig config) {
        this.config = config;
    }
}
