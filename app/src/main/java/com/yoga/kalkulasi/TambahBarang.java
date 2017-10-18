package com.yoga.kalkulasi;
import android.app.*;
import android.support.annotation.NonNull;
import android.view.Window;
import android.widget.*;
import android.view.*;
import android.content.*;
import com.yoga.kalkulasi.Model.*;
import android.database.*;
import java.text.*;
import java.util.*;
import com.yoga.kalkulasi.Control.*;
import android.text.*;
import com.yoga.kalkulasi.Tutorial.*;
import java.io.*;


public class TambahBarang extends Dialog
{

    barangHelper myDb;
    hargaBarangHelper myHarga;
    detailHargaBarangHelper myDetail;
    satuanHelper mySatuan;
    kategoriHelper myKategori;
    kalkulasiHelper myKalkulasi;
    detailKalkulasiHelper myDetailKalkulasi;

    detailItemHelper myItem;

    Spinner listSatuan;
    Spinner spinKategori;
    String[] nama_barang = {"barang 1","barang 2","barang 3"};
    String[] harga_barang = {"1000","2000","3000"};
    String[] keteranganBarang = {""};
    String[] nama_kategori,id_kategori = {""};
    String[] id_barang,id_satuan,nama_satuan = {""};
    EditText qty;

    String id_brg, nama_brg = "";

    String id_kalkulasi = "";



    TextView tv;
    Button btnSimpan,btnCancel;
    Cursor res,get,sat,har;
    String kode_satuan;
    int totalData = 0;

    String selectedBarang = "";
    String selectedNamaBarang = "";

    EditText teksNamaBarang;
    Button btnCariBarang;
    String id_project = "";
    String id_detail_kalkulasi = "";
    String[] listBarang = {""};
	boolean isFirstTime = false;

    public TambahBarang(Context context, String[] id_selected_barang, String id_detail_kalkulasi1, String id_project1)
    {

        super(context);

        id_detail_kalkulasi = id_detail_kalkulasi1;
        id_project = id_project1;

        myDb = new barangHelper(getContext());
        myHarga = new hargaBarangHelper(getContext());
        myDetail = new detailHargaBarangHelper(getContext());
        myItem = new detailItemHelper(getContext());
        mySatuan = new satuanHelper(getContext());
        myKategori = new kategoriHelper(getContext());
        myKalkulasi = new kalkulasiHelper(getContext());
        myDetailKalkulasi = new detailKalkulasiHelper(getContext());

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);
        setContentView(R.layout.activity_tambah_barang);
        setTitle("Tambah Barang");

        getIdKalkulasi();
		

        tv = (TextView) findViewById(R.id.textHasil);
        btnSimpan = (Button) findViewById(R.id.btnSimpan);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCariBarang = (Button) findViewById(R.id.btnCariBarang);


        getKategori();
        getBarang();
        readSatuan();

        qty = (EditText) findViewById(R.id.textQty);
        teksNamaBarang = (EditText) findViewById(R.id.teksNamaBarang);

        listSatuan = (Spinner) findViewById(R.id.listSatuan);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, nama_satuan);
        listSatuan.setAdapter(adapter2);

        getListBarang();

        btnCariBarang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    final DialogItem di = new DialogItem(getContext(), listBarang);

                    di.setOnDismissListener(new DialogInterface.OnDismissListener(){
                            @Override
                            public void onDismiss(DialogInterface p1)
                            {

                                if (di.getStatus())
                                {
                                    selectedBarang = di.getIdBarang();
                                    selectedNamaBarang = di.getNamaBarang();
                                    teksNamaBarang.setText(selectedNamaBarang);

                                    listSatuan.setSelection(Integer.parseInt(di.getSatuanBarang()) - 1);
                                }

                            }
                        });
                }
            });
        btnSimpan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    try
                    {
                        boolean flag = false;
                        if (!selectedBarang.equals("") || !qty.getText().toString().equals(""))
                        {

                            flag = myItem.insertData(id_detail_kalkulasi, selectedBarang, qty.getText().toString(), id_satuan[listSatuan.getSelectedItemPosition()]);

                            if (flag)
                            {
								
								if(getStatusTutorial()){
									TutorialEnam te = new TutorialEnam(getContext());
								}
                                setText(getContext(), "Berhasil");
                            }
                            else
                            {
                                setText(getContext(), "Nhggak Berhasil");
                            }
                            dismiss();
                        }
                        else
                        {
                            setText(getContext(), "Harap semua diisi");
                        }
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
                    }


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
		if(getStatusTutorial()){
			TutorialLima tl = new TutorialLima(getContext());
		}
		
		
    }

    public void getListBarang()
    {
        Cursor get = myItem.getDetailByDetailKalkulasi(id_detail_kalkulasi);
        StringBuffer sb = new StringBuffer();
        if (get != null && get.getCount() > 0)
        {
            while (get.moveToNext())
            {
                sb.append(get.getString(1) + ",");
            }
            listBarang = sb.toString().split(",");
        }

    }


    public void setText(Context con, String teks)
    {
        Toast.makeText(con, teks, Toast.LENGTH_SHORT).show();
    }

    public void getKategori()
    {
        Cursor res = myKategori.getAllData();
        StringBuffer nama = new StringBuffer();
        StringBuffer id = new StringBuffer();
        if (res != null && res.getCount() > 0)
        {
            while (res.moveToNext())
            {
                nama.append(res.getString(1) + ",");
                id.append(res.getString(0) + ",");
            }
        }
        nama_kategori = nama.toString().split(",");
        id_kategori = id.toString().split(",");
    }

    public void getIdKalkulasi()
    {
        Cursor get = myDetailKalkulasi.getDetailById(id_detail_kalkulasi);
        if (get != null && get.getCount() > 0)
        {
            while (get.moveToNext())
            {
                id_kalkulasi  = get.getString(1);
            }

        }
    }


    public void getBarang()
    {

        String id, tes = "";
        res = myDb.getAllData();
        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer id_buffer = new StringBuffer();
        StringBuffer harga = new StringBuffer();
        StringBuffer ket = new StringBuffer();
        if (res != null && res.getCount() > 0)
        {
            while (res.moveToNext())
            {
                harga.append(getHargaBarang(res.getString(0)) + ",");
                stringBuffer.append(res.getString(1) + " - ");
                stringBuffer.append("(" + res.getString(3) + " ");
                stringBuffer.append(getSatuanBarang(res.getString(0)) + ") - ");

                id_buffer.append(res.getString(0) + ",");

                stringBuffer.append(getHargaBarang(res.getString(0)) + ",");

            }
        }
        totalData = res.getCount();
        //setText(getContext(), stringBuffer.toString());
        nama_barang = stringBuffer.toString().split(",");
        id_barang = id_buffer.toString().split(",");
        harga_barang = harga.toString().split(",");
        //	keteranganBarang = ket.toString().split(",");
    }

    public String getHargaBarang(String kodeBarang)
    {
        har = myDetail.getHargaBarangTerakhir(kodeBarang);
        String harga= "";
        if (res != null && har.getCount() > 0)
        {
            while (har.moveToNext())
            {
                harga = har.getString(3);
            }
            //Toast.makeText(getContext(),"ada",Toast.LENGTH_SHORT).show();

        }
        return harga;
    }
    public String getSatuanBarang(String kodeBarang)
    {
        get = myHarga.getDataByKodeBarang(kodeBarang);
        String satuan= "";
        if (get != null && get.getCount() > 0)
        {
            while (get.moveToNext())
            {
                satuan = getNamaSatuan(get.getString(2));
            }
            //Toast.makeText(getContext(),"ada",Toast.LENGTH_SHORT).show();

        }
        return satuan;
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

    public String getNamaSatuan(String kodeSatuan)
    {
        sat = mySatuan.getSatuanById(kodeSatuan);
        String namaSatuan = "";
        if (sat != null && sat.getCount() > 0)
        {
            while (sat.moveToNext())
            { 

                namaSatuan = sat.getString(1);
            }
        }
        return namaSatuan;

    }
	
	public boolean getStatusTutorial(){

        boolean flag = true;
        try
        {
			File file = new File(getContext().getCacheDir() , "Tutorial");
            if (file.canRead())
            {
                flag = false;
            }
            else
            {
                flag = true;
            }
        }catch(Exception e){

		}

        return flag;
    }
}

