package com.sushi.fileUploader.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.sushi.fileUploader.MainActivity;
import com.sushi.fileUploader.R;
import com.sushi.fileUploader.SharedPreference;
import com.sushi.fileUploader.common.Utility;
import com.sushi.fileUploader.utility.AppConstants;
import com.sushi.fileUploader.utility.TinyDB;
import com.sushi.fileUploader.utility.Util;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout lytImage, lytLocation, lytFloor;
    private double latitude = 0.0, longitude = 0.0;
    private EditText edtUid;
    private ImageView imgLocArrow;
    private RelativeLayout rootLayout;
    private Button btnLogout;
    private Spinner ulbSpinner;
    SharedPreference sharedPreference;
    TinyDB tinyDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.hideStatusBar(this);
        setContentView(R.layout.activity_home);
        sharedPreference = SharedPreference.getInstance(this);
        tinyDB = new TinyDB(this);
        //method call for setting toolbar

        lytImage = findViewById(R.id.lyt_img);
        lytLocation = findViewById(R.id.lyt_location);
        lytFloor = findViewById(R.id.lyt_floor);
        imgLocArrow = findViewById(R.id.imgLocationArrow);
        ulbSpinner = findViewById(R.id.ulb_drop_spinner);
        edtUid = findViewById(R.id.edtHomeUID);
        rootLayout = findViewById(R.id.homeRootLayout);
        btnLogout=findViewById(R.id.btnLogOut);


        lytImage.setOnClickListener(this);
        lytLocation.setOnClickListener(this);
        lytFloor.setOnClickListener(this);
        imgLocArrow.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lyt_img:
                startActivity(new Intent(getApplicationContext(), ImageUpdateActivity.class));
                break;
            case R.id.imgLocationArrow:
                Utility.snackBar(rootLayout,"Under development",1000,Color.parseColor("#ff0000"));
//                if (edtUid.getText().toString().equals("")) {
//                    Utility.snackBar(rootLayout, "Please Enter UID first", 1000, Color.parseColor("#ff0000"));
//                } else if (edtUid.getText().toString().length() <= 6) {
//                    Utility.snackBar(rootLayout, "Please Enter UID first", 1000, Color.parseColor("#ff0000"));
//                } else if (ulbSpinner.getSelectedItemPosition() == 0) {
//                    Utility.snackBar(rootLayout, "Please Select ULB Code First", 1000, Color.parseColor("#ff0000"));
//                } else {
//                    String ulbSpin = ulbSpinner.getSelectedItem().toString();
//                    String uid = edtUid.getText().toString().trim();
//                    Intent intent = new Intent(getApplicationContext(), MapActivity.class);
//                    intent.putExtra(AppConstants.LATITUDE, latitude);
//                    intent.putExtra(AppConstants.LONGITUDE, longitude);
//                    intent.putExtra("ulbData", ulbSpin);
//                    intent.putExtra("uidCode", uid);
//                    startActivity(intent);
//                }
//             startActivity(new Intent(getApplicationContext(), MapActivity.class));
                break;
            case R.id.lyt_floor:
//                startActivity(new Intent(getApplicationContext(), FloorDetailActivity.class));
                Utility.snackBar(rootLayout,"Under development",1000,Color.parseColor("#ff0000"));
                break;
            case R.id.btnLogOut:
                sharedPreference.deleteAllPreference();
                Util.clearLoginPref(this, AppConstants.USER_ID, AppConstants.PASSWORD);
                Intent i = new Intent(this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        edtUid.setText("");
        ulbSpinner.setSelection(0);
    }
}