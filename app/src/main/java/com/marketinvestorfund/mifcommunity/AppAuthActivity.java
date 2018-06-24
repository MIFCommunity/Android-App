package com.marketinvestorfund.mifcommunity;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.widget.Toast;

import java.util.EnumMap;


public class AppAuthActivity extends AppCoreActivity {

    private WebView authWebView;
    protected EnumMap<APP_AUTHORIZED_PAGES_URI,Uri> AuthPagesUriMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_auth);

        FloatingActionButton auth_settings_fab = (FloatingActionButton) findViewById(R.id.auth_settings_fab);
        SetupSettingsActionButton(auth_settings_fab);

        authWebView = findViewById(R.id.auth_webview);

        //SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this );

        //TODO: Remove Debug entry.
        Log.d( "connection_profile=", getSecureConnectionPref( getApplicationContext() ).name() );

        if ( getSecureConnectionPref( getApplicationContext() ).equals( APP_SECURE_CONNECTION_PREF.NEVER ) ) {
            AuthPagesUriMap = BuildAuthPageMap( setAppSecureConnectionMode(false) );

            Toast.makeText(getApplicationContext(), "The connection to server is unsecure!", Toast.LENGTH_LONG).show();


        }
        else {
            AuthPagesUriMap = BuildAuthPageMap( setAppSecureConnectionMode(true) );


        }

        //TODO: Remove Debug entry.
        Log.d( "SecureConnectionMode= ", getAppSecureConnectionMode().toString() );
        Log.d( "AuthPagesUriMap= ", AuthPagesUriMap.get( APP_AUTHORIZED_PAGES_URI.valueOf("LOGIN") ).toString() );

        LoadPageContent( getApplicationContext(), authWebView, AuthPagesUriMap );

    }


    private void SetupSettingsActionButton( FloatingActionButton settingsFAB ) {

        super.SetupSettingsActionButton( getApplicationContext(), settingsFAB );

    }


    private EnumMap<APP_AUTHORIZED_PAGES_URI,Uri> BuildAuthPageMap( Boolean secure_connection ) {

        Uri.Builder uri_page = new Uri.Builder();

        if ( secure_connection )
            uri_page.scheme( getString( R.string.app_core_uri_scheme_secure ) );
        else
            uri_page.scheme( getString(R.string.app_core_uri_scheme_unsecure) );

        uri_page.authority( getString( R.string.app_core_uri_authority ) );

        String[] uri_enums = getResources().getStringArray( R.array.app_authorize_page_enums );
        String[] uri_paths = getResources().getStringArray( R.array.app_authorize_page_paths );

        EnumMap<APP_AUTHORIZED_PAGES_URI,Uri> PagesUriMap = new EnumMap<APP_AUTHORIZED_PAGES_URI,Uri>( APP_AUTHORIZED_PAGES_URI.class );

        for ( int i=0; i<uri_enums.length; i++ ) {
            uri_page.encodedPath( uri_paths[i] );
            Uri url = uri_page.build();
            PagesUriMap.put( APP_AUTHORIZED_PAGES_URI.valueOf( uri_enums[i] ), url );

            //TODO: Remove Debug entry.
            Log.d( "BuildAuthPageMap: ", "uri_page= " + url.toString() );
        }

        return PagesUriMap;
    }


//    @Override
//    private void LoadPageContent( Context context, Uri page ) {
//
//        authWebView.setWebViewClient( SetupWebViewClient( context, AuthPagesUriMap ) );
//
//        //TODO: Remove Debug entry.
//        Log.d( "LoadAuthPageContent: ", "page= " + page.toString() );
//
//        authWebView.loadUrl( page.toString() );
//    }


    //@Override
    protected static Object onRetryConfirmAction( Object argument ) {

        //pageUriMap = BuildAuthPageMap( setAppSecureConnectionMode(true) );

        //TODO: Remove Debug entry.
        //Log.d( "SecureConnectionMode= ", getAppSecureConnectionMode().toString() );
        //Log.d( "AuthPagesUriMap= ", AuthPagesUriMap.get(APP_AUTHORIZED_PAGES_URI.LOGIN).toString() );

        //LoadPageContent( AuthPagesUriMap.get(APP_AUTHORIZED_PAGES_URI.LOGIN) );

        AppCoreActivity.onRetryConfirmAction( argument );

        return null;
    }


    //@Override
    protected static Object onContinueWarningAction( Object argument ) {

        //pageUriMap = BuildAuthPageMap( setAppSecureConnectionMode(false) );

        //TODO: Remove Debug entry.
        //Log.d( "SecureConnectionMode= ", getAppSecureConnectionMode().toString() );
        //Log.d( "AuthPagesUriMap= ", pageUriMap.get(APP_AUTHORIZED_PAGES_URI.LOGIN).toString() );

        //LoadPageContent( context, webview, pageUriMap.get(APP_AUTHORIZED_PAGES_URI.LOGIN) );

        AppCoreActivity.onCancelWarningAction( argument );

        return null;
    }



}
