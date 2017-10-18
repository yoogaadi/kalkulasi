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
import com.yoga.kalkulasi.Control.*;
import android.view.View.*;
import com.yoga.kalkulasi.Tutorial.*;

public class DialogDetailListProject extends Dialog
{
    kalkulasiHelper myDb;
    barangHelper myBarang;
    detailHargaBarangHelper myHarga;
    detailItemHelper myDetail;
    satuanHelper mySatuan;
    detailKalkulasiHelper myDetKal;
    variabelHelper myVar;
    detailVariabelHelper myDetVar;
	
    ListView listItem;
	
    Button btnClose;
	
	String id_project = "-";
	
	String[] kodeBarang,barang = {};
	

    


    public DialogDetailListProject(Context context, String id_proj)
    {

        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);
        setContentView(R.layout.dialog_list_item_project);
        setTitle("Tambah Barang");

		id_project = id_proj;
      

        myDb = new kalkulasiHelper(getContext());
        myDetail = new detailItemHelper(getContext());
        myHarga = new detailHargaBarangHelper(getContext());
        myBarang = new barangHelper(getContext());
        mySatuan = new satuanHelper(getContext());
        myDetKal = new detailKalkulasiHelper(getContext());
        myVar = new variabelHelper(getContext());
        myDetVar = new detailVariabelHelper(getContext());
        

      

        btnClose = (Button) findViewById(R.id.btnClose);

      
        loadData();

        btnClose.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View p1)
                {

                    
                    dismiss();


                }


            });
     
        show();
    }


  
    public void loadData()
    {
		
        readData(id_project);
		listItem = (ListView) findViewById(R.id.listItem);
		ArrayAdapter adapter = new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1, barang);
		listItem.setAdapter(adapter);

       


    }
    public void setText(String teks)
    {
        Toast.makeText(getContext(), teks, Toast.LENGTH_SHORT).show();
    }

    


    public void readData(String id_project)
    {
        Cursor res = myDetKal.getDetailByProject(id_project);
		StringBuffer ss = new StringBuffer();
        if (res != null && res.getCount() > 0)
        {
            while (res.moveToNext())
            { 
               	ss.append(readItem(res.getString(0)));
            }
        }
		
		barang = ss.toString().split("=");
    }
	
	public StringBuffer readItem(String id_detail_kalkulasi){
		Cursor get = myDetail.getDetailByDetailKalkulasi(id_detail_kalkulasi);
		StringBuffer sb = new StringBuffer();
		if(get!=null && get.getCount()>0){
			while(get.moveToNext()){
				sb.append(myBarang.getNamaBarang(get.getString(1))+"=");
			}
		}
		
		return sb;
	}
	
	public void getItem(){
		Cursor get = myBarang.getAllData();
		StringBuffer kode_barang = new StringBuffer();
		if(get!=null && get.getCount()>0){
			while(get.moveToNext()){
				kode_barang.append(get.getString(0)+"=");
			}
		}
		kodeBarang = kode_barang.toString().split("=");
	}
}
    
