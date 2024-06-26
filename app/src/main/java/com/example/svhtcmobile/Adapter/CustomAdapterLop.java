package com.example.svhtcmobile.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.svhtcmobile.Api.ApiClient;
import com.example.svhtcmobile.Api.apiService.IQuanTriService;
import com.example.svhtcmobile.Controller.DanhSachLop;
import com.example.svhtcmobile.Model.He;
import com.example.svhtcmobile.Model.Lop;
import com.example.svhtcmobile.Model.LopDTO;
import com.example.svhtcmobile.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CustomAdapterLop extends ArrayAdapter {
    Context context;
    int resource;
    List<LopDTO> data;
    private SharedPreferences sharedPreferences;
    private IQuanTriService iQuanTriService;
    private int check = 0;
    public CustomAdapterLop(@NonNull Context context, int resource, @NonNull List<LopDTO> data) {
        super(context, resource, data);
        this.context = context;
        this.resource = resource;
        this.data = data;

        sharedPreferences = context.getSharedPreferences("Account", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        String ho = sharedPreferences.getString("ho", "");
        String ten = sharedPreferences.getString("ten", "");
        String username = sharedPreferences.getString("username", "");
        String quyen = sharedPreferences.getString("quyen", "");
        Retrofit retrofit = ApiClient.getClient(token);
        iQuanTriService = retrofit.create(IQuanTriService.class);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource, null); // nap giao dien
        TextView tvMaLop = convertView.findViewById(R.id.tvMaLop);
        TextView tvTenLop = convertView.findViewById(R.id.tvTenLop);
        TextView tvKhoaHoc = convertView.findViewById(R.id.tvKhoaHoc);
        ImageButton imbtnChinhSua = convertView.findViewById(R.id.imbtnChinhSua);
        ImageButton imbtnXoa = convertView.findViewById(R.id.imbtnXoa);
        LopDTO lop = data.get(position);
        tvMaLop.setText(lop.getMaLop());
        tvTenLop.setText(lop.getTenLop());
        tvKhoaHoc.setText(lop.getKhoaHoc());
        imbtnChinhSua.setImageResource(R.drawable.edit);
        imbtnXoa.setImageResource(R.drawable.delete_24px);
        imbtnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogXoaLop(lop);
            }
        });
        imbtnChinhSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogSuaLop(lop);
            }
        });
        return convertView;
    }

    private void openDialogXoaLop(LopDTO lop) {
        final Dialog dialog = new Dialog(this.context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_xoa_lop);

        Window window = dialog.getWindow();
        if(window == null) return;

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAtrributes = window.getAttributes();
        windowAtrributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAtrributes);
        TextView tvTenLop = dialog.findViewById(R.id.tvTenLop);
        Button btnOk = dialog.findViewById(R.id.btnOk);
        Button btnHuy = dialog.findViewById(R.id.btnHuy);

        tvTenLop.setText(lop.getTenLop());

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), lop.getMaLop(), Toast.LENGTH_LONG).show();
               iQuanTriService.xoaLop(lop.getMaLop()).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.code() == 200){
                            dialog.dismiss();
                        } else {
                            Toast.makeText(view.getContext(), "Xóa lớp thất bại!", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable throwable) {

                    }
                });

            }
        });
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setCancelable(true);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                ((DanhSachLop) context).DocDuLieuLop();
            }
        });
        dialog.show();
    }

    private void openDialogSuaLop(LopDTO lop){
        final Dialog dialog = new Dialog(this.context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_sua_lop);

        Window window = dialog.getWindow();
        if(window == null) return;

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAtrributes = window.getAttributes();
        windowAtrributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAtrributes);

        EditText edtMaLop = dialog.findViewById(R.id.edtMaLop);
        EditText edtTenLop = dialog.findViewById(R.id.edtTenLop);
        EditText edtNamBatDau = dialog.findViewById(R.id.edtNamBatDau);
        EditText edtNamKetThuc = dialog.findViewById(R.id.edtNamKetThuc);
        RadioButton rdbDong = dialog.findViewById(R.id.rdbDong);
        RadioButton rdbMo = dialog.findViewById(R.id.rdbMo);
        Button btnHuy = dialog.findViewById(R.id.btnHuy);
        Button btnOk = dialog.findViewById(R.id.btnOk);

        Spinner spHeDaoTao = dialog.findViewById(R.id.spHeDaoTao);
        List<He> danhSachHeDaoTao = new ArrayList<>();

        iQuanTriService.danhSachHeDaoTao().enqueue(new Callback<List<He>>() {
            @Override
            public void onResponse(Call<List<He>> call, Response<List<He>> response) {
                if(response.code() == 200){
                    danhSachHeDaoTao.addAll(response.body());
                    ArrayAdapter adapter_spHeDaoTao = new ArrayAdapter(dialog.getContext(), R.layout.layout_spinner, danhSachHeDaoTao);
                    spHeDaoTao.setAdapter(adapter_spHeDaoTao);
                    int index = 0;
                    for(int i = 0; i < danhSachHeDaoTao.size(); i++){
                        if(danhSachHeDaoTao.get(i).getId() == lop.getIdHe()){
                            index = i;
                            break;
                        }
                    }
                    spHeDaoTao.setSelection(index);
                }
            }

            @Override
            public void onFailure(Call<List<He>> call, Throwable throwable) {

            }
        });

        edtMaLop.setText(lop.getMaLop());
        edtTenLop.setText(lop.getTenLop());
        edtNamBatDau.setText(lop.getKhoaHoc().substring(0, 4));
        edtNamKetThuc.setText(lop.getKhoaHoc().substring(5));
        if(lop.getTrangThai()){ // lop dang mo
            rdbMo.setChecked(true);
            rdbDong.setChecked(false);
        } else { // lop dong
            rdbMo.setChecked(false);
            rdbDong.setChecked(true);
        }

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                check = 0;
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LopDTO tmp = new LopDTO();
                tmp.setMaLop(edtMaLop.getText().toString());
                tmp.setTenLop(edtTenLop.getText().toString());
                tmp.setKhoaHoc(edtNamBatDau.getText().toString() + "-" + edtNamKetThuc.getText().toString());
                tmp.setTrangThai(rdbMo.isChecked() ? true : false);
                for(He he : danhSachHeDaoTao){
                    if(he.getTenHe().equals(spHeDaoTao.getSelectedItem().toString())){
                        tmp.setIdHe(he.getId());
                        break;
                    }
                }
//                Toast.makeText(view.getContext(), "Click xoa " + tmp.getIdHe(), Toast.LENGTH_LONG).show();
                iQuanTriService.capNhatLop(tmp).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.code() == 200){
                            check = 1;
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable throwable) {

                    }
                });

            }
        });
        dialog.setCancelable(true);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if(check == 1)
                    ((DanhSachLop) context).DocDuLieuLop();
            }
        });
        dialog.show();
    }
}
