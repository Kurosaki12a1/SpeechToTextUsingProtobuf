package com.bku.speechtotext.network;

import com.google.common.io.ByteStreams;
import com.google.protobuf.InvalidProtocolBufferException;

import bku.Dataspeech;

public class ProtoRequest {

    public Dataspeech.postRequest postRequest(String strLanguage,String content){
        Dataspeech.postRequest.CONFIG config=Dataspeech.postRequest.CONFIG.newBuilder()
                .setEnableAutomaticPunctuation(true)
                .setEncoding("AMR")
                .setLanguageCode(strLanguage)
                .setModel("default")
                .setSampleRateHertz(8000)
                .build();

        Dataspeech.postRequest.AUDIO audio=Dataspeech.postRequest.AUDIO.newBuilder()
                .setUri(content)
                .build();

        return  Dataspeech.postRequest.newBuilder()
                .setAudio(audio)
                .setConfig(config)
                .build();
    }

    public Dataspeech.getRequest getRequest(byte[] data) throws InvalidProtocolBufferException {
        return Dataspeech.getRequest.parseFrom(data);
    }
}
