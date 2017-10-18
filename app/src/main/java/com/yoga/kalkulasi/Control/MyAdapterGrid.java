package com.yoga.kalkulasi.Control;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.yoga.kalkulasi.R;

public class MyAdapterGrid extends ArrayAdapter {
    String[] nama;
    String[] deskripsi;


    public MyAdapterGrid(Context context, String[] nama_1, String[] deskripsi_1){
        super(context, R.layout.list_grid_view ,R.id.namaBarang,nama_1);
        this.nama = nama_1;
        this.deskripsi = deskripsi_1;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.list_grid_view,parent,false);
        TextView nama_ = (TextView) row.findViewById(R.id.teksJudul);
        TextView deskripsi_ = (TextView) row.findViewById(R.id.teksDeskripsi);

        nama_.setText(nama[position]);
        deskripsi_.setText(deskripsi[position]);

        return row;
    }
}
