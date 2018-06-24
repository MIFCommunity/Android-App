package com.marketinvestorfund.mifcommunity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.EnumMap;

public class AppHomeActivity extends AppCoreActivity {
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    //protected WebView appWebView;

    protected static EnumMap<APP_CONTENT_PAGES_URI,Uri> AppContentUriMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        FloatingActionButton settings_fab = (FloatingActionButton) findViewById(R.id.settings_fab);
        SetupSettingsActionButton( settings_fab );

        //TODO: Remove Debug entry.
        Log.d( "connection_profile=", getSecureConnectionPref(getApplicationContext()).name() );

        if ( getSecureConnectionPref( getApplicationContext() ).equals( APP_SECURE_CONNECTION_PREF.NEVER ) ) {
            setAppSecureConnectionMode(false);

            Toast.makeText(getApplicationContext(), "The connection to server is unsecure!", Toast.LENGTH_LONG).show();
        }
        else {
            setAppSecureConnectionMode(true);
        }

        LaunchPrivacyPolicyDialog();


        AppContentUriMap = BuildAppPagesMap();

        //TODO: Remove Debug entry.
        Log.d( "SecureConnectionMode= ", getAppSecureConnectionMode().toString() );
        Log.d( "AppContentUriMap= ", AppContentUriMap.values().toString() );
    }


    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data ) {
        // Check which request we're responding to
        if ( requestCode == APP_REQUEST_CODE.PRIVACY_POLICY_EULA.ordinal() ) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {

            }
        }
    }


    public static Intent makeIntent(Context context) {
        return new Intent(context, AppHomeActivity.class );
    }


    private void SetupSettingsActionButton(FloatingActionButton fab) {

        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = makeIntent( getApplicationContext(), AppHomeActivity.class );
                startActivity(intent);
            }
        });
    }

    private void LaunchPrivacyPolicyDialog() {

        Boolean privacy_accepted  = getDefaultSharedPreferences( getApplicationContext() )
                .getBoolean( getString( R.string.app_core_connection_profile_pref), false );

        if ( !privacy_accepted ) {
            Intent launch_privacy = new Intent( getApplicationContext(), AppPrivacyActivity.class );
            startActivityForResult( launch_privacy, APP_REQUEST_CODE.PRIVACY_POLICY_EULA.ordinal() );
        }

    }


    /*@Override
    public void onBackPressed() {
        if ( ((WebView) this.getCurrentFocus()).canGoBack())
            ((WebView) this.getCurrentFocus()).goBack();
        else
            super.onBackPressed();
    }*/


   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/



    private EnumMap<APP_CONTENT_PAGES_URI,Uri> BuildAppPagesMap() {

        Uri.Builder app_uri_bldr = new Uri.Builder();

        if ( getAppSecureConnectionMode() )
            app_uri_bldr.scheme( getString(R.string.app_core_uri_scheme_secure) );
        else
            app_uri_bldr.scheme( getString(R.string.app_core_uri_scheme_unsecure) );

        app_uri_bldr.authority( getString(R.string.app_core_uri_authority) );

        String[] app_enums = getResources().getStringArray( R.array.app_authorize_page_enums );
        String[] app_paths = getResources().getStringArray( R.array.app_authorize_page_paths );

        EnumMap<APP_CONTENT_PAGES_URI,Uri> pagesUriMap = new EnumMap<APP_CONTENT_PAGES_URI,Uri>( APP_CONTENT_PAGES_URI.class );

        for ( int i=0; i<app_enums.length; i++ ) {
            app_uri_bldr.encodedPath( app_paths[i] );
            pagesUriMap.put( APP_CONTENT_PAGES_URI.valueOf( app_enums[i] ), app_uri_bldr.build() );

            //TODO: Remove Debug entry.
            Log.d( "BuildAppPagesMap: ", "app_uri_bldr= " + app_uri_bldr.build() + "'" );
        }

        return pagesUriMap;
    }


    private static void LoadAppContentPages( Context context, WebView webview, Uri page ) {

        webview.setWebViewClient( SetupContentWebViewClient( context ) );

        //TODO: Remove Debug entry.
        Log.d( "LoadAppContentPages: ", "page= " + page.toString() );

        webview.loadUrl( page.toString() );
    }


    private static WebViewClient SetupContentWebViewClient( Context context ) {

        return SetupWebViewClient( context, AppContentUriMap );
    }




    /**
     * A placeholder fragment containing a simple view.
     */
    public static class AppPageFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "ARG_SECTION_NUMBER";
        //public StringBuilder pageURL;
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */

        public WebView mWebView;


        public AppPageFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static AppPageFragment newInstance(int sectionNumber) {
            AppPageFragment fragment = new AppPageFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);

            /*
            URL loginURL = null;
            try {
                loginURL = new URL( getString(R.string.site_protocal_unsecure), getString(R.string.site_host), getResources().getStringArray(R.array.site_pages_uri)[sectionNumber] );
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            Log.d( "String:loginURL=", loginURL.toString() );
            */
            //webPage.loadUrl( loginURL.toString() );

            fragment.setArguments(args);

            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_home_screen, container, false);

            Bundle prefs = getArguments();
            int frag_id = prefs.getInt(ARG_SECTION_NUMBER);

            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText( getString(R.string.section_format, frag_id ));

            WebView webPage = rootView.findViewById(R.id.web_content);
            webPage.getSettings().setJavaScriptEnabled(true);

            LoadAppContentPages( getContext(), webPage, AppContentUriMap.get(frag_id-1) );

            return rootView;
        }

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        //private APP_CONTENT_PAGES_URI fragmentID;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            //fragmentID = APP_CONTENT_PAGES_URI.values()[position];

            return AppPageFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Returns total pages count.
            return getResources().getStringArray(R.array.app_content_page_enums).length;
        }
    }
}
