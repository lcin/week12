package com.android.uoso.week12.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.uoso.week12.R;
import com.android.uoso.week12.fragment.FrContact;
import com.android.uoso.week12.fragment.FrFound;
import com.android.uoso.week12.fragment.FrMe;
import com.android.uoso.week12.fragment.FrWechat;

public class ActWechat extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    private FrWechat frWechat;
    private FragmentManager manager;
    private FrContact frContact;
    private FrFound frFound;
    private FrMe frMe;
    private FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_wechat);
        manager = getSupportFragmentManager();
        RadioGroup group = findViewById(R.id.radio_group);
        //监听单选框的改变，随即切换fragment的显示
        group.setOnCheckedChangeListener(this);
        //默认选中第一个fragment
        RadioButton rbWechat = (RadioButton) group.getChildAt(0);
        rbWechat.setChecked(true);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        ft = manager.beginTransaction();
        switch (checkedId){
            case R.id.rb_wechat:
                hideAllFragment();
                //当fragment为null时，动态加载
                if (frWechat ==null){
                    frWechat = new FrWechat();
                    ft.add(R.id.frame_layout, frWechat);
                }else {//当不为null时，直接显示
                    ft.show(frWechat);
                }
                ft.commit();
                break;
            case R.id.rb_contact:
                hideAllFragment();
                //当fragment为null时，动态加载
                if (frContact ==null){
                    frContact = new FrContact();
                    ft.add(R.id.frame_layout, frContact);
                }else {//当不为null时，直接显示
                    ft.show(frContact);
                }
                ft.commit();
                break;
            case R.id.rb_found:
                hideAllFragment();
                //当fragment为null时，动态加载
                if (frFound ==null){
                    frFound = new FrFound();
                    ft.add(R.id.frame_layout, frFound);
                }else {//当不为null时，直接显示
                    ft.show(frFound);
                }
                ft.commit();
                break;
            case R.id.rb_me:
                hideAllFragment();
                //当fragment为null时，动态加载
                if (frMe ==null){
                    frMe = new FrMe();
                    ft.add(R.id.frame_layout, frMe);
                }else {//当不为null时，直接显示
                    ft.show(frMe);
                }
                ft.commit();
                break;
        }
    }

    /**
     * 隐藏所有fragment
     */
    private void hideAllFragment(){
        if (ft==null){
            return;
        }
        if(frWechat!=null){
            ft.hide(frWechat);
        }
        if (frContact!=null){
            ft.hide(frContact);
        }
        if (frFound!=null){
            ft.hide(frFound);
        }
        if (frMe!=null){
            ft.hide(frMe);
        }

    }
}
