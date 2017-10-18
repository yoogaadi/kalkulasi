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
import android.text.*;
import com.yoga.kalkulasi.Tutorial.*;

public class DialogBarangAction extends Dialog
{
    String id_barang = "";
    barangHelper myDb;
    hargaBarangHelper myHarga;
    detailHargaBarangHelper myDetail;
    satuanHelper mySatuan;
    kategoriHelper myKategori;


    EditText kodeBarang,hargaBarang,besaranBarang,namaBarang,deskripsi;
    Spinner kodeSatuan;
    Spinner spinKategori;
    Cursor res;
    String id_var, nama, value;
    String[] nama_satuan,id_satuan;
    String kode = "1";
    String tanggalSekarang;
    boolean flag= false;
    String[] nama_kategori,id_kategori = {""};
	

    String kode_barang,kode_satuan,nama_barang,kategori,keterangan,tanggal,harga,besaran = "";

    public DialogBarangAction(Context context, String id_bar, final String status)
    {

        super(context);
        id_barang = id_bar;

        myDb = new barangHelper(getContext());
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        tanggalSekarang = dateFormat.format(date);

        myHarga = new hargaBarangHelper(getContext());
        myDetail = new detailHargaBarangHelper(getContext());
        mySatuan = new satuanHelper(getContext());
        myKategori = new kategoriHelper(getContext());

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);
        setContentView(R.layout.barang_action);
        setTitle("Tambah Barang");
        
        getKategori();

        readSatuan();
        
        Button btnSimpan = (Button) findViewById(R.id.btnSimpan);
        Button btnCancel = (Button) findViewById(R.id.btnCancel);
        kodeBarang = (EditText) findViewById(R.id.kodeBarang);
        
        kodeSatuan = (Spinner) findViewById(R.id.satuanBarang);
        spinKategori = (Spinner) findViewById(R.id.spinnerKategori);
        
        hargaBarang = (EditText) findViewById(R.id.hargaBarang);
        besaranBarang = (EditText) findViewById(R.id.besaranBarang);
        namaBarang = (EditText) findViewById(R.id.namaBarang);
        deskripsi = (EditText) findViewById(R.id.deskripsi);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, nama_satuan);
        kodeSatuan.setAdapter(adapter);		
        
        spinKategori = (Spinner) findViewById(R.id.spinnerKategori);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, nama_kategori);
        spinKategori.setAdapter(adapter2);
		
		


        if (status.equals("edit"))
        {
            //Kurang nggolek idne kode satuan opo

            readBarang();
            readHarga();
            readDetail();		
            
            setText(getContext(),kategori);
            
            kodeBarang.setText(id_barang);
            spinKategori.setSelection(getPosKategori(kategori));
            kodeSatuan.setSelection(getPosSatuan(kode_satuan));
            hargaBarang.setText(harga);
            besaranBarang.setText(besaran);
            namaBarang.setText(nama_barang);
            deskripsi.setText(keterangan);
        }
        

        btnSimpan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {

                    boolean flg = false;
                    //setText(dialog.getContext(),namaVariabel.getText().toString()+" / "+id_detail);

                    if (namaBarang.getText().toString().equals(""))
                    {
                        setText(getContext(), "Harus diisi");
                    }
                    else
                    {
                        if (status.equalsIgnoreCase("tambah"))
                        {
                            if (myDb.insertData(kodeBarang.getText().toString(), namaBarang.getText().toString(), id_kategori[spinKategori.getSelectedItemPosition()], besaranBarang.getText().toString()))
                            {
                                flg = true;
                                
                                if (myHarga.insertData(kode, kodeBarang.getText().toString(), id_satuan[kodeSatuan.getSelectedItemPosition()], deskripsi.getText().toString()))
                                {
                                    flg = true;
                                    if (myDetail.insertData(tanggalSekarang, kode, kodeBarang.getText().toString(), hargaBarang.getText().toString()))
                                    {
										
                                        flg = true;
                                    }
                                    else
                                    {
                                        flg = false;
                                    }	
                                }
                                else
                                {
                                    flg = false;
                                }	
                            }
                            else
                            {
                                setText(getContext(),"Terjadi kesalahan saat menyimpan data, Nama tidak boleh sama");
                                flg = false;
                            }	 
                        }
                        else if (status.equalsIgnoreCase("edit"))
                        {
                            if (myDb.updateData(id_barang, namaBarang.getText().toString(), id_kategori[spinKategori.getSelectedItemPosition()], besaranBarang.getText().toString()))
                            {
                                flg = true;          

                                if (myHarga.updateData(kode, id_barang, id_satuan[kodeSatuan.getSelectedItemPosition()], deskripsi.getText().toString()))
                                {
                                    flg = true;
                                    if (tanggal.equalsIgnoreCase(tanggalSekarang))
                                    {
                                        if (myDetail.updateHarga(id_barang, tanggalSekarang, hargaBarang.getText().toString()))
                                        {
                                            flg = true;
                                        }
                                        else
                                        {
                                            flg = false;
                                        }
                                    }
                                    else
                                    {

                                        if (myDetail.insertData(tanggalSekarang, kode, id_barang, hargaBarang.getText().toString()))
                                        {
                                            flg = true;
                                        }
                                        else
                                        {
                                            flg = false;
                                        }
                                    }
                                }
                                else
                                {
                                    flg = false;
                                }

                            }
                            else
                            {
                                flg = false;
                            }
                        }
                        setStatus(flg);
                        dismiss();
						
                    }


                }
            });

        btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    setStatus(false);
                    dismiss();
                }
            });
        show();
    }
    
    public void setStatus(boolean flag){
        this.flag = flag;
        
    }
    public boolean getStatus(){
        return this.flag;
    }
    
    public void getKategori(){
        Cursor res = myKategori.getAllData();
        StringBuffer nama = new StringBuffer();
        StringBuffer id = new StringBuffer();
        if(res!=null && res.getCount() > 0){
            while(res.moveToNext()){
                nama.append(res.getString(1)+",");
                id.append(res.getString(0)+",");
            }
        }
        nama_kategori = nama.toString().split(",");
        id_kategori = id.toString().split(",");
    }

    public void setText(Context con, String teks)
    {
        Toast.makeText(con, teks, Toast.LENGTH_SHORT).show();
    }

    public int getPosSatuan(String id_satuan)
    {
        res = mySatuan.getAllData();
        int i = 0;
        if (res != null && res.getCount() > 0)
        {
            while (res.moveToNext())
            { 
                if (res.getString(0).equals(kode_satuan))
                {

                    break;
                }
                i++;
            }
        }
        return i;
    }
    
    public int getPosKategori(String id_kategori)
    {
        res = myKategori.getAllData();
        int i = 0;
        if (res != null && res.getCount() > 0)
        {
            while (res.moveToNext())
            { 
                if (res.getString(0).equals(id_kategori))
                {
                    break;
                }
                i++;
            }
        }
        return i;
    }
    public void readSatuan()
    {
        res = mySatuan.getAllData();
        StringBuffer nama = new StringBuffer();
        StringBuffer id = new StringBuffer();
        if (res != null && res.getCount() > 0)
        {
            while (res.moveToNext())
            { 
                id.append(res.getString(0) + ",");
                nama.append(res.getString(1) + ",");
            }

        }
        id_satuan = id.toString().split(",");
        nama_satuan = nama.toString().split(",");
    }

    public void readBarang()
    {
        res = myDb.getBarangById(id_barang);

        if (res != null && res.getCount() > 0)
        {
            while (res.moveToNext())
            { 
                nama_barang = res.getString(1);
                kategori = res.getString(2);
                besaran = res.getString(3);
            }
        }
        else
        {

        }
    }
    public void readHarga()
    {
        res = myHarga.getDataByKodeBarang(id_barang);

        if (res != null && res.getCount() > 0)
        {
            while (res.moveToNext())
            { 
                kode_satuan = res.getString(2);
                keterangan = res.getString(8);

            }
        }
        else
        {

        }
    }
    public void readDetail()
    {
        res = myDetail.getHargaBarangTerakhir(id_barang);
        if (res != null && res.getCount() > 0)
        {
            while (res.moveToNext())
            { 
                tanggal = res.getString(0);
                harga = res.getString(3);

            }
        }
        else
        {

        }
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
