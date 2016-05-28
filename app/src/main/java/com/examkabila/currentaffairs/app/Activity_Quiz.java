package com.examkabila.currentaffairs.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.app.DatePickerDialog;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class Activity_Quiz extends Activity_Parent_Banner_Ads {

    private Object_Question currentQues = null;
    Boolean firstTime = true;
    private ProgressDialog mDialog;
  // questionCount = 1;
    private ArrayList<Object_Question> listQuestion;
    TextView txtDate;
    private int year;
    private int month;
    private int day;

    private static final AttributeSet DatePickerDialog = null;
    private int back = 0;
    private int next = 0;
    private String dateTime = "";
    private ScrollView scAns;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        scAns = (ScrollView)findViewById(R.id.scrolViewSolution);
        /*requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/

        inti();
        resizeImageButtons(true);
        initCurrentTest();

    }
    private void inti() {
        listQuestion = new ArrayList<>();
        TextView txtHeader = (TextView) findViewById(R.id.txtHeader);
        txtHeader.setText("Quiz");
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
        //setCurrentDateOnview();
    }

   /*private void setDate(){
       DatePickerDialog dpd = new DatePickerDialog(this,
               new DatePickerDialog.OnDateSetListener() {

                   @Override
                   public void onDateSet(DatePicker view, int selectedYear,
                                         int selectedMonth, int selectedDay) {
                      *//* txtDate.setText(dayOfMonth + "-"
                               + (monthOfYear + 1) + "-" + year);*//*
                       year = selectedYear;
                       month= selectedMonth;
                       day = selectedDay;
                       txtDate.setText(stringComplete(year)+"-"+stringComplete(month+1)+"-"+stringComplete(day));
                       //txtDate.setText(stringComplete(day)+"-"+stringComplete(month+1)+"-"+stringComplete(year));

                       getQuesFromServer(true);
                   }
               }, year, (month+1), day);
       dpd.show();
   }*/

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
                        //txtDate.setText(stringComplete(year)+"-"+stringComplete(month+1)+"-"+stringComplete(day));
                        //txtDate.setText(stringComplete(day)+"-"+stringComplete(month+1)+"-"+stringComplete(year));
                        txtDate.setText(Globals.getdateFormat(day,month+1,year));
                        getQuesFromServer(true);
                    }
                }, year, (month), day);
        dpd.show();
    }

    public void setCurrentDateOnview() {


        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        //txtDate.setText(month + 1 + "-" + day + "-" + year);
        //txtDate.setText(stringComplete(year)+"-"+stringComplete(month+1)+"-"+stringComplete(day));


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

   /* protected Dialog onCreateDialog(int id){
       switch (id){
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this,datePickerListener,year,(month-1),day);
        }
        return null;
    }
    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener(){

        public void onDateSet(DatePicker view,int selectedYear,int selectedMonth,int selectedDay) {

            //dpresult.init(year, month, day, null);
            //setDataList(txtDate.getText().toString());
        }
    };*/



    public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub

    }



    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if(firstTime)
        {
            resizeImageButtons(false);
            firstTime = false;
            Intent i = getIntent();
            String myDate = i.getStringExtra("date");
            if(myDate.equals("")) {
                getQuesFromServer(false);
            }else{
                getDayMonYear(myDate);
                getQuesFromServer(false);
            }
        }
    }

    private void resizeImageButtons(Boolean hide){

        ImageButton imgBtnShowAns = (ImageButton)findViewById(R.id.imgBtnShowAns);
        ImageButton imgBtnNext = (ImageButton)findViewById(R.id.imgBtnNext);
        ImageButton imgBtnPrev = (ImageButton)findViewById(R.id.imgBtnPrev);
        ImageButton imgBtnStop = (ImageButton)findViewById(R.id.imgBtnStop);

        TextView txtShowAnswer = (TextView)findViewById(R.id.txtShowAns);
        if(hide){
            imgBtnShowAns.setVisibility(View.INVISIBLE);
            imgBtnNext.setVisibility(View.INVISIBLE);
            imgBtnPrev.setVisibility(View.INVISIBLE);
            imgBtnStop.setVisibility(View.INVISIBLE);
            txtShowAnswer.setVisibility(View.INVISIBLE);
        }else{
            imgBtnShowAns.setVisibility(View.VISIBLE);
            imgBtnNext.setVisibility(View.VISIBLE);
            imgBtnPrev.setVisibility(View.VISIBLE);
            imgBtnStop.setVisibility(View.VISIBLE);
            txtShowAnswer.setVisibility(View.VISIBLE);


            RelativeLayout rlytNextPrev = (RelativeLayout) findViewById(R.id.rlytNextPrev);

            int imageBtnSizeNextPrev = rlytNextPrev.getHeight() * 7 / 10;

            int imageBtnSizeOptions = rlytNextPrev.getHeight() *6/10;


            imgBtnShowAns.getLayoutParams().height = imageBtnSizeOptions;
            imgBtnShowAns.getLayoutParams().width = imageBtnSizeOptions;


            imgBtnNext.getLayoutParams().height = imageBtnSizeNextPrev;
            imgBtnNext.getLayoutParams().width = imageBtnSizeNextPrev;
            imgBtnPrev.getLayoutParams().height = imageBtnSizeNextPrev;
            imgBtnPrev.getLayoutParams().width = imageBtnSizeNextPrev;
            imgBtnStop.getLayoutParams().height = imageBtnSizeNextPrev;
            imgBtnStop.getLayoutParams().width = imageBtnSizeNextPrev;

        }

    }

    private void initCurrentTest(){


        firstTime = true;

        TextView txtQ = (TextView)findViewById(R.id.txtQuestion);
        txtQ.setTextSize( Globals.getAppFontSize(this));

        final ListView lv = (ListView)findViewById(R.id.listOptions);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int pos,
                                    long id) {

                if(currentQues.isAnsShown){
                    return;
                }
                updateOptionSelection(pos);


            }
        });

    }

    private void updateOptionSelection(int pos){
        //option no is pos +1;
        if(currentQues != null){
            int selectedOptionNo = 0;
            if(pos>=0){
                Object_Options objOption = currentQues.arrayOptions.get(pos);
                selectedOptionNo = objOption.optionNo;
            }

            currentQues.optionSelected = selectedOptionNo;

            ListView lv = (ListView)findViewById(R.id.listOptions);
            int index = lv.getFirstVisiblePosition();
            View v = lv.getChildAt(0);
            int top = (v == null) ? 0 : v.getTop();

            setOptions();

            lv.setSelectionFromTop(index, top);
        }else{
            Globals.showAlertDialogOneButton("Error", "Some Error has occured, try again.",Activity_Quiz.this, "OK", null, true);
        }
    }

    @SuppressLint("InlinedApi")
    public void getQuesFromServer(final boolean isClear)
    {
        Custom_ConnectionDetector cd = new Custom_ConnectionDetector(
                this);

        if(!cd.isConnectingToInternet()){
            Globals.showAlert("","No Internet Connectivity",this);
            return;
        }
       //DBHandler_Language dbH = new DBHandler_Language(this);
        Object_AppConfig appConfig = new Object_AppConfig(this);

       String langCode = appConfig.getLangCode();

        /*String date = "01";
        String month = "03";
        String year = "2016*/


       /* if (android.os.Build.VERSION.SDK_INT >= 14)
        {
            mDialog = new ProgressDialog(this,ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
        }else{
            mDialog = new ProgressDialog(this);
        }
        mDialog.setMessage("Please wait...");
        mDialog.setCancelable(false);
        mDialog.show();*/
        mDialog = Globals.showLoadingDialog(mDialog,this,false,"");

        final String url = Custom_ServerURL_Params.getCurrent_GK_Test_link();
        Log.i("SUSHIL", "URL IS -->" + url);

        // prepare the Request
		/*JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
		    new Response.Listener<JSONObject>()
		    {
		        @Override
		        public void onResponse(JSONObject response) {

		        	if(mDialog!= null){
		        		mDialog.dismiss();
		        	}

		            Log.i("HARSH","Response "+ response.toString());

		            if(response.has("errorNoMoreQuestions")){

		            	try {
							Globals.showAlertDialogOneButton("Error ", response.getString("errorNoMoreQuestions"), Activity_Current_GK_Test.this, "OK", null, false);

		            	} catch (JSONException e) {
							e.printStackTrace();
						}

		            	return;
		            }else if(response.has("errorNoQuestions")){

		            	try {
		            		OnClickListener listnerPositive = new OnClickListener() {

		            			@Override
		            			public void onClick(DialogInterface dialog, int arg1) {
		            				dialog.dismiss();
		            				Activity_Current_GK_Test.this.finish();
		            			}
		            		};

							Globals.showAlertDialogOneButton("Error ", response.getString("errorNoQuestions"), Activity_Current_GK_Test.this, "OK", listnerPositive, false);

		            	} catch (JSONException e) {
							e.printStackTrace();
						}

		            	return;
		            }

		            createQuestionObject(response,qNo);

		        }
		    },
		    new Response.ErrorListener()
		    {

		         @Override
		         public void onErrorResponse(VolleyError error) {
		        	 if(mDialog!= null){
			        		mDialog.dismiss();
			        	}
		            //Log.i("HARSH","Error.Response"+ error.networkResponse.toString());
		            Globals.showAlertDialogOneButton("Error", "Some Error has occured, try again.",Activity_Current_GK_Test.this, "OK", null, true);
		       }
		    }
		);

		// add it to the RequestQueue
		Globals.getRequestQueue(this).add(getRequest);*/
        String dateIME = "";
        if(day!=0 && year!=0){
            dateIME = stringComplete(year)+"-"+stringComplete(month+1)+"-"+stringComplete(day);
        }
        Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(
                Request.Method.POST,
                url,
                Custom_ServerURL_Params.getParams_GK_Test(langCode,dateIME,Activity_Quiz.this),
                new Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                       /* if(mDialog!= null){
                            mDialog.dismiss();
                        }*/
                        Globals.hideLoadingDialog(mDialog);

                        Log.i("HARSH","Response "+ response.toString());

                        if(response.has("errorNoMoreQuestions")){

                            try {
                                Globals.showAlertDialogOneButton("Error ", response.getString("errorNoMoreQuestions"), Activity_Quiz.this, "OK", null, false);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            return;
                        }else if(response.has("errorNoQuestions")){

                            try {
                                DialogInterface.OnClickListener listnerPositive = new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int arg1) {
                                        dialog.dismiss();
                                        Activity_Quiz.this.finish();
                                    }
                                };

                                Globals.showAlertDialogOneButton("Error ", response.getString("errorNoQuestions"), Activity_Quiz.this, "OK", listnerPositive, false);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            return;
                        }

                        createQuestionObject(response,isClear);


                    }

                }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError err) {
                //Log.i("SUSHIL", "ERROR VolleyError");
               /* if(mDialog!= null){
                    mDialog.dismiss();
                }*/
                Globals.hideLoadingDialog(mDialog);
                //Log.i("HARSH","Error.Response"+ error.networkResponse.toString());
                Globals.showAlertDialogOneButton("Error", "Some Error has occured, try again.",Activity_Quiz.this, "OK", null, true);

            }
        });

        Custom_VolleyAppController.getInstance().addToRequestQueue(
                jsonObjectRQST);


    }


    private void createQuestionObject(JSONObject responseMain , boolean isClear){
        try{
            JSONArray questionArray = responseMain.getJSONArray("Questions");
            if(questionArray.length()!=0) {
                Log.i("SUSHIL","lenth of array "+questionArray.length());
                if(isClear){
                    currentQues = null;
                    listQuestion = new ArrayList<>();
                }
            if(responseMain.has("date")){
             txtDate.setText(responseMain.getString("date"));
            }
            if(responseMain.has("back")){
              back = responseMain.getInt("back");
            }
            if(responseMain.has("next")){
                next = responseMain.getInt("next");
            }

                for (int j = 0; j < questionArray.length(); j++) {
                    JSONObject response = questionArray.getJSONObject(j);
                    Object_Question ques = new Object_Question();

                if(currentQues==null)
                    ques.questionNo = (j + 1);
                    else
                   ques.questionNo = currentQues.questionNo+(j+1);

                    ques.date = responseMain.getString("date");
                    ques.question = response.getString("Question");
                    if (response.has("Solution"))
                        ques.solution = response.getString("Solution");
                    else
                        ques.solution = "";
                    ques.arrayOptions = new ArrayList<Object_Options>();
                    ques.correctOption = response.getInt("correctOption");

                    JSONArray arrayJson = response.getJSONArray("options");

                    for (int i = 0; i < arrayJson.length(); i++) {
                        Object_Options obj = new Object_Options();

                        obj.optionNo = i + 1;
                        obj.optionText = arrayJson.getString(i);

                        ques.arrayOptions.add(obj);
                    }
                    listQuestion.add(ques);
                }

             if(currentQues==null)
                showQuestionOnView(listQuestion.get(0));
             else
                 showQuestionOnView(listQuestion.get(currentQues.questionNo));
            }else{
                getDayMonYear(dateTime);
                txtDate.setText(Globals.getdateFormat(day, month + 1, year));
                //Globals.showAlertDialogError
                Globals.showAlertDialogOneButton("Error", "Quiz not found of this day!",this, "OK", null, true);
                return;
               /* txtDate.setText(dateTime);
                Globals.showAlertDialogOneButton("Error", "Quiz not found of this day!",this, "OK", null, true);
                return;*/

            }

            }catch(Exception ex){
                Log.i("HARSH","Exception in parsing Question JSON "+ responseMain.toString());
                //ques = null;

                Globals.showAlertDialogOneButton("Error", "Some Error has occured, try again.",this, "OK", null, true);
                return;
            }

    }

    private void showQuestionOnView(Object_Question ques){
        scAns.setVisibility(View.GONE);
        currentQues = ques;
        getDayMonYear(currentQues.date);
        dateTime = currentQues.date;
        txtDate.setText(Globals.getdateFormat(day,month+1,year));
       /* try {
            String str[] = currentQues.date.split("-");
            year = Integer.parseInt(str[0]);
            month = Integer.parseInt(str[1]);
            day = Integer.parseInt(str[2]);

            *//*year = Integer.parseInt(currentQues.date.substring(0, 4));
            month = Integer.parseInt(currentQues.date.substring(6, 8));
            day = Integer.parseInt(currentQues.date.substring(currentQues.date.length()-2, currentQues.date.length()));*//*
            Log.i("SUSHIL","month is "+year+" "+month+" "+day);
        }catch (Exception e){
            e.printStackTrace();
        }*/
        TextView txtQ = (TextView)findViewById(R.id.txtQuestion);
        txtQ.setText("Q."+ currentQues.questionNo+ "   "+currentQues.question.trim());

        ScrollView scrollQues = (ScrollView)findViewById(R.id.scrolViewQuestion);
        scrollQues.smoothScrollTo(0, 0);

        ImageButton imgBtnShowAns = (ImageButton)findViewById(R.id.imgBtnShowAns);
        imgBtnShowAns.setSelected(false);
        setOptions();
        if(currentQues.isAnsShown){
            showSolution();
        }

    }

    private void setOptions(){

        ListView lv = (ListView)findViewById(R.id.listOptions);

        //DBHandler_Options dbh = new DBHandler_Options(this);

        ArrayList<Object_Row_Item_Options> data = new ArrayList<Object_Row_Item_Options>();

        //selectedOptionPos = -1;

        //int counter = 0;
        for(Object_Options obj:currentQues.arrayOptions)
        {
            Object_Row_Item_Options item=new Object_Row_Item_Options();
            item.text = obj.optionText.trim();
            if(obj.image!=null){
                Bitmap bit = BitmapFactory.decodeByteArray(obj.image, 0,
                        obj.image.length);
                item.iconBitmap = bit;
            }else{
                item.iconBitmap = null;
            }

            item.isCorrect = false;
            item.isWrong = false;
            item.isSelected = false;

            if(currentQues.optionSelected == obj.optionNo){
                item.isSelected = true;
            }

            if(currentQues.correctOption == obj.optionNo){
                item.isCorrect = true;
            }else if(currentQues.optionSelected == obj.optionNo){
                item.isWrong = true;
            }

            data.add(item);
        }

        Custom_ArrayAdaptor_Options adp=new Custom_ArrayAdaptor_Options(this,R.layout.row_listview_options_question,data,currentQues.isAnsShown);
        lv.setAdapter(adp);


    }

    public void onClickNext(View v){

        int qNo = 0;
        if(currentQues != null){
         //  qNo = currentQues.questionNo;

         questionCount++;

            /*if(questionCount==10){

            }*/
           /* ImageButton imgBtnPrev = (ImageButton)findViewById(R.id.imgBtnPrev);
            if( currentQues.questionNo == 1){
                imgBtnPrev.setEnabled(false);
            }else{
                imgBtnPrev.setEnabled(true);
            }*/
        if(currentQues.questionNo==listQuestion.size()){
            if(back==1) {
                String daTE = Globals.getCalculatedDate(stringComplete(year)+"-"+stringComplete(month+1)+"-"+stringComplete(day), "yyyy-MM-dd", -1);
                getDayMonYear(daTE);
                Log.i("SUSHIL", "date get PR " + daTE);
                //txtDate.setText(daTE);
                txtDate.setText(Globals.getdateFormat(day,month+1,year));
                getQuesFromServer(false);
            }else
                Globals.showAlert("","No more question",this);

        }else {
            //getQuesFromServer(qNo);
            showQuestionOnView(listQuestion.get(currentQues.questionNo));
        }
        }


    }
    public void onClickPrev(View v){

        int qNo = 0;

        if(currentQues != null){
           // qNo = currentQues.questionNo-2;

        if(currentQues.questionNo-2 >= 0)
            showQuestionOnView(listQuestion.get(currentQues.questionNo-2));
        else{
            if(next==1) {
                String daTE = Globals.getCalculatedDate(stringComplete(year)+"-"+stringComplete(month+1)+"-"+stringComplete(day), "yyyy-MM-dd", 1);
                getDayMonYear(daTE);
                Log.i("SUSHIL", "date get PR " + daTE);
                //txtDate.setText(daTE);
                txtDate.setText(Globals.getdateFormat(day,month+1,year));
                /*currentQues = null;
                listQuestion = new ArrayList<>();*/
                getQuesFromServer(true);
            }
            else
                Globals.showAlert("","No more question",this);
          }
        }
            //getQuesFromServer(qNo);




    }

    @Override
    public void onBackPressed() {
        //do nothing
        onClickStop(null);
    }

    public void onClickShowAns(View v){
        //v.setSelected(!v.isSelected());
        if(currentQues != null){
            //DBHandler_Questions dbH = new DBHandler_Questions(this);
            //DBHandler_Language dbH2 = new DBHandler_Language(this);
            //dbH.insertNewQuestion(currentQues, dbH2.getLangMap());

            if(!currentQues.isAnsShown){
                currentQues.isAnsShown = true;
                v.setSelected(true);
                setOptions();
                showSolution();

                /*if(currentQues.solution != null && !currentQues.solution.equals("")){
                    Globals.showAlertDialogOneButton("Solution", currentQues.solution, this, "OK", null, false);
                }*/
            }
        }

    }

    private  void showSolution(){
        if(currentQues.solution != null && !currentQues.solution.equals("")){
            //Globals.showAlertDialogOneButton("Solution", currentQues.solution, this, "OK", null, false);
            //ScrollView scAns = (ScrollView)findViewById(R.id.scrolViewSolution);
            scAns.setVisibility(View.VISIBLE);
            TextView txt = (TextView)findViewById(R.id.txtSolution);
            txt.setText(""+currentQues.solution);
        }
    }

    public void onClickStop(View v){

        String header = "Quit Quiz!";

        ExamOver(header, "Stop Test & go back to previous screen?",false);
    }

    private void ExamOver(String title , String msg , final Boolean isTimeOver){

        DialogInterface.OnClickListener listnerPositive = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                dialog.dismiss();
                Activity_Quiz.this.finish();
            }
        };
        DialogInterface.OnClickListener listnerNegative = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        };
        Globals.showAlertDialog(title, msg, this, "YES", listnerPositive, "NO", listnerNegative, false);
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
}
