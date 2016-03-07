package com.myth.loginservice.google;


import android.app.Activity;
import android.content.IntentSender;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.myth.loginservice.SocialRequestHandler;
import com.myth.loginservice.pojo.UserObject;

/**
 * Created by mitesh on 06/08/15.
 */
class GoogleMeCallback implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private final static int PLAY_SERVICES_RETRY_CODE = 111;
    private static final int PROFILE_PIC_SIZE = 400;
    GoogleApiClient myClient;
    SocialRequestHandler requestHandler;
    Activity activity;

    public GoogleMeCallback(Activity activity, SocialRequestHandler requestHandler) {
        this.requestHandler = requestHandler;
        this.activity = activity;
    }

    public void setGoogleClient(GoogleApiClient myClient) {
        this.myClient = myClient;
    }

    public void onConnected(Bundle connectionHint) {
        Person currentPerson = Plus.PeopleApi.getCurrentPerson(myClient);
        if (myClient.isConnected() && currentPerson != null) {
            String userName = currentPerson.getDisplayName();
            String userGooglePlusProfile = currentPerson.getUrl();
            String email = Plus.AccountApi.getAccountName(myClient);
            String gender = getGender(currentPerson.getGender());
            String personPhotoUrl = currentPerson.getImage().getUrl();
            personPhotoUrl = personPhotoUrl.substring(0,
                    personPhotoUrl.length() - 2)
                    + PROFILE_PIC_SIZE;

            UserObject userObject = new UserObject();
            userObject.userName = userName;
            userObject.profileLink = userGooglePlusProfile;
            userObject.profilePic = personPhotoUrl;
            userObject.email = email;
            userObject.type = "Google";
            userObject.gender = gender;
            userObject.password = "";

            this.requestHandler.onComplete(userObject);
        }
    }

    private String getGender(int gender) {
        if (gender == 0) {
            return "male";
        } else if (gender == 1) {
            return "female";
        } else {
            return "other";
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (result.hasResolution()) {
            this.requestHandler.onError();
            try {
                result.startResolutionForResult(this.activity, PLAY_SERVICES_RETRY_CODE);
            } catch (IntentSender.SendIntentException e) {
            }
        }
    }
}
