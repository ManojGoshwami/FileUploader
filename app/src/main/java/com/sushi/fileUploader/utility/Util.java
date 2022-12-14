package com.sushi.fileUploader.utility;

import android.app.Activity;
import android.content.Context;

import android.content.SharedPreferences;

import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.google.gson.JsonArray;
import com.sushi.fileUploader.SharedPreference;
import com.sushi.fileUploader.network.ApiClient;
import com.sushi.fileUploader.request.ApiInterface;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Util {
    public static boolean checkEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (email.trim().matches(emailPattern)) {
            return true;
        } else return false;
    }

    public static boolean isValidMobile(String mobile) {
        boolean check;
        String regex = "[6-9][0-9]{9}";
        check = mobile.matches(regex);
        return check;
    }

    public static boolean isValidPinCode(String pin) {
        boolean check;
        String regex = "[1-9][0-9]{5}";
        check = pin.matches(regex);
        return check;
    }

    public static boolean isValidPanCard(String pin) {
        boolean check;
        String regex = "[A-Z]{5}[0-9]{4}[A-Z]";
        check = pin.matches(regex);
        return check;
    }

    public static void hideStatusBar(Activity activity) {
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


//    public static boolean checkNetworkConnection(Context context) {
//        ConnectivityManager ConnectionManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = ConnectionManager.getActiveNetworkInfo();
//        if (networkInfo != null && networkInfo.isConnected()) {
//            return true;
//        } else {
//            return false;ImageUtil
//        }
//    }

    public static void putLong(Context context, String key, long value) {
        SharedPreferences pref = context.getSharedPreferences(AppConstants.MY_PREF, 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public static long getLong(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences(AppConstants.MY_PREF, 0); // 0 - for private mode
        return pref.getLong(key, 0);
    }

    public static void putString(Context context, String key, String value) {
        SharedPreferences pref = context.getSharedPreferences(AppConstants.MY_PREF, 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getString(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences(AppConstants.MY_PREF, 0); // 0 - for private mode
        return pref.getString(key, "");
    }

    public static void putInt(Context context, String key, int value) {
        SharedPreferences pref = context.getSharedPreferences(AppConstants.MY_PREF, 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static int getInt(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences(AppConstants.MY_PREF, 0); // 0 - for private mode
        return pref.getInt(key, 00000);
    }

    public static void clearLoginPref(Context context, String nameKey, String passKey) {
        SharedPreferences pref = context.getSharedPreferences(AppConstants.MY_PREF, 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(nameKey);
        editor.remove(passKey);

        editor.commit();
    }

    public static void getUAV(Context context, GoogleMap map) {
//        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        String ulb = SharedPreference.getInstance(context).getString("ulbCode", "");
        Call<JsonArray> call = apiService.getUAVLayers("HRD");
//        Call<JsonArray> call = apiService.getUAVLayers(ulb);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                try {
                    JsonArray jsonArray = response.body();

                    if (jsonArray != null && jsonArray.size() > 0) {
                        JSONObject jsonObject = new JSONObject(jsonArray.get(0).toString());

                        String baseUrl = jsonObject.getString("BASE_URL");
                        String service = jsonObject.getString("SERVICE");
                        String version = jsonObject.getString("VERSION");
                        String request = jsonObject.getString("REQUEST");
                        String layer = jsonObject.getString("LAYER_NAME");
                        String srs = jsonObject.getString("SRS");
                        String format = jsonObject.getString("FORMAT");
                        String trans = jsonObject.getString("TRANSPARENT");
                        TileProvider wmsTileProvider = TileProviderFactory.getOsgeoWmsTileProvider(
                                baseUrl, service, version, request, layer, srs, format, trans);
                        map.addTileOverlay(new TileOverlayOptions().tileProvider(wmsTileProvider));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

}
