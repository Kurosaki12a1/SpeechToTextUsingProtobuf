package com.bku.speechtotext.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecognitionAudio {
    @SerializedName("content")
    @Expose
    String content;
    @SerializedName("uri")
    @Expose
    String uri;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
