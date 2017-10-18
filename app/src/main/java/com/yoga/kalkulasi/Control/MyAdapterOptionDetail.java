package com.yoga.kalkulasi.Control;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.yoga.kalkulasi.R;

public class MyAdapterOptionDetail extends ArrayAdapter {
    String[] nama;
    String[] detail;


    public MyAdapterOptionDetail(Context context, String[] nama_param, String[] detail_param){
        super(context, R.layout.list_option_detail,R.id.teks,nama_param);
        this.nama = nama_param;
        this.detail= detail_param;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.list_option_detail,parent,false);
      
        TextView nama_ = (TextView) row.findViewById(R.id.teks);
        TextView detail_ = (TextView) row.findViewById(R.id.satuan);
        nama_.setText(nama[position]);
        detail_.setText(detail[position]);
        
        return row;
    }
}
