package com.bku.speechtotext.ui.Fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.bku.speechtotext.R;
import com.bku.speechtotext.data.database.OfflineDatabaseHelper;
import com.bku.speechtotext.data.database.SettingsLanguage;
import com.bku.speechtotext.ui.OriginalActivity;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsFragment extends Fragment {

    @BindView(R.id.spinnerChooseLanguage)
    Spinner spinLanguage;

    @BindView(R.id.btnConfirm)
    Button btnConfirm;

    String[] language = {"Tiếng Việt","English"};

    OfflineDatabaseHelper db;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this,v);

        db = new OfflineDatabaseHelper(getActivity());

        getDatabase();
        setSpinner();

        spinLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        btnConfirm.setText(getString(R.string.confirm));
                        break;
                    case 1:
                        btnConfirm.setText(getString(R.string.confirm_en));
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return v;
    }


    @OnClick(R.id.btnConfirm)
    public void onConfirm(){
        showDialog(spinLanguage.getSelectedItem().toString());
    }

    private void setDatabase(){
        db.addLanguage(new SettingsLanguage(spinLanguage.getSelectedItem().toString()));
    }

    private void getDatabase(){
        if(!db.getLanguageFromData().getLanguage().equals("default")) {
            ((OriginalActivity)getActivity()).renderUI(VoiceRecogniteFragment.newInstance(db.getLanguageFromData().getLanguage()));

        }
    }


    private void showDialog(String result) {

        final Dialog dialog = new Dialog(Objects.requireNonNull(getActivity()), R.style.DialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_save);
        Objects.requireNonNull(dialog.getWindow()).setGravity(Gravity.CENTER);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView txtInfo = (TextView) dialog.findViewById(R.id.information);
        TextView txtNoti = (TextView)dialog.findViewById(R.id.titleDialog);
        Button btnAgree = (Button) dialog.findViewById(R.id.agree);
        Button btnDecline = (Button)dialog.findViewById(R.id.decline);


        switch (result) {
            case "Tiếng Việt":
                txtInfo.setText(getString(R.string.save_option));
                txtNoti.setText(getString(R.string.noti_title));
                btnAgree.setText(getString(R.string.agree));
                btnDecline.setText(getString(R.string.decline));
                break;
            case "English":
                txtInfo.setText(getString(R.string.save_option_en));
                txtNoti.setText(getString(R.string.noti_title_en));
                btnAgree.setText(getString(R.string.agree_en));
                btnDecline.setText(getString(R.string.decline_en));
                break;

        }

        dialog.show();

        // avoid leak window better use dismiss
        btnAgree.setOnClickListener(v ->{
            setDatabase();
            dialog.dismiss();
            ((OriginalActivity)getActivity()).renderUI(VoiceRecogniteFragment.newInstance(result));

        });


        btnDecline.setOnClickListener(v -> {
            dialog.dismiss();
            ((OriginalActivity)getActivity()).renderUI(VoiceRecogniteFragment.newInstance(result));
        });
    }




    private void setSpinner(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()), android.R.layout.simple_spinner_item, language);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinLanguage.setAdapter(adapter);

        //Vietnamese default
        spinLanguage.setSelection(0);
    }
}
