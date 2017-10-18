package com.yoga.kalkulasi.Control;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.yoga.kalkulasi.R;

public class MyAdapterProject extends ArrayAdapter {
    String[] nama_item;

    public MyAdapterProject(Context context, String[] nama_item1){
        super(context, R.layout.list_project,R.id.namaProject,nama_item1);
        this.nama_item = nama_item1;

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.list_project,parent,false);
        TextView namaProject = (TextView) row.findViewById(R.id.namaProject);

        namaProject.setText(nama_item[position]);


        return row;
    }
}
