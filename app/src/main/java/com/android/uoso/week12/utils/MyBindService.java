package com.android.uoso.week12.utils;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.android.uoso.week12.activity.ActService;

public class MyBindService extends Service{

    private String path;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("========","onCreate");
    }

    public double getRandomNumber(){
        return Math.random(); //获取随机数
    }

    /**
     * 返回一个交流通道给服务
     * @param intent
     * @return
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //应该返回IBinder的实例，但是它是一个接口，所以返回它的实现类Binder的对象
        Log.i("========","onBind");
        if (intent!=null){
            path = intent.getStringExtra("path");
        }
        return new MyBinder();
    }

    public void download(){
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
                Toast.makeText(MyBindService.this, errMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }


    //实现一个类，对这个交流通道进行拓展，将需要传递的数据放入这个通道
    public class MyBinder extends Binder{
        //提供一个给其他组件获取当前绑定服务的方法
        public MyBindService getMyBindService(){
            return MyBindService.this;
        }

        /**
         * Binder提供的进行数据传递的方法
         * @param code 识别码 用于判断是哪一个动作
         * @param data 其他组件传递给Service的数据
         * @param reply 服务给其他组件的回复
         * @param flags 一个标记，表示是否有返回值  0 有（双向传递） 1 没有（单向传递）
         * @return
         * @throws RemoteException
         */
        @Override
        protected boolean onTransact(int code, @NonNull Parcel data, @Nullable Parcel reply, int flags) throws RemoteException {
            String string = data.readString();
           Log.i("======onTransact=====",string);
           reply.writeString("服务的回复内容");
            return super.onTransact(code, data, reply, flags);
        }
    }

    /**
     * 解绑
     * @param intent
     * @return
     */
    @Override
    public boolean onUnbind(Intent intent) {
        Log.i("========","onUnbind");
        return super.onUnbind(intent);
    }
}
