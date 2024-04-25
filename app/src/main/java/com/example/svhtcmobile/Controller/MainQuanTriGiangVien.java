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
import androidx.appcompat.widget.SearchView;

import com.example.svhtcmobile.Adapter.AdapterGiangVien;
import com.example.svhtcmobile.Api.ApiClient;
import com.example.svhtcmobile.Api.apiService.IQuanTriThongTin;
import com.example.svhtcmobile.Model.GiangVien;
import com.example.svhtcmobile.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class MainQuanTriGiangVien extends AppCompatActivity {
    public static final int PICK_IMAGE_REQUEST = 1;
    private ImageButton imgBtnBack, imgBtnLogout, btnAddGV;
    private TextView  tvVeNhaTruong, tvXemThongTinCaNhan;
    private ListView lvGiangVienKhoa;
    private SearchView searchView ;
    AdapterGiangVien adapterGV;
    String makhoa ;
    private ImageView ivAnhGV;
    List<String> DSK = new ArrayList<>();
    List<GiangVien> DSGV = new ArrayList<>();
    Spinner spKhoa;
    private List<GiangVien> filteredGiangVienList;
    String base64String;
    SharedPreferences accountSharedPref;
    IQuanTriThongTin iQuanTriThongTin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quan_tri_giang_vien);
        setControl();
        setEvent();


    }
    private void setControl() {
        // Ánh xạ các thành phần giao diện
        imgBtnBack = findViewById(R.id.imgBtnBack);
        imgBtnLogout = findViewById(R.id.imgBtnLogout);
        btnAddGV = findViewById(R.id.btnAddGV);


        tvVeNhaTruong = findViewById(R.id.tvVeNhaTruong);
        tvXemThongTinCaNhan = findViewById(R.id.tvXemThongTinCaNhan);
        lvGiangVienKhoa = findViewById(R.id.lvGiangVienKhoa);
        accountSharedPref = getSharedPreferences("Account", Context.MODE_PRIVATE);
        String token = accountSharedPref.getString("token", "");
        Retrofit retrofit = ApiClient.getClient(token);
        iQuanTriThongTin = retrofit.create(IQuanTriThongTin.class);
        spKhoa = findViewById(R.id.spinnerDSK);
        adapterGV = new AdapterGiangVien(MainQuanTriGiangVien.this, R.layout.giang_vien_item, DSGV,iQuanTriThongTin);
        lvGiangVienKhoa.setAdapter(adapterGV);


        searchView = findViewById(R.id.searchGV);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                MainQuanTriGiangVien.this.adapterGV.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterByMaGV(newText);
                return true ;
            }
        });
        iQuanTriThongTin.locMaKhoa().enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful() && response.body() != null) {

                    if(response.code() == 200){
                        DSK.addAll(response.body());
                        ArrayAdapter adapter_spLop = new ArrayAdapter(MainQuanTriGiangVien.this, R.layout.layout_spinner, DSK);
                        spKhoa.setAdapter(adapter_spLop);


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



    private void setEvent() {
        spKhoa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                makhoa = spKhoa.getSelectedItem().toString();
                iQuanTriThongTin.listGVByMaKhoa(makhoa).enqueue(new Callback<List<GiangVien>>() {
                    @Override
                    public void onResponse(Call<List<GiangVien>> call, Response<List<GiangVien>> response) {
                        DSGV.clear();
                        if (response.isSuccessful() && response.body() != null) {
                            DSGV.addAll(response.body());
                            adapterGV.notifyDataSetChanged();
                            Log.e("API Response", "Success: " + response.message());
                        } else {
                            Log.e("API Response list sinh vien", "Error: " + response.message());
                        }

                    }


                    @Override
                    public void onFailure(Call<List<GiangVien>> call, Throwable throwable) {
                        Log.e("API Response list sinh vien", "Failure: " + throwable.getMessage());
                    }

                });

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btnAddGV.setOnClickListener(view -> {
            // Tạo AlertDialog.Builder
            AlertDialog.Builder builder = new AlertDialog.Builder(MainQuanTriGiangVien.this);

            // Gắn layout cho dialog
            View dialogView = getLayoutInflater().inflate(R.layout.add_giang_vien, null);
            builder.setView(dialogView);
            EditText edtMaKhoa= dialogView.findViewById(R.id.loadMaKhoa);
            edtMaKhoa.setText(makhoa);
            ivAnhGV = dialogView.findViewById(R.id.ivAnhGV);

            // Lấy ra các button từ layout của dialog
            Button btnHuy = dialogView.findViewById(R.id.btnHuyGV);
            Button btnLuu = dialogView.findViewById(R.id.btnLuuGV);

            ImageButton btnChonAnh = dialogView.findViewById(R.id.btnChonAnhGV);


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

                    EditText edtMaGV = dialogView.findViewById(R.id.edtMaGV);
                    EditText edtHoGV = dialogView.findViewById(R.id.edtHoGV);
                    EditText edtTenGV = dialogView.findViewById(R.id.edtTenGV);
                    RadioGroup radioGroupHocHam = dialogView.findViewById(R.id.radioGroupHocHam);
                    RadioGroup radioGroupHocVi = dialogView.findViewById(R.id.radioGroupHocVi);
                    EditText edtSDT = dialogView.findViewById(R.id.edtSDTGV);
                    EditText edtChuyenMon = dialogView.findViewById(R.id.edtChuyenMon);

                    String maKhoa1 = edtMaKhoa.getText().toString();
                    String magv = edtMaGV.getText().toString();
                    String ho = edtHoGV.getText().toString();
                    String ten = edtTenGV.getText().toString();
                    String hocham;
                    String hocvi;
                    String chuyenmon = edtChuyenMon.getText().toString();
                    String sdt = edtSDT.getText().toString();

                    Boolean check = true;
                    for (GiangVien gv1 : DSGV) {
                        if (magv.equals(gv1.getMagv().trim())) {
                            check = false;
                        }
                    }

                    int selectedHocHamId = radioGroupHocHam.getCheckedRadioButtonId();
                    if (selectedHocHamId == R.id.radioButtonPhoGiaoSu) {
                        hocham = "Phó giáo sư";
                    }
                    else if (selectedHocHamId == R.id.radioButtonGiaoSu) {
                        hocham = "Giáo sư";
                    }
                    else hocham = "Không";

                    int selectedHocViId = radioGroupHocVi.getCheckedRadioButtonId();
                    if (selectedHocViId == R.id.radioButtonThacSi) {
                        hocvi = "Thạc sĩ";
                    }
                    else if (selectedHocViId == R.id.radioButtonTienSi) {
                        hocvi = "Tiến sĩ";
                    }
                    else hocvi = "Tiến sĩ Khoa Học";

                    if (check == true) {
                        GiangVien gv = new GiangVien(magv, ho, ten, hocham,hocvi,chuyenmon, sdt, base64String,magv.trim()+"@.ptithcm.edu.vn",maKhoa1);


                        iQuanTriThongTin.themGiangVien(gv).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {

                                    iQuanTriThongTin.timGiangVien(magv).enqueue(new Callback<GiangVien>() {
                                        @Override
                                        public void onResponse(Call<GiangVien> call, Response<GiangVien> response) {
                                            if (response.isSuccessful()) {
                                                GiangVien giangVien = response.body();
                                                gv.setHinhanh(giangVien.getHinhanh());

                                            } else {
                                                Toast.makeText(MainQuanTriGiangVien.this, "Có vấn đề!", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<GiangVien> call, Throwable t) {
                                            // Xử lý khi có lỗi xảy ra
                                        }
                                    });

                                    DSGV.add(gv);

                                    adapterGV.notifyDataSetChanged();
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
                    else Toast.makeText(getApplicationContext(), "Mã giảng viên đã tồn tại!", Toast.LENGTH_SHORT).show();
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
        // Xử lý sự kiện khi nhấn nút quay lại
        imgBtnBack.setOnClickListener(view -> finish());

        // Xử lý sự kiện khi nhấn nút đăng xuất
        imgBtnLogout.setOnClickListener(view -> {
            // Xử lý khi nhấn nút đăng xuất
            // Ví dụ: Đăng xuất và chuyển sang màn hình đăng nhập
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Tìm ImageView trong dialogView
                if (ivAnhGV != null) {
                    // Gán ảnh vào ImageView
                    ivAnhGV.setImageBitmap(bitmap);
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

    public void filterByMaGV(String maGV) {
        if (filteredGiangVienList == null) {
            filteredGiangVienList = new ArrayList<>();
        } else {
            filteredGiangVienList.clear();
        }

        if (maGV.isEmpty()) {
            filteredGiangVienList.addAll(DSGV);
        } else {
            for (GiangVien gv : DSGV) {
                if (gv.getMagv().toLowerCase().contains(maGV.toLowerCase())) {
                    filteredGiangVienList.add(gv);
                }
            }
            Log.e("LISTSEARCH", filteredGiangVienList.toString());
        }

//        adapterGV.setData(filteredGiangVienList);
        adapterGV.notifyDataSetChanged();
    }
}
