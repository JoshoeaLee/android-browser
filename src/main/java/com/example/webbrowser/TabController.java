package com.example.webbrowser;

import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;


public class TabController {

    MainActivity activity;
    BaseAdapter listAdapter;
    static final int TABS = 0;

    //Navigation Images
    ImageView exitTabs;

    public TabController(MainActivity activity){this.activity = activity;}

    public void showTabList(int state, MyHistory lastPage){

        activity.setContentView(R.layout.tab_page);
        exitTabs =  activity.findViewById(R.id.tab_back);
        ListView listView = activity.findViewById(R.id.tab_list);


        switch (state) {
            case TABS:
                listAdapter = new TabAdapter(activity, activity.getTabs());
                break;
        }


        listView.setAdapter(listAdapter);

        //Go back to where you were before on the main layout
        exitTabs.setOnClickListener(v -> {
            activity.setContentView(R.layout.activity_main);
            activity.getActiveController().openWebPage(lastPage.getUrlSite());
        });


        //Go to Tab Clicked
        listView.setOnItemClickListener((parent, view, position, id) -> {

            Controller controllerToUse = (Controller) listView.getItemAtPosition(position);
            String urlToUse =  controllerToUse.getWebView().getUrl();

            activity.setContentView(R.layout.activity_main);
            controllerToUse.openWebPage(urlToUse);
        });
    }
}
