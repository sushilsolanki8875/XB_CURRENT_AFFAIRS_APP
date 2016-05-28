package com.examkabila.currentaffairs.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;


import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Activity_MoreApps extends Activity {
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_apps);
        inti();
    }

    private void inti() {

        TextView txtHeader = (TextView) findViewById(R.id.txtHeader);
        txtHeader.setText("More Apps");
        txtHeader.setTextSize(Globals.getAppFontSize_Large(this));
        Custom_ConnectionDetector cd = new Custom_ConnectionDetector(this);
        if(cd.isConnectingToInternet()) {
            getMoreApps();
        }else{
            Globals.showAlertDialogError(this,Globals.INTERNET_ERROR);
        }
    }


    private void getMoreApps() {
        try {
            pd = Globals.showLoadingDialog(pd, this, false, "");
            HashMap<String, String> map = new HashMap<String, String>();
            Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(
                    Request.Method.POST,
                    Custom_ServerURL_Params.getMoreApps(),
                    map,
                    new Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Globals.hideLoadingDialog(pd);
                            parseMoreApps(response);
                        }

                    }, new ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError err) {
                    Log.i("SUSHIL", "ERROR VolleyError "+err.toString());
               /* if(mDialog!= null){
                    mDialog.dismiss();
                }*/
                    Globals.hideLoadingDialog(pd);
                    //Log.i("HARSH","Error.Response"+ error.networkResponse.toString());
                    Globals.showAlertDialogOneButton("Error", "Some Error has occured, try again.", Activity_MoreApps.this, "OK", null, true);

                }
            });

            Custom_VolleyAppController.getInstance().addToRequestQueue(
                    jsonObjectRQST);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseMoreApps(JSONObject respo) {
        if (respo == null) {
            return;
        } else {
            try {
                //JSONObject object = new JSONObject(arrayCategory);
                JSONArray array = respo.getJSONArray("apps");
                ArrayList<Object_MoreApps> list = new ArrayList<>();

                for (int i = 0; i < array.length(); i++) {
                    try {
                        JSONObject obj = array.getJSONObject(i);
                        Object_MoreApps more = new Object_MoreApps();

                        more.id = obj.getInt("id");
                        more.name = obj.getString("name");
                        more.url = obj.getString("url");
                        more.image = obj.getString("image");
                        list.add(more);
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                }
                Custom_Adapter_MoreApps adapter = new Custom_Adapter_MoreApps(this, list);
                ListView listView = (ListView) findViewById(R.id.list);
                listView.setAdapter(adapter);


            } catch (JSONException ex) {
                ex.printStackTrace();
            }


        }
    }


}
