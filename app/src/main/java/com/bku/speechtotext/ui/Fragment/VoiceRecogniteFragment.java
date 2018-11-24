package com.bku.speechtotext.ui.Fragment;

import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bku.speechtotext.R;
import com.bku.speechtotext.data.database.OfflineDatabaseHelper;
import com.bku.speechtotext.data.model.RecognitionAudio;
import com.bku.speechtotext.data.model.RecognitionBody;
import com.bku.speechtotext.data.model.RecognitionConfig;
import com.bku.speechtotext.data.model.SpeechRecognitionAlternative;
import com.bku.speechtotext.data.model.SpeechRecognitionResult;
import com.bku.speechtotext.data.model.SpeechRecognitionResultList;
import com.bku.speechtotext.data.retrofit.GoogleCloudService;
import com.bku.speechtotext.data.retrofit.NetworkHelper;

import com.bku.speechtotext.ui.OriginalActivity;
import com.bku.speechtotext.utils.AudioEncoder;
import com.bku.speechtotext.utils.PermissionUtil;
import com.emrekose.recordbutton.OnRecordListener;
import com.emrekose.recordbutton.RecordButton;

import java.io.File;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class VoiceRecogniteFragment extends Fragment {
    private static final String FILE_NAME = "SpeechToText.3gp";
    private static String TAG = "OriginalActivity";
    @BindView(R.id.btnRecord)
    RecordButton btnRecord;
    @BindView(R.id.txtResult)
    TextView txtResult;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private MediaRecorder mRecorder;
    private String audioPath;
    private boolean recordStarted = false;
    private CompositeDisposable mSubscription = new CompositeDisposable();
    private String strLanguage = "";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_voice_recognite, container, false);
        ButterKnife.bind(this, v);
        setHasOptionsMenu(true);

        checkAndRequestPermission();

        btnRecord.setRecordListener(new OnRecordListener() {
            @Override
            public void onRecord() {
                if (!recordStarted) {
                    startRecord();
                    recordStarted = true;
                }
            }

            @Override
            public void onRecordCancel() {
                onRecordFinish();
            }

            @Override
            public void onRecordFinish() {
                finishRecord();
                recordStarted = false;
            }
        });
        return v;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        switch (strLanguage){
            case "vi-VN":
                inflater.inflate(R.menu.menu_toolbar, menu);
                break;
            case "en-US":
                inflater.inflate(R.menu.menu_toolbar_en, menu);
                break;
                default:
                    break;
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.removeSettings:
                OfflineDatabaseHelper db = new OfflineDatabaseHelper(getActivity());
                switch (strLanguage){
                    case "vi-VN":
                        db.deleteLanguage(getString(R.string.vi_vn));
                        break;
                    case "en-US":
                        db.deleteLanguage(getString(R.string.en_us));
                        break;
                        default:
                            break;
                }
                ((OriginalActivity)Objects.requireNonNull(getActivity())).renderUI(new SettingsFragment());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public static VoiceRecogniteFragment newInstance(String language){
        VoiceRecogniteFragment fragment = new VoiceRecogniteFragment();
        Bundle args = new Bundle();
        args.putString("Language", language);
        fragment.setArguments(args);
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            if(Objects.equals(getArguments().getString("Language"), getString(R.string.vi_vn))){
                strLanguage="vi-VN";
                Log.d(TAG, "onCreate:  Set Vietnamese Language");
            }
            else if(Objects.equals(getArguments().getString("Language"), getString(R.string.en_us))){
                strLanguage="en-US";
                Log.d(TAG, "onCreate: Set English Language");
            }
        }
    }



    private void checkAndRequestPermission() {
        if (!PermissionUtil.checkSelfPermission(getActivity())) {
            PermissionUtil.requestPermission(getActivity());
        }
    }

    private void startRecord() {
        try {
            initRecorder();
            mRecorder.prepare();
            mRecorder.start();
        } catch (Exception e) {
            Log.w(TAG, e);
        }
    }

    private void finishRecord() {
        try {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
            voiceRecognize();
        } catch (Exception e) {
            Log.w(TAG, e);
        }
    }

    private void voiceRecognize() {
        Disposable d = buildRecognitionBody()
                .flatMap(recognitionBody ->
                        NetworkHelper.getRetrofit().create(GoogleCloudService.class)
                                .recognize(NetworkHelper.CLOUD_API_KEY, recognitionBody))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__ -> {
                    showLoading();
                    hideResult();
                })
                .doOnTerminate(this::hideLoading)
                .subscribe(this::showResult, e -> {
                    Log.w(TAG, e);
                    ConnectivityManager cm = (ConnectivityManager) Objects.requireNonNull(getActivity()).getSystemService(Context.CONNECTIVITY_SERVICE);
                    if (cm == null) {
                        Toast.makeText(getActivity(), "Need internet connection!", Toast.LENGTH_LONG).show();
                        return;
                    }
                    NetworkInfo netInfo = cm.getActiveNetworkInfo();
                    if (netInfo == null || !netInfo.isConnected()) {
                        Toast.makeText(getActivity(), "Need internet connection!", Toast.LENGTH_LONG).show();
                    }
                });
        mSubscription.add(d);
    }

    Observable<RecognitionBody> buildRecognitionBody() {
        return Observable.create(emitter -> {
            RecognitionAudio recognitionAudio = new RecognitionAudio();
            recognitionAudio.setContent(AudioEncoder.encodeBase64(audioPath));

            RecognitionConfig recognitionConfig = RecognitionConfig.newBuilder()
                    .setEnableAutomaticPunctuation(true)
                    .setEncoding("AMR")
                    .setLanguage(strLanguage)
                    .setModel("default")
                    .setSampleRateHertz(8000)
                    .build();

            RecognitionBody recognitionBody = new RecognitionBody();
            recognitionBody.setAudio(recognitionAudio);
            recognitionBody.setConfig(recognitionConfig);
            emitter.onNext(recognitionBody);
            emitter.onComplete();
        });
    }

    private void showResult(SpeechRecognitionResultList recognitionResults) {
        List<SpeechRecognitionResult> listResults = recognitionResults.getResult();
        if (listResults == null || listResults.size() <= 0) {
            return;
        }
        List<SpeechRecognitionAlternative> alternatives = listResults.get(0).getAlternatives();
        if (alternatives == null || alternatives.size() <= 0) {
            return;
        }
        txtResult.setVisibility(View.VISIBLE);
        txtResult.setText(alternatives.get(0).getTranscript());
    }

    private void hideResult() {
        txtResult.setVisibility(View.GONE);
    }

    private void initRecorder() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        audioPath = Environment.getExternalStorageDirectory().getPath() + "/" + FILE_NAME;
        File file = new File(audioPath);
        if (file.exists()) {
            file.delete();
        }
        mRecorder.setOutputFile(audioPath);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode != PermissionUtil.ALL_PERMISSION_CODE) {
            return;
        }
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                //finish();
                Objects.requireNonNull(getActivity()).getFragmentManager().popBackStack();
            }
        }
    }

    void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }
}
