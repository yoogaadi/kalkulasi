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

public class DialogTambahKalkulasi extends Dialog
{


    kalkulasiHelper myKalkulasi;
    detailKalkulasiHelper myDetailKalkulasi;
    

    EditText namaKalkulasi, deskripsiKalkulasi;
    CheckBox checkGlobal;
    Button btnSimpan,btnCancel;
    String id_project = "-";
    String id_kalkulasi_up = "";
    
    String global = "0";
    


    String[] id_tool,nama_tool;   
    
    public DialogTambahKalkulasi(Context context,String id_project1)
    {

        super(context);
        myKalkulasi = new kalkulasiHelper(getContext());
        myDetailKalkulasi = new detailKalkulasiHelper(getContext());
        
        id_project = id_project1;
        
       
      
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);
        setContentView(R.layout.dialog_tambah_kalkulasi);
        setTitle("Tambah Barang");
        
        btnSimpan = (Button) findViewById(R.id.btnSimpan);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        namaKalkulasi = (EditText) findViewById(R.id.namaKalkulasi);
        deskripsiKalkulasi = (EditText) findViewById(R.id.deskripsiKalkulasi);
      
        
        btnSimpan.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View p1)
                {
                    
                    if(checkGlobal.isChecked()){
                        global = "1";
                    }
                    
                    if(!namaKalkulasi.getText().toString().equals("") && !deskripsiKalkulasi.getText().toString().equals("")){
                        //setText(getContext(),"datanya = "+namaKalkulasi.getText().toString()+" id_up "+" "+deskripsiKalkulasi.getText().toString()+" = "+global);
                        try{
                            boolean flag = myKalkulasi.insertData(namaKalkulasi.getText().toString(), id_kalkulasi_up, deskripsiKalkulasi.getText().toString(),"1");
                            String id_kalkulasi = myKalkulasi.getLastKalkulasi();
                            flag = myDetailKalkulasi.insertData(id_kalkulasi,id_project,"X");
                            if(flag == true){
                                setText(getContext(),"Kalkulasi ditambah");
                            }else{
                                setText(getContext(),"Gagal menambah data");
                            }
                            dismiss();
                        }catch(Exception e){
                            setText(getContext(),e.toString());
                        }
                        
                    }
                }

            
        });
        
        btnCancel.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View p1)
                {
                    dismiss();
                }

            
        });
            
        

        show();
    }

    public void setText(Context con, String teks)
    {
        Toast.makeText(con, teks, Toast.LENGTH_SHORT).show();
    }
  
}
