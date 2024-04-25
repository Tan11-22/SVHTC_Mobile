package com.example.svhtcmobile.Api.apiService;

import com.example.svhtcmobile.Model.Khoa;
import com.example.svhtcmobile.Model.Nganh;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ILoginService {
    @POST("api/auth/user/login")
    Call<JsonObject> login(@Query("username") String username,
                           @Query("password") String password);

    @GET("api/auth/info")
    Call<JsonObject> getInfo(@Query("username") String username);

    @GET("api/auth/danh-sach-khoa")
    Call<List<Khoa>> getDanhSachKhoa();

    @GET("api/auth/danh-sach-nganh")
    Call<List<Nganh>> getDanhSachNganh();

    @PUT("api/auth/add-khoa")
    Call<Boolean> addKhoa(@Query("ma") String ma,
                          @Query("ten") String ten,
                          @Query("id") int id);

    @PUT("api/auth/edit-khoa")
    Call<Boolean> editKhoa(@Query("ma") String ma,
                           @Query("ten") String ten,
                           @Query("id") int id);

    @PUT("api/auth/delete-khoa")
    Call<Boolean> deleteKhoa(@Query("makhoa") String ma
    );
}
