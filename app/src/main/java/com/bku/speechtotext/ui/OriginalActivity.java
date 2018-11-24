package com.bku.speechtotext.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


import com.bku.speechtotext.R;
import com.bku.speechtotext.ui.Fragment.SettingsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;


public class OriginalActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_original);

        ButterKnife.bind(this);

        toolbar.setTitle("Speech To Text");
        setSupportActionBar(toolbar);

        //set Default UI

        renderUI(new SettingsFragment());

    }


 /*   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.removeSettings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }*/




    public void renderUI(Fragment replaceFragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.container, replaceFragment);

        transaction.commit();
    }

  /*  @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }*/
}
