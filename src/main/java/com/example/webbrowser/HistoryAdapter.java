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

public class HistoryAdapter extends BaseAdapter {

    private final List<MyHistory> historyList;
    private final LayoutInflater inflater;

    public HistoryAdapter(Activity activity, List<MyHistory> historyList) {
        super();
        this.historyList = historyList;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return historyList.size();
    }

    @Override
    public Object getItem(int position) {
        return historyList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        TextView textViewUrlTitle;
        TextView textViewUrlSite;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {

            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_row, null);

            holder.textViewUrlTitle = convertView.findViewById(R.id.url_title);
            holder.textViewUrlSite = convertView.findViewById(R.id.subtitle);

            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        MyHistory myHistory = historyList.get(position);
        holder.textViewUrlTitle.setText(myHistory.getUrlTitle());
        holder.textViewUrlSite.setText(myHistory.getUrlSite());


        return convertView;
    }
}
