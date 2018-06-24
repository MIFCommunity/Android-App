package com.marketinvestorfund.mifcommunity;

//import android.annotation.SuppressLint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class AppAuthFullActivity extends AppCompatActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    //private static final int UI_ANIMATION_DELAY = 300;
    //private final Handler mHideHandler = new Handler();
    //private View mContentView;
    //private final Runnable mHidePart2Runnable = new Runnable() {
        //@SuppressLint("InlinedApi")
        //@Override
        //public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.

            /*mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            */
        //}
    //};
    //private View mControlsView;
    //private final Runnable mShowPart2Runnable = new Runnable() {
        //@Override
        //public void run() {
            // Delayed display of UI elements
            //ActionBar actionBar = getSupportActionBar();
            //if (actionBar != null) {
            //    actionBar.show();
            //}
        //    mControlsView.setVisibility(View.VISIBLE);
        //}
    //};
    //private boolean mVisible;
    //private final Runnable mHideRunnable = new Runnable() {
    //    @Override
    //    public void run() {
    //        hide();
    //    }
    //};
    private WebView authWebView;
    private WebViewClient authWebClient;
    private URI authURL;
    
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_app_auth_full);
        //ActionBar actionBar = getSupportActionBar();
        //if (actionBar != null) {
        //    actionBar.setDisplayHomeAsUpEnabled(true);
        //    actionBar.hide();
        //}

        //mVisible = true;
        //mControlsView = findViewById(R.id.fullscreen_content_controls);

        //mContentView = findViewById(R.id.fullscreen_content);
        //mContentView = this.findViewById(R.id.authWebView_full).getRootView();

        // Set up the user interaction to manually show or hide the system UI.
        //mContentView.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //        toggle();
        //    }
        //});

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        //findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);


        authWebView = new WebView(this);
        //authWebView = (WebView) findViewById(R.id.authWebView_full);

        //setContentView( authWebView.getRootView() );
        setContentView( authWebView );


        //mContentView = authWebView;
        authWebView.getSettings().setJavaScriptEnabled(true);

        //authWebView.setWebChromeClient(new WebChromeClient()


        // Let's display the progress in the activity title bar, like the
        // browser app does.
        //getWindow().requestFeature(Window.FEATURE_PROGRESS);

        /*
        final AppCompatActivity activity = this;
        authWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                // Activities and WebViews measure progress with different scales.
                // The progress meter will automatically disappear when we reach 100%
                activity.setProgress(progress * 1000);
            }
        });

        authWebView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(activity, "Oh no! " + description, Toast.LENGTH_SHORT).show();
            }
        });
        */


        try {
        //    authURL = new URL( getString(R.string.site_protocal_unsecure), getString(R.string.site_host), getString(R.string.site_auth_uri) );
        authURL = new URI( getString(R.string.app_core_uri_scheme_unsecure),
                null,
                getString(R.string.app_core_uri_authority),
                -1,
                getResources().getStringArray(R.array.app_authorize_page_paths)[1],
                null,
                null );

        } catch (URISyntaxException e) {
        //} catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.d( "String:authURL=", authURL.toString() );

        //authWebView.setWebViewClient( AuthClient );
        authWebClient = new WebViewClient() {

        //@Override
        //public void onPageStarted(WebView view, String url, Bitmap favicon){

            //URI pageURL = null;
            //try {
            //pageURL = URI.create(url);
            //} catch (MalformedURLException e) {
            //    e.printStackTrace();
            //}

            //Log.d( "String:url=", pageURL.toString() );
            //Log.d( "String:authURL=", authURL.toString() );



            //if ( authURL.sameFile( pageURL.toURL() ) || (authURL.toString().compareToIgnoreCase(url.concat("/").toString() )==0) ) {
            //Log.d( "onPageStarted: ", "URL Targets are matched!)" );
                    /*
                    Intent upIntent = NavUtils.getParentActivityIntent(AppAuthFullActivity.this);
                    if (NavUtils.shouldUpRecreateTask(AppAuthFullActivity.this, upIntent)) {
                        // This activity is NOT part of this app's task, so create a new task
                        // when navigating up, with a synthesized back stack.
                        TaskStackBuilder.create(AppAuthFullActivity.this)
                                // Add all of this activity's parents to the back stack
                                .addNextIntentWithParentStack(upIntent)
                                // Navigate up to the closest parent
                                .startActivities();
                    } else {
                        // This activity is part of this app's task, so simply
                        // navigate up to the logical parent activity.
                        NavUtils.navigateUpTo(AppAuthFullActivity.this, upIntent);
                    }
                    */
            //}
            //else{
            //Log.d( "onPageStarted:" , "URL Targets are NOT matched!");
            //super.onPageStarted(view, url, favicon);
            //}
            //}

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request )
            {

                Log.d( "Start:Overide Func","<shouldOverrideUrlLoading>" );
                Log.d( "URI:url=", request.getUrl().toString() );
                Log.d( "URL:authURL=", authURL.toString() );
                Log.d( "Exit:Overide Func","<shouldOverrideUrlLoading>" );


                if ( authURL.equals(request.getUrl()) ) {
                    Log.d( "url & authURL: ", "Are Equal" );
                    //urlData = URLDecoder.decode(url.substring(APP_SCHEME.length()), "UTF-8");
                    //respondToData(urlData);
                    return true;
                }
                else
                    return false;
            }
        };

        authWebView.setWebViewClient( authWebClient );

        authWebView.loadUrl( authURL.toString() );

    }








    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    //private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
    //    @Override
    //    public boolean onTouch(View view, MotionEvent motionEvent) {
    //        if (AUTO_HIDE) {
    //            delayedHide(AUTO_HIDE_DELAY_MILLIS);
    //        }
    //        return false;
    //    }
    //};


    /*
    @Override
    public void onBackPressed() {
        if (authWebView.canGoBack())
            authWebView.goBack();
        else
            super.onBackPressed();
    }
    */

    /*
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (authWebView.hasWindowFocus()){
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
        }
        else
            super.onWindowFocusChanged(hasFocus);
    }
    */

    //@Override
    //protected void onPostCreate(Bundle savedInstanceState) {
    //    super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
    //    delayedHide(100);

        //setContentView(authWebView);
    //}


    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
     if (id == android.R.id.home) {
            // This ID represents the Home or Up button.
           NavUtils.navigateUpFromSameTask(this);
           return true;
      }
        return super.onOptionsItemSelected(item);
    }
    */

    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    // This activity is NOT part of this app's task, so create a new task
                    // when navigating up, with a synthesized back stack.
                    TaskStackBuilder.create(this)
                            // Add all of this activity's parents to the back stack
                            .addNextIntentWithParentStack(upIntent)
                            // Navigate up to the closest parent
                            .startActivities();
                } else {
                    // This activity is part of this app's task, so simply
                    // navigate up to the logical parent activity.
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    */

    //private void toggle() {
    //    if (mVisible) {
    //        hide();
    //    } else {
    //        show();
    //    }
    //}

   // private void hide() {
        // Hide UI first
        //ActionBar actionBar = getSupportActionBar();
        //if (actionBar != null) {
        //    actionBar.hide();
        //}
    //    mControlsView.setVisibility(View.GONE);
    //    mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
    //    mHideHandler.removeCallbacks(mShowPart2Runnable);
    //    mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    //}

    //@SuppressLint("InlinedApi")
    //private void show() {
        // Show the system bar
    //    mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    //            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
    //    mVisible = true;

        // Schedule a runnable to display UI elements after a delay
    //    mHideHandler.removeCallbacks(mHidePart2Runnable);
    //    mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    //}

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    //private void delayedHide(int delayMillis) {
    //    mHideHandler.removeCallbacks(mHideRunnable);
    //    mHideHandler.postDelayed(mHideRunnable, delayMillis);
    //}
}
