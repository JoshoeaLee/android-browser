package com.example.webbrowser;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.webkit.WebSettingsCompat;
import androidx.webkit.WebViewFeature;

import java.util.Stack;

public class Controller {

    MainActivity activity;


    //SearchBar
    EditText urlBar;
    ImageView cancel, bookmark;
    ProgressBar progressBar;


    //WebView
    private WebView webView;

    //NavigationImages
    ImageView backButton, forwardButton, homeButton, refreshButton, settingsButton, tabsButton;

    //History Related
    Stack<MyHistory> thisHistory = new Stack<>();
    Stack<MyHistory> forwardHistory = new Stack<>();

    private int loadIndicator = 0;

    /**
     * Constructor for controller.
     * Adds this webView as a tab and switches the screen to this current tab(controller)
     * Turns on Dark Mode if it is already on
     * @param activity The Main Activity
     */
        public Controller(MainActivity activity){
           this.activity = activity;

            //Add this tab to the list of tabs
            activity.addTab(this);
            activity.newTab();

            //Open this tab
           this.openWebPage(activity.getHome().getUrlSite());
        }

    /**
     * First method which sets up the layout from scratch.
     * @param link The website which you will load into at the start
     */
    @SuppressLint("SetJavaScriptEnabled")
    public void openWebPage(String link){

            //Set View
            activity.setContentView(R.layout.activity_main);
            //Add Things
            bookmark =  activity.findViewById(R.id.not_bookmark);
            urlBar =  activity.findViewById(R.id.url_bar);
            cancel =  activity.findViewById(R.id.cancelIcon);
            progressBar =  activity.findViewById(R.id.loading_bar);
            //Add NavBar Buttons
            backButton =  activity.findViewById(R.id.back_button);
            forwardButton =  activity.findViewById(R.id.forward_button);
            homeButton =  activity.findViewById(R.id.home_button);
            refreshButton =  activity.findViewById(R.id.refresh_button);
            settingsButton =  activity.findViewById(R.id.settings_button);
            tabsButton =  activity.findViewById(R.id.tabs_button);


            //Set Tab Number
            TextView tabNumber = activity.findViewById(R.id.tab_number);
            if(activity.getTabNumbers()==1){
                tabNumber.setVisibility(View.INVISIBLE);
            }
            else {
                tabNumber.setText(String.valueOf(activity.getTabNumbers()));
                tabNumber.setVisibility(View.VISIBLE);
            }

            //Mark this as active Tab
            activity.switchTab(this);

            //Add WebView
            webView =  activity.findViewById(R.id.joshView);
            //WebSettings Stuff
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webView.setWebViewClient(new JoshWebViewClient());
            webView.setWebChromeClient(new WebChromeClient(){

                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    super.onProgressChanged(view, newProgress);
                    progressBar.setProgress(newProgress);
                }
            });

            //If Dark Mode then switch accordingly
            if (!MainActivity.WEB_LIGHT) {
                MainActivity.WEB_LIGHT = true;  //setting it to true so that it'll get toggled to false
                this.toggleDarkMode();
            }

            this.searchUrl(link);


            //TOP SEARCH BAR//////////////////////////////////////////////
            //Making URL Bar work
        urlBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(urlBar.getWindowToken(),0 );
                    searchUrl(urlBar.getText().toString());
                    return true;
                }
                return false;
            }

        });

            //Bookmark Button
            bookmark.setOnClickListener(v -> Controller.this.setBookmark());


            //Cancel Button
            cancel.setOnClickListener(v -> urlBar.setText(""));


            //NAVIGATION BAR ///////////////////////////////////////
            //Back Button
            backButton.setOnClickListener(v -> Controller.this.internalBack());

            //Forward Button
            forwardButton.setOnClickListener(v -> Controller.this.internalForward());

            //Home Button
            homeButton.setOnClickListener((v -> searchUrl(activity.home.getUrlSite())));

            //Refresh Button
            refreshButton.setOnClickListener(v -> webView.reload());

            //Settings Popup
            settingsButton.setOnClickListener(v -> {
                PopupMenu settingsMenu = new PopupMenu(activity, v);
                settingsMenu.setOnMenuItemClickListener(activity);
                settingsMenu.inflate(R.menu.settings_menu);
                settingsMenu.show();
            });


            //Tabs PopUp
            tabsButton.setOnClickListener(v -> {
                PopupMenu tabMenu = new PopupMenu(activity, v);
                tabMenu.setOnMenuItemClickListener(activity);
                tabMenu.inflate(R.menu.tabs_menu);
                tabMenu.show();
            });

        }

    /**
     * Adds current page as bookmark.
     */
    public void setBookmark(){

            MyHistory currentPage = new MyHistory(webView.getTitle(), webView.getUrl());
            boolean onlyCopy = true;

            //I have just made the realization 10 minutes before handing this in that I could have just used a Set
            for (MyHistory bookmark: activity.getBookmarks()){
                if(bookmark.toString().equals(currentPage.toString())){
                    onlyCopy=false;
                }
            }

            if(onlyCopy) {
                activity.pushBookmark(currentPage);
                Toast.makeText(activity, R.string.bookmark_success, Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(activity, R.string.bookmark_failure, Toast.LENGTH_SHORT).show();
            }
        }

    /**
     * The internal back button within the browser. Doesn't close when pressed at the start
     */
    public void internalBack(){
            if (thisHistory.size()>1) {
                forwardHistory.push(thisHistory.pop());  //Lets the forward button work
                MyHistory prevPage = thisHistory.pop();
                Controller.this.searchUrl(prevPage.getUrlSite());

            }
            else{
                Toast.makeText(activity, R.string.back_failure, Toast.LENGTH_SHORT).show();
            }
        }

    /**
     * The internal forward button within the browser.
     */
    public void internalForward(){
            if(forwardHistory.size()>=1){
                MyHistory nextPage = forwardHistory.pop();
                Controller.this.searchUrl(nextPage.getUrlSite());
            }
            else{
                Toast.makeText(activity, R.string.forward_failure, Toast.LENGTH_SHORT).show();
            }
        }

    /**
     * Each controller has its own WebView. Each controller represents one tab.
     * @return The WebView for this controller's "tab".
     */
    public WebView getWebView() {
        return webView;
    }


    /**
     * Checks to see if the url in the TextEdit is valid and then searches.
     * @param url The string in the TextEdit
     */
    public void searchUrl(String url){

        boolean validUrl = Patterns.WEB_URL.matcher(url).matches();
        if(validUrl)
            webView.loadUrl(url);
        else
            webView.loadUrl("https://www.google.com/search?q=" + url);
    }

    /**
     * This covers all the options which can be found in popup menus.
     * @param item the menu option which is selected
     * @return If an option is clicked, 'true' will be returned, the popup will close and the
     * specified task will be carried out. If somewhere else is clicked 'false' will be returned and
     * nothing will be carried out.
     */
    @SuppressLint("NonConstantResourceId")
    public boolean menuClick(MenuItem item){
        switch (item.getItemId()){

            //Check Bookmarks
            case R.id.bookmarks:
                this.openBookmarksPage();
                return true;

                //Check History
            case R.id.history:
                this.openHistoryPage();
                return true;


                //Set Home Page
            case R.id.home_page:
                activity.setHome(new MyHistory(webView.getTitle(), webView.getUrl()));
                Toast.makeText(activity, R.string.new_home, Toast.LENGTH_SHORT).show();
                return true;

                //Set to Light/Dark Mode
            case R.id.dark_mode:
                this.toggleDarkMode();
                return true;


                    //Increase Text Size
            case R.id.text_size_plus:
                WebSettings webSettings = webView.getSettings();
                webSettings.setTextZoom(webSettings.getTextZoom() + 20);
                return true;

                //Decrease Text Size
            case R.id.text_size_minus:
                webSettings = webView.getSettings();
                webSettings.setTextZoom(webSettings.getTextZoom() - 20);
                return true;

                //Open New Tab
            case R.id.new_tab:
                Controller newTab = new Controller(activity);
                return true;

            //Close Tab
            case R.id.close_tab:
                this.closeTab();
                return true;

                //See Tabs
            case R.id.see_tab:
                this.openTabsPage();
                return true;

                //Click out of pop-up menus
                default:
                return false;
        }
    }


    /**
     * Used for the built-in back button. Closes the app if there are no more pages to
     * go back by.
     */
    public void backPage() {
        if (thisHistory.size()>1) {
            thisHistory.pop();
            MyHistory prevPage = thisHistory.pop();
            searchUrl(prevPage.getUrlSite());
        }
        else {
            activity.finish();
        }
    }

    /**
     * Opens the Bookmarks Layout. Click on a list item to go to it and press the back button to leave
     */
    public void openBookmarksPage(){
        activity.setContentView(R.layout.bookmark_page);
        BookmarkController bookmarkController = new BookmarkController( activity);

        //Saving current URL so it can be returned to after heading into the bookmark layout.
        MyHistory currentPage = new MyHistory(webView.getTitle(), webView.getUrl());

        if( activity.getBookmarks().size()>0) //Bookmarks Page
            bookmarkController.showBookmarkList(0, currentPage);

        else //Empty Bookmarks Page
            bookmarkController.showBookmarkList(1, currentPage);
    }

    /**
     * Opens the History Layout. Click on a list item to go to it. Press back button to leave and trash button to delete all history.
     */
    public void openHistoryPage(){
        activity.setContentView(R.layout.history_page);
        HistoryController historyController = new HistoryController(activity);

        //Saving current URL so it can be returned to after heading into the history layout.
        MyHistory currentPage = new MyHistory(webView.getTitle(), webView.getUrl());

        if(activity.getHistory().size()>0) //History Page
            historyController.showHistoryList(0, currentPage);

        else //Empty History Page
            historyController.showHistoryList(1, currentPage);
    }

    /**
     * Toggles dark mode on and off depending on the WEB_LIGHT variable in the Main Activity.
     * Uses deprecated methods.
     */
    public void toggleDarkMode(){
        if(MainActivity.WEB_LIGHT){
            //setForceDark was deprecated because a new way of doing this came with API 33 but we're using API30 so oh well
            if(WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)){
                WebSettingsCompat.setForceDark(webView.getSettings(), WebSettingsCompat.FORCE_DARK_ON);
                MainActivity.WEB_LIGHT =false;
            }
        }
        else
        if(WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
            WebSettingsCompat.setForceDark(webView.getSettings(), WebSettingsCompat.FORCE_DARK_OFF);
            MainActivity.WEB_LIGHT =true;
        }
    }

    /**
     * On last tab, closes the application. If not, switches to either the tab after itself or before.
     */
    public void closeTab(){
        if(activity.getTabNumbers()==1) {
            activity.removeTab();
            activity.getTabs().remove(this);
            activity.switchTab(null);
            activity.finish();
        }
        else{
            for(int i = 0; i<activity.getTabs().size(); i++){

                if (this==activity.getTabs().get(i)) {

                    Controller controllerToUse; //Tab which will be switched to
                    if(i<activity.getTabs().size()-1) { //If this is not the last tab
                        controllerToUse = activity.getTabs().get(i+1);
                    } else {
                        controllerToUse = activity.getTabs().get(i-1);
                    }

                    //Open other tab and remove this tab from the list of tabs.
                    String urlToUse = controllerToUse.getWebView().getUrl();
                    activity.switchTab(controllerToUse);
                    activity.removeTab();
                    controllerToUse.openWebPage(urlToUse);
                    activity.getTabs().remove(this);
                }
            }
        }
    }

    /**
     * Opens the tabs layout.
     */
    public void openTabsPage(){
        activity.setContentView(R.layout.tab_page);
        TabController tabController = new TabController( activity);
        MyHistory currentPage = new MyHistory(webView.getTitle(), webView.getUrl());
        tabController.showTabList(0, currentPage);
    }

    /**
     * Used to override some of the WebViewClient functions/
     * Specifically,
     */
    class JoshWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            urlBar.setText(webView.getUrl());
            loadIndicator = 0;
            progressBar.setVisibility(View.VISIBLE);

            MyHistory currentPage = new MyHistory(view.getTitle(), view.getUrl());
            Controller.this.thisHistory.push(currentPage);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            loadIndicator++;
            //As the onPageFinished method gets carried out multiple times,
            // I make sure to only save the page I'm on for history once
            if(loadIndicator==1) {
                //Updating History
                MyHistory currentPage = new MyHistory(view.getTitle(), view.getUrl());
                activity.pushHistory(currentPage);
            }
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}
