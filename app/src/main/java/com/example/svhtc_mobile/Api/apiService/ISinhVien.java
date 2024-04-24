package com.example.svhtc_mobile.Api.apiService;

import com.example.svhtc_mobile.Model.DDKLTC;
import com.example.svhtc_mobile.Model.DKLTC;
import com.example.svhtc_mobile.Model.HocPhiHocKy;
import com.example.svhtc_mobile.Model.HocPhiKeToan;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ISinhVien {
    //Api về học phí
    @GET("api/hoc-phi/hoc-ky")
    Call<List<HocPhiHocKy>> getDSHocPhiHocKy(@Query("maSV")String maSV,
                                             @Query("nienKhoa")String nienKhoa,
                                             @Query("hocKy")int hocKy);
    @GET("api/hoc-phi/sinh-vien")
    Call<List<HocPhiKeToan>> getDSHocPhiSinhVien(@Query("maSV")String maSV);
    @GET("api/hoc-phi/ke-toan")
    Call<List<HocPhiKeToan>> getDSHocPhiKeToan();
    @GET("api/hoc-phi/dong-hoc-phi")
    Call<Map<String,String>> dongHocPhi(@Query("maSV")String maSV,
                                        @Query("nienKhoa")String nienKhoa,
                                        @Query("hocKy")int hocKy,
                                        @Query("soTienDong")int soTienDong);
    @GET("api/hoc-phi/ap-hoc-phi")
    Call<Map<String,String>> apHocPhi(@Query("maSV")String maSV,
                                      @Query("nienKhoa")String nienKhoa,
                                      @Query("hocKy")int hocKy);
    //Api về đăng ký lớp tín chỉ
    @GET("api/lop-tin-chi/lopsv-dang-ky")
    Call<List<DKLTC>> getDSLopSVDangKy(@Query("maLop")String maLop,
                                       @Query("nienKhoa")String nienKhoa,
                                       @Query("hocKy")int hocKy);
    @GET("api/lop-tin-chi/ds-dang-ky")
    Call<List<DKLTC>> getDSDangKy( @Query("nienKhoa")String nienKhoa,
                                   @Query("hocKy")int hocKy);
    @GET("api/lop-tin-chi/da-dang-ky")
    Call<List<DDKLTC>> getDSDDKLTC(@Query("maSV")String maSV,
                                   @Query("nienKhoa")String nienKhoa,
                                   @Query("hocKy")int hocKy);

    @GET("api/lop-tin-chi/dang-ky")
    Call<Map<String, String>> dangKy(@Query("maSV")String maSV,
                                     @Query("maLTC")int maLTC);
    @GET("api/lop-tin-chi/bo-dang-ky")
    Call<Map<String, String>> boDangKy(@Query("maSV")String maSV,
                                       @Query("maLTC")int maLTC);


}
