package com.examkabila.currentaffairs.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by xb_sushil on 3/19/2016.
 */
public class Custom_Adapter_VideoRelated extends BaseAdapter {

        private Context mContext;
        private ArrayList<Object_Video> listCate;

        public Custom_Adapter_VideoRelated(Context mContext, ArrayList<Object_Video> list){
            this.mContext = mContext;
            this.listCate = list;

        }

        @Override
        public int getCount() {
            return listCate.size();
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
                convertView = inflater.inflate(R.layout.row_listview_video_related, parent, false);

            }


            TextView txtName = (TextView)convertView.findViewById(R.id.txtSubCatename);
            /*TextView txtDes = (TextView)convertView.findViewById(R.id.txtdes);
            TextView txtDate = (TextView)convertView.findViewById(R.id.txtDate);*/
            // Globals.setAppFontTextView(mContext, txtName);

            int totalContent = Globals.getScreenSize((Activity) mContext).y;
            int imgheight = totalContent - ((totalContent * 75) / 100);
            ImageView img = (ImageView)convertView.findViewById(R.id.imgCate);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) img
                    .getLayoutParams();
           // lp.width = (int)(imgheight * 1.4);
            lp.height = imgheight;
            img.setLayoutParams(lp);
            Globals.loadImageIntoImageView(img, listCate.get(position).image, mContext, R.mipmap.default_image, R.mipmap.default_image);
            txtName.setText(listCate.get(position).heading);
            /*txtDes.setText(listCate.get(position).des);
            txtDate.setText(listCate.get(position).date);*/
        /* final int catid = listCate.get(position).catId;
         final int isbusiness = listCate.get(position).isbusiness*/;
            final String youTubeKey = listCate.get(position).youTubeKey;
            final String DateTime = listCate.get(position).date;
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Object_AppConfig config = new Object_AppConfig(mContext);
                    config.setYouTubeKey(youTubeKey);
                    config.setDateTime(DateTime);
                    naviVideoPlayer();
                }
            });
            return convertView;
        }
        private void naviVideoPlayer(){
            Intent i = new Intent(mContext,Activity_Video_Player.class);
            ((Activity)mContext).startActivity(i);
            ((Activity)mContext).finish();
        }
    }


