package com.android.uoso.week12.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.uoso.week12.R;
import com.android.uoso.week12.utils.MyBindService;
import com.android.uoso.week12.utils.MyService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActService extends AppCompatActivity {

    @BindView(R.id.btn_start)
    Button btnStart;

    public static ProgressBar progressBar;
    public static TextView tvProgress;

    public static final String PATH = "https://qd.myapp.com/myapp/qqteam/pcqq/PCQQ2020.exe";
    public static int WHAT_PROGRESS = 10000;
    public static int WHAT_FINISH = 10001;
    public static final String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};//需要申请的权限
    @SuppressLint("HandlerLeak")
    public static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == WHAT_PROGRESS) {
                int progress = msg.arg1;
                progressBar.setProgress(progress);
                tvProgress.setText(progress + "%");
            } else if (msg.what == WHAT_FINISH) {
                byte[] result = (byte[]) msg.obj;
                tvProgress.setText("下载完成");
            }
        }
    };
    @BindView(R.id.btn_bind)
    Button btnBind;
    private MyBindService myBindService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_service);
        ButterKnife.bind(this);
        progressBar = findViewById(R.id.progress_bar);
        tvProgress = findViewById(R.id.tv_progress);

        //绑定服务
        Intent intent = new Intent(this, MyBindService.class);
        intent.putExtra("path", PATH);
        //BIND_AUTO_CREATE 当服务的实例不存在时则去创建实例，一般采用这个
        bindService(intent,conn,BIND_AUTO_CREATE);
    }




    //通过start的方式开启服务
    private void startMyService() {
        Intent intent = new Intent(this, MyService.class);
        intent.putExtra("path", PATH);
        startService(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //申请的第一个权限成功
                startMyService();
            }

        }
    }

    @OnClick({R.id.btn_start, R.id.btn_bind})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_start:
                //判断是否大于等于Android6.0
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    int i = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    //判断权限是否开启
                    if (i != PackageManager.PERMISSION_GRANTED) {
                        //申请动态权限
                        ActivityCompat.requestPermissions(this, permissions, 101);
                    } else {
                        startMyService();
                    }
                } else {
                    startMyService();
                }
                /*if(isConnected&&myBindService!=null){
                    myBindService.getRandomNumber();
                }*/
                break;
            case R.id.btn_bind:
                if(isConnected&&myBindService!=null){
                    myBindService.download();
                }
                break;
        }
    }
    private boolean isConnected = false;//当前服务是否连接
    public ServiceConnection conn = new ServiceConnection() {
        //当服务连接上，调用此方法
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            isConnected = true;
            //iBinder 指代是MyBinder对象 ，需要强转类型
            MyBindService.MyBinder myBinder = (MyBindService.MyBinder) iBinder;
            //拿到Service的对象，可以直接调用其内部方法
            myBindService = myBinder.getMyBindService();
            double randomNumber = myBindService.getRandomNumber();
            System.out.println(randomNumber+"");
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            data.writeString("传递给服务的数据");
            try {
                myBinder.transact(200,data,reply,0);
                String result = reply.readString();
                Log.i("======reply=====",result);

            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }
        //当服务断开连接，调用此方法,注意：调用unbinderService()不一定会触发此方法，服务只有被系统杀死的时候会触发
        @Override
        public void onServiceDisconnected(ComponentName name) {
            isConnected =false;
            System.out.println("onServiceDisconnected");
        }
    };
}
