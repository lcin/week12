package com.android.uoso.week12.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.uoso.week12.R;

public class FragmentB extends Fragment{

    private TextView textView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_b, null);
        textView = view.findViewById(R.id.text_view);
        Bundle bundle = getArguments();
        if (bundle!=null){
            String data = bundle.getString("data");
            textView.setText(data);
        }
        return view;
    }

    public void sendData(String data){
       textView.setText(data);
    }
}
