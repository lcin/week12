package com.android.uoso.week12.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.uoso.week12.R;
import com.android.uoso.week12.utils.MyAsyncTask;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MyPagerAdapter extends PagerAdapter{
    private Context context; //上下文
    private List<String> images = new ArrayList<>(); //图片地址

    public MyPagerAdapter(Context context) {
        this.context = context;
    }
    //设置图片地址
    public void setImages(List<String> list){
        images = list;
        notifyDataSetChanged();//刷新界面
    }

    /**
     *   返回viewpager可以滑动的可视页面个数
     */
    @Override
    public int getCount() {
        return images.size();
    }

    /**
     * 构造viewpager每个页面视图并且添加到容器中
     * @param container 容器对象，指的viewpager
     * @param position 当前页面的下标
     * @return
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
       /* View view = LayoutInflater.from(context).inflate(R.layout.item_pager, container, false);
        ImageView ivPager = view.findViewById(R.id.iv_pager);
        String url = images.get(position);//获取当前页面的图片链接
        Glide.with(context).load(url).into(ivPager);//加载图片
        container.addView(view);*/
        ImageView imageView = new ImageView(context);
        String url = images.get(position);

        Glide.with(context).load(url).into(imageView);
       // new MyAsyncTask(context,imageView).execute(url);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);//图片居中
        //代码设置图片宽高
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);//px
        imageView.setLayoutParams(params);
        container.addView(imageView);
        return imageView;
    }

    /**
     * 移除不可视的视图
     * @param container
     * @param position
     * @param object
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
       // super.destroyItem(container, position, object); 这句话需要注释或者删除，否则会报错
        container.removeView((View)object);
    }

    /**
     * 判断当前视图是否为instantiateItem所返回的视图
     * @param view
     * @param object
     * @return
     */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }
}
