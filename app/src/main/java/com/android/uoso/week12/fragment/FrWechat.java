package com.android.uoso.week12.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.uoso.week12.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportFragment;

public class FrWechat extends SupportFragment {


    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    Unbinder unbinder;

    public static final String[] types = {"top","shehui","guonei","guoji","yule","tiyu"};//新闻类型
    public static final String[] titles = {"头条","社会","国内","国际","娱乐","体育"};//新闻标题



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_wechat, null);
        unbinder = ButterKnife.bind(this, view);
        NewsPagerAdapter newsPagerAdapter = new NewsPagerAdapter(getActivity().getSupportFragmentManager());

        viewPager.setAdapter(newsPagerAdapter);
        viewPager.setOffscreenPageLimit(types.length);//设置预加载页面数量
        tabLayout.setupWithViewPager(viewPager);//将tablayout与viewpager绑定
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    class NewsPagerAdapter extends FragmentPagerAdapter{

        public NewsPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        //设置每个页面展示的fragment
        @Override
        public Fragment getItem(int position) {
            FrNews frNews = FrNews.newInstance(types[position]);
            return frNews;
        }
        //设置页面数量
        @Override
        public int getCount() {
            return types.length;
        }
        //获取页面标题
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
}
