package com.myth.loginservice.facebook;

import android.util.Log;

import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.myth.loginservice.SocialRequestHandler;
import com.myth.loginservice.pojo.UserObject;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mitesh on 05/08/15.
 */
class FacebookMeCallback implements GraphRequest.GraphJSONObjectCallback {

    SocialRequestHandler requestHandler;

    public FacebookMeCallback(SocialRequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    @Override
    public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
        if(graphResponse.getError() != null){
            this.requestHandler.onError();
            return;
        }
        JSONObject jsonObjectFinal = graphResponse.getJSONObject();
        String responseString = null;
        try {
            responseString = jsonObjectFinal.toString(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.v("LoginActivity", graphResponse.toString());
        JSONObject responseObj = null;
        String name = "";
        String email = "";
        String gender = "";
        String profileLink = "";
        String profilePic = "";
        try {
            responseObj = new JSONObject(responseString);
            name = responseObj.getString("name");
            email = responseObj.getString("email");
            gender = responseObj.getString("gender");
            profileLink = responseObj.getString("id");
            JSONObject pictureJson = responseObj.getJSONObject("picture");
            if (pictureJson != null) {
                JSONObject pictureDataJson = pictureJson.getJSONObject("data");
                if (pictureDataJson != null) {
                    profilePic = pictureDataJson.getString("url");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        UserObject userObject = new UserObject();
        if (responseObj != null) {
            userObject.userName = name;
            userObject.profileLink = profileLink;
            userObject.profilePic = profilePic;
            userObject.email = email;
            userObject.gender = gender;
            userObject.type = "Facebook";
            userObject.password = "";
        }
        this.requestHandler.onComplete(userObject);
    }
}
