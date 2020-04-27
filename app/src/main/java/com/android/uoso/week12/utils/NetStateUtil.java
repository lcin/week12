package com.android.uoso.week12.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

/**
 * 判断网络状态的工具类
 */
public class NetStateUtil {
    //判断当前网络连接状态
    public static boolean isConnected(Context context){
        //获取网络管理者对象
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //获取可用网络的信息
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
               /* NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);*/
        if (networkInfo!=null&&networkInfo.isConnected()&&networkInfo.isAvailable()){
           return true;
        }else {
           return false;
        }
    }
    //是否为wifi
    public static boolean isWifi(Context context){
       return isTypeConnected(context,ConnectivityManager.TYPE_WIFI);
    }
    //是否为移动数据网络
    public static boolean isMobile(Context context){
        return isTypeConnected(context,ConnectivityManager.TYPE_MOBILE);
    }

    //判断指定类型网络是否连接
    public static boolean isTypeConnected(Context context,int type){
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getNetworkInfo(type);
        if (networkInfo!=null&&networkInfo.isConnected()&&networkInfo.isAvailable()){
            return true;
        }else {
            return false;
        }
    }


}
