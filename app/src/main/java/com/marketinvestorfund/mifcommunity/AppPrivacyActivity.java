package com.marketinvestorfund.mifcommunity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.CheckBox;

public class AppPrivacyActivity extends AppCoreActivity {

    private WebView privacyWebView;
    private CheckBox privacyCheckbox;
    //private AppCoreActivity appCore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_privacy);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //toolbar.setVisibility();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        privacyWebView = (WebView) findViewById(R.id.privacy_webview);
        //setContentView(privacyWebView);

        privacyWebView.getSettings().setJavaScriptEnabled(false);

        //privacyWebView.setWebViewClient();

        //appCore = AppCoreActivity.getAppCoreConnectivity();
        //AppCoreActivity.


        privacyWebView.loadUrl(  BuildPrivacyPolicyUri().toString() );

        privacyCheckbox = (CheckBox) findViewById(R.id.privacy_checkbox );
        SetupPrivacyCheckBox(privacyCheckbox);

    }

    @Override
    public void onBackPressed() {

        SetupPrivicyConfirmDialog( privacyCheckbox.isChecked() );
    }


    private Uri BuildPrivacyPolicyUri() {

        Uri uri = BuildContentUri( BuildBaseUri( getApplicationContext(), getString( R.string.app_privacy_policy_uri_authority) )
                , getString(R.string.app_privacy_policy_uri_path) );

        //TODO: Remove Debug entry.
        Log.d( "BuildPrivacyPolicyUri: ", uri.toString() );

         return uri;
    }


    private void SetupPrivacyCheckBox( final CheckBox privacy ) {

        privacy.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                SetupPrivicyConfirmDialog( privacy.isChecked() );

            }

        });

    }


    private AlertDialog SetupPrivicyConfirmDialog( Boolean isSigned ) {

        AlertDialog.Builder builder = new AlertDialog.Builder( getApplicationContext() );

        if ( isSigned ) {
            builder.setMessage( R.string.activity_privacy_policy_confirm_message );
        }
        else {
            builder.setMessage( R.string.activity_privacy_policy_warning_message )
                    .setPositiveButton(R.string.activity_privacy_policy_button_accept, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked 'Accept' button
                            onConfirmAction();
                            dialog.dismiss();
                        }

                    }
            );
        }

        builder.setTitle( R.string.activity_privacy_policy_dialog_title )

                .setCancelable(true)

                .setNegativeButton(R.string.activity_privacy_policy_button_reject, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked 'Reject' button
                        onRejectAction();
                        dialog.dismiss();
                    }
                })
        ;

        return builder.create();
    }

    private void onConfirmAction() {
        //setRequestedOrientation( APP_REQUEST_CODE.PRIVACY_POLICY_EULA.ordinal() );
        setResult( RESULT_OK );
        finish();
    }

    private void onRejectAction() {

        setResult( RESULT_CANCELED);
        finish();
    }


}
