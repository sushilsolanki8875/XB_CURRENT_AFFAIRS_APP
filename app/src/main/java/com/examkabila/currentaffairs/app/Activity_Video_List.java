package com.examkabila.currentaffairs.app;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class Activity_Video_List extends Activity_Parent_Banner_Ads {
      private ProgressDialog pd;
      private ListView listView;
    private TextView txtDate;
    private int year;
    private int month;
    private int day;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        inti();
        Custom_ConnectionDetector cd = new Custom_ConnectionDetector(this);
        if(cd.isConnectingToInternet()) {
            Intent intent = getIntent();
            String myDate = intent.getStringExtra("date");
            if(myDate.equals(""))
                getvideoServer();
            else{
                getDayMonYear(myDate);
                txtDate.setText(Globals.getdateFormat(day,month+1,year));
                getvideoServer();
            }

        }else {
            Globals.showAlert("ERROR", Globals.INTERNET_ERROR, this);
            LinearLayout linear = (LinearLayout)findViewById(R.id.linearCard);
            linear.setVisibility(View.GONE);
        }
    }


    private void inti() {

        TextView txtHeader = (TextView) findViewById(R.id.txtHeader);
        txtHeader.setText("Videos");
        LinearLayout linearDate = (LinearLayout)findViewById(R.id.linearDate);
        linearDate.setVisibility(View.VISIBLE);
        txtDate = (TextView)findViewById(R.id.date);
        txtHeader.setTextSize(Globals.getAppFontSize_Large(this));
        txtDate.setTextSize(Globals.getAppFontSize_Large(this));
        ImageView imagedate = (ImageView)findViewById(R.id.calender);
        imagedate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // showDialog(DATE_DIALOG_ID);
                setDate();

            }
        });
        txtDate.setClickable(true);
        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate();
            }
        });
        setCurrentDateOnview();
    }
    public void setCurrentDateOnview() {


        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        //txtDate.setText(month + 1 + "-" + day + "-" + year);
        //txtDate.setText(stringComplete(day)+"-"+stringComplete(month+1)+"-"+stringComplete(year));
        //txtDate.setText(stringComplete(year)+"-"+stringComplete(month+1)+"-"+stringComplete(day));
        txtDate.setText(Globals.getdateFormat(day, month + 1, year));
        // dpresult.init(year, month, day, null);

    }

    private String stringComplete(int c){
        String st = "";
        if(c<10){
            st = "0"+c;
        }else{
            st = c+"";
        }
        return st;
    }
    private void setDate(){
        DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int selectedYear,
                                          int selectedMonth, int selectedDay) {
                      /* txtDate.setText(dayOfMonth + "-"
                               + (monthOfYear + 1) + "-" + year);*/
                        year = selectedYear;
                        month= selectedMonth;
                        day = selectedDay;
                        //txtDate.setText(stringComplete(day)+"-"+stringComplete(month+1)+"-"+stringComplete(year));
                        //txtDate.setText(stringComplete(year)+"-"+stringComplete(month+1)+"-"+stringComplete(day));
                        txtDate.setText(Globals.getdateFormat(day, month+1, year));
                        getvideoServer();
                    }
                }, year, (month), day);
        dpd.show();
    }

    public void naviYouTube(View v){
        Log.i("SUSHIL","url call "+Custom_ServerURL_Params.getURL_youtube());
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(Custom_ServerURL_Params.getURL_youtube()));
        this.startActivity(i);
    }

    public void naviVideoPlayer(View v){
        Intent i = new Intent(this,Activity_Video_Player.class);
        startActivity(i);
    }
    private void getvideoServer() {
        try {
            pd = Globals.showLoadingDialog(pd, this, false, "");
           Object_AppConfig config = new Object_AppConfig(this);
            //Object_Local_Config config = new Object_Local_Config(this);
            String dateTime = stringComplete(year)+"-"+stringComplete(month+1)+"-"+stringComplete(day);
            Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(
                    Request.Method.POST,
                    Custom_ServerURL_Params.getVideoList(),
                    Custom_ServerURL_Params.getParams_GK_Test(config.getLangCode(),dateTime,this), new Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.i("SUSHIL", "json Response recieved !!"+response);
                    Globals.hideLoadingDialog(pd);
                   // responseCategory(response);
                    responseVideo(response);
                }


            }, new ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError err) {
                    Log.i("SUSHIL", "ERROR VolleyError");
                    Globals.showShortToast(Activity_Video_List.this, "Error occured on server, Try later!");
                    if(listView==null) {
                        LinearLayout linear = (LinearLayout) findViewById(R.id.linearCard);
                        linear.setVisibility(View.GONE);
                    }
                    Globals.hideLoadingDialog(pd);
                    Globals.showAlertDialogOneButton("Error", "Some Error has occured, try again.", Activity_Video_List.this, "OK", null, true);

                }
            });

            Custom_VolleyAppController.getInstance().addToRequestQueue(
                    jsonObjectRQST);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void responseVideo(JSONObject response){
        LinearLayout linear = (LinearLayout)findViewById(R.id.linearCard);
        if(response==null){
           linear.setVisibility(View.GONE);
        }else{
            try{
                JSONArray videoArray = response.getJSONArray("video");
                ArrayList<Object_Video> list = new ArrayList<>();
                if(videoArray.length()!=0) {
                    linear.setVisibility(View.VISIBLE);


                    for (int i = 0; i < videoArray.length(); i++) {
                        JSONObject obj = videoArray.getJSONObject(i);
                        if (i == 0) {
                            Object_AppConfig config = new Object_AppConfig(this);
                            config.setYouTubeKey(obj.getString("youtube_key"));
                            config.setDateTime(obj.getString("date"));
                            TextView txt = (TextView) findViewById(R.id.txtCatName);
                            txt.setText(obj.getString("name"));
                            ImageView img = (ImageView) findViewById(R.id.imgBtnCategory);
                            Globals.loadImageIntoImageView(img, obj.getString("image"), this, R.mipmap.default_image, R.mipmap.default_image);
                        } else {
                            Object_Video objVideo = new Object_Video();
                            objVideo.heading = obj.getString("name");
                            objVideo.des = obj.getString("des");
                            objVideo.date = obj.getString("date");
                            objVideo.youTubeKey = obj.getString("youtube_key");
                            objVideo.image = obj.getString("image");
                            list.add(objVideo);
                        }
                    }
                    Custom_Adapter_Video adapterVideo = new Custom_Adapter_Video(this,list);
                     listView = (ListView)findViewById(R.id.listVideo);
                     listView.setAdapter(adapterVideo);
                     getFullscreenAdds();
                }else{
                    Globals.showShortToast(this,"Videos not found");
                   // LinearLayout linear = (LinearLayout)findViewById(R.id.linearCard);
                   /* this.listView.setEmptyView(findViewById(R.id.emptyElement));*/
                    Custom_Adapter_Video adapterVideo = new Custom_Adapter_Video(this,list);
                    listView = (ListView)findViewById(R.id.listVideo);
                    listView.setAdapter(adapterVideo);
                    linear.setVisibility(View.GONE);
                }

            }catch (Exception e){
                  e.printStackTrace();
            }
        }

    }

    private void getDayMonYear(String date){
        try {
            String str[] = date.split("-");
            year = Integer.parseInt(str[0]);
            month = Integer.parseInt(str[1]);
            day = Integer.parseInt(str[2]);
            month = month-1;
            Log.i("SUSHIL","month is "+year+" "+month+" "+day);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getFullscreenAdds(){
        new Handler().postDelayed(new Runnable() {

			/*
			 * Showing splash screen with a timer. This will be useful when you
			 * want to show case your app logo / company
			 */

            @Override
            public void run() {
                // This method will be executed once the timer is over

                //navigation();
                showAdsSlideNews();


            }
        }, 10000);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //showAdsSlideNews();
        this.finish();
    }
}
