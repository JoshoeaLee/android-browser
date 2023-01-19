package com.example.webbrowser;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.PopupMenu;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;




//CREDIT
//Search Bar, Progress Bar, setting up and understanding webclient -> https://www.youtube.com/watch?v=vB5ZOcu4aBE
//ListAdapters and CustomListView -> Karsten's powerpoint
//Shared Preferences Stuff -> Kirita and Jude
//GSon/JSon Stuff -> https://stackoverflow.com/questions/37048731/gson-library-in-android-studio


public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    ArrayList<MyHistory> history = new ArrayList<>();
    ArrayList<MyHistory> bookmarks = new ArrayList<>();
    ArrayList<Controller> tabs = new ArrayList<>();
    Controller activeController;
    static boolean WEB_LIGHT = true; //Light/Dark Mode WebView
    MyHistory home = new MyHistory("Google","https://www.google.com" );
    int tabNumbers=0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Retrieving anything saved
        SharedPreferences sharedPreferences = getSharedPreferences("browser.preferences", Context.MODE_PRIVATE);
        Gson gson = new Gson();

        //Different things to load in
        String jsonHistory = sharedPreferences.getString("HISTORY","" );
        String jsonBookmarks = sharedPreferences.getString("BOOKMARKS", "");
        String jsonHome = sharedPreferences.getString("HOME", "");

        //Types to get
        Type pagesType = new TypeToken<List<MyHistory>>(){}.getType();
        Type pageType = new TypeToken<MyHistory>(){}.getType();

        //Retrieved Data
        List<MyHistory> loadedHistory = gson.fromJson(jsonHistory, pagesType);
        List<MyHistory> loadedBookmarks = gson.fromJson(jsonBookmarks, pagesType);
        MyHistory loadedHome = gson.fromJson(jsonHome, pageType);


        //Loading in Data

        if(loadedHistory!=null)
            history.addAll(loadedHistory);

        if(loadedBookmarks!=null)
            bookmarks.addAll(loadedBookmarks);

        if(loadedHome!=null)
            home = loadedHome;

        //New Tab
        activeController = new Controller(this);

}

    @SuppressLint("ApplySharedPref")
    @Override
    protected void onStop() {
        super.onStop();

        //Saving History/Bookmarks/Home Page (Converting to string)
        Gson gson = new Gson();
        String jsonHistory = gson.toJson(history);
        String jsonBookmarks = gson.toJson(bookmarks);
        String jsonHome = gson.toJson(home);


        SharedPreferences sharedPreferences = getSharedPreferences("browser.preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //Committing everything that needs to be saved.
         editor.putString("HISTORY", jsonHistory);
         editor.putString("BOOKMARKS", jsonBookmarks);
         editor.putString("HOME", jsonHome);

         editor.commit();

    }

    @Override
    public void onBackPressed() {
        activeController.backPage();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return activeController.menuClick(item);
    }


    //Tab Related
    public void newTab(){tabNumbers++;}
    public void removeTab(){tabNumbers--;}
    public void addTab(Controller controller){tabs.add(controller);}
    public int getTabNumbers(){return tabNumbers;}

    public ArrayList<Controller> getTabs(){return tabs;}
    public void switchTab(Controller controller){activeController=controller;}


    //Home Related
    public MyHistory getHome(){return home;}
    public void setHome(MyHistory newHome){this.home=newHome;}

    //Get current webView
    public Controller getActiveController() {return activeController;}

    //History/Bookmarks
    public ArrayList<MyHistory> getHistory(){return history;}
    public ArrayList<MyHistory> getBookmarks(){return bookmarks;}
    public void pushHistory(MyHistory myHistory){history.add(myHistory);}
    public void pushBookmark(MyHistory myHistory){bookmarks.add(myHistory);}


}