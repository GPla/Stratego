package com.mo.stratego;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.support.multidex.MultiDex;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.mo.stratego.bluetooth.BluetoothHandler;

import java.util.List;

public class AndroidLauncher extends AndroidApplication {
    private BluetoothHandler comHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        //disable not needed features to save battery
        config.useCompass = false;
        config.useAccelerometer = false;

        comHandler = new BluetoothHandler(this);
        requestPermissions();

        StrategoGame.INSTANCE.init(comHandler);

        // run game
        initialize(StrategoGame.INSTANCE, config);
    }

    /**
     * Request the permissions necessary for Bluetooth communication.
     */
    private void requestPermissions() {
        Dexter.withActivity(this).withPermissions(
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.ACCESS_FINE_LOCATION)
              .withListener(new MultiplePermissionsListener() {
                  @Override
                  public void onPermissionsChecked(MultiplePermissionsReport report) {
                      if (!report.areAllPermissionsGranted()) {
                          requestPermissions();
                      }
                  }

                  @Override
                  public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                      token.continuePermissionRequest();
                  }
              }).check();
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        // needed because of apk size
        MultiDex.install(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // close listener -> connection can only be established if both
        // devices have the app open
        comHandler.closeListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
