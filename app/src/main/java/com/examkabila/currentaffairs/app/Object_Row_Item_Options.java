package com.examkabila.currentaffairs.app;

import android.graphics.Bitmap;

public class Object_Row_Item_Options {

	public Object_Row_Item_Options() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	public Object_Row_Item_Options(Bitmap iconBitmap,String text){
		this.iconBitmap = iconBitmap;
		this.text = text;
	}
	public String text;
	public Bitmap iconBitmap;
	public Boolean isCorrect;
	public Boolean isWrong;
	public Boolean isSelected;
}
