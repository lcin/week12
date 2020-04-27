package com.android.uoso.week12.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.uoso.week12.IpAddress;
import com.android.uoso.week12.R;
import com.android.uoso.week12.adapter.MyPagerAdapter;
import com.android.uoso.week12.adapter.NewsAdapter;
import com.android.uoso.week12.model.MessageEvent;
import com.android.uoso.week12.model.News;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportFragment;

public class FrNews extends SupportFragment implements ViewPager.OnPageChangeListener {
    Unbinder unbinder;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.ll_point)
    LinearLayout llPoint;
    @BindView(R.id.list_view)
    ListView listView;
    @BindView(R.id.rl_banner)
    RelativeLayout rlBanner;
    //1.创建一个Handler对象，重写handleMessage（）方法
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //此方法在主线程中执行，用于接受从子线程发送回来的消息并处理
            if (msg.what == 10) {
                //收到消息，滑动到下一个页面
                int curPosition = viewPager.getCurrentItem();//0 1 2 3 4
                int nextPostion = (curPosition + 1) % list.size();//下一页下标  1 2 3 4 0
                viewPager.setCurrentItem(nextPostion, true);//设置viewpager展示页的下标    是否平滑滚动
                //继续发送消息触发滚动
                handler.sendEmptyMessageDelayed(10, 3000);
            }
        }
    };

    private MyPagerAdapter pagerAdapter;
    private List<String> list;
    private String type;
    private NewsAdapter newsAdapter;

    //创建新闻页面对象，并且传递新闻类型
    public static FrNews newInstance(String type) {
        Bundle args = new Bundle();
        args.putString("type", type);
        FrNews fragment = new FrNews();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_news, null);
        unbinder = ButterKnife.bind(this, view);
        Bundle bundle = getArguments();
        type = bundle.getString("type");
        if (TextUtils.isEmpty(type)) {
            type = "top";
        }
        initBanner();
        initListView();
        //订阅事件
        EventBus.getDefault().register(this);
        return view;
    }

    //接收到事件
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefresh(MessageEvent messageEvent){
        if (messageEvent!=null&&messageEvent.getType()==1){
            initListView();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//注销事件
    }

    //初始化新闻列表
    private void initListView() {
        newsAdapter = new NewsAdapter(getContext());
        listView.setAdapter(newsAdapter);
        //请求新闻接口
        RequestParams params = new RequestParams(IpAddress.getApi(IpAddress.GET_NEWS));
        params.addParameter("key", IpAddress.APP_KEY);
        params.addParameter("type", type);
        x.http().post(params, new Callback.CommonCallback<News>() {
            @Override
            public void onSuccess(News news) {
                if (news != null) {
                    if (news.getError_code() == 0) {
                        News.ResultBean result = news.getResult();
                        if (result != null) {
                            List<News.ResultBean.DataBean> data = result.getData();
                            newsAdapter.setList(data);
                        }
                    } else {
                        Toast.makeText(_mActivity, news.getReason(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println(ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


    //初始化轮播图
    private void initBanner() {
        pagerAdapter = new MyPagerAdapter(getContext());
        viewPager.setOffscreenPageLimit(4);//设置viewpager预加载页面数量
        viewPager.setAdapter(pagerAdapter);
        getImages();
        viewPager.addOnPageChangeListener(this);
        initPoint(0);
    }


    @Override
    public void onPause() {
        super.onPause();
        handler.removeMessages(10);//停止轮播图的滚动
    }

    @Override
    public void onResume() {
        super.onResume();
        //rlBanner.requestFocus();//获取焦点，解决listview抢占焦点的问题
        handler.sendEmptyMessageDelayed(10, 3000);//开始滚动
    }

    /**
     * 获取图片
     */
    private void getImages() {
        list = new ArrayList<>();
        /*list.add(R.mipmap.img1);
        list.add(R.mipmap.img2);
        list.add(R.mipmap.img3);
        list.add(R.mipmap.img4);
        list.add(R.mipmap.img5);*/
        list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1585196264620&di=67bacd90ca6acdb31a84e56ce9d11c5e&imgtype=0&src=http%3A%2F%2Fpic2.zhimg.com%2F50%2Fv2-57ed15fbf04fc93bbeb37038853c1f88_hd.jpg");
        list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1585196264619&di=c4d65e043f8a69d01496d5f40a6d7644&imgtype=0&src=http%3A%2F%2Fattach.bbs.miui.com%2Fforum%2F201806%2F29%2F180430gfw14az64ykhn4k4.jpg");
        list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1585196264619&di=a8b62f74e55aa9c587e20c242b707ce3&imgtype=0&src=http%3A%2F%2Fpic4.zhimg.com%2F50%2Fv2-5b4a8f03bb73a9471e2dee66c696cfc5_hd.jpg");
        list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1585196264618&di=118d8a2a0359c8bc38cea3dd6221b829&imgtype=0&src=http%3A%2F%2Fpic2.zhimg.com%2F50%2Fv2-a52848f69efcd0baad6107107b64be2f_hd.jpg");
        list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1585196264618&di=37a56dd5d5107b905638d14740708b9c&imgtype=0&src=http%3A%2F%2Fattach.bbs.letv.com%2Fforum%2F201512%2F06%2F093810sxjxayyhtetxnhpn.jpg");
        pagerAdapter.setImages(list);
        //发一条空白延时消息，通知handler开始轮播
    }


    //模拟子线程发送消息
    private void sendMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //子线程，执行耗时操作
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                /*handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //回调主线程
                        Toast.makeText(_mActivity, "下载成功", Toast.LENGTH_SHORT).show();
                    }
                });*/
                //1.在需要传递数据的地方构造一条消息
                Message message = handler.obtainMessage();
                // Message message1 = Message.obtain();
                //2.把数据放置在消息里
                message.what = 10;//设置标记，用于在接受消息时识别
                message.obj = "下载完成";
                message.arg1 = 10101;
                message.arg2 = 1111;
                //3.发送消息
                handler.sendMessage(message);
                //发送一条空的消息
                handler.sendEmptyMessage(20);
                //发送一条延时消息
                handler.sendMessageDelayed(message, 5000);
                //发送一条延时空消息
                handler.sendEmptyMessageDelayed(20, 5000);

                /*getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //切回主线程
                        Toast.makeText(_mActivity, "runOnUiThread", Toast.LENGTH_SHORT).show();
                    }
                });*/
                /*btn.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(_mActivity, "btn.post", Toast.LENGTH_SHORT).show();
                    }
                });*/

            }
        }).start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * 当页面滑动时调用
     *
     * @param position
     * @param positionOffset
     * @param positionOffsetPixels
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    /**
     * 滑动停止会调用此方法
     *
     * @param position
     */
    @Override
    public void onPageSelected(int position) {
        initPoint(position);
    }

    /**
     * 动态添加小圆点
     */
    private void initPoint(int position) {
        llPoint.removeAllViews();//清除所有view
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = 20;
        params.rightMargin = 20;
        for (int i = 0; i < list.size(); i++) {
            ImageView imageView = new ImageView(getContext());
            imageView.setLayoutParams(params);
            if (i == position) {//当前页面添加白色圆点
                //imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.point_white));
                imageView.setImageResource(R.drawable.point_white);
            } else {
                imageView.setImageResource(R.drawable.point_gray);
            }
            llPoint.addView(imageView);
        }
    }

    /**
     * 当页面滑动状态改变调用此方法
     *
     * @param state
     */
    @Override
    public void onPageScrollStateChanged(int state) {
        switch (state) {
            case ViewPager.SCROLL_STATE_DRAGGING: //开始滑动
                break;
            case ViewPager.SCROLL_STATE_SETTLING://停止滑动
                break;
            case ViewPager.SCROLL_STATE_IDLE://停止滑动前
                break;
        }
    }
}
