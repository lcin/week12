package com.android.uoso.week12.utils;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.android.uoso.week12.activity.ActService;

public class MyService extends Service{
    /**
     * onCreate只会执行一次，服务一旦被创建，就不会再执行此方法
     */
    @Override
    public void onCreate() {
        super.onCreate();
    }
    /**
     * 当开启服务就会执行此方法
     * @param intent 意图
     * @param flags  一个标签，服务的运行模式
     * @param startId 唯一的标识，服务运行的系统id号
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //此方法执行在主线程中，如果做耗时操作，则需要开启子线程
        //因为Service的生命周期很长，所以耗时操作执行完成时需要关闭服务
        //stopSelf();关闭当前服务
        if (intent!=null){
            String path = intent.getStringExtra("path");
            HttpUtil.download(path, new HttpUtil.CallBack() {
                @Override
                public void onProgress(int progress) {
                    Log.i("=====progress=====",progress+"%");
                    //发送消息，通知页面更新进度条
                    Message message = Message.obtain();
                    message.what = ActService.WHAT_PROGRESS;
                    message.arg1 = progress;
                    ActService.handler.sendMessage(message);
                }

                @Override
                public void onFinish(byte[] result) {
                    Log.i("=====result=====",result.length+"b");
                    Message message = Message.obtain();
                    message.what = ActService.WHAT_FINISH;
                    message.obj = result;
                    ActService.handler.sendMessage(message);
                    stopSelf();//下载完成，关闭服务
                }

                @Override
                public void onError(String errMsg) {
                    Toast.makeText(MyService.this, errMsg, Toast.LENGTH_SHORT).show();
                }
            });
        }
        return START_NOT_STICKY;
        //START_NOT_STICKY 非粘性 当系统杀死Service的时候不会再次启动
        //START_STICKY  粘性 当系统杀死Service的时候会再次启动，并且会传入一个为null的intent
        //START_REDELIVER_INTENT  当系统杀死Service的时候会再次启动，并且通过服务传递最后一个intent
    }

    /**
     * 当服务被销毁时会调用此方法
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
