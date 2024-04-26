package com.example.svhtcmobile.Controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.svhtcmobile.Adapter.AdapterSinhVien;
import com.example.svhtcmobile.Api.ApiClient;
import com.example.svhtcmobile.Api.apiService.IQuanTriThongTin;
import com.example.svhtcmobile.Model.SinhVien;
import com.example.svhtcmobile.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainQuanTriSinhVien extends AppCompatActivity {
    public static final int PICK_IMAGE_REQUEST = 1;
    private ImageButton imgBtnBack, imgBtnLogout, btnAddSV;
    private TextView tvMaLop, tvTenKhoa, tvVeNhaTruong, tvXemThongTinCaNhan, text1;
    private ImageView ivAnhSinhVien;
    private ListView lvSinhVienLop;
    AdapterSinhVien adapterSV;
    String malop ;
    SharedPreferences accountSharedPref;
    IQuanTriThongTin iQuanTriThongTin;
    List<String> DSL = new ArrayList<>();
    List<SinhVien> DSSV = new ArrayList<>();
    Spinner spLop;

    String base64String;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quan_tri_sinh_vien);
        setControl();
        setEvent();


    }

    private void setEvent() {
        spLop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                spLop.getSelectedItem().toString();
                    malop = spLop.getSelectedItem().toString();
                    iQuanTriThongTin.listSVByMaLop(malop).enqueue(new Callback<List<SinhVien>>() {
                        @Override
                        public void onResponse(Call<List<SinhVien>> call, Response<List<SinhVien>> response) {
                            DSSV.clear();
                            if (response.isSuccessful() && response.body() != null) {
                                DSSV.addAll(response.body());
                                Log.d("dSSV size", String.valueOf(DSSV.size()));
                                adapterSV.notifyDataSetChanged();
                                Log.e("API Response", "Success: " + response.message());
                            } else {
                                Log.e("API Response list sinh vien", "Error: " + response.message());
                            }

                        }


                        @Override
                        public void onFailure(Call<List<SinhVien>> call, Throwable throwable) {
                            Log.e("API Response list sinh vien", "Failure: " + throwable.getMessage());
                        }

                    });
//                    adapterSV.notifyDataSetChanged();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // Xử lý sự kiện khi nhấn nút quay lại
        imgBtnBack.setOnClickListener(view -> finish());

        // Xử lý sự kiện khi nhấn nút đăng xuất
        imgBtnLogout.setOnClickListener(view -> finish());

        // Xử lý sự kiện khi nhấn vào nút thêm sinh viên mới
        btnAddSV.setOnClickListener(view -> {
            // Tạo AlertDialog.Builder
            AlertDialog.Builder builder = new AlertDialog.Builder(MainQuanTriSinhVien.this);

            // Gắn layout cho dialog
            View dialogView = getLayoutInflater().inflate(R.layout.add_sinh_vien, null);
            builder.setView(dialogView);
            EditText edtMaLop= dialogView.findViewById(R.id.loadMaLop);
            edtMaLop.setText(malop);
            ivAnhSinhVien = dialogView.findViewById(R.id.ivAnhSinhVien);

            // Lấy ra các button từ layout của dialog
            Button btnHuy = dialogView.findViewById(R.id.btnHuy);
            Button btnLuu = dialogView.findViewById(R.id.btnLuu);

            ImageButton btnChonAnh = dialogView.findViewById(R.id.btnChonAnh);


            btnChonAnh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent, PICK_IMAGE_REQUEST);
                }
            });

            // Tạo và hiển thị dialog
            AlertDialog dialog = builder.create();
            dialog.show();

            // Thiết lập hành động cho nút "Lưu"
            btnLuu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    EditText edtMaSV = dialogView.findViewById(R.id.edtMaSV);
                    EditText edtHoSV = dialogView.findViewById(R.id.edtHoSV);
                    EditText edtTenSV = dialogView.findViewById(R.id.edtTenSV);
                    RadioGroup radioGroupPhai = dialogView.findViewById(R.id.radioGroupPhai);
                    EditText edtDiaChi = dialogView.findViewById(R.id.edtDiaChi);
                    EditText edtSDT = dialogView.findViewById(R.id.edtSDT);
                    EditText edtNgaySinh = dialogView.findViewById(R.id.edtNgaySinh);

                    String maLop = edtMaLop.getText().toString();
                    String maSV = edtMaSV.getText().toString();
                    String hoSV = edtHoSV.getText().toString();
                    String tenSV = edtTenSV.getText().toString();
                    String diaChiSV = edtDiaChi.getText().toString();
                    String sdtSV = edtSDT.getText().toString();
                    String inputDate = edtNgaySinh.getText().toString();
                    String ngaySinh="";
                    Boolean check = true;
                    for (SinhVien sV1 : DSSV) {
                        if (maSV.equals(sV1.getMasv().trim())) {
                            check = false;
                        }
                    }
                    int genderValue = 0;
                    int selectedGenderId = radioGroupPhai.getCheckedRadioButtonId();
                    if (selectedGenderId == R.id.radioButtonFemale) {
                        genderValue = 1; // Nếu chọn giới tính nữ, gán giá trị là 1
                    }
                    SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");

                    // Định dạng của chuỗi ngày đầu ra
                    SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

                    try {
                        // Chuyển đổi chuỗi ngày thành kiểu Date
                        Date date = inputFormat.parse(inputDate);

                        // Chuyển đổi thành chuỗi theo định dạng mới
                        ngaySinh= outputFormat.format(date);


                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    boolean phaiSV;
                    if (genderValue == 1) phaiSV = true;
                    else phaiSV = false;// 1: Nữ, 0: Nam
                    if (check == true) {
                        Log.e("adddffd",base64String);
                        SinhVien sv = new SinhVien(maSV, hoSV, tenSV, phaiSV,  diaChiSV,ngaySinh, maLop,  false,sdtSV,base64String,maSV.trim()+"@student.ptithcm.edu.vn");


                        iQuanTriThongTin.themSinhVien(sv).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {

                                    iQuanTriThongTin.timSinhVien(sv.getMasv()).enqueue(new Callback<SinhVien>() {
                                        @Override
                                        public void onResponse(Call<SinhVien> call, Response<SinhVien> response) {
                                            if (response.isSuccessful()) {
                                                SinhVien sinhVien = response.body();
                                                 sv.setHinhanh(sinhVien.getHinhanh());

                                            } else {
                                                Toast.makeText(MainQuanTriSinhVien.this, "Có vấn đề!", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<SinhVien> call, Throwable t) {
                                            // Xử lý khi có lỗi xảy ra
                                        }
                                    });

                                    DSSV.add(sv);

                                    adapterSV.notifyDataSetChanged();
                                    Toast.makeText(getApplicationContext(), "Thêm thành công!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Thêm không thành công!", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                // Xử lý khi có lỗi xảy ra
                            }
                        });

                        dialog.dismiss();

                    }
                    else Toast.makeText(getApplicationContext(), "Mã sinh viên đã tồn tại!", Toast.LENGTH_SHORT).show();
                }
            });

            // Thiết lập hành động cho nút "Hủy"
            btnHuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Xử lý khi nhấn nút "Hủy"
                    // Ví dụ: Đóng dialog
                    dialog.dismiss();
                }
            });
        });

        // Xử lý sự kiện khi nhấn vào nút "Về nhà trường"
        tvVeNhaTruong.setOnClickListener(view -> {
            // Xử lý khi nhấn vào nút "Về nhà trường"
            // Ví dụ: Chuyển sang màn hình về trang chủ của trường
        });

        // Xử lý sự kiện khi nhấn vào nút "Xem thông tin cá nhân"
        tvXemThongTinCaNhan.setOnClickListener(view -> {
            // Xử lý khi nhấn vào nút "Xem thông tin cá nhân"
            // Ví dụ: Chuyển sang màn hình xem thông tin cá nhân
        });

    }

    private void setControl() {
        accountSharedPref = getSharedPreferences("Account", Context.MODE_PRIVATE);
        String token = accountSharedPref.getString("token", "");
        Retrofit retrofit = ApiClient.getClient(token);
        iQuanTriThongTin = retrofit.create(IQuanTriThongTin.class);
        // Ánh xạ các thành phần giao diện
        imgBtnBack = findViewById(R.id.imgBtnBack);
        imgBtnLogout = findViewById(R.id.imgBtnLogout);
        btnAddSV = findViewById(R.id.btnAddSV);
        tvMaLop = findViewById(R.id.tvMaLop);

        tvVeNhaTruong = findViewById(R.id.tvVeNhaTruong);
        tvXemThongTinCaNhan = findViewById(R.id.tvXemThongTinCaNhan);
        lvSinhVienLop = findViewById(R.id.lvSinhVienLop);

        spLop = findViewById(R.id.spinnerDSL);
        adapterSV = new AdapterSinhVien(MainQuanTriSinhVien.this, R.layout.sinh_vien_item, DSSV, iQuanTriThongTin);
        lvSinhVienLop.setAdapter(adapterSV);

        iQuanTriThongTin.locMaLop().enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful() && response.body() != null) {

                    if(response.code() == 200){
                        DSL.addAll(response.body());
                        ArrayAdapter adapter_spLop = new ArrayAdapter(MainQuanTriSinhVien.this, R.layout.layout_spinner, DSL);
                        spLop.setAdapter(adapter_spLop);


                    }
                } else {
                    Log.e("API Response", "Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable throwable) {
                Log.e("API Response", "Error: " + throwable.getMessage());

            }  });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Tìm ImageView trong dialogView
                if (ivAnhSinhVien != null) {
                    // Gán ảnh vào ImageView
                    ivAnhSinhVien.setImageBitmap(bitmap);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    base64String = Base64.encodeToString(byteArray, Base64.DEFAULT);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
