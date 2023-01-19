package com.example.webbrowser;


import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class BookmarkController {

    MainActivity activity;
    static final int BOOKMARKS = 0;
    static final int EMPTY = 1;
    BaseAdapter listAdapter;

    //Navigation Images
    ImageView exitBookmarks;

    public BookmarkController(MainActivity activity){this.activity = activity;}

    public void showBookmarkList(int state, MyHistory lastPage){
        activity.setContentView(R.layout.bookmark_page);

        exitBookmarks =  activity.findViewById(R.id.bookmark_nav_back);

        ListView listView =  activity.findViewById(R.id.bookmark_list);

        switch (state) {
            case BOOKMARKS:
                listAdapter = new BookmarkAdapter(activity, activity.getBookmarks());
                break;

            case EMPTY:
                List<MyHistory> emptyBookmarkList = new ArrayList<>();

                emptyBookmarkList.add(new MyHistory(activity.getString(R.string.empty_list), activity.getString(R.string.empty_bookmarks)));

                listAdapter = new BookmarkAdapter(activity, emptyBookmarkList);
                break;
        }

        listView.setAdapter(listAdapter);

        exitBookmarks.setOnClickListener(v -> {
            activity.setContentView(R.layout.activity_main);
            activity.getActiveController().openWebPage(lastPage.getUrlSite());
        });


        listView.setOnItemClickListener((parent, view, position, id) -> {

            MyHistory urlToUse = (MyHistory) listView.getItemAtPosition(position);
            activity.setContentView(R.layout.activity_main);
            activity.getActiveController().openWebPage(urlToUse.getUrlSite());
        });
    }
}