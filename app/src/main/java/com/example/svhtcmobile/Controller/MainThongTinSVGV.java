package com.example.svhtcmobile.Controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.svhtcmobile.Api.ApiClient;
import com.example.svhtcmobile.Api.apiService.IThongKe;
import com.example.svhtcmobile.Model.SinhVien;
import com.example.svhtcmobile.R;

import java.util.Base64;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainThongTinSVGV extends AppCompatActivity {
    private EditText edtMasv, edtHo, edtTen, edtDiaChi, edtNgaysinh, edtLop;
    private CheckBox ckbDangHoc;
    private RadioGroup rBtnPhai;
    private RadioButton rbtnNam,rbtnNu;
    private ImageView imgAvatar;
    private Button btnCapnhat;
    private Uri selectedImageUri;
    private Bitmap photo;
    private String picturePath;

    private static final int REQUEST_CODE_PICK_IMAGE = 101;

    SharedPreferences accountSharedPref;

    IThongKe iThongKe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_thong_tin_svgv);
        setControl();
        setEvent();
        String username = accountSharedPref.getString("username", "");
        iThongKe.getSinhVienById(username).enqueue(new Callback<SinhVien>() {
            @Override
            public void onResponse(Call<SinhVien> call, Response<SinhVien> response) {
                SinhVien sinhVien = response.body();
                if (sinhVien == null) {
                    Toast.makeText(MainThongTinSVGV.this, "Lấy thông tin thất bại", Toast.LENGTH_SHORT).show();
                }
                Log.e("checklogcallthongsvgv", String.valueOf(response.body()));
                Log.e("checklogcallthongsvgv", username);
                System.out.println(sinhVien.getImageResource());
                setup(sinhVien);
            }
            @Override
            public void onFailure(Call<SinhVien> call, Throwable throwable) {
                Toast.makeText(MainThongTinSVGV.this, "Lấy thông tin thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setup(SinhVien sinhVien) {
        edtMasv.setText(sinhVien.getMasv());
        edtHo.setText(sinhVien.getHo());
        edtTen.setText(sinhVien.getTen());
        edtLop.setText(sinhVien.getLop());
        edtDiaChi.setText(sinhVien.getDiachi());
        edtNgaysinh.setText(sinhVien.getNgaysinh());
        if(!sinhVien.getPhai()){
            rbtnNam.setChecked(true);
        }else {
            rbtnNu.setChecked(true);
        }
        if(sinhVien.getImageResource()!=null){
            byte[] imgdata = new byte[0];
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                imgdata = Base64.getDecoder().decode(sinhVien.getImageResource());
            }
            Bitmap bitmap = BitmapFactory.decodeByteArray(imgdata,0,imgdata.length);
            imgAvatar.setImageBitmap(bitmap);
        }else{
            Toast.makeText(MainThongTinSVGV.this,"Không có thông tin hình ảnh",Toast.LENGTH_SHORT).show();
        }
    }

    public void setControl() {
        btnCapnhat = findViewById(R.id.buttonCapNhat);
        imgAvatar = findViewById(R.id.imageViewHinhAnh);
        edtDiaChi = findViewById(R.id.editTextDiaChi);
        edtMasv = findViewById(R.id.editTextMaSV);
        edtHo = findViewById(R.id.editTextHo);
        edtTen = findViewById(R.id.editTextTen);
        edtNgaysinh = findViewById(R.id.editTextNgaySinh);
        edtLop = findViewById(R.id.editTextLop);
        rbtnNam = findViewById(R.id.radioButtonNam);
        rbtnNu = findViewById(R.id.radioButtonNu);
        imgAvatar = findViewById(R.id.imageViewHinhAnh);

        accountSharedPref = getSharedPreferences("Account", Context.MODE_PRIVATE);
        String token = accountSharedPref.getString("token", "");
        Retrofit retrofit = ApiClient.getClient(token);
        iThongKe = retrofit.create(IThongKe.class);
    }
    public void setEvent() {

//        imgAvatar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                pickImageFromGallery();
//            }
//        });
        btnCapnhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageFromGallery();
            }
        });
    }

    public void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            Toast.makeText(MainThongTinSVGV.this, "Lay anh thanh cong", Toast.LENGTH_SHORT).show();
            imgAvatar.setImageURI(selectedImageUri);
            // Xử lý hình ảnh đã chọn ở đây
//            String[] filePathColumn = {MediaStore.Images.Media.DATA};
//            Cursor cursor = getContentResolver().query(selectedImageUri,
//                    filePathColumn, null, null, null);
//            cursor.moveToFirst();
//
//            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//            picturePath = cursor.getString(columnIndex);
//            cursor.close();
//
//            Bitmap photo = (Bitmap) data.getExtras().get("data");
//            imgAvatar.setImageBitmap(photo);
            // Sau đó, gửi hình ảnh đã chọn đến server
            // ...
//            ContentResolver contentResolver = getBaseContext().getContentResolver();
//            try {
//                ParcelFileDescriptor parcelFileDescriptor = contentResolver.openFileDescriptor(selectedImageUri, "r");
//                FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
//                FileInputStream fileInputStream = new FileInputStream(fileDescriptor);
//                byte[] buffer = new byte[fileInputStream.available()];
//                fileInputStream.read(buffer);
//                RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), buffer);
//                MultipartBody.Part part = MultipartBody.Part.createFormData("image", "filename.jpg", requestBody);
//
//                ApiService.apiService.uploadFile(part).enqueue(new Callback<ResponseBody>() {
//                    @Override
//                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//
//                    }
//
//                    @Override
//                    public void onFailure(Call<ResponseBody> call, Throwable throwable) {
//
//                    }
//                });
//            } catch (FileNotFoundException e) {
//                throw new RuntimeException(e);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
        }
    }

}