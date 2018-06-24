package com.marketinvestorfund.mifcommunity;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

abstract class AppCoreActivity extends AppCompatActivity {


    public enum APP_SECURE_CONNECTION_PREF {
        ALWAYS,
        WHEN_POSSIBLE,
        NEVER;
    }

    public enum APP_AUTHORIZED_PAGES_URI {
        REGISTER,
        LOGIN,
        RESET_PASSWORD,
        LOST_PASSWORD;
    }

    public enum APP_CONTENT_PAGES_URI {
        FORUM,
        ACTIVITY,
        MEMBER,
        GROUPS,
        ACCOUNT,
        BLOG;
    }

    public enum APP_REQUEST_CODE {
        NONE,
        PRIVACY_POLICY_EULA;
    }


    private static boolean AppSecureConnectionMode;


    protected static Intent makeIntent(Context context, Class name) {
        return new Intent(context, name );
    }


    protected static SharedPreferences getDefaultSharedPreferences( Context context ) {
        return PreferenceManager.getDefaultSharedPreferences( context );
    }


    protected static APP_SECURE_CONNECTION_PREF getSecureConnectionPref( Context context ) {

        SharedPreferences prefs = getDefaultSharedPreferences( context );

        return APP_SECURE_CONNECTION_PREF.valueOf( prefs.getString( context.getString(R.string.app_core_connection_profile_pref)
                , APP_SECURE_CONNECTION_PREF.WHEN_POSSIBLE.name() ) );
    }


    protected static Boolean setAppSecureConnectionMode(boolean secure) {
        AppSecureConnectionMode = secure;
        return AppSecureConnectionMode;
    }


    protected static Boolean getAppSecureConnectionMode() {
        return AppSecureConnectionMode;
    }


    protected static Uri.Builder BuildBaseUri( Context context, String authority) {

        Uri.Builder uri_base_builder = new Uri.Builder();

        if ( getAppSecureConnectionMode() )
            uri_base_builder.scheme( context.getString(R.string.app_core_uri_scheme_secure) );
        else
            uri_base_builder.scheme( context.getString(R.string.app_core_uri_scheme_unsecure) );

        uri_base_builder.authority( authority );

        //TODO: Remove Debug entry.
        Log.d( "SecureConnectionMode = ", getAppSecureConnectionMode().toString() );
        Log.d( "uri_base_builder = ", uri_base_builder.build().toString() );

        return uri_base_builder;
    }


    protected static Uri BuildContentUri( Uri.Builder uriBaseBuilder, String path ) {

        uriBaseBuilder.path( path );

        //TODO: Remove Debug entry.
        Log.d( "uri_content_builder = ", uriBaseBuilder.build().toString() );

        return uriBaseBuilder.build();
    }


    protected static void SetupSettingsActionButton( final Context context, FloatingActionButton fab ) {

        fab.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = makeIntent( context, AppSettingsActivity.class );
                context.startActivity(intent);
            }

        });
    }


    protected static WebViewClient SetupWebViewClient( final Context context, final Object enumMap ) {

        WebViewClient webviewclient = new WebViewClient() {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading( WebView view, WebResourceRequest request ) {

                if ( super.shouldOverrideUrlLoading( view, request ) ) {
                    return true;
                }
                else {
                    if ( CheckOverrideUrlLoading( context, enumMap, request.getUrl() ) ) {
                        // Put Override Url Code here!!!
                        onOverrideUrlLoading( context );

                        return true;
                    }

                }

                return false;
            }


            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if ( super.shouldOverrideUrlLoading(view, url) ) {
                    return true;
                }
                else {
                    if ( CheckOverrideUrlLoading( context, enumMap, Uri.parse(url) ) ) {
                        // Put Override Url Code here!!!
                        onOverrideUrlLoading(context);

                        return true;
                    }

                }

                return false;

            }


            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {

                switch( getSecureConnectionPref( context ) ) {
                    case ALWAYS:
                        SetupConfirmDialog( context, handler ).create().show();
                        break;

                    default:
                        SetupWarningDialog( context, handler ).create().show();

                }

                super.onReceivedSslError(view, handler, error);
            }

/*
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

                String url_path = request.getUrl().getPath();
                Boolean path_exists = false;

                for ( Uri page_uri : AppContentUriMap.values() ) {
                    path_exists = path_exists || page_uri.getPath().equals( request.getUrl().getPath() );

                    Log.d( "AuthPagesUriMap: v= ", "'" + path_exists + "' = '" + page_uri.getPath() + "' | '" + request.getUrl().getPath() + "'" );
                }

                //TODO: Remove Debug entry.
                Log.d( "Start:Overide Func","<shouldOverrideUrlLoading>" );
                Log.d( "Url: Uri.toString()= ", request.getUrl().toString() );
                Log.d( "Url: Uri.getPath()= ", request.getUrl().getPath() );
                Log.d( "Url: getEncodedPath()= ", request.getUrl().getEncodedPath() );
                Log.d( "path_exists= ", path_exists.toString() );

                if ( !path_exists ) {
                    //TODO: Remove Debug entry.
                    Log.d( "Condition#1: ", "Does Not Exist!" );

                    //Intent launch_home = AppHomeActivity.makeIntent( context );
                    //makeIntent( launch_home );

                    //TODO: Remove Debug entry.
                    Log.d( "Exit:Overide Func","<shouldOverrideUrlLoading>" );
                    return true;
                }
                else {
                    //TODO: Remove Debug entry.
                    Log.d("Exit:Overide Func", "<shouldOverrideUrlLoading>");
                    return super.shouldOverrideUrlLoading(view, request);
                }

            }


            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                String url_path = Uri.parse(url).getPath();
                Boolean path_exists = false;
                Boolean path_exists2 = false;

                for ( Uri page_uri : AppContentUriMap.values() ) {
                    path_exists = path_exists || page_uri.getPath().equals( url_path.substring(0,url_path.length()-1) );
                    Log.d( "AuthPagesUriMap: v= ", "'" + path_exists + "' = '" + page_uri.getPath() + "' | '" + url_path.substring( 0, url_path.length()-1 ) + "'" );

                    path_exists2 = path_exists2 || page_uri.getPathSegments().equals( Uri.parse(url).getPathSegments() );
                    Log.d( "AuthPagesUriMap: v= ", "'" + path_exists2 + "'" );
                }

                //TODO: Remove Debug entry.
                Log.d( "Start:Overide Func","<shouldOverrideUrlLoading>(Depreciated)" );
                Log.d( "Url: Uri.toString()= ", "'" + Uri.parse(url).toString() + "'" );
                Log.d( "Url: Uri.getPath()= ", "'" + url_path + "'" );
                Log.d( "Url: getPathSegments= ", "'" + Uri.parse(url).getPathSegments().toString() + "'" );
                Log.d( "Url: getEncodedPath= ", "'" + Uri.parse(url).getEncodedPath().toString() + "'" );
                Log.d( "path_exists= ", path_exists.toString() );

                if ( !path_exists ) {
                    //TODO: Remove Debug entry.
                    Log.d( "Condition#1: ", "Does Not Exist!" );

                    //Intent launch_home = AppHomeActivity.makeIntent(getApplicationContext());
                    //startActivity(launch_home);

                    //TODO: Remove Debug entry.
                    Log.d( "Exit:Overide Func","<shouldOverrideUrlLoading>(Depreciated)" );
                    return true;
                    //return false;
                }
                else {
                    //TODO: Remove Debug entry.
                    Log.d("Exit:Overide Func", "<shouldOverrideUrlLoading>(Depreciated)");
                    return super.shouldOverrideUrlLoading(view, url);
                }

            }


            @Override
            public void onReceivedSslError( WebView view, SslErrorHandler handler, SslError error ) {

                switch( AppHomeActivity.getSecureConnectionPref( context ) ) {
                    case ALWAYS:
                        //ConnectionConfirmDialog( handler ).show();
                        break;

                    default:
                        //ConnectionWarningDialog( handler ).show();

                }

                super.onReceivedSslError(view, handler, error);
            }*/

        };

        return webviewclient;
    }


    private static boolean CheckOverrideUrlLoading(Context context, Object enumMap, Uri url ) {

        //TODO: Remove Debug entry.
        Log.d( "CheckOverrideUrlLoading","Started" );

        Method method_values = null;
        try {
            method_values = enumMap.getClass().getMethod("values", (Class<?>) null );

            Collection<Uri> enumMap_values = null;
            try {
                enumMap_values = (Collection<Uri>) method_values.invoke(enumMap, (Object) null );
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }


            for ( Uri whitelisted_uri : enumMap_values ) {

                if ( !whitelisted_uri.getPathSegments().equals( url.getPathSegments() ) ) {

                    Log.d( "whitelisted_uri: ", "getPathSegments() := " + whitelisted_uri.getPathSegments().toString() );
                    Log.d( "url:  ", "getPathSegments() := " + url.getPathSegments().toString() );
                    Log.d( "Uri.path= ", "Matched is found!" );

                    return true;
                }
                else {
                    //TODO: Remove Debug entry.
                    Log.d( "whitelisted_uri: ", "getPathSegments() := " + whitelisted_uri.getPathSegments().toString() );
                    Log.d( "url:  ", "getPathSegments() := " + url.getPathSegments().toString() );
                    Log.d( "Uri.path= ", "Could not be matched!" );
                }

            }

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return false;
    }


    private static void onOverrideUrlLoading(Context context) {
        //TODO: Remove Debug entry.
        Log.d( "Condition#1: ", "Url Override is required. " );

        //TODO: Define default Url Overide Action...
        //Intent launch = makeIntent( context,  );
        //context.startActivity( launch );

        //TODO: Remove Debug entry.
        Log.d( "Exit:Overide Func","<shouldOverrideUrlLoading>" );

    }



    protected static AlertDialog.Builder SetupConfirmDialog( final Context context, final Object param ) {

        AlertDialog.Builder confirm_builder = new AlertDialog.Builder( context );

        confirm_builder.setMessage( R.string.activity_core_secure_connection_confirm_message )

                .setTitle( R.string.activity_core_secure_connection_dialog_title )

                .setCancelable(false)

                .setPositiveButton(R.string.activity_core_secure_connection_button_retry, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked 'Retry' button
                        onRetryConfirmAction( param );
                        dialog.dismiss();
                    }
                })

                .setNegativeButton(R.string.activity_core_secure_connection_button_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked 'Cancel' button
                        onCancelConfirmAction( param );
                        dialog.cancel();
                    }
                })
        ;

        return confirm_builder;
    }



    protected static AlertDialog.Builder SetupWarningDialog( final Context context, final Object param ) {

        AlertDialog.Builder warning_builder = new AlertDialog.Builder( context );

        warning_builder.setMessage( R.string.activity_core_secure_connection_warning_message )

                .setTitle( R.string.activity_core_secure_connection_dialog_title )

                .setCancelable(false)

                .setPositiveButton( R.string.activity_core_secure_connection_button_continue, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked 'Continue' button
                        onContinueWarningAction( param );
                        dialog.dismiss();
                    }
                })

                .setNegativeButton( R.string.activity_core_secure_connection_button_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked 'Cancel' button
                        onCancelWarningAction( param );
                        dialog.cancel();
                    }
                })
        ;

        return warning_builder;
    }


    protected static Object onContinueWarningAction( Object argument ) {

        ( (SslErrorHandler) argument).proceed();

        return null;
    }


    protected static Object onCancelWarningAction( Object argument ) {

        ( (SslErrorHandler) argument).cancel();

        return null;
    }


    protected static Object onRetryConfirmAction( Object argument ) {

        ( (SslErrorHandler) argument).proceed();

        return null;
    }


    protected static Object onCancelConfirmAction( Object argument ) {

        ( (SslErrorHandler) argument).cancel();

        return null;
    }



    protected void LoadPageContent( Context context, WebView webview, Object enumMap ) {

        webview.setWebViewClient( SetupWebViewClient( context, enumMap ) );

        Method method_values = null;
        Collection<Uri> pages = null;
        try {
            method_values = enumMap.getClass().getMethod("values", (Class<?>) null );

            try {
                pages = (Collection<Uri>) method_values.invoke( enumMap,(Object) null );
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        for ( Uri page : pages ) {
            //TODO: Remove Debug entry.
            Log.d("LoadAuthPageContent: ", "page= " + page.toString());

            webview.loadUrl( page.toString() );
        }
    }


}
