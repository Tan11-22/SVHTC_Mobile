package com.example.svhtcmobile.Api.apiService;

import com.example.svhtcmobile.Model.CTDTDTO;
import com.example.svhtcmobile.Model.Khoa;
import com.example.svhtcmobile.Model.LopDTO;
import com.example.svhtcmobile.Model.Nganh;
import com.example.svhtcmobile.Model.TaiKhoanDTO;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ILoginService {
    @POST("auth/user/login")
    Call<JsonObject> login(@Query("username") String username,
                           @Query("password") String password);

    @GET("auth/info")
    Call<JsonObject> getInfo(@Query("username") String username);

    @GET("auth/danh-sach-khoa")
    Call<List<Khoa>> getDanhSachKhoa();

    @GET("auth/danh-sach-nganh")
    Call<List<Nganh>> getDanhSachNganh();

    @PUT("auth/add-khoa")
    Call<Boolean> addKhoa(@Query("ma") String ma,
                          @Query("ten") String ten,
                          @Query("id") int id);

    @PUT("auth/edit-khoa")
    Call<Boolean> editKhoa(@Query("ma") String ma,
                           @Query("ten") String ten,
                           @Query("id") int id);

    @PUT("auth/delete-khoa")
    Call<Boolean> deleteKhoa(@Query("makhoa") String ma
    );


    @GET("auth/danh-sach-tai-khoan")
    Call<List<TaiKhoanDTO>> getDanhSachTaiKhoan(@Query("id")int id);

    @PUT("auth/reset-pass")
    Call<Boolean> resetPassword(@Query("username") String username);

    @PUT("auth/set-status")
    Call<Boolean> setStatus(@Query("username") String username,
                            @Query("status") boolean status
    );

    @GET("auth/get-ctdt")
    Call<List<CTDTDTO>> getCTDT(@Query("ma-lop") String maLop,
                                @Query("nien-khoa") String nienKhoa);
    @GET("auth/danh-sach-lop")
    Call<List<JsonObject>> getDanhSachLop();

    @POST("auth/find-danh-sach-cthp")
    Call<List<JsonObject>> getDanhSachCTHP(@Query("masv") String maSV,
                                           @Query("nienkhoa") String nienKhoa,
                                           @Query("hocky") int hocKy);

}
