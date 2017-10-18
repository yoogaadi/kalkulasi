package com.yoga.kalkulasi;
import android.support.v4.app.*;
import android.os.*;
import android.view.*;
import com.yoga.kalkulasi.Model.*;
import android.widget.*;
import android.database.*;
import android.content.*;
import android.widget.AbsoluteLayout.*;
import com.yoga.kalkulasi.*;
import android.support.design.widget.*;
import com.yoga.kalkulasi.Control.*;

public class ToolFragmentActivity extends Fragment
{

    toolHelper myDb;
    ListView lv;
    Cursor res;
    MyAdapterChevron adapter;
    String[] arr, arr_id;
    int totalData = 0;
    String id_user;
    String[] nama_tool,id_tool = {"ce","12"};

    FloatingActionButton fab;
    LinearLayout linearTool,linearEmpty;


    public ToolFragmentActivity()
    {

    }

    public static ToolFragmentActivity newInstance()
    {
        ToolFragmentActivity fragment = new ToolFragmentActivity();
        return fragment;
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.menu_tambah, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.tambah:
                DialogTool dv = new DialogTool(getContext(), "", "tambah");
                //dv.showDialog(this,"Tambah Variabel");
                dv.setOnDismissListener(new DialogInterface.OnDismissListener(){
                        @Override
                        public void onDismiss(DialogInterface p1)
                        {
                            Intent in = new Intent(getContext(), ToolView.class);
                            String id_tool = myDb.getLastTool();
                            in.putExtra("id_tool", id_tool);
                            startActivity(in);

                        }
                    });
            default:
                return super.onOptionsItemSelected(item);
        }

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        myDb = new toolHelper(getContext());
        View view =  inflater.inflate(R.layout.tool_activity, container, false);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);

        readData();
        
        linearTool = (LinearLayout) view.findViewById(R.id.linearMain);
        linearEmpty = (LinearLayout) view.findViewById(R.id.linearEmpty);
        
        fab.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View p1)
                {
                    final DialogTool dv = new DialogTool(getContext(), "", "tambah");
                    //dv.showDialog(this,"Tambah Variabel");
                    dv.setOnDismissListener(new DialogInterface.OnDismissListener(){
                            @Override
                            public void onDismiss(DialogInterface p1)
                            {
                                if(dv.getFlag()){
                                    Intent in = new Intent(getContext(), ToolView.class);
                                    String id_tool = myDb.getLastTool();
                                    in.putExtra("id_tool", id_tool);
                                    startActivity(in);
                                }
                            }
                        });
                }

            });	
        

        if(totalData>0){
            linearEmpty.setVisibility(LinearLayout.GONE);
            lv = (ListView) view.findViewById(R.id.listTool);
            adapter = new MyAdapterChevron(getActivity(),  nama_tool);
            lv.setAdapter(adapter);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
                    {

                        Intent in = new Intent(getContext(), ToolView.class);
                        in.putExtra("id_tool", id_tool[i]);
                        startActivity(in);
                        //Toast.makeText(getContext(),arr[i]+" id = "+arr_id[i],Toast.LENGTH_SHORT).show();
                    }
                });
            
        }else{
            linearEmpty.setVisibility(LinearLayout.VISIBLE);
        }
        
        return view;
    }

    public void setText(String teks){
        Toast.makeText(getContext(),teks,Toast.LENGTH_SHORT).show();
    }

    public void readData()
    {

        res = myDb.getAllData();
        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer id_buffer = new StringBuffer();
        if (res != null && res.getCount() > 0)
        {
            while (res.moveToNext())
            { 
                stringBuffer.append(res.getString(1) + ",");
                id_buffer.append(res.getString(0) + ",");
            }
        }
        totalData = res.getCount();

        nama_tool = stringBuffer.toString().split(",");
        id_tool = id_buffer.toString().split(",");
    }



}
