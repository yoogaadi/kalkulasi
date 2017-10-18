package com.yoga.kalkulasi;
import android.support.v7.app.*;
import android.support.v7.widget.Toolbar;
import com.yoga.kalkulasi.Model.*;
import android.database.*;
import android.os.*;
import android.view.*;
import android.content.*;
import android.widget.*;
import java.text.*;
import java.util.*;
import com.google.android.gms.ads.*;

public class ProjectAction extends AppCompatActivity 
{
    Toolbar toolbar;
    projectHelper myDb;
    Button btnSimpan,btnCancel;
    EditText textNama, textDesc;
    static Intent in = null;
    String id_user;
    String status;
    
    String id_project, nama_project,deskripsi_project;
	
	private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_action);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Project");
		
		mInterstitialAd = new InterstitialAd(this);
		//mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712"); 
		mInterstitialAd.setAdUnitId("ca-app-pub-9240036468098328/3757928560");
		mInterstitialAd.loadAd(new AdRequest.Builder().build());

        if(getIntent().hasExtra("id_user")){
            id_user = getIntent().getExtras().getString("id_user");
        }
        status = getIntent().getExtras().getString("status");
        

        myDb = new projectHelper(this);
        textNama = (EditText) findViewById(R.id.textNamaProject);
        textDesc = (EditText) findViewById(R.id.textDeskripsi);
        
        if(status.equals("edit")){
            try{
            id_project = getIntent().getExtras().getString("id_project");
            
            readData(id_project);
              
            textNama.setText(nama_project);
            textDesc.setText(deskripsi_project);
            }
            catch(Exception e){
                setText(e.toString());
            }
            
        }
        
       
        btnSimpan = (Button) findViewById(R.id.btnSimpan);
        btnCancel = (Button) findViewById(R.id.btnCancel);

        

        btnSimpan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    String tanggal = df.format(c.getTime());
                    if (textNama.getText().toString().equals("") || textDesc.getText().toString().equals(""))
                    {
                        setText("Harap isi semua data");
                    }
                    else
                    {
                        if(status.equals("tambah")){
                            boolean flag = myDb.insertData(id_user, textNama.getText().toString(), textDesc.getText().toString(), tanggal, tanggal);
                            if (flag)
                            {
                                setText("Project telah ditambahkan");
                                String id_project = myDb.getLastProject();
                                in = new Intent(ProjectAction.this, ProjectView.class);
                                in.putExtra("id_project", id_project);
                                startActivity(in);
								if (mInterstitialAd.isLoaded()) {
									mInterstitialAd.show();
								} else {

								}
                            }
                            else
                            {
                                setText("Gagal menambah project");
                            }
                        }else if(status.equals("edit")){
                            boolean flag = myDb.updateData(id_project,id_user, textNama.getText().toString(), textDesc.getText().toString(), tanggal);
                            if (flag)
                            {
                                setText("Project berhasil diubah");
                                in = new Intent(ProjectAction.this, ProjectView.class);
                                in.putExtra("id_project", id_project);
                                startActivity(in);
                            }
                            else
                            {
                                setText("Gagal merubah data");
                            }
                        }
                        
                    }
                }
            });

        btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    in = new Intent(ProjectAction.this, MainActivity.class);
                    startActivity(in);
                }
            });
    }

    public void setText(String text)
    {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
    
    public void readData(String id_project){
    
        Cursor res = myDb.getProjectById(id_project);
        if(res!=null && res.getCount()>0){
            while(res.moveToNext()){
                id_user = res.getString(1);
                nama_project = res.getString(2);
                deskripsi_project = res.getString(3);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {

            case android.R.id.home:
                Intent in = new Intent(ProjectAction.this, MainActivity.class);
                //in.putExtra("id_kalkulasi",id_kalkulasi);
                startActivity(in);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
  

}
