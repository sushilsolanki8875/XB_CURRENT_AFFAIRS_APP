package com.examkabila.currentaffairs.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;



import java.util.ArrayList;


public class Custom_Adapter_MoreApps extends BaseAdapter {

     private Context mContext;
    private ArrayList<Object_MoreApps> list;

    public Custom_Adapter_MoreApps(Context mContext, ArrayList<Object_MoreApps> list){
       this.mContext = mContext;
        this.list = list;

    }

    @Override
    public int getCount() {
        return list.size();
    }


    @Override
    public Object getItem(int position) {
        return null;
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_row_more_apps, parent, false);
            }
        ImageView img = (ImageView)convertView.findViewById(R.id.imgCate);

        TextView txtName = (TextView)convertView.findViewById(R.id.txtSubCatename);
        txtName.setTextSize(Globals.getAppFontSize((Activity)mContext));
       // Globals.setAppFotTextView(mContext, txtName);
        Globals.loadImageIntoImageView(img, list.get(position).image, mContext, R.mipmap.default_image, R.mipmap.default_image);
        txtName.setText(list.get(position).name);
       final String url = list.get(position).url;
        convertView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 navi(url);
             }
         });
         return convertView;
    }

    private void navi(String link){
        Log.i("SUSHIL","link navui "+link);
        if(!link.trim().isEmpty() && link!= null){
            if(link.startsWith("http")) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(link));
                ((Activity)mContext).startActivity(i);
            }
        }
    }



}
