

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


public class DialogTambahKalkulasiProject extends Dialog implements SearchView.OnQueryTextListener
{ 

    kalkulasiHelper myDb;
    detailKalkulasiHelper myDetailKal;
    detailVariabelHelper myVar;
    detailItemHelper myItem;

    ListView listKal;

    SearchView search;
    ArrayAdapter<String> adapter;
    String[] arr = {"1","2","22","312"};
    String[] nama_kalkulasi, id_kalkulasi = {};
    String[] id_sub = {};
    TextView textAlert;


    TextView tv;

    Button btnSimpan,btnCancel;
    Cursor res,get,sat,har;
    String kode_satuan;
    int totalData = 0;
    String id_project;
	
	boolean isFirstTime = true;


    public DialogTambahKalkulasiProject(Context context,String id_proj)
    {

        super(context);

        myDb = new kalkulasiHelper(getContext());
        myDetailKal = new detailKalkulasiHelper(getContext());
        myVar = new detailVariabelHelper(getContext());
        myItem = new detailItemHelper(getContext());

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);
        setContentView(R.layout.project_tambah_kalkulasi_template);
        setTitle("Tambah Barang");
        textAlert = (TextView) findViewById(R.id.textAlert);
        
        id_project = id_proj;
        
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View p1)
                {
                    dismiss();
                }

           
       });
       
        search = (SearchView) findViewById(R.id.id_search);
        
        loadData();
 
        show();

    }



    public void setText(Context con, String teks)
    {
        Toast.makeText(con, teks, Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onQueryTextSubmit(String p1)
    {
        // TODO: Implement this method
        return false;
    }

    @Override
    public boolean onQueryTextChange(String p1)
    {
        // TODO: Implement this method
        String newText = p1;
        adapter.getFilter().filter(newText);
        
        return false;
    }
    
    public void loadData(){
        getKalkulasi();
        if(totalData > 0){
            search.setVisibility(SearchView.VISIBLE);
            textAlert.setVisibility(TextView.GONE);
            listKal = (ListView) findViewById(R.id.listKalkulasi);
            adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, nama_kalkulasi);
            listKal.setAdapter(adapter);
            listKal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
                    {
                        for(int x = 0; x < nama_kalkulasi.length;x++){
                            if(adapter.getItem(i).equals(nama_kalkulasi[x])){

                                insertCopyDetailKalkulasi(id_kalkulasi[x]);
                                getSubKalkulasi(id_kalkulasi[x]);
                               
                                for(int a = 0; a<id_sub.length;a++){
                                    insertCopyDetailKalkulasi(id_sub[a]);
                                }
                            }
                        }
                        setText(getContext(),"Data telah ditambahkan");
						
						if(getStatusTutorial()){
							TutorialSepuluh ts = new TutorialSepuluh(getContext());
							setStatusTutorial();
							
						}
                        dismiss();
                    }
                });
                
            
            search.setOnQueryTextListener(this);
        }else{
            search.setVisibility(SearchView.GONE);
            textAlert.setVisibility(TextView.VISIBLE);
        }
        
        
    }
    public void insertCopyDetailKalkulasi(String id_kalkulasi){
        //id detail lama malah ambil detail baru yg sebelumnya
               
        String id_det_kal = myDetailKal.getIdDetailKalkulasi(id_kalkulasi,"-");
        Boolean flag = myDetailKal.insertCopyData(id_det_kal,id_project);
        
        String id_detail_baru = myDetailKal.getLastIdKalkulasi();
        
        flag = myVar.insertCopyVariabel(id_det_kal,id_detail_baru);
        flag = myItem.insertCopyItem(id_det_kal,id_detail_baru);
      
       
    }
    
 

    public void getKalkulasi()
    {
        res = myDb.getKalkulasiParrent();
        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer id_buffer = new StringBuffer();

        if (res != null && res.getCount() > 0)
        {
            while (res.moveToNext())
            {
                if(myDetailKal.isTemplate(res.getString(0))){
                id_buffer.append(res.getString(0) + ",");
                stringBuffer.append(res.getString(1) + ",");
                    totalData++;
                }

            }
        }
       

        nama_kalkulasi = stringBuffer.toString().split(",");
        id_kalkulasi = id_buffer.toString().split(",");

    }

    public void getSubKalkulasi(String id_kalkulasi_up){
        res = myDb.getKalkulasiByParrent(id_kalkulasi_up);
        StringBuffer id_buffer = new StringBuffer();

        if (res != null && res.getCount() > 0)
        {
            while (res.moveToNext())
            {
                id_buffer.append(res.getString(0) + ",");
            }
        }
        totalData = res.getCount();
        id_sub = id_buffer.toString().split(",");
    }
	
	public void setStatusTutorial(){
		try {

			 File file = new File(getContext().getCacheDir(),"Tutorial");
            FileOutputStream fos = new FileOutputStream(file);
            fos.write("ada".getBytes());
            fos.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
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

