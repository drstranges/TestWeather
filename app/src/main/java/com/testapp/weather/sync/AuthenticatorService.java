package com.testapp.weather.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * AuthenticatorService Stub
 * Created on 24.12.2015.
 */
public class AuthenticatorService extends Service {

    private Authenticator mAuthenticator;

    @Override
    public void onCreate() {
        mAuthenticator = new Authenticator(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
