package com.bku.speechtotext.utils;

import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

public class AudioEncoder {
    private static String TAG = "AudioEncoder";
    public static String encodeBase64(String path) {
        byte[] audioBytes;
        String audioBase64 = "";
        try {
            File audioFile = new File(path);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            FileInputStream fis = new FileInputStream(audioFile);
            byte[] buf = new byte[1024];
            int n;
            while (-1 != (n = fis.read(buf)))
                baos.write(buf, 0, n);
            audioBytes = baos.toByteArray();

            audioBase64 = Base64.encodeToString(audioBytes, Base64.NO_WRAP);
        } catch (Exception e) {
            Log.w(TAG, e);
        }
        return audioBase64;
    }
}
