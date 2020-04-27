package com.android.uoso.week12.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 自定义广播接收者
 */
public class MyRecevier extends BroadcastReceiver{
    //收到广播执行此方法，在主线程中
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent!=null){
            String action = intent.getAction();
            if ("com.android.uoso.week12.TEST".equals(action)){
                String data = intent.getStringExtra("data");
                System.out.println(data);
            }else if("android.net.conn.CONNECTIVITY_CHANGE".equals(action)){
                boolean connected = NetStateUtil.isConnected(context);
                boolean wifi = NetStateUtil.isWifi(context);
                boolean mobile = NetStateUtil.isMobile(context);
                Log.i("======connected=====",connected+"");
                Log.i("======wifi=====",wifi+"");
                Log.i("======mobile=====",mobile+"");
            }else if ("android.provider.Telephony.SMS_RECEIVED".equals(action)){
                //读取短信内容
                //获取意图里的内容
                Bundle bundle = intent.getExtras();
                Object[] objects = (Object[]) bundle.get("pdus");
                //遍历数组,将对象转成SmsMessage
                for (Object pdus: objects ) {
                   byte[] bytes = (byte[]) pdus;
                    SmsMessage sms = SmsMessage.createFromPdu(bytes);
                    String phone = sms.getOriginatingAddress();//获取发送手机号
                    String msg = sms.getMessageBody();//获取短信内容
                    long timestampMillis = sms.getTimestampMillis();//获取发送短信的时间
                    Date date = new Date(timestampMillis);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String time = sdf.format(date);
                    Log.i("=====手机号=====",phone);
                    Log.i("=====短信内容=====",msg);
                    Log.i("=====发送时间=====",time);
                }
            }
        }
    }
}
