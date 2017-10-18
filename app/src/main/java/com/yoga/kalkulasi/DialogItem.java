

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
import android.widget.AdapterView.*;
import com.yoga.kalkulasi.Tutorial.*;


public class DialogItem extends Dialog implements SearchView.OnQueryTextListener
{ 


    barangHelper myItem;
    detailHargaBarangHelper myDetHarga;
    hargaBarangHelper myHarga;
    satuanHelper mySatuan;
    kategoriHelper myKategori;

    ListView listKal;

    ImageButton btnTambah;

    LinearLayout info,konten;


    ArrayAdapter<String> adapter;
    String[] arr = {"1","2","22","312"};
    String[] nama_barang, id_barang,harga_barang,satuan_barang = {};
    String[] id_sub = {};
    NumberControl nc;

    Spinner spinKategori;

    TextView tv;
    Button btnSimpan,btnCancel;
    Cursor res,get,sat,har;
    String kode_satuan;
    int totalData = 0;
    String id_project;

    String selectedBarang ="";
    String selectedNamaBarang ="";
    String selectedSatuan = "";
    String selectIdKategori = "-";

    String[] id_kategori,nama_kategori;

    boolean flag = false;

    String[] listBarang = {};
	

    public DialogItem(Context context, String[] list)
    {

        super(context);

        myItem = new barangHelper(getContext());
        myDetHarga = new detailHargaBarangHelper(getContext());
        myHarga = new hargaBarangHelper(getContext());
        mySatuan = new satuanHelper(getContext());
        myKategori = new kategoriHelper(getContext());

        listBarang = list;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);
        setContentView(R.layout.dialog_item);
        setTitle("Tambah Barang");
        info = (LinearLayout) findViewById(R.id.info);
        konten = (LinearLayout) findViewById(R.id.konten);
		
		

        nc = new NumberControl();
        btnTambah = (ImageButton) findViewById(R.id.btnTambah);

        getKategori();

        spinKategori = (Spinner) findViewById(R.id.spinKategori);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, nama_kategori);
        spinKategori.setAdapter(adapter2);
        spinKategori.setOnItemSelectedListener(new OnItemSelectedListener(){

                @Override
                public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4)
                {
                    selectIdKategori = id_kategori[p3];
                    loadData();
                }

                @Override
                public void onNothingSelected(AdapterView<?> p1)
                {
                    // TODO: Implement this method
                }


            });


        loadData();
        btnTambah.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View p1)
                {
                    final DialogBarangAction di = new DialogBarangAction(getContext(), "", "tambah");

                    di.setOnDismissListener(new DialogInterface.OnDismissListener(){
                            @Override
                            public void onDismiss(DialogInterface p1)
                            {
                                if (di.getStatus())
                                {
                                    loadData();
                                    setText(getContext(), "Pricelist telah ditambahkan");
                                }
                                else
                                {

                                }
                            }
                        });
                }            
            });

        show();

    }



    public void setText(Context con, String teks)
    {
        Toast.makeText(con, teks, Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onQueryTextSubmit(String p1)
    {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String p1)
    {
        String newText = p1;
        adapter.getFilter().filter(newText);
        return false;
    }



    public void loadData()
    {
        getitem(selectIdKategori);

        if (totalData > 0)
        {
            
            konten.setVisibility(LinearLayout.VISIBLE);
            info.setVisibility(LinearLayout.GONE);
            listKal = (ListView) findViewById(R.id.listItem);
            adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, nama_barang);
            listKal.setAdapter(adapter);
            listKal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
                    {
                        for (int x = 0; x < nama_barang.length;x++)
                        {
                            if (adapter.getItem(i).equals(nama_barang[x]))
                            {

                                setNamaBarang(nama_barang[x]);
                                setIdBarang(id_barang[x]);
                                setSatuanBarang(satuan_barang[x]);

                            }
                        }
                        setStatus(true);
                        dismiss();
                    }
                });
        }
        else
        {
            
            konten.setVisibility(LinearLayout.GONE);
            info.setVisibility(LinearLayout.VISIBLE);
        }


    }

    public void getitem(String id_kategori)
    {
        if (id_kategori.equalsIgnoreCase("-"))
        {
            res = myItem.getAllData();
        }
        else
        {
            res = myItem.getBarangByKategori(id_kategori);
        }

        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer id_buffer = new StringBuffer();
        StringBuffer satuan_brg = new StringBuffer();

        if (res != null && res.getCount() > 0)
        {
            while (res.moveToNext())
            {
                id_buffer.append(res.getString(0) + ",");
                stringBuffer.append(res.getString(1) + " (" + res.getString(3) + " " + mySatuan.getNamaSatuan(myHarga.getKodeSatuan(res.getString(0))) + ") - " + nc.getNumberFormat(Double.parseDouble(myDetHarga.getHargaBarang(res.getString(0)))) + "=");
                satuan_brg.append(myHarga.getKodeSatuan(res.getString(0)) + ",");
            }
        }
        totalData = res.getCount();
        
        nama_barang = stringBuffer.toString().split("=");
        id_barang = id_buffer.toString().split(",");
        satuan_barang = satuan_brg.toString().split(",");
    }

    public void getKategori()
    {
        res = myKategori.getAllData();
        int a = 0;
        StringBuffer nama = new StringBuffer();
        StringBuffer id = new StringBuffer();
        if (res != null && res.getCount() > 0)
        {
            while (res.moveToNext())
            {
                if (a == 0)
                {
                    nama.append("Tampilkan semua,");
                    id.append("-,");
                }
                nama.append(res.getString(1) + ",");
                id.append(res.getString(0) + ",");
                a++;
            }
        }
        id_kategori = id.toString().split(",");
        nama_kategori = nama.toString().split(",");
    }
    
    

    public void setIdBarang(String id_barang)
    {
        selectedBarang = id_barang;

    }
    public String getIdBarang()
    {
        return selectedBarang;
    }

    public void setNamaBarang(String nama_barang)
    {
        selectedNamaBarang = nama_barang;

    }
    public String getNamaBarang()
    {
        return selectedNamaBarang;
    }

    public void setSatuanBarang(String satuan)
    {
        this.selectedSatuan = satuan;
    }
    public String getSatuanBarang()
    {
        return this.selectedSatuan;
    }
    public void setStatus(boolean stat)
    {
        this.flag = stat;
    }
    public boolean getStatus()
    {
        return this.flag;
    }

}

