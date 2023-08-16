package com.heartratemonitor.bmitrack.calculaterate.AdsUtils.FirebaseADHandlers;


import static com.heartratemonitor.bmitrack.calculaterate.AdsUtils.Utils.Constants.isNull;

import android.app.Application;
import android.util.Log;

import com.google.android.gms.ads.MobileAds;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.heartratemonitor.bmitrack.calculaterate.AdsUtils.PreferencesManager.AppPreferences;
import com.heartratemonitor.bmitrack.calculaterate.AdsUtils.Utils.Constants;
import com.heartratemonitor.bmitrack.calculaterate.AdsUtils.Utils.Global;
import com.heartratemonitor.bmitrack.calculaterate.R;
import com.heartratemonitor.bmitrack.calculaterate.SharePreferences;

import java.io.FileInputStream;

public class MyApplication extends Application {
    private static SharePreferences preferences;
    private static MyApplication instance;
 /*   public static SharePreferences getPreferences() {
        if (preferences == null) preferences = new SharePreferences(getInstance());
        return preferences;
    }*/

    public static synchronized MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        AppPreferences appPreferencesManger = new AppPreferences(this);
        FirebaseMessaging.getInstance().subscribeToTopic(Constants.ADSJSON);
        FirebaseApp.initializeApp(this);
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        Constants.adsJsonPOJO = Global.getAdsData(appPreferencesManger.getAdsModel());

        if (Constants.adsJsonPOJO != null && !isNull(Constants.adsJsonPOJO.getParameters().getApp_open_ad().getDefaultValue().getValue())) {
            Constants.adsJsonPOJO = Global.getAdsData(appPreferencesManger.getAdsModel());
            Constants.hitCounter = Integer.parseInt(Constants.adsJsonPOJO.getParameters().getApp_open_ad().getDefaultValue().getHits());
        } else {
            FirebaseUtils.initiateAndStoreFirebaseRemoteConfig(this, adsJsonPOJO -> {
                appPreferencesManger.setAdsModel(adsJsonPOJO);
                Constants.adsJsonPOJO = adsJsonPOJO;
                Constants.hitCounter = Integer.parseInt(Constants.adsJsonPOJO.getParameters().getApp_open_ad().getDefaultValue().getHits());
            });
        }

        MobileAds.initialize(this, initializationStatus -> {});
        new AppOpenAds(this);



    }

}