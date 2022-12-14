package com.sushi.fileUploader;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

public class MyExceptionHandler implements Thread.UncaughtExceptionHandler {
    private final Context myContext;
    private final String LINE_SEPARATOR = "\n";
    private String errorMSG="";
    private String errorPage="";
    private String brandName="";
    private String device="";
    private String model="";
    private String app_Version="";
    private String userId="";
    private SharedPreferences login_pref;

    public MyExceptionHandler(Context context) {
        myContext = context;
        login_pref= PreferenceManager.getDefaultSharedPreferences(myContext);
    }

    public void uncaughtException(Thread thread, Throwable exception) {
        StringWriter stackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(stackTrace));
        // exception.get
        StringBuilder errorReport = new StringBuilder();
        errorReport.append("************ CAUSE OF ERROR ************\n\n");
        errorReport.append(stackTrace.toString());

        errorMSG=String.valueOf(exception.getCause());
        errorPage="InventiaHR";

        errorReport.append("\n************ DEVICE INFORMATION ***********\n");
        errorReport.append("Brand: ");
        errorReport.append(Build.BRAND);
        brandName=Build.BRAND;
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Device: ");
        errorReport.append(Build.DEVICE);
        device=Build.DEVICE;
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Model: ");
        errorReport.append(Build.MODEL);
        model=Build.MODEL;
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Id: ");
        errorReport.append(Build.ID);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Product: ");
        errorReport.append(Build.PRODUCT);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("\n************ FIRMWARE ************\n");
        errorReport.append("SDK: ");
        errorReport.append(Build.VERSION.SDK);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Release: ");
        errorReport.append(Build.VERSION.RELEASE);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Incremental: ");
        errorReport.append(Build.VERSION.INCREMENTAL);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("App version : ");
        errorReport.append(BuildConfig.VERSION_NAME);
        app_Version=BuildConfig.VERSION_NAME;
        errorReport.append(LINE_SEPARATOR);
        userId=login_pref.getString("userid","");
             sendEmail(errorReport.toString());
    }

    private void sendEmail(String error){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "sushantsingh2244@gmail.com" });
        intent.putExtra(Intent.EXTRA_SUBJECT, "File Upload Crash Report");
        intent.putExtra(Intent.EXTRA_TEXT, error);
        myContext.startActivity(Intent.createChooser(intent, ""));
    }
}
