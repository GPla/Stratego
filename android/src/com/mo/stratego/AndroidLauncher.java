package com.mo.stratego;

import android.Manifest;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.mo.stratego.bluetooth.BluetoothHandler;
import com.mo.stratego.model.communication.ICommunication;

import java.util.List;

public class AndroidLauncher extends AndroidApplication {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        //disable not needed features to save battery
        config.useCompass = false;
        config.useAccelerometer = false;

        ICommunication handler = new BluetoothHandler(getApplicationContext());
        requestPermissions();

        // run game
        initialize(new StrategoGame(handler), config);
    }

    //TODO cleanup
    private void requestPermissions() {
        Dexter.withActivity(this).withPermissions(
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.ACCESS_FINE_LOCATION)
              .withListener(new MultiplePermissionsListener() {
                  @Override
                  public void onPermissionsChecked(MultiplePermissionsReport report) {
                      if (report.areAllPermissionsGranted()) {

                      } else {
                          requestPermissions();
                      }
                  }

                  @Override
                  public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                      token.continuePermissionRequest();
                  }
              }).check();
    }


}
