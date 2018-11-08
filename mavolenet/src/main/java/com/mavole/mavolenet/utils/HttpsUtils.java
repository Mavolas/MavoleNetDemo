package com.mavole.mavolenet.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;


import com.mavole.mavolenet.R;
import java.lang.reflect.Field;

import static android.content.Context.WIFI_SERVICE;

/**
 * Created by mavole
 * on 2018/7/4.
 */
public class HttpsUtils {






    public static String GetAndroidString(Context context, String key)
    {
        try {
            Object RObj = new R.string();
            Field field = RObj.getClass().getField(key);
            int index = field.getInt(RObj);

            String value = context.getResources().getString(index);

            return value;
        } catch (Exception e) {
            return "";
        }
    }


}
