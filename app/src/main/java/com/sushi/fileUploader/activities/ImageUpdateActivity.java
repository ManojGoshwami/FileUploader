package com.sushi.fileUploader.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.textfield.TextInputEditText;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.sushi.fileUploader.CameraActivity;
import com.sushi.fileUploader.MyApplication;
import com.sushi.fileUploader.MyExceptionHandler;
import com.sushi.fileUploader.R;
import com.sushi.fileUploader.common.Utility;
import com.sushi.fileUploader.dbHelper.ImageFile_Helper;
import com.sushi.fileUploader.fragments.CameraFragment;
import com.sushi.fileUploader.holder.ImageFileAdapter;
import com.sushi.fileUploader.listener.ImageCallbackListener;
import com.sushi.fileUploader.models.ImageApiResponse;
import com.sushi.fileUploader.models.ImageFileModel;
import com.sushi.fileUploader.network.ApiClient;
import com.sushi.fileUploader.request.ApiInterface;
import com.sushi.fileUploader.utility.Util;
import com.sushi.fileUploader.viewModel.LocationViewModel;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageUpdateActivity extends AppCompatActivity implements ImageCallbackListener, View.OnClickListener {
    private ImageButton btnClick, btnUpload;
    private Button btnNext;
    private double latitude = 0.0, longitude = 0.0;
    //    private RecyclerView imgRecycler;
    private ImageView propImg, btnRemoveImg;
    private LocationViewModel locationViewModel;
    private TextInputEditText edtUID;
    private FrameLayout layoutImg;
    private RelativeLayout rootLayout;
    private ImageFileModel imageFileModel = new ImageFileModel();
    private ImageFileAdapter imageFileAdapter;
    private ArrayList<ImageFileModel> fileInfos = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private String timeStamp;
    private String imgPath = "";
    File list_file;
    private Dialog dialog = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.hideStatusBar(this);
        setContentView(R.layout.activity_image_update);
        //method call for setting toolbar
        setToolbar();
        rootLayout = (RelativeLayout) findViewById(R.id.rootLayout);
//        imgRecycler = (RecyclerView) findViewById(R.id.imgRecycler);
        propImg = (ImageView) findViewById(R.id.propImg);
        layoutImg = (FrameLayout) findViewById(R.id.layoutImg);
        btnRemoveImg = (ImageView) findViewById(R.id.btnRemoveImg);
        btnClick = (ImageButton) findViewById(R.id.btnClick);
        btnUpload = (ImageButton) findViewById(R.id.btnUpload);
        edtUID = findViewById(R.id.edtUID);


        locationViewModel = ViewModelProviders.of(this).get(LocationViewModel.class);
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));

        timeStamp = new SimpleDateFormat("HHmmss").format(new Date());


        //Location coardinates Fetch

        getCoordinates();



        CameraFragment.Companion.getImageCallback(this);
        btnClick.setOnClickListener(this);
        btnUpload.setOnClickListener(this);
        btnRemoveImg.setOnClickListener(this);


    }

    @Override
    protected void onResume() {
        // recyclerData();
        super.onResume();
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnClick:
                if (!edtUID.getText().toString().equals("") && !(edtUID.getText().length() < 6))
                    checkCameraPermission();
                else
                    Utility.snackBar(rootLayout, "Please Enter UID first", 1000, Color.parseColor("#ff0000"));
                break;
            case R.id.btnUpload:
                if (Utility.isOnline(this))
                    uploadImageRecord();
                else
                    Utility.snackBar(rootLayout, "Please cfheck Your Internet Connection", 1000, Color.parseColor("#ff0000"));
                break;
            case R.id.btnRemoveImg:
                btnClick.setEnabled(true);
                layoutImg.setVisibility(View.GONE);
                ImageFile_Helper.getInstance().deleteAll(ImageUpdateActivity.this);
                break;
        }
    }

    private void checkCameraPermission() {
        Dexter.withActivity(ImageUpdateActivity.this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).withListener(new MultiplePermissionsListener() {

                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            Intent intent = new Intent(ImageUpdateActivity.this, CameraActivity.class);
                            intent.putExtra("pageAction", true);
                            startActivity(intent);
                        }
                        if (report.isAnyPermissionPermanentlyDenied()) {
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    @Override
    public void imageCallback(File file) {
        try {
            Utility.playBeep(ImageUpdateActivity.this, null);
            if (file.exists()) {

                imgPath = file.getAbsolutePath();
                try {

                    Compressor compressor = new Compressor(ImageUpdateActivity.this);
                    compressor.setMaxWidth(800);
                    compressor.setMaxHeight(500);
                    compressor.setQuality(40);
                    compressor.setDestinationDirectoryPath(Utility.mkFileDir().getPath());
                    compressor.setCompressFormat(Bitmap.CompressFormat.JPEG);
                    File compressedImageFile = compressor.compressToFile(file);

                    savePropertyImage(compressedImageFile.getAbsolutePath());

                } catch (IOException e) {
                    e.printStackTrace();
                }

//                new ImageAsync(file, imageFileModel,edtUID.getText().toString(), imgPath, new PhotoCompressedListener() {
//                    @Override
//                    public void compressedPhoto(String path) {
//                        Log.e("Main Activity", "path : " + path);
//                        recyclerData();
//                    }
//                }).execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void savePropertyImage(String propImgPath) {
        if (!propImgPath.equals("")) {

            String imgPath = edtUID.getText().toString() + "_prop_" + timeStamp + ".jpg";
            File file = new File(propImgPath);
            String path = Utility.renameFile(file.getName(), imgPath);

            ImageFileModel imageFileModel = new ImageFileModel();
            imageFileModel.setUNIQUE_ID(edtUID.getText().toString());
            imageFileModel.setIMAGE_NAME(path.substring(path.lastIndexOf("/") + 1));
            imageFileModel.setIMAGE_NAME_PATH(path);

            ImageFile_Helper.saveImageFile(imageFileModel, MyApplication.getInstance());
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    layoutImg.setVisibility(View.VISIBLE);
                    Bitmap bmp = BitmapFactory.decodeFile(path);
                    propImg.setImageBitmap(bmp);
                    btnClick.setEnabled(false);
                    // Stuff that updates the UI

                }
            });

            // recyclerData();
        }

    }

   /* private void recyclerData() {
        fileInfos = ImageFile_Helper.getInstance().getAllImage(getApplicationContext());
        imageFileAdapter = new ImageFileAdapter(fileInfos, MainActivity.this);
        imgRecycler.setAdapter(imageFileAdapter);
        imageFileAdapter.notifyDataSetChanged();
    }*/

    //==============================Upload To Server================================

    private boolean uploadImageRecord() {
        boolean updateRecord = false;
        if (ContextCompat.checkSelfPermission(ImageUpdateActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            callUploadApi();
        }

        return updateRecord;
    }

    private void callUploadApi() {

        ArrayList<Integer> arrayListIds = ImageFile_Helper.getInstance().getAll(ImageUpdateActivity.this);
        if (arrayListIds.size() > 0) {
            dialog = Utility.startProgress(this);
            for (int i = 0; i < arrayListIds.size(); i++) {
                try {
                    list_file = ImageFile_Helper.getInstance().getFileById(arrayListIds.get(i), ImageUpdateActivity.this);
                    RequestBody requestFile =
                            RequestBody.create(MediaType.parse("multipart/form-data"), list_file);

                    // MultipartBody.Part is used to send also the actual file name
                    MultipartBody.Part body =
                            MultipartBody.Part.createFormData("image", list_file.getName(), requestFile);

                    //-------------------------------- Retrofit Call------------------------------------------//

                    ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

                    Call<ImageApiResponse> call = apiInterface.uploadImageToServer(body);

                    int finalI = i;
                    call.enqueue(new Callback<ImageApiResponse>() {
                        @Override
                        public void onResponse(Call<ImageApiResponse> call, Response<ImageApiResponse> response) {
                            Utility.stopProgress(dialog);
                            if (response != null && response.body() != null) {
                                Log.e("Response", "" + response);
                                Utility.snackBar(rootLayout, "Image upload successfully", 1000, Color.parseColor("#357C3C"));
                                ImageFile_Helper.getInstance().DeleteById(arrayListIds.get(finalI), ImageUpdateActivity.this);
                                btnClick.setEnabled(true);
                                layoutImg.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onFailure(Call<ImageApiResponse> call, Throwable t) {
                            Utility.stopProgress(dialog);
                            Log.e("Error", "uploading data");
                            Utility.snackBar(rootLayout, "Try again...", 2000, Color.parseColor("#ff0000"));
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } else {
            Utility.snackBar(rootLayout, "Please Click image first", 2000, Color.parseColor("#ff0000"));
        }
    }
    /*method for getting location coordinates*/
    private void getCoordinates() {
        locationViewModel.getLocationLiveData().observe(this, new Observer<Location>() {
            @Override
            public void onChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();

                Log.d("cgeckjdj", String.valueOf(latitude));

//                saveSurveyorLocation();
            }
        });
    }
    /*method for setting toolbar*/
    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView title = findViewById(R.id.title);
        title.setText("Update Image");
        // hiding default title text on toolbar
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}
