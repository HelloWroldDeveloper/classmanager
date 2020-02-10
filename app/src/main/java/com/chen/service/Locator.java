package com.chen.service;

import android.content.Context;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

public class Locator {

    //执行 基础定位
    public static LocationClient locate(Context applicationContext, LocationClientOption option, BDAbstractLocationListener listener){
        LocationClient locationClient=new LocationClient(applicationContext);
        locationClient.registerLocationListener(listener);
        if(option!=null){
            locationClient.setLocOption(option);
        }
        locationClient.start();
        return locationClient;
    }
}
