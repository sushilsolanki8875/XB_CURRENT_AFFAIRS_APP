package com.examkabila.currentaffairs.app;

import java.util.ArrayList;

public class Object_Question {

	//QuesId integer NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,
	//QuesCatId integer,Question text,CorrectOption integer,Hint text,Solution text,
	//LangId integer, IsFav integer DEFAULT 0, Image blob
	public String date;
	public long quesId;
	public int catId;
	public String question;
	public int correctOption;
	public String hint;
	public String solution;
	public int langId;
	public int isFav;
	public byte[] image;
	public long examQuestionId;
	public int questionNo;
	public int optionSelected;
	public int attemptLater;
	public Boolean isAnsShown = false;;
	public ArrayList<Object_Options> arrayOptions;

	public String bankName = "";
	public String examYear = "";
	public int totalOptions;
	public byte[] solutionImage ;
	public String langCode;
	
}
