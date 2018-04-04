package com.paohdigitalyouth.paohkeyboard;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class SettingActivity extends AppCompatActivity {
    CheckBox fontCheck;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        sharedPreferences = getSharedPreferences("Htetz",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        boolean check = sharedPreferences.getBoolean("embedFont",true);
        fontCheck = findViewById(R.id.fontCheck);
        fontCheck.setChecked(check);
        fontCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                editor.putBoolean("embedFont",b);
                editor.commit();
                editor.apply();
//                    new SimpleKeyboard().onStartInputView(null,true);
            }
        });
    }
}
