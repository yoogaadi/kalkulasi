package com.yoga.kalkulasi;
import android.support.v4.app.*;
import android.os.*;
import android.view.*;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.database.*;
import com.yoga.kalkulasi.Model.*;
import android.widget.Toast;
import android.widget.AdapterView;
import android.content.Intent;
import android.content.*;


public class ToolFragmentDua extends Fragment
{
    detailToolHelper myDetailTool;
    toolHelper myTool;
    variabelHelper myVariabel;

    ListView lv;
    ArrayAdapter adapter;
    String[] arr,arr_id = {};
    int totalData = 0;
    String id_kalkulasi,nama;
    Cursor res;
    String id_tool = "-";
    View view;
    String select_id_var;

    String[] nama_variabel,id_variabel = {""};

    public ToolFragmentDua()
    {

    }

    public ToolFragmentDua(String id_tool)
    {
        this.id_tool = id_tool;

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
        view = inflater.inflate(R.layout.tool_fragment_dua, container, false);
        myTool = new toolHelper(getContext());
        myDetailTool = new detailToolHelper(getContext());
        myVariabel = new variabelHelper(getContext());

        loadData();

        return view;
    }

    private void loadData(){

        getVariabelByTool();
        if(totalData >0 ){
            lv = (ListView) view.findViewById(R.id.listVariabel);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, nama_variabel);
            lv.setAdapter(adapter);
            registerForContextMenu(lv);

        }
    }

    public void setText(String teks)
    {
        Toast.makeText(getContext(), teks, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menus, View v2, ContextMenu.ContextMenuInfo menuInfo2)
    {
        // TODO: Implement this method
        super.onCreateContextMenu(menus, v2, menuInfo2);
        menus.setHeaderTitle("Select the Action");
        menus.add(0, v2.getId(), 0, "Edit Variabel");
        menus.add(0, v2.getId(), 0, "Hapus Variabel");


    }


    @Override
    public boolean onContextItemSelected(MenuItem items)
    {
        AdapterView.AdapterContextMenuInfo info;
        // TODO: Implement this method
        if (items.getTitle().toString().equals("Hapus Variabel"))
        {
            info = (AdapterView.AdapterContextMenuInfo) items.getMenuInfo();

            select_id_var = id_variabel[info.position];
            //id_var = arr_id[info.position];
            //ShowDialog();
        }else if (items.getTitle().toString().equals("Edit Variabel"))
        {
            info = (AdapterView.AdapterContextMenuInfo) items.getMenuInfo();

            select_id_var = id_variabel[info.position];
            
            VariabelToolAction dv = new VariabelToolAction(getContext(), id_tool,select_id_var,"edit");
            //dv.showDialog(this,"Tambah Variabel");
            dv.setOnDismissListener(new DialogInterface.OnDismissListener(){
                    @Override
                    public void onDismiss(DialogInterface p1)
                    {
                        loadData();
                    }
                });
        }
        return super.onContextItemSelected(items);

    }


    public void getVariabelByTool(){
        Cursor res = myDetailTool.getVariableByTool(id_tool);
        StringBuffer nama = new StringBuffer();
        StringBuffer id_var = new StringBuffer();
        if(res!=null && res.getCount()>0){
            while(res.moveToNext()){
                nama.append(myVariabel.getNamaVariabel(res.getString(1))+",");
                id_var.append(res.getString(1)+",");
            }

        }
        totalData = res.getCount();
        id_variabel = id_var.toString().split(",");
        nama_variabel = nama.toString().split(",");

    }

}
