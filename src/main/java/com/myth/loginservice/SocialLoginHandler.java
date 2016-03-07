package com.myth.loginservice;

import android.app.Activity;
import android.content.Intent;

import com.myth.loginservice.facebook.FacebookLoginHelper;
import com.myth.loginservice.google.GoogleLoginHelper;
import com.myth.loginservice.listeners.ISocialLoginHelper;
import com.myth.loginservice.listeners.ISocialLoginListener;

/**
 * Created by mitesh on 06/08/15.
 */
public class SocialLoginHandler implements ISocialLoginHelper {

    private FacebookLoginHelper fbHelper;
    private GoogleLoginHelper glHelper;

    public enum SocialLogin {
        FACEBOOK,
        GOOGLE;
    }

    public SocialLoginHandler() {
        fbHelper = new FacebookLoginHelper();
        glHelper = new GoogleLoginHelper();
    }

    public void onCreate(Activity activity, ISocialLoginListener listener) {
        fbHelper.onCreate(activity, listener);
        glHelper.onCreate(activity, listener);
    }

    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        fbHelper.onActivityResult(requestCode, resultCode, data);
        glHelper.onActivityResult(requestCode, resultCode, data);
    }

    public void onLogin(SocialLogin socialLogin) {
        if (SocialLogin.GOOGLE.equals(socialLogin)) {
            if (glHelper != null) {
                glHelper.connect();
            }
        } else if (SocialLogin.FACEBOOK.equals(socialLogin)) {
            if (fbHelper != null) {
                fbHelper.connect();
            }
        }
    }
}
