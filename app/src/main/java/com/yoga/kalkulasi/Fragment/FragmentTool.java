package com.yoga.kalkulasi.Fragment;
import android.support.v4.app.*;
import android.os.*;
import android.view.*;
import com.yoga.kalkulasi.Model.*;
import android.widget.*;
import android.database.*;
import android.content.*;
import android.widget.AbsoluteLayout.*;
import com.yoga.kalkulasi.*;

public class FragmentTool extends Fragment
{

    toolHelper myDb;
    ListView lv;
    Cursor res;
    ArrayAdapter adapter;
    String[] arr, arr_id;
    int totalData = 0;
    String id_user;
    String[] arr_data = {"ce","12"};

    LinearLayout linearTool,linearEmpty;


    public FragmentTool()
    {

    }

    public static FragmentTool newInstance()
    {
        FragmentTool fragment = new FragmentTool();
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


        readData();


        lv = (ListView) view.findViewById(R.id.listTool);
        adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, arr);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
                {

                    Intent in = new Intent(getContext(), ToolView.class);
                    in.putExtra("id_tool", arr_id[i]);
                    startActivity(in);
                    //Toast.makeText(getContext(),arr[i]+" id = "+arr_id[i],Toast.LENGTH_SHORT).show();
                }
            });

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
        arr = stringBuffer.toString().split(",");
        arr_id = id_buffer.toString().split(",");
    }



}
