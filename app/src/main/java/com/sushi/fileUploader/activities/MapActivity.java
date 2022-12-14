package com.sushi.fileUploader.activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;
import com.sushi.fileUploader.R;
import com.sushi.fileUploader.utility.AppConstants;
import com.sushi.fileUploader.utility.Util;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnCameraIdleListener {
    private static final int MY_PERMISSIONS_REQUEST_CODE = 123;
    private GoogleMap map;
    public static double latitude = 0.0, longitude = 0.0;
    private LatLng KS = new LatLng(29.94, 78.16);
    private String ulbData,uidCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.hideStatusBar(this);
        setContentView(R.layout.map_layout);

        // set toolbar
        setToolbar();
        permissionCheck();
        // method for initializing views
        initializeViews();

        latitude = getIntent().getDoubleExtra(AppConstants.LATITUDE, 0.0);
        longitude = getIntent().getDoubleExtra(AppConstants.LONGITUDE, 0.0);
        ulbData=getIntent().getExtras().getString("ulbData");
        uidCode=getIntent().getExtras().getString("uidCode");
//        ulbData=getIntent().getExtras().getInt("ulbData");
        Toast.makeText(this, "UlB Data is "+ulbData, Toast.LENGTH_SHORT).show();


    }

    /*method for initializing views*/
    private void initializeViews() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.frg);  //use SuppoprtMapFragment
        mapFragment.getMapAsync(this);

        MaterialButton selectLocBtn = findViewById(R.id.select_location);
        selectLocBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // showGeofenceAlert();
                ViewDialog alert = new ViewDialog();
                alert.showDialog(MapActivity.this);
//                finish();

            }
        });


        final MaterialButton mapTogglebtn = findViewById(R.id.change_map_btn);
        mapTogglebtn.setOnClickListener(v -> {
            if (mapTogglebtn.getText().toString().equals("Satellite map")) {
                mapTogglebtn.setText("Normal map");
                map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            } else {
                mapTogglebtn.setText("Satellite map");
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }
        });

    }

    /*method for setting toolbar*/
    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // hiding default title text on toolbar
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(v -> finish());

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        map.setMyLocationEnabled(true);
        map.setOnCameraIdleListener(this);
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                latitude=latLng.latitude;
                longitude=latLng.longitude;
                Toast.makeText(MapActivity.this, "You Click On "+latLng, Toast.LENGTH_LONG).show();
            }
        });
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 18));


        //=============================
        Util.getUAV(this, map);

    }

    @Override
    public void onCameraIdle() {

        latitude = map.getCameraPosition().target.latitude;
        longitude = map.getCameraPosition().target.longitude;
    }
    private void permissionCheck() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                + ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                + ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                + ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                + ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Files, Photos and Location" + " Storage permissions are required to do the task.");
                builder.setTitle("Please grant those permissions");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(MapActivity.this, new String[]
                                        {Manifest.permission.CAMERA,
                                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                                Manifest.permission.ACCESS_FINE_LOCATION},
                                MY_PERMISSIONS_REQUEST_CODE);
                    }
                });
                builder.setNeutralButton("Cancel", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                // Directly request for required permissions, without explanation
                ActivityCompat.requestPermissions(this, new String[]
                                {Manifest.permission.CAMERA,
                                        Manifest.permission.ACCESS_COARSE_LOCATION,
                                        Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_CODE);
            }
        } else {
            //  Toast.makeText(this,"Permissions already granted",Toast.LENGTH_SHORT).show();
        }
    }

    /*method for showing geo-fencing alert*/
    public class ViewDialog {
        public void showDialog(Activity activity) {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.alert_layout);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            FrameLayout mDialogYes = dialog.findViewById(R.id.yes_btn);
            mDialogYes.setOnClickListener(v -> {
                Toast.makeText(activity, "Your Lat long is "+latitude + longitude, Toast.LENGTH_SHORT).show();
                dialog.dismiss();

            });

            FrameLayout mDialogNo = dialog.findViewById(R.id.no_btn);
            mDialogNo.setOnClickListener(v -> {
                dialog.cancel();


            });

            dialog.show();
        }
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        try {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                View v = getCurrentFocus();
                if (v instanceof EditText) {
                    Rect outRect = new Rect();
                    v.getGlobalVisibleRect(outRect);
                    if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                        v.clearFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (imm != null)
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                }
            }
            return super.dispatchTouchEvent(event);
        } catch (Exception e) {
            return false;
        }
    }

}