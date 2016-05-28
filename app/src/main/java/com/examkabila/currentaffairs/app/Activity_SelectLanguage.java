package com.examkabila.currentaffairs.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Activity_SelectLanguage extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_language);
        inti();
    }

    private void inti(){
        TextView txtHeader = (TextView)findViewById(R.id.txtHeader);
        txtHeader.setText("Select Language");
        txtHeader.setTextSize(Globals.getAppFontSize_Large(this));
        final Object_AppConfig config = new Object_AppConfig(this);
        Button btnHindi = (Button) findViewById(R.id.btnHindi);
        Button btnEnglish = (Button)findViewById(R.id.btnEnglish);
        btnEnglish.setTextSize(Globals.getAppFontSize(this));
        btnHindi.setTextSize(Globals.getAppFontSize(this));
        btnHindi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                config.SetisLanguageSelect(1);
                config.setLangCode("hi");
                naviHome();
            }
        });

        btnEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                config.SetisLanguageSelect(1);
                config.setLangCode("en");
                naviHome();
            }
        });
    }

    private void naviHome(){
        Intent i = new Intent(this,Activity_Home.class);
        startActivity(i);
        this.finish();
    }
}
