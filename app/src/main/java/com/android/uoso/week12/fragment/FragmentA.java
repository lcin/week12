package com.android.uoso.week12.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.uoso.week12.R;

public class FragmentA extends Fragment{
    //无参构造函数
    public FragmentA() {
    }

    //加载布局和初始化控件
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_a, null);
        //初始化控件
        TextView textView = view.findViewById(R.id.text_view);
        return view;
    }
}
