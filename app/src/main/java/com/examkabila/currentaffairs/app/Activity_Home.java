package com.examkabila.currentaffairs.app;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Activity_Home extends Activity {
    private ListView listViewOptions;
    private ArrayList<String> listOptions;
    public static boolean isPushNoti = false;
    public static String android_id = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        android_id = Settings.Secure.getString(getBaseContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        inti();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(isPushNoti){
            isPushNoti = false;
            if(!GCMIntentService.pushMessageHeader.equals("")){
                //openDialog();
                Globals.showAlertDialogOneButton("Message",GCMIntentService.pushMessageHeader +"\n"+GCMIntentService.pushMessageText, this, "OK", null, false);
            }
        }
    }

    private void inti() {
        TextView txtHeader = (TextView) findViewById(R.id.txtHeader);
        txtHeader.setText("Home");
        txtHeader.setTextSize(Globals.getAppFontSize_Large(this));
        //txtDate.setTextSize(Globals.getAppFontSize(this));
        ImageView imgOption = (ImageView)findViewById(R.id.btnAction);
        imgOption.setVisibility(View.VISIBLE);
        imgOption.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                onClickOptionsHome(v);
            }
        });
        ImageView imgRead = (ImageView)findViewById(R.id.imgRead);
        ImageView imgQuiz= (ImageView)findViewById(R.id.imgQuiz);
        ImageView imgVideos = (ImageView)findViewById(R.id.imgVideos);
        ImageView imgMore = (ImageView)findViewById(R.id.imgMore);
        setParams(imgRead);
        setParams(imgQuiz);
        setParams(imgVideos);
        setParams(imgMore);


    }
    private void setParams(ImageView img){
        LinearLayout.LayoutParams lpImage = (LinearLayout.LayoutParams)img.getLayoutParams();
        int marginDefault =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());
        int width = (Globals.getScreenSize(this).x  -(marginDefault*3))/ 2;
        // int heightLinear = Globals.getScreenSize(this).y/3;
       // int height = width;
        lpImage.width = width;
        lpImage.height = width;
        img.setLayoutParams(lpImage);
    }
    public void naviQuiz(View v){
        Intent i = new Intent(this,Activity_Quiz.class);
        i.putExtra("date","");
        startActivity(i);
    }

    public void naviVideo(View v){
        Intent i = new Intent(this,Activity_Video_List.class);
        i.putExtra("date","");
        startActivity(i);
    }
    public void naviMore(View v){
        Intent i = new Intent(this,Activity_MoreApps.class);
       // i.putExtra("date","");
        startActivity(i);
    }

    public void naviSettings(View v){
        Intent i = new Intent(this,Activity_Settings.class);
        startActivity(i);
    }
    public void naviDownload(View v){
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(Custom_ServerURL_Params.getURL_Download()));
        this.startActivity(i);
    }

    public void naviRead(View v){
        Intent i = new Intent(this,Activity_Read.class);
        startActivity(i);
    }
    public void onClickOptionsHome(View v) {


        if (listViewOptions == null) {
            //initOptionsList();
            listViewOptions = (ListView)findViewById(R.id.listViewOptions);
        }

        toggleOptionsVisibility(null);
    }

    public void toggleOptionsVisibility(View v) {

        LinearLayout parent = (LinearLayout)findViewById(R.id.llytOptionsContainer);
        if (listViewOptions.getVisibility() == View.VISIBLE) {
            // Its visible
            parent.setVisibility(View.GONE);
            listViewOptions.setVisibility(View.INVISIBLE);
        } else {
            // Either gone or invisible
            initOptionsList();
            parent.setVisibility(View.VISIBLE);
            listViewOptions.setVisibility(View.VISIBLE);
        }

    }

    public void initOptionsList() {

        // new
        // ListView(this);
        // int width = Globals.getScreenSize(this).x / 2;

        // RelativeLayout.LayoutParams lp = new
        // RelativeLayout.LayoutParams(width,RelativeLayout.LayoutParams.WRAP_CONTENT);
        // lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        // lp.addRule(RelativeLayout.BELOW,R.id.llytHomeHeader);

        // listViewOptions.setLayoutParams(lp);
        // listViewOptions.setBackgroundResource(R.color.app_black);
        // listViewOptions.setPadding(2, 2, 2, 2);
        // listViewOptions.setVisibility(View.GONE);

        String[] values = new String[] {"Settings"};



        listOptions = new ArrayList<String>();

        for (int i = 0; i < values.length; i++) {

			/*Object_Options obj = new Object_Options();*/
			/*obj.setText(values[i]);
			if(listDrawable.size() > i)
				obj.setStateDrawable(listDrawable.get(i));*/
            //obj.setImageResourceId(imgIds[i]);
            listOptions.add(values[i]);
        }

		/*Custom_AdapterOptions adapter = new Custom_AdapterOptions(this,
				listOptions);*/
        List<Map<String, String>> data = new ArrayList<Map<String,String>>();

        for(int i = 0;i<listOptions.size();i++)
        {
            Map<String, String> map = new HashMap<String, String>();
            map.put("Name", listOptions.get(i));

            data.add(map);
        }

        SimpleAdapter adaptor = new SimpleAdapter(this, data, R.layout.list_row_action_selection, new String[]{"Name"}, new int[]{R.id.txtCityName});
        listViewOptions.setAdapter(adaptor);

        // RelativeLayout root =
        // (RelativeLayout)findViewById(R.id.rlytHomeRoot);
        // root.addView(listViewOptions);

        listViewOptions.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int pos,
                                    long id) {

                optionSelected(pos);
            }


        });
    }

    private void optionSelected(int pos) {
        toggleOptionsVisibility(null);

        if (listOptions != null && listOptions.size() > pos) {
            String name = listOptions.get(pos);
            if (name != null) {

                Class<?> nextClass = null;

                if(name.equals("Settings")){
                    nextClass = Activity_Settings.class;
                }
                if (nextClass != null) {
                    Intent intent = new Intent(this, nextClass);
                    startActivity(intent);
                }
            }
        }

    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Globals.showAlertDialog("Alert", "Do you really want to exit ?",
                    this, "Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {

                        }
                    }, "Exit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {

                           // Dialogback();
                            Activity_Home.this.finish();
                        }
                    }, false);
        }
        return false;
        // Disable back button..............
    }


}
