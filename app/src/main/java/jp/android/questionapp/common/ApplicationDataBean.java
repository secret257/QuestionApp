package jp.android.questionapp.common;

import android.app.Application;

public class ApplicationDataBean extends Application {

    private String userId;

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}
