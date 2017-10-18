package com.yoga.kalkulasi;
import android.app.*;
import android.os.*;
import android.widget.*;
import com.yoga.kalkulasi.Model.*;
import android.support.v7.app.*;

import android.support.v7.widget.Toolbar;
import android.view.View.*;
import android.view.*;
import android.content.*;
import android.database.*;

public class KalkulasiAction extends AppCompatActivity 
{
    Toolbar toolbar;
    kalkulasiHelper myDb;
    detailKalkulasiHelper myDetail;
    Button btn_simpan, btnCancel;
    EditText namaKalkulasi,deskripsi;
    String id_kalkulasi,nama,desc,id_kalkulasi_up = "";
    Cursor res;
    String status;
    String id_project = "-";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kalkulasi_action);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //id_user = getIntent().getExtras().getString("id_user");
        status = getIntent().getExtras().getString("status");
        id_project = getIntent().getExtras().getString("id_project");

        myDb = new kalkulasiHelper(this);
        myDetail = new detailKalkulasiHelper(this);

        namaKalkulasi = (EditText) findViewById(R.id.namaKalkulasi);
        deskripsi = (EditText) findViewById(R.id.deskripsiKalkulasi);
        btn_simpan = (Button) findViewById(R.id.btnSimpan);
		btnCancel = (Button) findViewById(R.id.btnCancel);

        Toast.makeText(this, status, Toast.LENGTH_SHORT).show();
        if (status.equals("edit"))
        {
            id_kalkulasi = getIntent().getExtras().getString("id_kalkulasi");
            readData();
            namaKalkulasi.setText(nama);
            deskripsi.setText(desc);
        }
       
        if (getIntent().hasExtra("id_kalkulasi_up"))
        {
            id_kalkulasi_up = getIntent().getExtras().getString("id_kalkulasi_up");
        }
        
        if(getIntent().hasExtra("id_project")){
            id_project = getIntent().getExtras().getString("id_project");
        }



        btn_simpan.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v)
                {
                    if (!namaKalkulasi.equals("") && !deskripsi.equals(""))
                    {
                        String stat = "Ditambah";
                        boolean flag = false;

                        ///setText(namaKalkulasi.getText().toString()+" - "+id_kalkulasi_up+" - "+deskripsi.getText().toString());

                        if (status.equals("edit"))
                        {
                            
                            flag = myDb.updateData(id_kalkulasi, namaKalkulasi.getText().toString(), id_kalkulasi_up, deskripsi.getText().toString());
                            
                            stat = "Diubah";

                            Intent in = new Intent(KalkulasiAction.this, KalkulasiView.class);
                            in.putExtra("id_project",id_project);
                            in.putExtra("id_kalkulasi", id_kalkulasi);
                            startActivity(in);

                        }
                        else if (status.equals("tambah"))
                        {
                            flag = myDb.insertData(namaKalkulasi.getText().toString(), id_kalkulasi_up, deskripsi.getText().toString(),"1");
                            String last_kalkulasi = myDb.getLastKalkulasi();
                            
                            flag = myDetail.insertData(last_kalkulasi, id_project,"X");
                            
                            String id_kalkulasi = myDb.getLastKalkulasi();
                            Intent in = new Intent(KalkulasiAction.this, KalkulasiView.class);
                            in.putExtra("id_project",id_project);
                            in.putExtra("id_kalkulasi", id_kalkulasi);
                            startActivity(in);
                            
                        }
                        if (flag == true)
                        {
                            setText("Data Berhasil " + stat);
                        }
                        else
                        {
                            setText("Ups.. Gagal saat menyimpan data");
                        }
                    }
                }
            });	
			
		btnCancel.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					Intent in = new Intent(KalkulasiAction.this, MainActivity.class);
					in.putExtra("menu", "Kalkulasi");
					startActivity(in);
				}

				
			});
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {

            case android.R.id.home:
                Intent in = new Intent(KalkulasiAction.this, MainActivity.class);
                in.putExtra("menu", "Kalkulasi");
                startActivity(in);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    public void setText(String text)
    {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();

    }
    public void readData()
    {
        res = myDb.getKalkulasiById(id_kalkulasi);

        if (res != null && res.getCount() > 0)
        {
            while (res.moveToNext())
            { 
                nama = res.getString(1);
                id_kalkulasi_up = res.getString(2);
                desc = res.getString(3);
            }

        }
        else
        {

        }
    }
}
