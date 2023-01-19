package com.example.webbrowser;


import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

public class HistoryController {

    MainActivity activity;
    static final int HISTORY = 0;
    static final int EMPTY = 1;
    BaseAdapter listAdapter;

    //Navigation Images
    ImageView exitHistory, emptyHistory;


    public HistoryController(MainActivity activity){
        this.activity = activity;
    }

    public void showHistoryList(int state, MyHistory lastPage){
        activity.setContentView(R.layout.history_page);

        exitHistory =  activity.findViewById(R.id.history_nav_back);
        emptyHistory =  activity.findViewById(R.id.history_nav_delete);

        ListView listView =  activity.findViewById(R.id.history_list);

        switch (state) {
            case HISTORY:
                listAdapter = new HistoryAdapter(activity, activity.getHistory());
                break;

            case EMPTY:
                List<MyHistory> emptyHistoryList = new ArrayList<>();

                emptyHistoryList.add(new MyHistory(activity.getString(R.string.empty_list), activity.getString(R.string.empty_history)));

                listAdapter = new HistoryAdapter(activity, emptyHistoryList);
                break;
        }

        listView.setAdapter(listAdapter);

        exitHistory.setOnClickListener(v -> {
            activity.setContentView(R.layout.activity_main);
            activity.getActiveController().openWebPage(lastPage.getUrlSite());
        });

        emptyHistory.setOnClickListener(v -> {
            activity.getHistory().clear();
            HistoryController.this.showHistoryList(1, lastPage);
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {

            MyHistory urlToUse = (MyHistory) listView.getItemAtPosition(position);
            activity.setContentView(R.layout.activity_main);
            activity.getActiveController().openWebPage(urlToUse.getUrlSite());
        });
      }
    }