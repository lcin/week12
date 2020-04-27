package com.android.uoso.week12;

import android.app.Application;
import android.content.IntentFilter;

import com.android.uoso.week12.utils.MyRecevier;

import org.xutils.DbManager;
import org.xutils.x;

import me.yokeyword.fragmentation.Fragmentation;

public class App extends Application{
    private static Application mApp;
    private static DbManager.DaoConfig daoConfig;
    @Override
    public void onCreate() {
        super.onCreate();
        // 建议在Application里初始化
        Fragmentation.builder()
                // 显示悬浮球 ; 其他Mode:SHAKE: 摇一摇唤出   NONE：隐藏
                .stackViewMode(Fragmentation.BUBBLE)
                .debug(BuildConfig.DEBUG)
                .install();
       mApp = this;
       //初始化xutils3框架
        x.Ext.init(this);
        //初始化数据库
         daoConfig = new DbManager.DaoConfig()
                .setDbName("my_db")//设置数据库名称
                .setDbVersion(1)//设置数据库版本
                .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                        //数据库升级操作
                    }
                });
        //动态注册广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.android.uoso.week12.TEST");
        //接收网络状态改变的广播
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        MyRecevier myRecevier = new MyRecevier();
        if (myRecevier!=null){
            registerReceiver(myRecevier,intentFilter);
        }
    }

    /**
     * 获取全局数据库配置
     * @return
     */
    public static DbManager.DaoConfig getDaoConfig(){
        return daoConfig;
    }

    /**
     * 获取全局上下文
     * @return
     */
    public static Application getAppContext(){
        return mApp;
    }
}
