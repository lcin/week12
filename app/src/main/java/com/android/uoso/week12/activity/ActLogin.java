package com.android.uoso.week12.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.uoso.week12.R;
import com.android.uoso.week12.utils.CookieUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActLogin extends AppCompatActivity {
    public static String SP_NAME = "my_sp";

    @BindView(R.id.ed_username)
    EditText edUsername;
    @BindView(R.id.ed_password)
    EditText edPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.btn_send)
    Button btnSend;
    @BindView(R.id.btn_service)
    Button btnService;
    private SharedPreferences sp;
    public static String[] permissions = {Manifest.permission.RECEIVE_SMS};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_login);
        ButterKnife.bind(this);
        String username = (String) CookieUtil.get("username", "");
        if (!TextUtils.isEmpty(username)) {
            goToWechat();
        }
        /*boolean isLogin = (boolean) CookieUtil.get("isLogin", false);
        if (isLogin){
            goToWechat();
        }*/
        requestPermission();
    }

    //动态申请短信权限
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int i = checkSelfPermission(Manifest.permission.RECEIVE_SMS);
            if (i != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, permissions, 101);
            }
        }
    }



   /* @Override
    protected void onDestroy() {
        super.onDestroy();
        //注销广播接收者
        unregisterReceiver(new MyRecevier());
    }*/

    /**
     * 保存用户的用户名和密码
     */
    private void saveUsername() {
        String username = edUsername.getText().toString().trim();
        String password = edPassword.getText().toString().trim();
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "请输入用户名和密码", Toast.LENGTH_SHORT).show();
            return;
        }
        CookieUtil.put("username", username);
        CookieUtil.put("password", password);
        CookieUtil.put("isLogin", true);
    }

    /**
     * 跳转主页面
     */
    private void goToWechat() {
        Intent intent = new Intent(this, ActWechat2.class);
        startActivity(intent);
    }

    @OnClick({R.id.btn_login, R.id.btn_send,R.id.btn_service,R.id.btn_video})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                saveUsername();
                goToWechat();
                break;
            case R.id.btn_send:
                sendBroad();
                break;
            case R.id.btn_service:
                Intent intent = new Intent(this, ActService.class);
                startActivity(intent);
                break;
            case R.id.btn_video:
                Intent intent1 = new Intent(this, ActVideo.class);
                startActivity(intent1);
                break;
        }
    }

    /**
     * 发送一条广播
     */
    private void sendBroad() {
        Intent intent = new Intent("com.android.uoso.week12.TEST");
        intent.putExtra("data", "这是广播携带的数据");
        //8.0新特性
        intent.setPackage(getPackageName());
        sendBroadcast(intent);
    }

}
