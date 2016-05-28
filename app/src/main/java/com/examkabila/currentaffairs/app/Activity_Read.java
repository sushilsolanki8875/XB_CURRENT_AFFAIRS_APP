package com.examkabila.currentaffairs.app;

import android.animation.Animator;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class Activity_Read extends Activity_Parent_Banner_Ads implements
        GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener {
    private ProgressDialog pd;
    private int currentNewsIndex = -1;
    //private ArrayList<Integer> arraySelectedCatIds ;

    View viewMoving;
    View viewStatic;

    RelativeLayout viewLoading;

    final int MAX_TOUCH_VALUE = 0;
    float x = 0;
    float y = 0;


    final long DEFAULT_MAX_SLIDE_DURATION = 350;
    final long DEFAULT_MIN_SLIDE_DURATION = 100;

    private Boolean isSlideInProgress = false;
    private Boolean isAnimInProgress = false;

    private Boolean isSlideUp = true;
    private Boolean isMovingViewCurrent = true;
    private Boolean isNoMoreNews = false;

    private Boolean isTopIconBarHidden = false;


    ImageView imgGoToTop;
    int index;
    //private ActionBarDrawerToggle mDrawerToggle;
    //private DrawerLayout mDrawerLayout;
    private static final String DEBUG_TAG = "Gestures";
    private GestureDetectorCompat mDetector;

    private RelativeLayout rlytNewsContent;


    private TextView txtDate;
    private int year;
    private int month;
    private int day;
    String date = "";

    ArrayList<Object_ReadGK> listNewsItemServer = new ArrayList<Object_ReadGK>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        initHome();

    }

    public void naviQuiz(View v){
        Intent i = new Intent(this,Activity_Quiz.class);
        i.putExtra("date", stringComplete(year) + "-" + stringComplete(month + 1) + "-" + stringComplete(day));
        startActivity(i);
    }

    public void naviVideo(View v){
        Intent i = new Intent(this,Activity_Video_List.class);
        i.putExtra("date",stringComplete(year) + "-" + stringComplete(month + 1) + "-" + stringComplete(day));
        startActivity(i);
    }

    private void initHome() {

        ///arraySelectedCatIds = new ArrayList<Integer>();
        /*DBHandler_Main db = new DBHandler_Main(this);
        db.createDataBase();
*/
        // Instantiate the gesture detector with the
        // application context and an implementation of
        // GestureDetector.OnGestureListener
        mDetector = new GestureDetectorCompat(this, this);
        // Set the gesture detector as the double tap
        // listener.
        mDetector.setOnDoubleTapListener(this);
        mDetector.setIsLongpressEnabled(false);


        //rlytDrawerPane = (RelativeLayout)findViewById(R.id.rlytDrawerPane);
        rlytNewsContent = (RelativeLayout) findViewById(R.id.rlytNewsContent);
        //rlytMainContent = (RelativeLayout) findViewById(R.id.rlytMainContent);
        //mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        //imgMenu = (ImageView)findViewById(R.id.imgMenu);
        //imgGoToTop = (ImageView) findViewById(R.id.imgGoToTop);

        //int drawerWidth = (int) (2.3 *Globals.getScreenSize(this).x / 3);
        //rlytDrawerPane.getLayoutParams().width = drawerWidth;

        //ImageView btnCatAll = (ImageView)findViewById(R.id.btnCatAll);
        //btnCatAll.getLayoutParams().width = drawerWidth;


        //Drawable d = getResources().getDrawable(R.drawable.viewall);
        //int hImage = d.getIntrinsicHeight();
        //int wImage = d.getIntrinsicWidth();

        //int newImageHeight = hImage * (drawerWidth - Globals.dpToPx(10+10)) / wImage;
        //btnCatAll.getLayoutParams().height = newImageHeight + Globals.dpToPx(10+10);

        //TextView txt = (TextView)findViewById(R.id.txtCatHeading);
        //Typeface tfCat = Typeface.createFromAsset(getAssets(), Globals.DEFAULT_CAT_FONT);
        //txt.setTypeface(tfCat);
        /*
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,R.drawable.home_header_menu, R.string.drawer_open, R.string.drawer_close) {
			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				Log.i("Bytes", "onDrawerClosed: " + getTitle());
				isDrawerOpen=true;
				//btnMenu.setBackgroundResource(R.drawable.anim_menu_to_arrow);
				//drawerEventAnim();
			}

			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
				Log.i("Bytes", "onDrawerClosed: " + getTitle());

				isDrawerOpen=false;
				//btnMenu.setBackgroundResource(R.drawable.anim_arrow_to_menu);
				//drawerEventAnim();

				if(isCategoryChanged){
					refresh();
					isCategoryChanged = false;
				}

			}

			@Override
			public void onDrawerSlide(View drawerView, float slideOffset)
			{
				super.onDrawerSlide(drawerView, slideOffset);
				float moveFactor = (rlytDrawerPane.getWidth() * slideOffset);
				rlytMainContent.setTranslationX(moveFactor);
			}


		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);
		mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

		 */

        txtDate = (TextView) findViewById(R.id.txtDateRead);
        txtDate.setText("");
        TextView txtHeader = (TextView)findViewById(R.id.headerNews);
        txtHeader.setTextSize(Globals.getAppFontSize_Large(this));
        txtDate.setTextSize(Globals.getAppFontSize_Large(this));
        ImageView imagedate = (ImageView) findViewById(R.id.imgDate);
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
        // setCurrentDateOnview();
        if ((new Custom_ConnectionDetector(this)).isConnectingToInternet()) {
            //new Custom_GCM_Register(this);
        }

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.rlytNewsHeaderIconContainer);
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideTopIconsBar();
                return true;
            }
        });
        refresh();
    }


    private void setDate() {
        DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int selectedYear,
                                          int selectedMonth, int selectedDay) {
                      /* txtDate.setText(dayOfMonth + "-"
                               + (monthOfYear + 1) + "-" + year);*/
                        year = selectedYear;
                        month = selectedMonth;
                        day = selectedDay;
                        //txtDate.setText(stringComplete(year)+"-"+stringComplete(month+1)+"-"+stringComplete(day));
                        //txtDate.setText(stringComplete(day)+"-"+stringComplete(month+1)+"-"+stringComplete(year));
                        txtDate.setText(Globals.getdateFormat(day, month + 1, year));
                        serverCallForCategoriesAndNews();
                        //getQuesFromServer(true);
                    }
                }, year, (month), day);
        dpd.setButton(DatePickerDialog.BUTTON_POSITIVE, "Get News", dpd);
        dpd.show();

    }

    public void getNews(View v){
       String daTE = Globals.getCalculatedDate(stringComplete(year)+"-"+stringComplete(month+1)+"-"+stringComplete(day), "yyyy-MM-dd", -1);
        getDayMonYear(daTE);
        serverCallForCategoriesAndNews();
    }

    private void setDateSlide(boolean nextDate) {
        String daTE = "";
       if(nextDate)
           daTE = Globals.getCalculatedDate(stringComplete(year)+"-"+stringComplete(month+1)+"-"+stringComplete(day), "yyyy-MM-dd", 1);
        else
           daTE = Globals.getCalculatedDate(stringComplete(year)+"-"+stringComplete(month+1)+"-"+stringComplete(day), "yyyy-MM-dd", -1);

        getDayMonYear(daTE);

        DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int selectedYear,
                                          int selectedMonth, int selectedDay) {
                      /* txtDate.setText(dayOfMonth + "-"
                               + (monthOfYear + 1) + "-" + year);*/
                        year = selectedYear;
                        month = selectedMonth;
                        day = selectedDay;
                        //txtDate.setText(stringComplete(year)+"-"+stringComplete(month+1)+"-"+stringComplete(day));
                        //txtDate.setText(stringComplete(day)+"-"+stringComplete(month+1)+"-"+stringComplete(year));
                        txtDate.setText(Globals.getdateFormat(day, month + 1, year));
                        serverCallForCategoriesAndNews();
                        //getQuesFromServer(true);
                    }
                }, year, (month), day);
        dpd.setButton(DatePickerDialog.BUTTON_POSITIVE, "Get News", dpd);
        dpd.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Log.i("SUSHIL", "sushil cancel call"+date);
                getDayMonYear(date);
            }
        });
       /* dpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Log.i("SUSHIL", "sushil cancel call"+date);
                getDayMonYear(date);
            }
        });*/
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

    private String stringComplete(int c) {
        String st = "";
        if (c < 10) {
            st = "0" + c;
        } else {
            st = c + "";
        }
        return st;
    }

    private void getDayMonYear(String date) {
        try {
            String str[] = date.split("-");
            year = Integer.parseInt(str[0]);
            month = Integer.parseInt(str[1]);
            day = Integer.parseInt(str[2]);
            month = month - 1;
            Log.i("SUSHIL", "month is " + year + " " + month + " " + day);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refresh() {

        //mDrawerLayout.setEnabled(false);
        isMovingViewCurrent = false;


        if ((new Custom_ConnectionDetector(this)).isConnectingToInternet()) {
            // addLoadingView();
            serverCallForCategoriesAndNews();
            //new Custom_GCM_Register(this);
        } else {

            //final Handler handler = new Handler();
            //handler.postDelayed(new Runnable() {
            //@Override
            // public void run() {

            //loadNewsCatFromDB();
            //}
            //}, 1000);

            Globals.showAlert("ERROR", Globals.INTERNET_ERROR, this);
        }

    }


    private void serverCallForCategoriesAndNews() {
        try {
            Log.i("HARSH", "FirstCall");

            String url = Custom_ServerURL_Params.getCurrentNews();
            Log.i("SUSHIL", "Cat URL -- " + url);
            String dateTime = "";
            if (day != 0 && year != 0) {
                dateTime = stringComplete(year) + "-" + stringComplete(month + 1) + "-" + stringComplete(day);
            }
            pd = Globals.showLoadingDialog(pd, this, false, "");
            //CustomRequest jsObjRequest = new CustomRequest(Method.POST, url, params, this.createRequestSuccessListener(), this.createRequestErrorListener());
            Object_AppConfig objAppConfig = new Object_AppConfig(Activity_Read.this);

            Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(Request.Method.POST,
                    //objAppConfig.getVersionNoCategory()
                    url, Custom_ServerURL_Params.getParams_newsSlidet(objAppConfig.getLangCode(), dateTime, this),
                    new Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i("SUSHIL", "Response" + response);
                            //mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                            Globals.hideLoadingDialog(pd);
                            parseAppConfigJson(response);
                           /* if (viewLoading != null) {
                                //  hideLoadingView();
                            }*/

                        }
                    }, new ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError err) {
                    Log.i("SUSHIL", "ERROR VolleyError");
                    Globals.hideLoadingDialog(pd);
                    Globals.showShortToast(Activity_Read.this,"Error occured on server, Try later!");
					/*
					DialogInterface.OnClickListener listner = new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Activity_Home.this.finish();

						}
					};
					 */

                    //loadNewsCatFromDB();
                    /*Globals.showAlertDialogOneButton(
                            Globals.TEXT_CONNECTION_ERROR_HEADING,
                            Globals.TEXT_LOADING_FROM_PREVIOUS_SESSION,
                            Activity_Home.this, "OK", null, false);*/


                }
            });

            Custom_VolleyAppController.getInstance().addToRequestQueue(
                    jsonObjectRQST);

        } catch (Exception e) {
            Globals.hideLoadingDialog(pd);
            Log.i("HARSH",
                    "Excetion FIRSTCALL" + e.getMessage() + "\n"
                            + e.getStackTrace());


        }

    }

    private void parseAppConfigJson(JSONObject response) {

        if (response == null) {

            return;
        }
        Log.i("SUSHIL", "RESPONCE parseAppConfigJson is : " + response.toString());
        try {

           // Object_AppConfig objConfig = new Object_AppConfig(this);

            // boolean hasNews = false;
            // boolean hasCategory = false;

            //// If news is there insert new news News
            if (response.has("slide")) {
                JSONArray slideArray = response.getJSONArray("slide");
                if (slideArray.length() != 0) {
                    listNewsItemServer = null;
                    listNewsItemServer = new ArrayList<>();
                    Log.i("SUSHIL", "insertNewAndDeleteOldNews news onResponse" + response);

                    try {

                        for (int i = 0; i < slideArray.length(); i++) {
                            JSONObject object = slideArray.getJSONObject(i);
                            Object_ReadGK obj = new Object_ReadGK();
                            obj.id = object.getInt("id");
                            obj.content = object.getString("content");

                            obj.image = object.getString("image");
                            obj.date = object.getString("date");
                            if (i == 0) {
                                // txtDate.setText(obj.date);
                                getDayMonYear(obj.date);
                                date = obj.date;
                                txtDate.setText(Globals.getdateFormat(day, month + 1, year));
                            }
                            listNewsItemServer.add(obj);
                        }

               /* Custom_JsonParserNews parserObject = new Custom_JsonParserNews();
                listNewsItemServer = parserObject.getParsedJsonMainNews(response.getJSONArray("news"),objConfig
                        .getRootCatId());*/
                        //Object_ListItem_MainNews objDummySettings = new Object_ListItem_MainNews();
                        //objDummySettings.setId(-999);
                        //listNewsItemServer.add(objDummySettings);
                        currentNewsIndex = -1;
                        isMovingViewCurrent = true;
                        viewStatic = createNewsView();
                        currentNewsIndex++;
                        rlytNewsContent.bringChildToFront(viewStatic);
                        if (rlytNewsContent.getChildCount() > 1)
                            rlytNewsContent.removeViews(0, rlytNewsContent.getChildCount() - 1);
                        preLoadImages();
                        //hasNews = true;

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    getDayMonYear(date);
                    txtDate.setText(Globals.getdateFormat(day, month + 1, year));
                    Globals.showAlert("", "No data found of this date.", this);
                    // Globals.showAlert("ERROR","Data not found of this date",this);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void preLoadImages() {
        if (listNewsItemServer != null)
            for (Object_ReadGK item : listNewsItemServer) {
                String url = item.image;
                if (!TextUtils.isEmpty(url)) {
                    Globals.preloadImage(getApplicationContext(), url);

                    //
                    // .resizeDimen(R.dimen.article_image_preview_width, R.dimen.article_image_preview_height)
                    //.centerCrop()
                }

            }
    }

    private View createNewsView() {

        if (listNewsItemServer == null) {
            Toast.makeText(this, "Please wait!", Toast.LENGTH_SHORT).show();
            return null;
        } else if (listNewsItemServer.size() == 0) {
            return null;
        }

        Log.i("SUSHIL", "CurrentNewsIndex:" + currentNewsIndex + "size of " + listNewsItemServer.size());
        int copyCurrentNewsIndex = currentNewsIndex;

        if (isMovingViewCurrent) {
            Log.i("SUSHIL", "call moving "+copyCurrentNewsIndex);
            if (currentNewsIndex >= listNewsItemServer.size()) {
                currentNewsIndex = listNewsItemServer.size();
                Toast.makeText(this, "You are done for the day.", Toast.LENGTH_SHORT).show();
                showAdsSlideNews();
                //setDateSlide(false);
                return null;
            }
            copyCurrentNewsIndex++;

        } else {
            Log.i("SUSHIL", "call not moving "+copyCurrentNewsIndex);
            if (copyCurrentNewsIndex <= 0) {
                copyCurrentNewsIndex = 0;
                currentNewsIndex = copyCurrentNewsIndex;
                Toast.makeText(this, "No more news to show at this moment.", Toast.LENGTH_SHORT).show();
               // setDateSlide(true);
                return null;
            }
            copyCurrentNewsIndex--;
        }
        View newView = null;
        if (copyCurrentNewsIndex != listNewsItemServer.size()) {
            Object_ReadGK objNews = listNewsItemServer.get(copyCurrentNewsIndex);
            LayoutInflater inflater = (LayoutInflater) this.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);

            newView = inflater.inflate(R.layout.view_news_sliding_home, rlytNewsContent, false);
            ImageView imgViewNews = (ImageView) newView.findViewById(R.id.imgHome);


            TextView txtViewNews = (TextView) newView.findViewById(R.id.txtHeading);
            //TextView txtAuthor = (TextView) newView.findViewById(R.id.txtAuthorDate);
            txtViewNews.setText(objNews.content);
            txtViewNews.setTextSize(Globals.getAppFontSize_Small(this));
           // txtAuthor.setText("By: ExamKabila " + Globals.getdateFormat(day, month + 1, year));
           /* RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) imgViewNews.getLayoutParams();
            int totalContent = Globals.getScreenSize(this).y;
            int imgheight = totalContent - ((totalContent * 70) / 100);
            //lp.width = (int)(imgheight * 1.4);
            lp.height = imgheight;
            imgViewNews.setLayoutParams(lp);*/
            if(objNews.image.equals("")){
                imgViewNews.setVisibility(View.GONE);
            }else {
                imgViewNews.setVisibility(View.VISIBLE);
                Globals.loadImageIntoImageView(imgViewNews, objNews.image, this, R.mipmap.ic_launcher, R.mipmap.ic_launcher);
            }
            if (isMovingViewCurrent) {
                rlytNewsContent.addView(newView, 0);

            } else {
                rlytNewsContent.addView(newView);

            }


            if (isSlideInProgress && !isMovingViewCurrent) {
                newView.setY(-1 * rlytNewsContent.getHeight());
            }
           /* newView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideTopIconsBar();
                }
            });*/

        } else {
            Log.i("SUSHIL", "curent index " + currentNewsIndex);
            currentNewsIndex = listNewsItemServer.size();
            LayoutInflater inflater = (LayoutInflater) this.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            newView = inflater.inflate(R.layout.custom_view_startquiz, rlytNewsContent, false);
            int yearL = year;
            int monthL = month;
            int dayL = day;
            String daTE = Globals.getCalculatedDate(stringComplete(yearL)+"-"+stringComplete(monthL+1)+"-"+stringComplete(dayL), "yyyy-MM-dd", -1);
            try {
                String str[] = daTE.split("-");
                yearL = Integer.parseInt(str[0]);
                monthL = Integer.parseInt(str[1]);
                dayL = Integer.parseInt(str[2]);
                monthL = monthL - 1;
                //Log.i("SUSHIL", "month is " + year + " " + month + " " + day);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Button btn  = (Button)newView.findViewById(R.id.btnLastDate);
            btn.setText(Globals.getdateFormat(dayL, monthL + 1, yearL));
            if (isMovingViewCurrent) {
                rlytNewsContent.addView(newView, 0);

            } else {
                rlytNewsContent.addView(newView);

            }


            if (isSlideInProgress && !isMovingViewCurrent) {
                newView.setY(-1 * rlytNewsContent.getHeight());
            }
        }
        Log.i("SUSHIL", "current index at laST  "+currentNewsIndex);
        return newView;
    }

    private void setFirstView() {
        currentNewsIndex = -1;
        isMovingViewCurrent = true;
        View view = createNewsView();

        if (view != null) {
            viewStatic = view;
            scaleAnimationOver();
        }
        viewMoving = null;

        if (rlytNewsContent.getChildCount() > 1)
            rlytNewsContent.removeViews(1, rlytNewsContent.getChildCount() - 1);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        y = e.getY();
        return true;
    }

    @Override
    public void onShowPress(MotionEvent event) {
        Log.d(DEBUG_TAG, "onShowPress: ");//+ event.toString());
    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        Log.d(DEBUG_TAG, "onSingleTapUp: ");//+ event.toString());
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent event) {
        Log.d(DEBUG_TAG, "onDoubleTap: ");//+ event.toString());
        //showTopIconsBar();
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent event) {
        Log.d(DEBUG_TAG, "onDoubleTapEvent: ");//+ event.toString());
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
        Log.d(DEBUG_TAG, "onSingleTapConfirmed: ");//+ event.toString());
        // tapOnView();
        Object_AppConfig config = new Object_AppConfig(this);
        if(config.isTopBar()){
            hideTopIconsBar();
            config.setToBar(false);
        }else{
            showTopIconsBar();
            config.setToBar(true);
        }
       // hideTopIconsBar();
        return true;
    }


    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {


        if (isAnimInProgress || isNoMoreNews)
            return true;

        if (e1 == null || e2 == null)
            return true;

        Log.d(DEBUG_TAG, "onScroll: X = " + distanceX + " and Y = " + distanceY);//+ e1.toString()+e2.toString());

        Log.d(DEBUG_TAG, "onScroll: e1.y = " + e1.getY() + " and e2.y = " + e2.getY());
        y = e1.getY(); // Motion event for first touch of scroll.
        float totalDisplacementY = Math.abs(e1.getY() - e2.getY());

        if (Math.abs(distanceY) > Math.abs(distanceX)) {
            if (distanceY > 0) {
                isSlideUp = true;
                Log.i("Bytes", "ACTION_MOVE UP");
            } else {
                isSlideUp = false;
                Log.i("Bytes", "ACTION_MOVE DOWN");
            }

            if (!isSlideInProgress) {
                isSlideInProgress = true;
                if (isSlideUp) {
                    isMovingViewCurrent = true;
                    viewMoving = viewStatic;
                    //int backUpId = currentNewsIndex;
                    viewStatic = createNewsView();
                    if (viewStatic == null) {
                        viewStatic = viewMoving;
                        viewMoving = null;
                        isNoMoreNews = true;
                        isSlideInProgress = false;
                        //currentNewsIndex = backUpId;
                        return false;
                    }
                } else {
                    isMovingViewCurrent = false;
                    //int backUpId = currentNewsIndex;
                    viewMoving = createNewsView();
                    if (viewMoving == null) {
                        isNoMoreNews = true;
                        isSlideInProgress = false;
                        //currentNewsIndex = backUpId;
                        return false;
                    }
                }

            }

            if (isSlideInProgress) {
                if (isMovingViewCurrent)
                    slide(-1 * (int) totalDisplacementY, 0);
                else
                    slide((-1 * rlytNewsContent.getHeight()) + (int) totalDisplacementY, 0);
            }
        }
        return true;
    }


    @Override
    public void onLongPress(MotionEvent e) {

    }


    @Override
    public boolean onTouchEvent(MotionEvent touchevent) {
        this.mDetector.onTouchEvent(touchevent);
        // Be sure to call the superclass implementation

        switch (touchevent.getAction()) {
            // when user first touches the screen we get x and y coordinate
            case (MotionEvent.ACTION_UP):

               // hideTopIconsBar();
                slideComplete(0, touchevent.getY());
                return false;
            case (MotionEvent.ACTION_CANCEL):
                Log.d(DEBUG_TAG, "onTouchCancel: ");
                //hideTopIconsBar();
                slideComplete(0, touchevent.getY());
                return false;

            default:
                return super.onTouchEvent(touchevent);
        }

        //return super.onTouchEvent(touchevent);
    }

  /*  @Override
    public boolean onDown(MotionEvent event) {
        Log.d(DEBUG_TAG,"onDown: " );//+ event.toString());
        y = event.getY();
        return true;
    }*/

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2,
                           float velocityX, float velocityY) {
        Log.d(DEBUG_TAG, "onFling: VelocityX = " + velocityX + " VelocityY = " + velocityY);//+ event1.toString()+event2.toString());

        if (e1.getX() < e2.getX()) {
            Log.d(DEBUG_TAG, "Left to Right swipe performed");
        }

        if (e1.getX() > e2.getX()) {
            Log.d(DEBUG_TAG, "Right to Left swipe performed");
        }

        if (e1.getY() < e2.getY() | e1.getY() > e2.getY()) {
            Log.d(DEBUG_TAG, "Up to Down swipe performed");
            slideComplete(velocityY, e2.getY());

        }

        // if (e1.getY() > e2.getY()) {
        //  Log.d(DEBUG_TAG, "Down to Up swipe performed");
        // }

        return true;
    }


    private void slide(int height, long duration) {

        if (viewMoving != null) {

            viewMoving.animate().setDuration(duration)
                    .translationY(height)
                    .alpha(1.0f);
            Log.i("Bytes", "viewMoving not null");

            if (viewStatic != null && currentNewsIndex <= listNewsItemServer.size()) {
                RelativeLayout imgContainer = (RelativeLayout) viewStatic.findViewById(R.id.rlytImgContainer);
                RelativeLayout imgCover = (RelativeLayout) viewStatic.findViewById(R.id.rlytImgCover);
                float scale = Math.abs((float) viewMoving.getY() / rlytNewsContent.getHeight());


                float alpha = 1 - scale; // scale 0 alpha is 1 and when scale is 1 aplha is 0
                scale = (float) (0.80 + scale * 0.20); //


                Log.i("DARSH", "scale" + scale + "alpha " + alpha);
                if (imgCover != null)
                    imgCover.setAlpha(alpha);
                //imgCover.animate()
                //LinearLayout.LayoutParams params =(LinearLayout.LayoutParams) imgContainer.getLayoutParams();

                if (imgContainer != null)
                    imgContainer.animate().setDuration(duration).scaleX(scale).scaleY(scale);

            } else {
                Log.i("viewStatic", "viewMoving is null");
            }

        } else {
            Log.i("Bytes", "viewMoving is null");
        }


    }

    private void slideComplete(float velocity, float currentY) {

        Log.i("DARSH", "slideComplete");
        if (isNoMoreNews) {
            isNoMoreNews = false;
        }

        if (isSlideInProgress) {
            int moveTo = 0;
            isSlideInProgress = false;
            boolean newViewShown = false;
            ;

            if (viewMoving != null) {

                if (isSlideUp) {
                    moveTo = -1 * rlytNewsContent.getHeight();
                    if (isMovingViewCurrent) {

                        newViewShown = true;
                        if (currentNewsIndex >= listNewsItemServer.size() )
                            currentNewsIndex = listNewsItemServer.size() ;
                        else {
                            currentNewsIndex++;
                        }

                    }

                } else if (!isMovingViewCurrent) {
                    newViewShown = true;
                    if (currentNewsIndex <= 0)
                        currentNewsIndex = 0;
                    else {

                        currentNewsIndex--;
                    }

                }

                if (currentNewsIndex == 0) {
                    /*imgGoToTop.setImageResource(R.drawable.home_header_refresh);
                    imgGoToTop.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            onClickRefresh(arg0);

                        }
                    });*/

                } else {
                    /*imgGoToTop.setImageResource(R.drawable.home_header_up);
                    imgGoToTop.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            setFirstView();

                        }
                    });*/
                }

                if (newViewShown) {

                    //setHeader();
                }

				/*
				float currentMovement = Math.abs(y - y2) ;
				long elapseTimeMilliSec =(long) (( System.nanoTime() - startTime )/ 1000000.0);
				if(elapseTimeMilliSec > 0){
				long speed = (long) ((currentMovement - MAX_TOUCH_VALUE)/elapseTimeMilliSec);
				 */
                float currentMovement = Math.abs(y - currentY);
                long newDuration = DEFAULT_MAX_SLIDE_DURATION;
                float speed = Math.abs(velocity);
                long delay = 10;
                if (speed > 0) {
                    newDuration = (long) (((rlytNewsContent.getHeight() - currentMovement) * 1000) / speed);
                    delay = 0;
                }


                Log.i("Bytes", "newDuration = " + newDuration);

                if (newDuration > DEFAULT_MAX_SLIDE_DURATION)
                    newDuration = DEFAULT_MAX_SLIDE_DURATION;
                if (newDuration < DEFAULT_MIN_SLIDE_DURATION)
                    newDuration = DEFAULT_MIN_SLIDE_DURATION;


                viewMoving.animate().setDuration(newDuration).setStartDelay(delay)
                        .translationY(moveTo)
                        .alpha(1.0f).setListener(new Animator.AnimatorListener() {

                    @Override
                    public void onAnimationStart(Animator animation) {
                        isAnimInProgress = true;
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        moveAnimationOver();

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        //moveAnimationOver();Fon
                    }
                });

                if (viewStatic != null && currentNewsIndex <= listNewsItemServer.size()) {
                    RelativeLayout imgContainer = (RelativeLayout) viewStatic.findViewById(R.id.rlytImgContainer);
                    RelativeLayout imgCover = (RelativeLayout) viewStatic.findViewById(R.id.rlytImgCover);
                    float alpha = 1;
                    Log.i("DARSH", "alpha " + alpha);

                    if (isSlideUp) {
                        alpha = 0;
                    }
                    if (imgCover != null)
                        imgCover.animate().setStartDelay(delay).setDuration(newDuration).alpha(alpha);

                    //imgCover.animate()
                    //LinearLayout.LayoutParams params =(LinearLayout.LayoutParams) imgContainer.getLayoutParams();

                    if (imgContainer != null)
                        imgContainer.animate().setStartDelay(delay).setDuration(newDuration).scaleX(1 - alpha).scaleY(1 - alpha).setListener(new Animator.AnimatorListener() {

                            @Override
                            public void onAnimationStart(Animator animation) {
                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                scaleAnimationOver();

                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {
                                //scaleAnimationOver();
                            }
                        });

                } else {
                    Log.i("viewStatic", "viewMoving is null");
                }
            }
        }
    }

    private void moveAnimationOver() {

        isAnimInProgress = false;
		/*
		if(isSlideUp && isMovingViewCurrent){
			rlytNewsContent.removeView(viewMoving);
		}else if (!isSlideUp && isMovingViewCurrent){
			rlytNewsContent.removeView(viewStatic);
			viewStatic = viewMoving;
		}else if (isSlideUp && !isMovingViewCurrent){
			rlytNewsContent.removeView(viewMoving);
		}else if(!isSlideUp && !isMovingViewCurrent)
		 */

        if (!isSlideUp) {
            rlytNewsContent.removeView(viewStatic);
            viewStatic = viewMoving;

        } else {
            rlytNewsContent.removeView(viewMoving);
        }


        if (viewMoving != null)
            viewMoving.animate().setListener(null);
        viewMoving = null;

        rlytNewsContent.bringChildToFront(viewStatic);
        if (rlytNewsContent.getChildCount() > 1)
            rlytNewsContent.removeViews(0, rlytNewsContent.getChildCount() - 1);
    }

    private void scaleAnimationOver() {
        Log.i("DARSH", "scaleAnimationOver");
        if (!isAnimInProgress) {
            if (viewStatic != null) {
                RelativeLayout imgContainer = (RelativeLayout) viewStatic.findViewById(R.id.rlytImgContainer);
                imgContainer.animate().setListener(null);

                if ((isSlideUp && isMovingViewCurrent) || (!isSlideUp && !isMovingViewCurrent)) {

                    //ImageView imageDeatil = (ImageView) viewStatic.findViewById(R.id.imgShowDetail);
                    //RelativeLayout rlytButtonsContainer = (RelativeLayout)findViewById(R.id.rlytButtonsContainer);
                    //RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)imageDeatil.getLayoutParams();

                    //int originalX = lp.width;
                    //int originalY = lp.height;

                    //lp.height = 10;
                    //lp.width = 10;

                    //imageDeatil.setLayoutParams(lp);

                    //Animation zoomin = AnimationUtils.loadAnimation(this, R.anim.zoomin);
                    //Animation zoomout = AnimationUtils.loadAnimation(this, R.anim.zoomout);
                    // imageDeatil.setAnimation(zoomin);
                    //imageDeatil.setAnimation(zoomout);

                    //imageDeatil.startAnimation(zoomin);
                    Object_AppConfig config = new Object_AppConfig(this);
                    if ((isSlideUp && isMovingViewCurrent)) {
                        hideTopIconsBar();
                        config.setToBar(false);
                    } else {
                        showTopIconsBar();
                        config.setToBar(true);
                    }

                }
            }
        }


    }

    private void hideTopIconsBar() {

        if (!isTopIconBarHidden) {

            RelativeLayout layout = (RelativeLayout) findViewById(R.id.rlytNewsHeaderIconContainer);
            layout.animate().setDuration(300).translationY(-1 * layout.getHeight());
            isTopIconBarHidden = true;

        }


    }

    private void showTopIconsBar() {
        if (isTopIconBarHidden) {
            RelativeLayout layout = (RelativeLayout) findViewById(R.id.rlytNewsHeaderIconContainer);
            layout.animate().setDuration(300).translationY(0);
            isTopIconBarHidden = false;
        }
    }

}
