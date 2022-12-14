package com.sushi.fileUploader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.sushi.fileUploader.activities.FloorDetailActivity;
import com.sushi.fileUploader.activities.MapActivity;
import com.sushi.fileUploader.common.Utility;
import com.sushi.fileUploader.utility.AppConstants;

public class Home extends AppCompatActivity implements View.OnClickListener {

   private LinearLayout lytImage,lytLocation,lytFloor;
   private double latitude = 0.0, longitude = 0.0;
   private EditText edtUid;
   private ImageView imgLocArrow;
   private RelativeLayout rootLayout;
   private Spinner ulbSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        lytImage=findViewById(R.id.lyt_img);
        lytLocation=findViewById(R.id.lyt_location);
        lytFloor=findViewById(R.id.lyt_floor);
        imgLocArrow=findViewById(R.id.imgLocationArrow);
        ulbSpinner=findViewById(R.id.ulb_drop_spinner);
        edtUid=findViewById(R.id.edtHomeUID);
        rootLayout=findViewById(R.id.homeRootLayout);

        lytImage.setOnClickListener(this);
        lytLocation.setOnClickListener(this);
        lytFloor.setOnClickListener(this);
        imgLocArrow.setOnClickListener(this);


//        ulbSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                String ulbSpin=ulbSpinner.getSelectedItem().toString();
//                Intent intent=new Intent(getApplicationContext(),MapActivity.class);
//                intent.putExtra(AppConstants.LATITUDE, latitude);
//                intent.putExtra(AppConstants.LONGITUDE, longitude);
//                intent.putExtra("ulbData",ulbSpin);
//                startActivity(intent);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
    }

    @Override
    public void onClick(View view) {
     switch (view.getId()){
         case R.id.lyt_img:
             startActivity(new Intent(getApplicationContext(), MainActivity.class));
             break;
         case R.id.imgLocationArrow:
             Utility.snackBar(rootLayout,"Under development",1000,Color.parseColor("#ff0000"));
//              if(edtUid.getText().toString().equals("")){
//                  Utility.snackBar(rootLayout, "Please Enter UID first", 1000, Color.parseColor("#ff0000"));
//              }
//              else if (edtUid.getText().toString().length()<= 6){
//                  Utility.snackBar(rootLayout, "Please Enter UID first", 1000, Color.parseColor("#ff0000"));
//              }else if (ulbSpinner.getSelectedItemPosition()==0){
//                  Utility.snackBar(rootLayout, "Please Select ULB Code First", 1000, Color.parseColor("#ff0000"));
//              }
//              else{
//                  String ulbSpin=ulbSpinner.getSelectedItem().toString();
//                  String uid=edtUid.getText().toString().trim();
//                  Intent intent=new Intent(getApplicationContext(),MapActivity.class);
//                  intent.putExtra(AppConstants.LATITUDE, latitude);
//                  intent.putExtra(AppConstants.LONGITUDE, longitude);
//                  intent.putExtra("ulbData",ulbSpin);
//                  intent.putExtra("uidCode",uid);
//                  startActivity(intent);
//              }
//             startActivity(new Intent(getApplicationContext(), MapActivity.class));
             break;
         case R.id.lyt_floor:
             Utility.snackBar(rootLayout,"Under development",1000,Color.parseColor("#ff0000"));

//             startActivity(new Intent(getApplicationContext(), FloorDetailActivity.class));
             break;
     }

    }
}