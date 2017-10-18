package com.yoga.kalkulasi;
import android.app.*;
import android.view.Window;
import android.widget.*;
import android.view.*;
import android.content.*;
import com.yoga.kalkulasi.Model.*;
import android.database.*;

public class VariabelToolAction extends Dialog
{
    String id_detail_kalkulasi,id_variabel = "";
    variabelHelper myVar;
    detailVariabelHelper myDetail;
    satuanHelper mySatuan;
    detailToolHelper myDetailTool;

    EditText namaVariabel;
    Spinner spinSatuan;
    Cursor res,get;
    String id_var, nama, value;
    String[] nama_satuan, kode_satuan;

    String id_tool,status,select_id_var;
    boolean flags = false;
    String satuan;


    public VariabelToolAction(Context context, String id_tools, String select_id, String stat)
    {

        super(context);
        id_tool = id_tools;
        status = stat;
        select_id_var = select_id;

        myVar = new variabelHelper(getContext());
        myDetail = new detailVariabelHelper(getContext());
        myDetailTool = new detailToolHelper(getContext());
        mySatuan = new satuanHelper(getContext());

        readVar(select_id_var);
        readSatuan();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);
        setContentView(R.layout.variable_tool_action);
        setTitle("Tambah Variabel");

        Button btnSimpan = (Button) findViewById(R.id.btnSimpan);
        Button btnCancel = (Button) findViewById(R.id.btnCancel);

        spinSatuan = (Spinner) findViewById(R.id.spinSatuan);
        namaVariabel = (EditText) findViewById(R.id.namaVariabel);

        if (status.equals("edit"))
        {
            namaVariabel.setText(nama);
            spinSatuan.setSelection(Integer.parseInt(satuan)-1);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, nama_satuan);
        spinSatuan.setAdapter(adapter);


        btnSimpan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    boolean flag = false;
                    //setText(dialog.getContext(),namaVariabel.getText().toString()+" / "+id_detail);

                    if (namaVariabel.getText().toString().equals(""))
                    {
                        setText(getContext(), "Harus diisi");
                    }
                    else
                    {
                        if (status.equals("edit"))
                        {
                            if (myVar.updateData(select_id_var, namaVariabel.getText().toString(), kode_satuan[spinSatuan.getSelectedItemPosition()]))
                            {
                                flag = true;
                            }

                        }
                        else
                        {
                            if (myVar.insertData(namaVariabel.getText().toString(), kode_satuan[spinSatuan.getSelectedItemPosition()]))
                            {
                                flag = true;
                                String id_var = myVar.getLastVariable();

                                if (myDetailTool.insertData(id_tool, id_var))
                                {
                                    flag = true;


                                }
                            }
                        }
                        if (flag)
                        {
                            setText(getContext(), "Data berhasil disimpan");
                        }
                        else
                        { 
                            setText(getContext(), "Ups.. terjadi kesalahan ketika menyimpan data");
                        }

                        setFlag(flag);
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

    public void setFlag(boolean flag)
    {
        flags = flag;
    }

    public boolean getFlag()
    {
        return flags;
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
                satuan = get.getString(3);
            }
        }
        else
        {
            setText(getContext(), "gaada datanya");
        }
    } 

    public void readVar(String id_var)
    {
        res = myVar.getVariableById(id_var);

        if (res != null && res.getCount() > 0)
        {
            while (res.moveToNext())
            { 
                nama = res.getString(1);

            }
        }
        else
        {

        }
    }


}
