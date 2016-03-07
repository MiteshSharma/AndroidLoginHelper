package com.myth.loginservice.facebook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.myth.loginservice.listeners.ISocialLoginListener;
import com.myth.loginservice.SocialRequestHandler;
import com.myth.loginservice.pojo.UserObject;

import java.util.Arrays;

/**
 * Created by mitesh on 05/08/15.
 */
public class FacebookLoginHelper extends SocialRequestHandler implements FacebookCallback<LoginResult> {

    CallbackManager callbackManager;
    ISocialLoginListener listener;
    Activity context;

    public void onCreate(Activity context, ISocialLoginListener listener) {
        FacebookSdk.sdkInitialize(context);
        this.listener = listener;
        this.context = context;
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, this);
    }

    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void connect() {
        LoginManager.getInstance().logInWithReadPermissions((Activity)this.context, Arrays.asList("public_profile", "email", "user_friends"));
    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        // App code
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new FacebookMeCallback(this));
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, name, email, gender, picture");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    public void onComplete(UserObject userObject) {
        this.listener.onSocialLoginSuccess(userObject);
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onError(FacebookException e) {

    }
}
