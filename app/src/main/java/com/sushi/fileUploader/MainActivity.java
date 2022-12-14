package com.sushi.fileUploader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.multidex.BuildConfig;
import android.app.ProgressDialog;
import android.content.Context;

import android.content.Intent;

import android.graphics.Rect;

import android.os.Bundle;

import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;
import com.sushi.fileUploader.activities.HomeActivity;
import com.sushi.fileUploader.models.LoginApiResponse;
import com.sushi.fileUploader.utility.AppConstants;
import com.sushi.fileUploader.utility.Util;
import com.sushi.fileUploader.viewModel.LoginViewModel;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int PERMISSION_REQUEST_CODE = 101;
    private static final int MY_PERMISSIONS_REQUEST_CODE = 123;
    private Button loginBtn;
    SharedPreference sharedPreference;

    private TextInputEditText userName, userPassword;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreference = SharedPreference.getInstance(this);

        Window window = MainActivity.this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.colorAccent));

        initializeViews();

        userName.setText("Demo");
        userPassword.setText("Demo@1234");


        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sharedPreference.getString("true","").equals("1")){
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    /*method for initializing views*/
    private void initializeViews() {
        userName = findViewById(R.id.name_edit_text);
        userPassword = findViewById(R.id.pass_edit_text);
        loginBtn = findViewById(R.id.login_btn);
        TextView txtAppVersion = findViewById(R.id.textlogin_version);
        txtAppVersion.setText("Version: " + BuildConfig.VERSION_NAME);
        loginBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn:
                loginApiCall();
                break;
        }

    }

    /*method for login */
    private void loginApiCall() {
        if (userName.getText().toString().equals("")) {
            Toast.makeText(this, "User Id can't be left empty", Toast.LENGTH_LONG).show();
        } else if (userPassword.getText().toString().equals("")) {
            Toast.makeText(this, "Password can't be left empty", Toast.LENGTH_LONG).show();
        } else {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(AppConstants.USER_ID, userName.getText().toString().trim());
            jsonObject.addProperty(AppConstants.PASSWORD, userPassword.getText().toString().trim());
            loginViewModel.init(jsonObject);

            ProgressDialog progressDialog = ProgressDialog.show(this, "", "Please wait...", false);
            loginViewModel.getIsLoading().observe(this, aBoolean -> {
                if (aBoolean) {
                    progressDialog.show();
                } else {
                    progressDialog.dismiss();
                }
            });

            loginViewModel.getLogin(jsonObject).observe(this, new Observer<LoginApiResponse>() {
                @Override
                public void onChanged(LoginApiResponse loginApiResponse) {
                    if (loginApiResponse != null && loginApiResponse.getCode().equals(AppConstants.SUCCESS_CODE)) {
                        sharedPreference.putString("true", "1");
                        Util.putString(MainActivity.this, AppConstants.USER_ID, userName.getText().toString());
                        Util.putString(MainActivity.this, AppConstants.DESIGNATION, loginApiResponse.getDesignation());
                        Util.putString(MainActivity.this, AppConstants.NAME, loginApiResponse.getProfileName());

                        startActivity(new Intent(MainActivity.this, HomeActivity.class));
                        finish();
                    } else {
                        Toast.makeText(MainActivity.this, loginApiResponse.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
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