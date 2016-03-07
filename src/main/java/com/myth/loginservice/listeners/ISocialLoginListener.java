package com.myth.loginservice.listeners;

import com.myth.loginservice.pojo.UserObject;

/**
 * Created by mitesh on 05/08/15.
 */
public interface ISocialLoginListener {
    public void onSocialLoginSuccess(UserObject userObject);
    public void onSocialLoginError();

}
