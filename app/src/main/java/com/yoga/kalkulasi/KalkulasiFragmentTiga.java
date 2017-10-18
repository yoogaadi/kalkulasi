package com.yoga.kalkulasi;
import android.support.v4.app.*;
import android.os.*;
import android.view.*;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.database.*;
import com.yoga.kalkulasi.Model.*;
import android.widget.Toast;
import android.view.ContextMenu.*;
import android.support.v7.app.*;
import android.content.*;
import android.widget.AdapterView.*;
import com.yoga.kalkulasi.Control.*;


public class KalkulasiFragmentTiga extends Fragment
{
    variabelHelper myVar;
    detailVariabelHelper myDetail;
    satuanHelper mySatuan;

    ListView lv;
    MyAdapterOptionDetail adapter;
    String[] arr,arr_id,detail = {};
    int totalData = 0;
    String id_detail,id_var;
    View view;
    Cursor res;Cursor get;
    String id_project;

    public KalkulasiFragmentTiga()
    {

    }

    public KalkulasiFragmentTiga(String id_detail_kal)
    {
        id_detail = id_detail_kal;
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
        view = inflater.inflate(R.layout.kalkulasi_fragment_3, container, false);
        myVar = new variabelHelper(getContext());
        myDetail = new detailVariabelHelper(getContext());
        mySatuan = new satuanHelper(getContext());
        
        lv = (ListView) view.findViewById(R.id.listSub);

        loadData(view);
        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        // TODO: Implement this method
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Select the Action");
        menu.add(0, v.getId(), 0, "Edit Variabel");
        menu.add(0, v.getId(), 0, "Hapus Variabel");

    } 

    public void loadData(View view)
    {
        readData();
        
        if (totalData > 0)
        {
            adapter = new MyAdapterOptionDetail(getActivity(), arr,detail);
            lv.setAdapter(adapter);
            registerForContextMenu(lv);
        }
        else
        {
            lv.setVisibility(view.INVISIBLE);
        }
        
    }
    


    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        AdapterContextMenuInfo info;
        // TODO: Implement this method
        if (item.getTitle().toString().equals("Edit Variabel"))
        {
            
            info = (AdapterContextMenuInfo) item.getMenuInfo();

            VariabelAction dv = new VariabelAction(getContext(), id_detail,arr_id[info.position], "edit");
            //dv.showDialog(getContext(),"Edit Variabel");
            dv.setOnDismissListener(new DialogInterface.OnDismissListener(){
                    @Override
                    public void onDismiss(DialogInterface p1)
                    {
                        loadData(view);
                    }
                });
            //setText(String.valueOf(info.position));
        }
        else if (item.getTitle().toString().equals("Hapus Variabel"))
        {
            info = (AdapterContextMenuInfo) item.getMenuInfo();
            id_var = arr_id[info.position];
            ShowDialog();
            
        }

        return super.onContextItemSelected(item);

    }


    public void setText(String teks)
    {
        Toast.makeText(getContext(), teks, Toast.LENGTH_SHORT).show();
    }
    public void read(){
        Cursor get = myDetail.getAllData();
            
        if(get!=null && get.getCount()>0){
            while(get.moveToNext()){
                
                setText("detail = "+get.getString(0)+" variabel = "+get.getString(1));

            }
        }
    }

    public void readData()
    {
        try{
       
        res = myDetail.getDetailById(id_detail);
        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer id_buffer = new StringBuffer();
        StringBuffer det = new StringBuffer();
        if (res != null && res.getCount() > 0)
        {
            while (res.moveToNext())
            { 
                id_buffer.append(res.getString(1) + ",");
                stringBuffer.append(getNamaVariabel(res.getString(1)) + ",");
                det.append(res.getString(2)+" "+mySatuan.getNamaSatuan(myVar.getSatuan(res.getString(1)))+ ",");
             
            }
        }
        totalData = res.getCount();
        arr = stringBuffer.toString().split(",");
        arr_id = id_buffer.toString().split(",");
        detail = det.toString().split(",");
        }catch(Exception e){
            setText(e.toString());
        }
     
    }

    public String getNamaVariabel(String id)
    {
        String nama = "";
        get = myVar.getVariableById(id);

        if (get != null && get.getCount() > 0)
        {
            while (get.moveToNext())
            { 
                nama = get.getString(1);
            }

        }
        return nama;
    }
    public void hapus()
    {

        myDetail.deleteData(id_detail, id_var);
        myVar.deleteData(id_var);	
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
                    loadData(view);
                    return;
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
