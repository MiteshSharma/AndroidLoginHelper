package com.myth.loginservice.google;

import android.app.Activity;
import android.content.Intent;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.myth.loginservice.listeners.ISocialLoginListener;
import com.myth.loginservice.SocialRequestHandler;
import com.myth.loginservice.pojo.UserObject;

/**
 * Created by mitesh on 06/08/15.
 */
public class GoogleLoginHelper extends SocialRequestHandler {

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private final static int PLAY_SERVICES_RETRY_CODE = 111;
    private GoogleApiClient myClient;
    private Activity context;
    private ISocialLoginListener listener;
    private GoogleMeCallback callback;

    public void onCreate(Activity activity, ISocialLoginListener listener) {
        this.listener = listener;
        this.context = activity;
        this.callback = new GoogleMeCallback(activity, this);
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, (Activity)context,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                this.context.finish();
            }
            return false;
        }
        return true;
    }

    public void onStart() {
        if (myClient != null)
            myClient.connect();
    }

    public void onStop() {
        if (myClient!= null && myClient.isConnected()) {
            myClient.disconnect();
        }
    }

    public boolean isConnected() {
        myClient = new GoogleApiClient.Builder(this.context).addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_PROFILE)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .addConnectionCallbacks(this.callback)
                .addOnConnectionFailedListener(this.callback)
                .build();
        if (myClient!= null && myClient.isConnected()) {
            return true;
        }
        return false;
    }

    public void connect() {
        if (checkPlayServices()) {
            myClient = new GoogleApiClient.Builder(this.context).addApi(Plus.API)
                    .addScope(Plus.SCOPE_PLUS_PROFILE)
                    .addScope(Plus.SCOPE_PLUS_LOGIN)
                    .addConnectionCallbacks(this.callback)
                    .addOnConnectionFailedListener(this.callback)
                    .build();
            this.callback.setGoogleClient(myClient);
            myClient.connect();
        }
    }

    public void disconnect() {
        if (myClient!= null && myClient.isConnected()) {
            myClient.disconnect();
        }
    }

    @Override
    public void onComplete(UserObject userObject) {
        this.listener.onSocialLoginSuccess(userObject);
    }

    @Override
    public void onError() {

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLAY_SERVICES_RETRY_CODE && resultCode == Activity.RESULT_OK) {
            myClient.connect();
        } else if (requestCode == PLAY_SERVICES_RETRY_CODE && resultCode != Activity.RESULT_OK) {
            this.listener.onSocialLoginError();
        }
    }
}