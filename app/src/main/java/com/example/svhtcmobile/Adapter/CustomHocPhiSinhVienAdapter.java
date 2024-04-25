package com.example.svhtcmobile.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.svhtcmobile.R;
import com.example.svhtcmobile.Model.HocPhiSinhVien;

import java.util.List;

public class CustomHocPhiSinhVienAdapter extends ArrayAdapter {
    Context context;
    int resource;
    List<HocPhiSinhVien> data;
    public CustomHocPhiSinhVienAdapter(@NonNull Context context, int resource, List<HocPhiSinhVien> data) {
        super(context, resource, data);
        this.context = context;
        this.resource = resource;
        this.data = data;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource, null);
        TextView tvNienKhoa= convertView.findViewById(R.id.tvNienKhoa);
        TextView tvHocKy = convertView.findViewById(R.id.tvHocKy);
        TextView tvHocPhi = convertView.findViewById(R.id.tvHocPhi);
        TextView tvDaDong = convertView.findViewById(R.id.tvDaDong);
        TextView tvNo=convertView.findViewById(R.id.tvNo);

        HocPhiSinhVien hp = data.get(position);
        tvNienKhoa.setText(hp.getNienKhoa());
        tvHocKy.setText(String.valueOf(hp.getHocKy()));
        tvHocPhi.setText(String.valueOf(hp.getHocPhi()));
        tvDaDong.setText(String.valueOf(hp.getDaDong()));
        tvNo.setText(String.valueOf(hp.getNo()));
        return convertView;
    }
}
