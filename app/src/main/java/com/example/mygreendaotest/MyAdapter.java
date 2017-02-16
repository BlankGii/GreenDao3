package com.example.mygreendaotest;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mygreendaotest.bean.User;

import java.util.List;

/**
 * Created by gcy on 2017/2/15 0015.
 */
public class MyAdapter extends BaseAdapter {
    private List<User> data;
    private Context mContext;
    private Typeface mTypeface;

    public MyAdapter(Context context, List<User> data) {
        this.data = data;
        mContext = context;
        mTypeface = Typeface.createFromAsset(context.getAssets(), "Frn111n.ttf");
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyView myView = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.list_item, null);
            myView = new MyView();
            myView.tv_firstLetter = (TextView) convertView.findViewById(R.id.tv_firstLetter);
            myView.tv_userName = (TextView) convertView.findViewById(R.id.tv_userName);
            myView.tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
            convertView.setTag(myView);
        } else {
            myView = (MyView) convertView.getTag();
        }
//            myView.tv_firstLetter
        String userName = data.get(position).getUsername();
        String firstChar = userName.substring(0, 1).toUpperCase();
        myView.tv_userName.setText(userName);
        myView.tv_phone.setText(data.get(position).getPhone());
        myView.tv_firstLetter.setText(firstChar);
        myView.tv_firstLetter.setTypeface(mTypeface);
        return convertView;
    }

    class MyView {
        TextView tv_firstLetter;
        TextView tv_userName;
        TextView tv_phone;
    }
}
