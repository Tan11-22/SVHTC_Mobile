package com.example.svhtc_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.svhtc_mobile.Api.ApiClient;
import com.example.svhtc_mobile.Api.apiService.ILoginService;
import com.example.svhtc_mobile.Controller.KhoaController;
import com.example.svhtc_mobile.Model.UserInfo;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {


    ImageView ivDangNhap;
    private boolean passwordShowing;
    private boolean loginStatus = false;
    private UserInfo userInfo;
    SharedPreferences accountSharedPref;
    TextView tvTen, tvMa, tvChuaDangNhap, tvDangNhap;

    LinearLayout llPGV;
    ImageView ivKhoa, ivAccount;
    ILoginService iLoginService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setControl();
        setEvent();
    }

    private void setEvent() {
        ivDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!loginStatus){
                    showDialogLogin();
                } else {
                    logout();
                }
            }
        });
        ivKhoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, KhoaController.class));
            }
        });
    }

    private void setControl() {
        ivDangNhap = findViewById(R.id.ivDangNhap1);
        tvTen = findViewById(R.id.tvTen);
        tvMa = findViewById(R.id.tvMa);
        tvChuaDangNhap = findViewById(R.id.tvChuaDangNhap);
        tvDangNhap = findViewById(R.id.tvDangNhap);
        ivKhoa = findViewById(R.id.ivKhoa);
        ivAccount = findViewById(R.id.ivAccount);
        llPGV = findViewById(R.id.llPGV);
        Retrofit retrofit = ApiClient.getClient("");
        iLoginService = retrofit.create(ILoginService.class);
    }

    private void showDialogLogin() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_login);

        Window window = dialog.getWindow();
        if(window == null) return;

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAtrributes = window.getAttributes();
        windowAtrributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAtrributes);

        passwordShowing = false;

        EditText edtUsername = dialog.findViewById(R.id.edtUsername);
        EditText edtPassword = dialog.findViewById(R.id.edtPassword);
        ImageView passwordIcon = dialog.findViewById(R.id.passwordIcon);
        Button btnDangNhap = dialog.findViewById(R.id.btnDangNhap);



        passwordIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordShowing) {
                    passwordShowing = false;
                    edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordIcon.setImageResource(R.drawable.password_show);
                } else {
                    passwordShowing = true;
                    edtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordIcon.setImageResource(R.drawable.password_hide);
                }
                edtPassword.setSelection(edtPassword.length());
            }
        });

        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUsername.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                iLoginService.login(username,password).enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.code() == 200) {
                            JsonObject jsonObject= response.body();
                            saveTokenAccount(username, jsonObject.get("accessToken").getAsString());
                            Retrofit retrofit = ApiClient.getClient(jsonObject.get("accessToken").getAsString());
                            iLoginService = retrofit.create(ILoginService.class);
                            iLoginService.getInfo(username).enqueue(new Callback<JsonObject>() {
                                @Override
                                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                    if(response.code() == 200) {
                                        JsonObject jsonObject = response.body();
                                        userInfo = mapJsonObjectToUI(jsonObject);
                                        saveNameAccountandRole(userInfo.getHo(), userInfo.getTen(), userInfo.getTenQuyen());
                                        showInfo(userInfo);
                                        llPGV.setVisibility(View.VISIBLE);
                                    }

                                }

                                @Override
                                public void onFailure(Call<JsonObject> call, Throwable throwable) {
                                    Toast.makeText(MainActivity.this,"Đăng nhập thất bại!", Toast.LENGTH_SHORT).show();
                                }
                            });
                            dialog.dismiss();
                        } else {
                            Toast.makeText(MainActivity.this,"Đăng nhập thất bại!", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable throwable) {
                        Toast.makeText(MainActivity.this,"Đăng nhập thất bại!" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("loimoivcl", throwable.getMessage());
                    }
                });
            }
        });

        dialog.setCancelable(true);
        dialog.show();
    }

    private UserInfo mapJsonObjectToUI(JsonObject jsonObject) {
        UserInfo ui = new UserInfo();
        ui.setUsername(jsonObject.get("username").getAsString());
        ui.setHo(jsonObject.get("ho").getAsString());
        ui.setTen(jsonObject.get("ten").getAsString());
        ui.setTenQuyen(jsonObject.get("tenQuyen").getAsString());
//        ui.setIdQuyen(jsonObject.get("idQuyen").getAsInt());
        return ui;
    }
    private void showInfo(UserInfo userInfo) {

        tvTen.setText(userInfo.getHo() + " "+userInfo.getTen());
        tvMa.setText("Mã: "+ userInfo.getUsername()+ " - "+userInfo.getTenQuyen());
        tvTen.setVisibility(View.VISIBLE);
        tvMa.setVisibility(View.VISIBLE);
        tvChuaDangNhap.setVisibility(View.GONE);
        tvDangNhap.setText("Đăng xuất");
        ivDangNhap.setImageResource(R.drawable.logout);
        loginStatus = true;
    }

    private void logout() {
        loginStatus = false;
        tvTen.setText("");
        tvMa.setText("");
        tvTen.setVisibility(View.GONE);
        tvMa.setVisibility(View.GONE);
        tvChuaDangNhap.setVisibility(View.VISIBLE);
        tvDangNhap.setText("Đăng nhập");
        ivDangNhap.setImageResource(R.drawable.login);
        userInfo = null;
    }

    private void saveTokenAccount(String username, String token){
        accountSharedPref = getSharedPreferences("Account", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = accountSharedPref.edit();
        editor.putString("username", username);
        editor.putString("token", token);
        editor.apply();
    }
    private void saveNameAccountandRole(String ho, String ten, String quyen){
        accountSharedPref = getSharedPreferences("Account", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = accountSharedPref.edit();
        editor.putString("ho", ho);
        editor.putString("ten", ten);
        editor.putString("quyen", quyen);
        editor.apply();
    }

    private void clearSharedPref(SharedPreferences pref){
        pref.edit().clear().apply();
    }
}