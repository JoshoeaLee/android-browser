package com.example.webbrowser;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class TabAdapter extends BaseAdapter {
    private final List<Controller> tabList;
    private final LayoutInflater inflater;


    public TabAdapter(Activity activity, List<Controller> tabList){
        super();
        this.tabList = tabList;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return tabList.size();
    }

    @Override
    public Object getItem(int position) {
        return tabList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        TextView textViewUrlTitle;
        TextView textViewTabNumber;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TabAdapter.ViewHolder holder;

        if (convertView == null) {

            holder = new TabAdapter.ViewHolder();
            convertView = inflater.inflate(R.layout.item_row, null);

            holder.textViewUrlTitle = convertView.findViewById(R.id.url_title);
            holder.textViewTabNumber = convertView.findViewById(R.id.subtitle);

            convertView.setTag(holder);
        } else
            holder = (TabAdapter.ViewHolder) convertView.getTag();

         int tabNo = (position+1);
        Controller controller =  tabList.get(position);
        holder.textViewUrlTitle.setText(controller.getWebView().getTitle());
        holder.textViewTabNumber.setText("Tab Number " + tabNo);

        return convertView;
    }
}

