package com.sushi.fileUploader.request;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sushi.fileUploader.models.ImageApiResponse;
import com.sushi.fileUploader.models.LoginApiResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiInterface {

    @Multipart
    @POST("UploadMultipartImages/PostFormData")
    Call<ImageApiResponse> uploadImageToServer(@Part MultipartBody.Part image);

    //============Added by Sushant @19Sept==================
    @GET("Api_TBL_UAV_LAYERS/GetTBL_UAV_LAYERS")
    Call<JsonArray> getUAVLayers(@Query("ULBCODE") String district);

    @POST("Authenticate")
    Call<LoginApiResponse> userAuthenticate(@Body JsonObject jsonObject);
}

