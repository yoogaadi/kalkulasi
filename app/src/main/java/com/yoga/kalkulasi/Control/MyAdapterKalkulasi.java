package com.yoga.kalkulasi.Control;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.yoga.kalkulasi.R;

public class MyAdapterKalkulasi extends ArrayAdapter {
    String[] nama_kalkulasi;
    String[] total_kalkulasi;


    public MyAdapterKalkulasi(Context context, String[] nama_kalkulasi1, String[] total_kalkulasi1){
        super(context, R.layout.list_kalkulasi,R.id.namaKalkulasi,nama_kalkulasi1);
        this.nama_kalkulasi = nama_kalkulasi1;
        this.total_kalkulasi= total_kalkulasi1;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.list_kalkulasi,parent,false);
        TextView namaKalkulasi = (TextView) row.findViewById(R.id.namaKalkulasi);
        TextView totalKalkulasi = (TextView) row.findViewById(R.id.totalKalkulasi);

        namaKalkulasi.setText(nama_kalkulasi[position]);
        totalKalkulasi.setText(total_kalkulasi[position]);
      
        return row;
    }
}
