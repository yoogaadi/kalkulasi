package com.yoga.kalkulasi;
import android.app.*;
import android.view.Window;
import android.widget.*;
import android.view.*;
import android.content.*;
import com.yoga.kalkulasi.Model.*;
import android.database.*;

public class VariabelAction extends Dialog
{
    String id_detail_kalkulasi,id_variabel = "";
    variabelHelper myVar;
    detailVariabelHelper myDetail;
    satuanHelper mySatuan;
    EditText namaVariabel,valueVariabel;
    Spinner spinSatuan;
    Button btnOpenTool;
    Cursor res,get;
    String id_var, nama, value;
    String[] nama_satuan, kode_satuan;
    String satuan_var;
    boolean flag = false;

    public VariabelAction(Context context, String id_det, String id_vars, final String status)
    {

        super(context);
        id_detail_kalkulasi = id_det;
        id_variabel = id_vars;

        myVar = new variabelHelper(getContext());
        myDetail = new detailVariabelHelper(getContext());
        mySatuan = new satuanHelper(getContext());

        readSatuan();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);
        setContentView(R.layout.variable_action);
        setTitle("Tambah Variabel");

        Button btnSimpan = (Button) findViewById(R.id.btnSimpan);
        Button btnCancel = (Button) findViewById(R.id.btnCancel);
        btnOpenTool = (Button) findViewById(R.id.btnOpenTool);

        spinSatuan = (Spinner) findViewById(R.id.spinSatuan);
        namaVariabel = (EditText) findViewById(R.id.namaVariabel);
        valueVariabel = (EditText) findViewById(R.id.valueVariabel);

        btnOpenTool.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View p1)
                {
                    DialogSelectTool ds = new DialogSelectTool(getContext());
                    ds.show();
                }


            });



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, nama_satuan);
        spinSatuan.setAdapter(adapter);

        if (status.equals("edit"))
        {
            //setText(getContext(), "detail = "+id_detail);
            readDetail();
            readVar();
            valueVariabel.setText(value.toString());
            namaVariabel.setText(nama.toString());
            spinSatuan.setSelection(Integer.parseInt(satuan_var)-1);
            
        }
        
        
        btnSimpan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                   
                    //setText(dialog.getContext(),namaVariabel.getText().toString()+" / "+id_detail);

                    if (namaVariabel.getText().toString().equals("") || valueVariabel.getText().toString().equals(""))
                    {
                        setText(getContext(), "Harus diisi");
                    }
                    else
                    {
                        if (status.equals("tambah"))
                        {

                            if (myVar.insertData(namaVariabel.getText().toString(), kode_satuan[spinSatuan.getSelectedItemPosition()]))
                            {
                                flag = true;
                                String id_var = myVar.getLastVariable();
                               
                                setText(getContext(), id_detail_kalkulasi);
                                if (myDetail.insertData(id_detail_kalkulasi, id_var, valueVariabel.getText().toString(), ""))
                                {
                                    flag = true;

                                }
                            }
                           
                        }
                        else if (status.equals("edit"))
                        {
                            if (myVar.updateData(id_var, namaVariabel.getText().toString(), kode_satuan[spinSatuan.getSelectedItemPosition()]))
                            {
                                flag = true;
                                if (myDetail.updateValue(id_detail_kalkulasi, id_var, valueVariabel.getText().toString()))
                                {
                                    flag = true;
                                }
                                
                            }
                          
                        }
                        if(flag == true){
                            setText(getContext(),"Data berhasil ditambahkan");
                        }else{
                            setText(getContext(),"Ups.. terjadi kesalahan ketika menyimpan data");
                        }

                        dismiss();
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
    }
    
    public boolean getFlag(){
        return this.flag;
    }

    public void setText(Context con, String teks)
    {
        Toast.makeText(con, teks, Toast.LENGTH_SHORT).show();
    }
    public void readSatuan()
    {
        res = mySatuan.getAllData();
        StringBuffer nama = new StringBuffer();
        StringBuffer kode = new StringBuffer();
        if (res != null && res.getCount() > 0)
        {
            while (res.moveToNext())
            { 
                kode.append(res.getString(0) + ",");
                nama.append(res.getString(1) + ",");
            }
            nama_satuan = nama.toString().split(",");
            kode_satuan = kode.toString().split(",");
        }
        else
        {

        }
    } 
    public void readDetail()
    {

        get = myDetail.getDetailByIdAndKalkulasi(id_detail_kalkulasi, id_variabel);

        if (get != null && get.getCount() > 0)
        {
            while (get.moveToNext())
            { 
                id_var = get.getString(1);
                value = get.getString(2);
                
            }
        }
        else
        {
            setText(getContext(), "gaada datanya");
        }
    } 

    public void readVar()
    {
        res = myVar.getVariableById(id_var);

        if (res != null && res.getCount() > 0)
        {
            while (res.moveToNext())
            { 
                nama = res.getString(1);
                satuan_var = res.getString(2);
            }
        }
    }
}
