package com.yoga.kalkulasi.Control;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.yoga.kalkulasi.R;

public class MyDetailAdapter extends ArrayAdapter {
    String[] nama_barang;
    String[] harga_barang;
		String[] besaran_barang;


    public MyDetailAdapter(Context context, String[] nama_barang1, String[] harga_barang1,String[] besaran_barang1){
        super(context, R.layout.list_detail_kalkulasi,R.id.namaBarang,nama_barang1);
        this.nama_barang = nama_barang1;
        this.harga_barang = harga_barang1;
				this.besaran_barang = besaran_barang1;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.list_detail_kalkulasi,parent,false);
        TextView namaBarang = (TextView) row.findViewById(R.id.namaBarang);
        TextView hargaBarang = (TextView) row.findViewById(R.id.hargaBarang);
				TextView qtyBarang = (TextView) row.findViewById(R.id.qtyBarang);
				
        namaBarang.setText(nama_barang[position]);
        hargaBarang.setText(harga_barang[position]);
				qtyBarang.setText(besaran_barang[position]);
				

        return row;
    }
}
