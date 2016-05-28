package com.examkabila.currentaffairs.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Config;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Activity_Video_Player extends YouTubeBaseActivity implements
        YouTubePlayer.OnInitializedListener {
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    private ListView listView;
    private ProgressDialog pd;
    // YouTube player view
    private YouTubePlayerView youTubeView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        getvideoServer();
        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);

        // Initializing video player with developer key
        youTubeView.initialize(Globals.DEVELOPER_KEY, this);
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = String.format(
                    getString(R.string.error_player), errorReason.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                        YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {

            // loadVideo() will auto play video
            // Use cueVideo() method, if you don't want to play it automatically
            Object_AppConfig config = new Object_AppConfig(this);
            player.loadVideo(config.getYouTubeKey());

            // Hiding player controls
            player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
            player.setPlaybackEventListener(new YouTubePlayer.PlaybackEventListener() {
                @Override
                public void onPlaying() {

                }

                @Override
                public void onPaused() {

                }

                @Override
                public void onStopped() {
                    Log.i("SUSHIL","on stoped call");
                    Globals.showAlertDialog("Alert", "Do you want to Start Quiz ?",
                            Activity_Video_Player.this, "Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    naviQuiz();
                                    dialog.dismiss();
                                }
                            }, "No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {

                                    // Dialogback();
                                    // Activity_Video_Player.this.finish();
                                }
                            }, true);
                }

                @Override
                public void onBuffering(boolean b) {

                }

                @Override
                public void onSeekTo(int i) {

                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(Globals.DEVELOPER_KEY, this);
        }
    }

    private YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtube_view);
    }

    private void getvideoServer() {
        try {
            pd = Globals.showLoadingDialog(pd, this, false, "");
            Object_AppConfig config = new Object_AppConfig(this);
            Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(
                    Request.Method.POST,
                    Custom_ServerURL_Params.getVideoLisRe(),
                    Custom_ServerURL_Params.getParams_GK_Test(config.getLangCode(),config.getDateTime(),this), new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.i("SUSHIL", "json Response recieved !!"+response);
                    Globals.hideLoadingDialog(pd);
                    // responseCategory(response);
                    responseVideo(response);
                }


            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError err) {
                    Log.i("SUSHIL", "ERROR VolleyError");
                    Globals.hideLoadingDialog(pd);


                }
            });

            Custom_VolleyAppController.getInstance().addToRequestQueue(
                    jsonObjectRQST);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void responseVideo(JSONObject response){

        if(response==null){
            return;
        }else{
            try{
                JSONArray videoArray = response.getJSONArray("video");
                if(videoArray.length()!=0) {

                    ArrayList<Object_Video> list = new ArrayList<>();

                    for (int i = 0; i < videoArray.length(); i++) {
                        JSONObject obj = videoArray.getJSONObject(i);

                            Object_Video objVideo = new Object_Video();
                            objVideo.heading = obj.getString("name");
                            objVideo.des = obj.getString("des");
                            objVideo.date = obj.getString("date");
                            objVideo.youTubeKey = obj.getString("youtube_key");
                            objVideo.image = obj.getString("image");
                            list.add(objVideo);

                    }
                    Custom_Adapter_VideoRelated adapterVideo = new Custom_Adapter_VideoRelated(this,list);
                    listView = (ListView)findViewById(R.id.listVideoRelated);
                    listView.setAdapter(adapterVideo);
                }else{
                    listView = (ListView)findViewById(R.id.listVideoRelated);
                    listView.setVisibility(View.GONE);
                }

            }catch (Exception e){
                listView = (ListView)findViewById(R.id.listVideoRelated);
                listView.setVisibility(View.GONE);
            }
        }

    }

    public void naviQuiz(){
        Object_AppConfig config = new Object_AppConfig(this);

        Intent i = new Intent(this,Activity_Quiz.class);
        i.putExtra("date",config.getDateTime());
        startActivity(i);
    }
}
