package com.android.uoso.week12.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.uoso.week12.R;
import com.android.uoso.week12.model.Person;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PersonAdapter extends BaseAdapter {
    private Context context;
    private List<Person> data = new ArrayList<>();

    public PersonAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Person> list, int pageIndex) {
        if (list==null){
            return;
        }
        if (pageIndex == 0) {
            data.clear();
        }
        data.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Person getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView ==null){
            convertView =  LayoutInflater.from(context).inflate(R.layout.item_person, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
           holder = (ViewHolder) convertView.getTag();
        }
        Person person = getItem(position);
        holder.tvId.setText(person.getId()+"");
        holder.tvName.setText(person.getName());
        holder.tvAge.setText(String.valueOf(person.getAge()));
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_id)
        TextView tvId;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_age)
        TextView tvAge;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
