package com.yoga.kalkulasi;
import android.app.*;
import android.view.Window;
import android.widget.*;
import android.view.*;
import android.content.*;
import com.yoga.kalkulasi.Model.*;
import android.database.*;
import java.text.*;
import java.util.*;

public class KategoriAction extends Dialog
{
    String id_barang = "";

    kategoriHelper myKategori;

    EditText teksNamaKategori;


    String nama_kategori,id_kategori = "";


    public KategoriAction(Context context, String id_kat, final String status)
    {

        super(context);


        myKategori = new kategoriHelper(getContext());

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);
        setContentView(R.layout.activity_tambah_kategori);
        setTitle("Tambah Barang");



        teksNamaKategori = (EditText) findViewById(R.id.teksNamaKategori);

        Button btnSimpan = (Button) findViewById(R.id.btnSimpan);
        Button btnCancel = (Button) findViewById(R.id.btnCancel);




        if (status.equals("edit"))
        {
            //Kurang nggolek idne kode satuan opo
            this.id_kategori = id_kat;

            getKategori(id_kategori);

            teksNamaKategori.setText(nama_kategori);
        }



        btnSimpan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {

                    if(status.equalsIgnoreCase("tambah")){
                        if(myKategori.insertData(teksNamaKategori.getText().toString())){
                            setText(getContext(),"Kategori berhasil ditambah");
                        }else{
                            setText(getContext(),"Gagal menambah kategori");
                        }
                    }else if(status.equalsIgnoreCase("edit")){
                        if(myKategori.updateData(id_kategori,teksNamaKategori.getText().toString())){
                            setText(getContext(),"Kategori berhasil diubah");
                        }else{
                            setText(getContext(),"Gagal merubah kategori");
                        }
                    }
                    
                    dismiss();


                }
            });

        btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    dismiss();
                }
            });
        show();
    }

    public void getKategori(String id_kategori)
    {
        Cursor res = myKategori.getKategoriById(id_kategori);

        if (res != null && res.getCount() > 0)
        {
            while (res.moveToNext())
            {
                nama_kategori = res.getString(1);
                id_kategori = res.getString(0);
            }
        }
    }

    public void setText(Context con, String teks)
    {
        Toast.makeText(con, teks, Toast.LENGTH_SHORT).show();
    }

    /*
     public void readDetail(){
     res = myDetail.getDetailById(id_detail);

     if(res!=null && res.getCount()>0){
     while (res.moveToNext()){ 
     id_var = res.getString(1);
     value = res.getString(2);

     setText(getContext(),res.getString(0)+" - "+res.getString(1)+" - "+res.getString(2));
     }
     }else{

     }
     } 

     public void readVar(){
     res = myVar.getVariableById(id_var);

     if(res!=null && res.getCount()>0){
     while (res.moveToNext()){ 
     nama = res.getString(1);

     }
     }else{

     }
     }*/


}
    
