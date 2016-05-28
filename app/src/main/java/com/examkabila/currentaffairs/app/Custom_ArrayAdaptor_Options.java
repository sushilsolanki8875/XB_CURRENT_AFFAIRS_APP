package com.examkabila.currentaffairs.app;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class Custom_ArrayAdaptor_Options extends ArrayAdapter<Object_Row_Item_Options>  {

	Activity context;
	int resourceId;
	ArrayList<Object_Row_Item_Options> data = null;
	private Boolean isAnsShown;
	public Custom_ArrayAdaptor_Options(Activity context,int resourceId ,ArrayList<Object_Row_Item_Options> data,Boolean isAnsShown){
		super(context, resourceId,data);
		
		this.context = context;
		this.resourceId = resourceId;
		this.data = data;
		this.isAnsShown = isAnsShown;
	}
	
	static class ViewHolder{
		ImageView imgIcon;
		TextView txtTitle;
	}
	@Override
	public View getView(int position,View convertView,ViewGroup parent){
		
		View row = convertView;
		ViewHolder holder = null;
		if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(resourceId, parent, false);
            
            holder = new ViewHolder();
            holder.imgIcon = (ImageView)row.findViewById(R.id.imgRowIcon);
            holder.txtTitle = (TextView)row.findViewById(R.id.txtRowText);
            
            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)row.getTag();
        }
        
		Object_Row_Item_Options item = data.get(position);
        holder.txtTitle.setText(item.text.replace(String.valueOf((char) 160), " ").trim());
        holder.txtTitle.setTextSize(Globals.getAppFontSize(context));
        holder.imgIcon.setImageBitmap(item.iconBitmap);
        
        if(this.isAnsShown){
        	if(item.isCorrect){
        		row.setBackgroundResource(R.drawable.bg_row_item_questions_options_select);
        	}else if(item.isWrong){
        		row.setBackgroundResource(R.drawable.bg_row_item_options_wrong);
        	}else{
        		row.setBackgroundResource(R.drawable.bg_row_item_options_unselect);
        		
        	}
        
        }else{
        	if(item.isSelected){
        		row.setBackgroundResource(R.drawable.bg_row_item_questions_options_select);
        	}else{
        		row.setBackgroundResource(R.drawable.bg_row_item_options_unselect);
        	}
        }
        
        return row;
	}
}
