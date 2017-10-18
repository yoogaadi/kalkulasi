package com.yoga.kalkulasi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.*;
import android.os.*;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.database.*;
import com.yoga.kalkulasi.Model.*;
import android.widget.Toast;
import com.yoga.kalkulasi.Control.*;
import java.text.*;
import java.util.*;

import com.yoga.kalkulasi.Control.*;
import com.yoga.kalkulasi.Tutorial.*;

public class KalkulasiFragmentEmpat extends Fragment
{
    kalkulasiHelper myDb;
    barangHelper myBarang;
    detailHargaBarangHelper myHarga;
    detailItemHelper myDetail;
    detailKalkulasiHelper myKal;
    satuanHelper mySatuan;
	
	NumberControl nc;
	


    ListView lv;
    MyAdapterOptionDetail adapter;
	
    String[] nama_barang,kode_barang, harga_barang, qty_barang,satuan_item= {"1","2"};
    int dataTotal = 0;
    String id_detail_kalkulasi;
    String id_kalkulasi = "";

    Cursor res,bar,det;
    View view;
    String kd_barang;
	

    public KalkulasiFragmentEmpat()
    {

    }

    public KalkulasiFragmentEmpat(String id_kal)
    {
        id_detail_kalkulasi = id_kal;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        // TODO: Implement this method
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // TODO: Implement this method
        
        try{
            
        
        view = inflater.inflate(R.layout.kalkulasi_fragment_4, container, false);
        myDb = new kalkulasiHelper(getContext());
        myDetail = new detailItemHelper(getContext());
        myHarga = new detailHargaBarangHelper(getContext());
        myBarang = new barangHelper(getContext());
        myKal = new detailKalkulasiHelper(getContext());
		mySatuan = new satuanHelper(getContext());
		
        nc = new NumberControl();
        
        lv = (ListView) view.findViewById(R.id.listItem);
        loadData();
        }catch(Exception e){
            setText(e.toString());
        }
		
        return view;
    }

    public void loadData()
    {
        id_kalkulasi = myKal.getIdKalkulasi(id_detail_kalkulasi);
        readDetail();
        //setText(id_kalkulasi);
        if (dataTotal > 0)
        {

            adapter = new MyAdapterOptionDetail(getContext(), nama_barang, harga_barang);
            lv.setAdapter(adapter);
            registerForContextMenu(lv);

        }
        else
        {
            lv.setVisibility(view.INVISIBLE);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menus, View v2, ContextMenu.ContextMenuInfo menuInfo2)
    {
        // TODO: Implement this method
        super.onCreateContextMenu(menus, v2, menuInfo2);
        menus.setHeaderTitle("Select the Action");
        menus.add(0, v2.getId(), 0, "Hapus Item");

    }


    @Override
    public boolean onContextItemSelected(MenuItem items)
    {
        AdapterView.AdapterContextMenuInfo info;
        // TODO: Implement this method
        if (items.getTitle().toString().equals("Hapus Item"))
        {
            info = (AdapterView.AdapterContextMenuInfo) items.getMenuInfo();

            kd_barang = kode_barang[info.position];
            //id_var = arr_id[info.position];
            ShowDialog();
        }
        return super.onContextItemSelected(items);

    }

    public void setText(String teks)
    {
        Toast.makeText(getContext(), teks, Toast.LENGTH_SHORT).show();
    }

    public void readDetail(){
        det = myDetail.getDetailByDetailKalkulasi(id_detail_kalkulasi);
        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer idBuffer = new StringBuffer();
        StringBuffer hargaBuffer = new StringBuffer();
       
        if(det != null && det.getCount() >0){
            while(det.moveToNext()){
                stringBuffer.append(myBarang.getNamaBarang(det.getString(1))+" ("+ det.getString(2)+" "+mySatuan.getNamaSatuan(det.getString(3))+ ")=");
                idBuffer.append(det.getString(1) + ",");
               
                double hg = Double.parseDouble(myHarga.getHargaBarang(det.getString(1)));
                double qt = Double.parseDouble(det.getString(2));
                double besaran = Double.parseDouble(myBarang.getBesaran(det.getString(1)));
               
                double total = ((hg / besaran) * qt);
                hargaBuffer.append(nc.getNumberFormat(total)+"=");
            }
        }
        dataTotal = det.getCount();
        nama_barang = stringBuffer.toString().split("=");
        kode_barang = idBuffer.toString().split(",");
        harga_barang = hargaBuffer.toString().split("=");
    }
 
  
    public void hapus()
    {
        myDetail.deleteData(id_detail_kalkulasi, kd_barang);
        setText("Data telah dihapus");
    }

    public void ShowDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);
        builder.setTitle("Hapus");
        builder.setMessage("Anda yakin akan menghapus data ini?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id)
                {
                    hapus();
                    loadData();
                }
            })
            .setNegativeButton("Cancel ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {

                    return;

                }
            });

        builder.create().show();
    }

}


