package com.examkabila.currentaffairs.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

public class Activity_Splash extends Activity implements SeekBar.OnSeekBarChangeListener {

    private final int SPLASH_TIME_OUT = 2000;

    private DilatingDotsProgressBar mDilatingDotsProgressBar;
    private final float scaleMin = 1.2f;
    private final float scaleMax = 4.0f;
    private final float saturation = 0.75f;
    private final float value = 0.55f;
    private final int numDotsMin = 2;


    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    /**
     * Substitute you own sender ID here. This is the project number you got
     * from the API Console.
     */
    //
    String SENDER_ID = "779536891671";

    GoogleCloudMessaging gcm;
    Context context;
    String regid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        resizeImages();
        mDilatingDotsProgressBar = (DilatingDotsProgressBar) findViewById(R.id.progress);
        mDilatingDotsProgressBar.show(500);
        inti();
        //hideScreenAfterTimeOut();
    }


    private void resizeImages(){
        ImageView imgViewLogo = (ImageView)findViewById(R.id.imgLogoXB);
        //ImageView imgViewName = (ImageView)findViewById(R.id.imgLogoName);

        int screenWidth = Globals.getScreenSize(this).x;
        int logoWidth = screenWidth/100 * 60 ;// 17%
        //int nameWidth = screenWidth/100 * 78 ;// 64%

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        Bitmap logo = BitmapFactory.decodeResource(getResources(), R.mipmap.xb, options);
        logo = Globals.scaleToWidth(logo,logoWidth);
        //Bitmap name = BitmapFactory.decodeResource(getResources(), R.drawable.logo_name, options);
        //name = Globals.scaleToWidth(name,nameWidth);

        imgViewLogo.setImageBitmap(logo);
        //imgViewName.setImageBitmap(name);
    }
    private void hideScreenAfterTimeOut(){
        new Handler().postDelayed(new Runnable() {

			/*
			 * Showing splash screen with a timer. This will be useful when you
			 * want to show case your app logo / company
			 */

            @Override
            public void run() {
                // This method will be executed once the timer is over

                navigation();


            }
        }, SPLASH_TIME_OUT);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // setupSeekbars();
    }


    @Override
    public void onProgressChanged(final SeekBar seekBar, final int progress, final boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(final SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(final SeekBar seekBar) {

    }

    private int getHSVColor(int progress) {
        float[] hsvColor = {0, saturation, value};
        hsvColor[0] = 360f * progress / 100f;
        return Color.HSVToColor(hsvColor);
    }

    private float lerp(float min, float max, float progress) {
        return (min * (1.0f - progress)) + (max * progress);
    }

    private int seekbarProgressFromValue(float min, float max, float currentValue, int seekbarMax) {
        float progress = currentValue - min;
        float totalProgress = max - min;
        float progressPercent = progress / totalProgress;
        return (int) (progressPercent * seekbarMax);
    }


    private void inti() {
       // resizeImages();
        context = getApplicationContext();
        Object_AppConfig configObj = new Object_AppConfig(context);
        Custom_ConnectionDetector cd = new Custom_ConnectionDetector(this);
        if(cd.isConnectingToInternet()) {
            getaddsConfig();
            // Check device for Play Services APK. If check succeeds, proceed with
            //  GCM registration.
            if (checkPlayServices()) {
                gcm = GoogleCloudMessaging.getInstance(this);
                regid = configObj.getRegistrationId(context);

                if (regid.isEmpty()) {
                    registerInBackground();
                    // timeHandler();
                } else {
                    hideScreenAfterTimeOut();
                }
            } else {
                hideScreenAfterTimeOut();
                Log.i("SUSHIL", "No valid Google Play Services APK found.");
            }
        }else {
            hideScreenAfterTimeOut();
            Log.i("SUSHIL", "Internat Connection Lost");
        }
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i("SUSHIL", "This device is not supported - Google Play Services.");
                finish();
            }
            return false;
        }
        return true;
    }

    public void registerInBackground() {
        try {
            Custom_ConnectionDetector con = new Custom_ConnectionDetector(this);
            if(con.isConnectingToInternet()) {
                new AsyncTask() {
                    @Override
                    protected Object doInBackground(Object... params) {
                        // TODO Auto-generated method stub
                        String msg = "";
                        try {
                            Object_AppConfig configObj = new Object_AppConfig(context);
                            if (gcm == null) {
                                gcm = GoogleCloudMessaging.getInstance(context);
                            }
                            regid = gcm.register(SENDER_ID);
                            msg = "Device registered, registration ID=" + regid;
                            Log.i("SUSHIL", "sushil " + regid);
                            // You should send the registration ID to your server over HTTP,
                            // so it can use GCM/HTTP or CCS to send messages to your app.
                            // The request to your server should be authenticated if your app
                            // is using accounts.


                            // For this demo: we don't need to send it because the device
                            // will send upstream messages to a server that echo back the
                            // message using the 'from' address in the message.

                            // Persist the registration ID - no need to register again.
                            configObj.storeRegistrationId(context, regid);
                            if (regid != null)
                                 sendRegistrationIdToBackend();

                        } catch (IOException ex) {
                            msg = "Error :" + ex.getMessage();
                            //hideScreenAfterTimeOut();

                        }catch (Exception e){
                            e.printStackTrace();
                          //  hideScreenAfterTimeOut();
                        }
                        return msg;
                    }
                }.execute(null, null, null);
            }else{
                //timeHandler();
                hideScreenAfterTimeOut();
            }
        }catch (Exception e){
            navigation();
            e.printStackTrace();
        }
    }

    private void sendRegistrationIdToBackend() {
        try {
            TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            String Imei = telephonyManager.getDeviceId();
            if(Imei==null){
                Imei = "";
            }
            Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(
                    Request.Method.POST,
                    Custom_ServerURL_Params.getURL_GcmRegister(),
                    Custom_ServerURL_Params.getParams_GCMRegister(this,Imei), new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.i("SUSHIL", "json Response recieved !!"+response);
                    parseResponce(response);
                }


            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError err) {
                    Log.i("SUSHIL", "ERROR VolleyError");
                    navigation();
                }
            });

            Custom_VolleyAppController.getInstance().addToRequestQueue(
                    jsonObjectRQST);
        } catch (Exception e) {
            e.printStackTrace();
            navigation();
        }
    }

    private void parseResponce(JSONObject obj){
        if(obj==null){
            return;
        }else{
            try {
                if(obj.getString("status").equals("registered")){
                    navigation();
                }
            }catch (JSONException e){
                navigation();
                e.printStackTrace();
            }

        }
    }

    private void navigation(){

        Intent i = null;
        int selectLangId = 0;

        try {
            Object_AppConfig config = new Object_AppConfig(Activity_Splash.this);
            selectLangId = config.isLanguageSelect();
        } catch (Exception ex) {
            Log.e("SUSHIL", "Error in hideScreenAfterTimeOut , Activity Splash");
        }

        if (selectLangId > 0) { // already selected
            i = new Intent(Activity_Splash.this, Activity_Home.class);
        } else {
            i = new Intent(Activity_Splash.this, Activity_SelectLanguage.class);
        }

        startActivity(i);
        finish();

    }

    private void getaddsConfig(){
        try{
            HashMap<String,String> map = new HashMap<>();
        Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(
                Request.Method.POST,
                Custom_ServerURL_Params.getAdvertisement_link(),
                map, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.i("SUSHIL", "json Response recieved !!"+response);
                parseConfig(response);
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError err) {
                Log.i("SUSHIL", "ERROR VolleyError");
              //  navigation();
            }
        });

        Custom_VolleyAppController.getInstance().addToRequestQueue(
                jsonObjectRQST);
    } catch (Exception e) {
        e.printStackTrace();
        //navigation();
    }
    }

    private void parseConfig(JSONObject obj){
        if(obj==null){
            //navigation();
            return;
        }else{
            try {
                Object_AppConfig config = new Object_AppConfig(this);
                if (obj.has("ShowAdvt")) {
                    config.SetshowAdds(obj.getInt("ShowAdvt"));
                }
                if (obj.has("AdvtId")) {
                    config.SetadTypeId(obj.getInt("AdvtId"));
                }
                if (obj.has("AdvtIntertialId")) {
                    config.SetadTypeInterId(obj.getInt("AdvtIntertialId"));
                }
                return;
            }catch (JSONException e){
                e.printStackTrace();
                //navigation();
            }catch (Exception e){
                e.printStackTrace();
               // navigation();
            }
        }

    }

}
