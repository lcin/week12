package com.android.uoso.week12.utils;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

public class MyIntentService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public MyIntentService(String name) {
        super(name);
    }

    /**
     * 需要添加一个无参的构造函数
     */
    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 此方法运行在子线程中，可以进耗时操作，当执行完此方法的时候会自动调用stopSelf
     * @param intent
     */
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}
