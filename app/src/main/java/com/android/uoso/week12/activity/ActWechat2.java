package com.android.uoso.week12.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.uoso.week12.R;
import com.android.uoso.week12.fragment.FrContact;
import com.android.uoso.week12.fragment.FrFound;
import com.android.uoso.week12.fragment.FrMe;
import com.android.uoso.week12.fragment.FrWechat;

import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.SupportFragment;

public class ActWechat2 extends SupportActivity implements RadioGroup.OnCheckedChangeListener {

    private FrWechat frWechat;
    private FrContact frContact;
    private FrFound frFound;
    private FrMe frMe;
    private RadioGroup group;
    private int curPosition = 0;//当前展示的fragment下标
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_wechat);
        group = findViewById(R.id.radio_group);
        loadFragment();
        group.setOnCheckedChangeListener(this);
       RadioButton rbWechat = (RadioButton) group.getChildAt(0);
       rbWechat.setChecked(true);
    }

    /**
     * 装载fragment
     */
    private void loadFragment() {
        if (frWechat==null){//若fragment为空，则新建并加载
            frWechat = new FrWechat();
            frContact = new FrContact();
            frFound = new FrFound();
            frMe = new FrMe();
        }else {
            frWechat = findFragment(FrWechat.class);
            frContact =  findFragment(FrContact.class);
            frFound = findFragment(FrFound.class);
            frMe = findFragment(FrMe.class);
        }
        //装载多个根fragment
        loadMultipleRootFragment(R.id.frame_layout,0,frWechat,frContact,frFound,frMe);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.rb_wechat:
                showHideFragment(frWechat,getPreFragment());
                curPosition = 0;
                break;
            case R.id.rb_contact:
                showHideFragment(frContact,getPreFragment());
                curPosition = 1;
                break;
            case R.id.rb_found:
                showHideFragment(frFound,getPreFragment());
                curPosition = 2;
                break;
            case R.id.rb_me:
                showHideFragment(frMe,getPreFragment());
                curPosition = 3;
                break;
        }
    }

    /**
     * 获取上一个显示的fragment对象
     */
    private SupportFragment getPreFragment(){
        switch (curPosition){
            case 0:
                return frWechat;
            case 1:
                return frContact;
            case 2:
                return frFound;
            case 3:
                return frMe;
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FrMe.REQUEST_CODE){
            if (grantResults[0]== PackageManager.PERMISSION_GRANTED){
                //申请的第一个权限成功
                frMe.writeSD();
            }

        }
    }
}
