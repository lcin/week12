package com.android.uoso.week12.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.uoso.week12.R;
import com.android.uoso.week12.fragment.FragmentB;

public class MainActivity extends AppCompatActivity {

    private FragmentB fragmentB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadFragmentB();
        Button button = findViewById(R.id.btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentB.sendData("初始化之后传递数据");
            }
        });

    }

    /**
     * 动态加载FragmentB
     */
    private void loadFragmentB() {
        //1.获取fragment管理器
        FragmentManager manager = getSupportFragmentManager();
        //2.开启一个事务
        FragmentTransaction transaction = manager.beginTransaction();
        fragmentB = new FragmentB();
        Bundle bundle = new Bundle();
        bundle.putString("data","这是传递的参数");
        bundle.putInt("int",1000);
        fragmentB.setArguments(bundle);
        //3.向容器中添加fragment
        transaction.add(R.id.frame_layout, fragmentB);
        //4.提交事务
        transaction.commit();
    }
}
