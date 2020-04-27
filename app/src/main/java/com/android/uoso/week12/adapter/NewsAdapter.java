package com.android.uoso.week12.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.uoso.week12.R;
import com.android.uoso.week12.model.News;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsAdapter extends BaseAdapter {
    private Context context;
    private List<News.ResultBean.DataBean> list = new ArrayList<>();

    public NewsAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<News.ResultBean.DataBean> data) {
        if (data != null) {
            list = data;
            notifyDataSetChanged();
        }
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public News.ResultBean.DataBean getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
       if (convertView==null){
           convertView =   LayoutInflater.from(context).inflate(R.layout.item_news, parent, false);
           holder = new ViewHolder(convertView);
           convertView.setTag(holder);
       }else {
           holder = (ViewHolder) convertView.getTag();
       }
        News.ResultBean.DataBean item = getItem(position);
       holder.tvTitle.setText(item.getTitle());
       holder.tvDate.setText(item.getDate());
       holder.tvAuthor.setText(item.getAuthor_name());
       setImage(holder.img1,item.getThumbnail_pic_s());
        setImage(holder.img2,item.getThumbnail_pic_s02());
        setImage(holder.img3,item.getThumbnail_pic_s03());
        return convertView;
    }

    public void setImage(ImageView imageView,String imgUrl){
        if (TextUtils.isEmpty(imgUrl)){//当没有图片则隐藏
            imageView.setVisibility(View.GONE);
        }else {
            imageView.setVisibility(View.VISIBLE);
            Glide.with(context).load(imgUrl).into(imageView);
        }
    }


    static class ViewHolder {
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_date)
        TextView tvDate;
        @BindView(R.id.tv_author)
        TextView tvAuthor;
        @BindView(R.id.img1)
        ImageView img1;
        @BindView(R.id.img2)
        ImageView img2;
        @BindView(R.id.img3)
        ImageView img3;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
