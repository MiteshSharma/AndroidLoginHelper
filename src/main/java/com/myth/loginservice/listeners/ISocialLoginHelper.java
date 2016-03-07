package com.myth.loginservice.listeners;

import android.app.Activity;
import android.content.Intent;

import com.myth.loginservice.SocialLoginHandler;

/**
 * Created by mitesh on 07/08/15.
 */
public interface ISocialLoginHelper {

    public void onCreate(Activity activity, ISocialLoginListener listener);
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data);
    public void onLogin(SocialLoginHandler.SocialLogin socialLogin);
}
