package com.examkabila.currentaffairs.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;



public class Activity_Settings extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        inti();
    }


    private void inti() {

        TextView txtHeader = (TextView) findViewById(R.id.txtHeader);
        txtHeader.setText("Settings");
        txtHeader.setTextSize(Globals.getAppFontSize_Large(this));
        //Globals.setAppFontTextView(this, txtHeader);
        //set font


        TextView txtSetttigs = (TextView) findViewById(R.id.txtSetttigs);
        TextView txtShare = (TextView) findViewById(R.id.txtShare);
        TextView txtRate = (TextView) findViewById(R.id.txtRate);
        TextView txtNoti = (TextView) findViewById(R.id.txtNotification);
        TextView txtLangu = (TextView) findViewById(R.id.txtDisclaimer);
        TextView txtContact = (TextView) findViewById(R.id.txtAbout);
        txtContact.setTextSize(Globals.getAppFontSize(this));
        txtLangu.setTextSize(Globals.getAppFontSize(this));
        txtNoti.setTextSize(Globals.getAppFontSize(this));
        txtRate.setTextSize(Globals.getAppFontSize(this));
        txtShare.setTextSize(Globals.getAppFontSize(this));
        txtSetttigs.setTextSize(Globals.getAppFontSize_Large(this));

        final CheckBox checkBoxNoti = (CheckBox) findViewById(R.id.checkBoxNotification);


       /* TextView[] txtArray = {txtSetttigs, txtNotification, txtAbout, txtShare, txtRate};
        for (TextView txt : txtArray) {
            Globals.setAppFontTextView(this, txt);
        }*/

        /*txtShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareClick();
            }
        });

        txtRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Custom_ConnectionDetector cd = new Custom_ConnectionDetector(Activity_Settings.this);
                if (cd.isConnectingToInternet()) {
                    Custom_AppRater.rateIt(Activity_Settings.this);

                }
            }
        });*/
        LinearLayout linearLayoutShare = (LinearLayout)findViewById(R.id.linearShare);
        LinearLayout linearLayoutRate = (LinearLayout)findViewById(R.id.linearRate);
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.linearContact);
        checkBoxNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificationEn(checkBoxNoti);
            }
        });



        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationAboutUs();
            }
        });
        linearLayoutShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareClick();
            }
        });
        linearLayoutRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Custom_AppRater.rateIt(Activity_Settings.this);
            }
        });
    }


    private void navigationAboutUs(){
        Intent i = new Intent(this,Activity_ContactUs.class);
        startActivity(i);
    }
    public void changeLan(View v){
        Dialog dialog = onCreateDialogSingleChoice();
        dialog.show();
    }

    public Dialog onCreateDialogSingleChoice() {

//Initialize the Alert Dialog
        int itemSelected;
       final Object_AppConfig config = new Object_AppConfig(this);
        String code = config.getLangCode();
        if(code.equals("hi")){
            itemSelected = 0;
        }else{
            itemSelected = 1;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//Source of the data in the DIalog
        CharSequence[] array = {"Hindi","English"};

// Set the dialog title
        builder.setTitle("Select Language")
// Specify the list array, the items to be selected by default (null for none),
// and the listener through which to receive callbacks when items are selected
                .setSingleChoiceItems(array,itemSelected, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                          if(which==0){
                              config.setLangCode("hi");
                          }else{
                              config.setLangCode("en");
                          }

                     }
                })

// Set the action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
// User clicked OK, so save the result somewhere
// or return them to the component that opened the dialog

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        return builder.create();
    }
    private void notificationEn(CheckBox checkBoxNoti) {
        Object_AppConfig cofig = new Object_AppConfig(Activity_Settings.this);
        if (checkBoxNoti.isChecked()) {
            cofig.setNotificationEnabled(true);
        } else {
            cofig.setNotificationEnabled(false);
        }
    }

    public void shareClick() {

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, Globals.SHARE_APP_MSG + "\n " + Globals.SHARE_LINK_GENERIC);
        //sendIntent.setPackage("com.whatsapp");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

}
