package com.bku.speechtotext.data.retrofit;


import com.bku.speechtotext.data.model.RecognitionBody;
import com.bku.speechtotext.data.model.SpeechRecognitionResultList;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface GoogleCloudService {
    @POST("/v1p1beta1/speech:recognize")
    Observable<SpeechRecognitionResultList> recognize(@Query("key") String key, @Body RecognitionBody recognitionBody);
}
