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

public class BookmarkAdapter extends BaseAdapter {
    private final List<MyHistory> bookmarkList;
    private final LayoutInflater inflater;


    public BookmarkAdapter(Activity activity, List<MyHistory> bookmarkList){
        super();
        this.bookmarkList = bookmarkList;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return bookmarkList.size();
    }

    @Override
    public Object getItem(int position) {
        return bookmarkList.get(position);
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
        BookmarkAdapter.ViewHolder holder;

        if (convertView == null) {

            holder = new BookmarkAdapter.ViewHolder();
            convertView = inflater.inflate(R.layout.item_row, null);

            holder.textViewUrlTitle = convertView.findViewById(R.id.url_title);
            holder.textViewUrlSite = convertView.findViewById(R.id.subtitle);

            convertView.setTag(holder);
        } else
            holder = (BookmarkAdapter.ViewHolder) convertView.getTag();

        MyHistory myHistory = bookmarkList.get(position);
        holder.textViewUrlTitle.setText(myHistory.getUrlTitle());
        holder.textViewUrlSite.setText(myHistory.getUrlSite());


        return convertView;
    }
}