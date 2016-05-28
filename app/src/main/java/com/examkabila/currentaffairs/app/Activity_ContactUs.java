package com.examkabila.currentaffairs.app;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Activity_ContactUs extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        inti();
    }

    private void inti() {

        TextView txtHeader = (TextView) findViewById(R.id.txtHeader);
        txtHeader.setText("About us");
        txtHeader.setTextSize(Globals.getAppFontSize_Large(this));



        TextView txtAboutusheader = (TextView) findViewById(R.id.txtAboutusheader);
        TextView txtAboutusContent = (TextView) findViewById(R.id.txtAboutusContent);
        txtAboutusContent.setTextSize(Globals.getAppFontSize(this));
        txtAboutusheader.setTextSize(Globals.getAppFontSize(this));
    }
}
