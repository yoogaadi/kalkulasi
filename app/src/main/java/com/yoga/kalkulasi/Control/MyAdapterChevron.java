package com.yoga.kalkulasi.Control;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.yoga.kalkulasi.R;

public class MyAdapterChevron extends ArrayAdapter {
    String[] nama_barang;
  
    public MyAdapterChevron(Context context, String[] nama_barang1){
        super(context, R.layout.list_chevron,R.id.namaBarang,nama_barang1);
        this.nama_barang = nama_barang1;
        
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.list_chevron,parent,false);
        TextView namaBarang = (TextView) row.findViewById(R.id.teks);
        
        namaBarang.setText(nama_barang[position]);
     

        return row;
    }
}
