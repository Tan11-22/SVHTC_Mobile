package com.example.svhtcmobile.Api.apiService;

import com.example.svhtcmobile.Model.GiangVien;
import com.example.svhtcmobile.Model.Lop;
import com.example.svhtcmobile.Model.MonHoc;
import com.example.svhtcmobile.Model.SinhVien;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IQuanTriThongTin {
    @GET("sinh-vien/tim-sinh-vien")
    Call<SinhVien> timSinhVien(@Query("ma-sv") String masv);
    @GET("sinh-vien/danh-sach-lop")
    Call<List<Lop>> danhSachLop();
    @GET("sinh-vien/loc-ma-lop")
    Call<List<String>> locMaLop();
    @GET("sinh-vien/danh-sach-sv-lop")
    Call<List<SinhVien>> listSVByMaLop(@Query("ma-lop") String malop);
    @POST("sinh-vien/them-sinh-vien-moi")
    Call<Void> themSinhVien(@Body SinhVien sinhVien);
    @POST("sinh-vien/update-sinh-vien")
    Call<Void> updateSinhVien(@Body SinhVien sinhVien);
    @POST("sinh-vien/xoa-sinh-vien")
    Call<Void> xoaSinhVien(@Query("ma-sv") String masv);
    @GET("mon-hoc/danh-sach-mon-hoc")
    Call<List<MonHoc>> danhSachMonHoc() ;
    @GET("mon-hoc/tim-mon-hoc")
    Call<MonHoc> timMonHoc(@Query("ma-mon-hoc") String mamh);
    @POST("mon-hoc/them-mon-hoc-moi")
    Call<Void> themMonHocMoi(@Body MonHoc monHoc);
    @POST("mon-hoc/update-mon-hoc")
    Call<Void> updateMonHoc(@Body MonHoc monHoc);
    @POST("mon-hoc/xoa-mon-hoc")
    Call<Void> xoaMonHoc(@Query("mamh") String mamh);
    @POST("sinh-vien/da-nghi-hoc")
    Call<Void> updateDaNghiHoc(@Query("masv") String masv);
    @POST("sinh-vien/quen-mat-khau")
    Call<Void> quenMatKhau(@Query("email") String email,@Query("password") String password);
    @POST("sinh-vien/doi-mat-khau")
    Call<Void> doiMatKhau(@Query("username") String username,@Query("password") String password);
    @POST("sinh-vien/encode-ten-anh")
    Call<Map<String,String>> encodeTenAnh(@Query("ten-anh") String tenanh);

    @GET("giang-vien/tim-giang-vien")
    Call<GiangVien> timGiangVien(@Query("ma-gv") String magv);

    @GET("giang-vien/loc-ma-khoa")
    Call<List<String>> locMaKhoa();
    @GET("giang-vien/danh-sach-gv-khoa")
    Call<List<GiangVien>> listGVByMaKhoa(@Query("ma-khoa") String makhoa);
    @POST("giang-vien/them-giang-vien-moi")
    Call<Void> themGiangVien(@Body GiangVien giangVien);
    @POST("giang-vien/update-giang-vien")
    Call<Void> updateGiangVien(@Body GiangVien giangVien);
    @POST("giang-vien/xoa-giang-vien")
    Call<Void> xoaGiangVien(@Query("ma-gv") String magv);
}
