package com.example.map1configurationgooglemapsapis;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class MainActivity extends AppCompatActivity {
    boolean autorisation_accordee;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();
        if(autorisation_accordee){
            if(verefieGooglePlayServices()){
                Toast.makeText(this, "services googleplay est disponible", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "services googleplay n'est pas disponible", Toast.LENGTH_SHORT).show();

            }
        }
    }

    private boolean verefieGooglePlayServices() {
        GoogleApiAvailability googleApiAvailability=GoogleApiAvailability.getInstance();
        int result=googleApiAvailability.isGooglePlayServicesAvailable(this);
        if(result== ConnectionResult.SUCCESS){
          return true;
        } else if (googleApiAvailability.isUserResolvableError(result)) {
            Dialog dialog=googleApiAvailability.getErrorDialog(this, result, 201, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    Toast.makeText(MainActivity.this, "service annulé par l'utilisateur", Toast.LENGTH_SHORT).show();

                }
            });
            dialog.show();
        }
        return false;
    }

    private void checkPermission() {
        Dexter.withContext(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                autorisation_accordee=true;
                Toast.makeText(MainActivity.this, "permission accordée", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
             Intent intent= new Intent();
             intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
             Uri uri=Uri.fromParts("package",getPackageName(),"");
             intent.setData(uri);
             startActivity(intent);
             // nrslo l'utilisateur l setting
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }


}