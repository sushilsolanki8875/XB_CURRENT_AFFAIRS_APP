package com.examkabila.currentaffairs.app;

import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.inmobi.ads.InMobiAdRequestStatus;
import com.inmobi.ads.InMobiInterstitial;
import com.inmobi.sdk.InMobiSdk;
//import com.chartboost.sdk.CBLocation;
//import com.chartboost.sdk.Chartboost;
import com.startapp.android.publish.StartAppAd;
import com.startapp.android.publish.StartAppSDK;
//import mobi.vserv.android.ads.AdPosition;
//import mobi.vserv.android.ads.AdType;
//import mobi.vserv.android.ads.VservManager;

public class Activity_Parent_IntertialAds extends Activity {

	//private VservManager manager;
	protected StartAppAd startAppAd = null;
	protected int questionCount = 1;
	//private boolean showingChartboost = false;

	InMobiInterstitial interstitial;
	InterstitialAd mInterstitialAd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("SUSHIL", "call Admob inCreat");
		Object_AppConfig config = new Object_AppConfig(this);
		if(config.getshowAdds() ==Globals.APP_TRUE){
						
			if(config.getadTypeInterId() == Globals.ADD_TYPE_INMOBI )
				//InMobi.initialize(this, Globals.AD_INMOBI_PROPERTY_ID);
				InMobiSdk.init(this, Globals.AD_INMOBI_ACCOUNT_ID);
			else if(config.getadTypeInterId() == Globals.ADD_TYPE_STARTAPP){
				startAppAd = new StartAppAd(this);
				StartAppSDK.init(this, Globals.STARTAPP_DEVELOPER_ID,  Globals.STARTAPP_APP_ID, true);
			}else if(config.getadTypeInterId() == Globals.ADD_TYPE_ADMOB){
				//Log.i("SUSHIL", "call Admob inCreat");

					mInterstitialAd = new InterstitialAd(this);
					mInterstitialAd.setAdUnitId("ca-app-pub-1478920275640700/4636610274");
					requestNewInterstitial();

			}
		}
	}
	
	
	
	private void showAds(){
		Object_AppConfig config = new Object_AppConfig(this);
		if(config.getshowAdds() ==Globals.APP_TRUE && questionCount > 2)//
		{
			switch (config.getadTypeInterId()) {
			case Globals.ADD_TYPE_INMOBI:
				loadInMobiIntertial();
				break;
			case Globals.ADD_TYPE_ADMOB:
				//showChartBoostIntertial();
				loadADMOBIntertial();
				break;
			case Globals.ADD_TYPE_STARTAPP:
				showStartAppIntertial();
				break;

			default:
				break;
			}
		}
	}


	public void showAdsSlideNews(){
		Object_AppConfig config = new Object_AppConfig(this);
		if(config.getshowAdds() ==Globals.APP_TRUE)//
		{
			switch (config.getadTypeInterId()) {
				case Globals.ADD_TYPE_INMOBI:
					loadInMobiIntertial();
					break;
				case Globals.ADD_TYPE_ADMOB:
					//showChartBoostIntertial();
					loadADMOBIntertial();
					break;
				case Globals.ADD_TYPE_STARTAPP:
					showStartAppIntertial();
					break;

				default:
					break;
			}
		}
	}


	private void showStartAppIntertial(){
		if(startAppAd != null){
			startAppAd.showAd();
			startAppAd.loadAd();
		}
	}
	
	private void loadADMOBIntertial(){
		Log.i("SUSHIL", "Load Add");
		/*ca-app-pub-1478920275640700/4636610274*/

		if (mInterstitialAd.isLoaded()) {
			mInterstitialAd.show();
		}

		mInterstitialAd.setAdListener(new AdListener() {
			public void onAdClosed() {
				//displayInterstitial();
				Log.i("SUSHIL", "On Closed");

            // if(!mInterstitialAd.isLoaded())
				  requestNewInterstitial();
			}
		});

	}

	private void requestNewInterstitial() {
		Log.i("SUSHIL","unic id "+Activity_Home.android_id);
		AdRequest adRequest = new AdRequest.Builder()
				/*.addTestDevice("d15385ea10ff4c18")*/
				.build();

		mInterstitialAd.loadAd(adRequest);
		//mInterstitialAd.show();

	}

	/*public void displayInterstitial() {
		if (mInterstitialAd.isLoaded()) {
			mInterstitialAd.show();
		}
	}*/

	private void loadInMobiIntertial(){
		Log.i("HARSH", "INIT AD");
		// Create an ad.
		if(interstitial == null){
			System.out.println("interstitial is null");
		
			InMobiInterstitial.InterstitialAdListener interstitialAdListener = new InMobiInterstitial.InterstitialAdListener() {
				@Override
				public void onAdLoadSucceeded(InMobiInterstitial ad) {
				if(ad.isReady()){
				ad.show();
				}
				}
				@Override
				public void onAdDisplayed(InMobiInterstitial ad) {}
				@Override
				public void onAdDismissed(InMobiInterstitial ad) {}
				@Override
				public void onAdInteraction(InMobiInterstitial ad, Map<Object, Object> params) {}
				@Override
				public void onAdRewardActionCompleted(InMobiInterstitial ad, Map<Object, Object> rewards) {}
				@Override
				public void onUserLeftApplication(InMobiInterstitial ad) {}
				@Override
				public void onAdLoadFailed(InMobiInterstitial arg0,
						InMobiAdRequestStatus arg1) {
					// TODO Auto-generated method stub
					
				}
				};
				
		interstitial = new InMobiInterstitial(this, Globals.AD_INMOBI_INTERTIAL_PLACEMENT_ID, interstitialAdListener);

		}
	   
		interstitial.load();
	}
	
	
	private void showChartBoostIntertial(){
		
		Log.i("HARSH", "showChartBoostIntertial called");
		//Chartboost.cacheInterstitial(CBLocation.LOCATION_DEFAULT);
		//Chartboost.showInterstitial(CBLocation.LOCATION_DEFAULT);

		
	}
	
	

	@Override
	protected void onResume() {
	    super.onResume();
	    if(startAppAd != null)
	    	startAppAd.onResume();
	    //if(showingChartboost)
	    	//Chartboost.onResume(this);
	        
	}
	
	@Override
	protected void onPause() {
	    super.onPause();
	    if(startAppAd != null)
	    	startAppAd.onPause();
	    //if(showingChartboost)
	    	//Chartboost.onPause(this);
	}
	
	@Override
	protected void onDestroy() {		
		showAds();
		super.onDestroy();
		//if(showingChartboost)
			//Chartboost.onDestroy(this);
	}
	
	/*
	@Override
	public void onStart() {
	    super.onStart();
	    //if(showingChartboost)
	    	//Chartboost.onStart(this);
	}
	
	
        
	@Override
	public void onStop() {
	    super.onStop();
	    //if(showingChartboost)
	    	//Chartboost.onStop(this);
	}


	@Override
	public void onBackPressed() {
	   // if (Chartboost.onBackPressed())
	        //return;
	    //else
	        super.onBackPressed();
	}
	*/

}
