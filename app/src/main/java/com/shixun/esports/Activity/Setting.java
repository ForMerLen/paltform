package com.shixun.esports.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageButton;

import com.shixun.esports.MainActivity;
import com.shixun.esports.R;

public class Setting extends AppCompatActivity implements View.OnClickListener{

    private ImageButton exit;
    SharedPreferences sprfMain;
    SharedPreferences.Editor editorMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        initView();
        initEvent();

    }

    private void initEvent() {
        exit.setOnClickListener(this);
    }

    private void initView() {
        exit = (ImageButton)findViewById(R.id.exit);
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.exit:
                sprfMain= PreferenceManager.getDefaultSharedPreferences(this);
                editorMain=sprfMain.edit();
                editorMain.clear();
                editorMain.commit();
                Intent intent = new Intent();
                intent.setClass(Setting.this, MainActivity.class);
                startActivity(intent);
                ActivityCompat.finishAffinity(this);
        }
    }

}
