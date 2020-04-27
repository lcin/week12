package com.android.uoso.week12.fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.graphics.PointF;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.android.uoso.week12.R;
import com.android.uoso.week12.model.MessageEvent;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportFragment;

public class FrFound extends SupportFragment {
    @BindView(R.id.btn_event)
    Button btnEvent;
    Unbinder unbinder;
    @BindView(R.id.iv_frame_anim)
    ImageView ivFrameAnim;
    @BindView(R.id.btn_rotate)
    Button btnRotate;
    @BindView(R.id.btn_alpha)
    Button btnAlpha;
    @BindView(R.id.btn_scale)
    Button btnScale;
    @BindView(R.id.btn_translate)
    Button btnTranslate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_found, null);
        unbinder = ButterKnife.bind(this, view);
        startFrameAnimation();
        return view;
    }

    //开始播放帧动画
    private void startFrameAnimation() {
        //将动画设置成图片背景
        ivFrameAnim.setImageResource(R.drawable.frame_anim);
        //获取帧动画对应的drawable对象
        AnimationDrawable drawable = (AnimationDrawable) ivFrameAnim.getDrawable();
        //开启帧动画
        drawable.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btn_event)
    public void onViewClicked() {
        //发布事件
        MessageEvent messageEvent = new MessageEvent();
        messageEvent.setType(1);//1 刷新首页新闻  2退出登录  3下载完成
        messageEvent.setObj("eventbus传递的数据");
        EventBus.getDefault().post(messageEvent);
    }

    @OnClick({R.id.btn_rotate, R.id.btn_alpha, R.id.btn_scale, R.id.btn_translate})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_rotate:
                //beginAnimation(R.anim.rotate,btnRotate);
                //构建一个旋转动画  360°
                ObjectAnimator rotation = ObjectAnimator.ofFloat(btnRotate, "rotation", 0f, 360f);
                rotation.setDuration(2000);
                rotation.setRepeatCount(-1);
                rotation.start();
                break;
            case R.id.btn_alpha:
               // beginAnimation(R.anim.alpha,btnAlpha);
                ObjectAnimator alpha = ObjectAnimator.ofFloat(btnAlpha, "alpha", 1f, 0f, 0.5f);
                alpha.setDuration(2000);
                alpha.start();
                break;
            case R.id.btn_scale:
                //beginAnimation(R.anim.scale,btnScale);
                ObjectAnimator scaleX = ObjectAnimator.ofFloat(btnScale, "scaleX", 1f,3f,2f);
                ObjectAnimator scaleY = ObjectAnimator.ofFloat(btnScale, "scaleY", 1f,3f,2f);
                scaleX.setDuration(2000);
                scaleY.setDuration(2000);
                scaleX.start();
                scaleY.start();
                break;
            case R.id.btn_translate:
                //beginAnimation(R.anim.translate,btnTranslate);
                ObjectAnimator translationX = ObjectAnimator.ofFloat(btnTranslate, "translationX", 0,100);
                translationX.setDuration(2000);
                translationX.start();
                ObjectAnimator sX = ObjectAnimator.ofFloat(btnTranslate, "scaleX", 1f,3f,2f);
                ObjectAnimator sY = ObjectAnimator.ofFloat(btnTranslate, "scaleY", 1f,3f,2f);
                ObjectAnimator rt = ObjectAnimator.ofFloat(btnTranslate, "rotation", 0f, 360f);
                //创建一个动画集合
                AnimatorSet set = new AnimatorSet();
                set.play(translationX).after(sX).with(sY).after(rt);
                set.start();
                break;
        }
    }
    //开始补间动画
    private void beginAnimation(int res,View view){
        //加载一个动画资源， res/anim
        Animation rotate = AnimationUtils.loadAnimation(getContext(), res);
        //开始动画
        view.startAnimation(rotate);
        //设置动画监听事件
        rotate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                //开始动画
                Log.i("=======","onAnimationStart");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //结束动画
                Log.i("=======","onAnimationEnd");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                //重复动画
                Log.i("=======","onAnimationRepeat");

            }
        });
    }
}
