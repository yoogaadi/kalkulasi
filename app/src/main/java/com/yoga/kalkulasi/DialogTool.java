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

public class DialogTool extends Dialog
{


    toolHelper myTool;

    EditText namaTool,deskripsiTool;

    boolean flag = false;
    String status;


    String id_tool,nama_tool,deskripsi;          
    public DialogTool(Context context, String tool, String stat)
    {

        super(context);
        this.id_tool = tool;
        this.status = stat;

        myTool = new toolHelper(getContext());


        setText(getContext(),status);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);
        setContentView(R.layout.dialog_tool);
        setTitle("Tambah Barang");

        

        Button btnSimpan = (Button) findViewById(R.id.btnSimpan);
        Button btnCancel = (Button) findViewById(R.id.btnCancel);
        namaTool = (EditText) findViewById(R.id.namaTool);
        deskripsiTool = (EditText) findViewById(R.id.deskripsiTool);

        if (status.equals("edit"))
        {
            readData();
            
            namaTool.setText(nama_tool);
            deskripsiTool.setText(deskripsi);
        }



        btnSimpan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {

                    if(status.equals("edit")){
                        flag = myTool.updateData(id_tool,namaTool.getText().toString(),deskripsiTool.getText().toString());
                    }else if(status.equals("tambah")){
                        flag = myTool.insertData(namaTool.getText().toString(),deskripsiTool.getText().toString(),"1");
                    }
                    if(flag){
                        setText(getContext(),"Data berhasil disimpan");
                    }else{
                        setText(getContext(),"Ups.. terdapat kesalahan saat menyimpan data");
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

    public boolean getFlag(){
        return flag;
    }

    public void setText(Context con, String teks)
    {
        Toast.makeText(con, teks, Toast.LENGTH_SHORT).show();
    }

    public void readData(){
        Cursor res = myTool.getToolById(id_tool);
        if(res!=null && res.getCount()>0){
            while(res.moveToNext()){
                nama_tool = res.getString(1);
                deskripsi = res.getString(2);
            }
        }
    }





}
